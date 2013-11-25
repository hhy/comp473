package proj.preprocess;

import java.awt.Color;
import java.awt.Point;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import proj.mnist.ImagesFileMnist;
import proj.mnist.MatrixFile;
import proj.mnist.TrainingData;

public class ThinningSuen {

	int[][] binaryImage;

	public ThinningSuen(int[][] m) {
		this.binaryImage = m;
	}

	private int[][] doThinning() {
		int a, b;
		boolean hasChange;
		do {
			hasChange = false;
			for (int y = 1; y + 1 < binaryImage.length; y++) {
				for (int x = 1; x + 1 < binaryImage[y].length; x++) {
					a = getA(binaryImage, y, x);
					b = getB(binaryImage, y, x);
					if (binaryImage[y][x] == 1
							&& 2 <= b
							&& b <= 6
							&& a == 1
							&& ((binaryImage[y - 1][x] * binaryImage[y][x + 1]
									* binaryImage[y][x - 1] == 0) || (getA(
									binaryImage, y - 1, x) != 1))
							&& ((binaryImage[y - 1][x] * binaryImage[y][x + 1]
									* binaryImage[y + 1][x] == 0) || (getA(
									binaryImage, y, x + 1) != 1))) {
						binaryImage[y][x] = 0;
						hasChange = true;
					}
				}
			}
		} while (hasChange);

		return binaryImage;
	}

	private int[][] suenThinning(int[][] binaryImage) {
		int a, b;
		int[][] binaryImageClone = binaryImage.clone();
		List<Point> pointsToChange = new LinkedList();
		boolean hasChange;

		do {
			hasChange = false;
			for (int i = 1; i + 1 < binaryImage.length; i++) {
				for (int j = 1; j + 1 < binaryImage[i].length; j++) {
					a = aP(binaryImage, i, j);
					b = bP(binaryImage, i, j);

					if (binaryImage[i][j] == 1
							&& 2 <= b
							&& b <= 6
							&& a == 1
							&& (binaryImage[i - 1][j] == 0
									&& binaryImage[i][j + 1] == 0 && binaryImage[i + 1][j] == 0)
							&& (binaryImage[i][j + 1] == 0
									&& binaryImage[i + 1][j] == 0 && binaryImage[i - 1][j] == 0)) {
						pointsToChange.add(new Point(j, i));
						hasChange = true;
					}
				}
			}
			for (Point point : pointsToChange) {
				binaryImage[point.getY()][point.getX()] = 0;
			}

			pointsToChange.clear();

			for (int i = 1; i + 1 < binaryImage.length; i++) {
				for (int j = 1; j + 1 < binaryImage[i].length; j++) {
					a = getA(binaryImage, i, j);
					b = getB(binaryImage, i, j);
					if (binaryImage[i][j] == 1
							&& 2 <= b
							&& b <= 6
							&& a == 1
							&& (binaryImage[i - 1][j] == 0
									&& binaryImage[i][j + 1] == 0 && binaryImage[i][j - 1] == 0)
							&& (binaryImage[i - 1][j] == 0
									&& binaryImage[i + 1][j] == 0 && binaryImage[i][j - 1] == 0)) {
						pointsToChange.add(new Point(j, i));

						hasChange = true;
					}
				}
			}

			for (Point point : pointsToChange) {
				binaryImage[point.getY()][point.getX()] = 0;
			}
			pointsToChange.clear();

		} while (hasChange);

		return binaryImageClone;

	}

	class Point {

		private int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
	};

	/**
	 * returns aP, the number of zero to one transitions in the neighborhood
	 * 
	 * @param binaryImage
	 * @param y
	 * @param x
	 * @return
	 */
	private int aP(int[][] binaryImage, int y, int x) {
		int count = 0;
		// p1 -> p2
		if (binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
			count++;
		}
		// p2 -> p3
		if (binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
			count++;
		}
		// p3-> p4
		if (binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
			count++;
		}
		// p4->p5
		if (binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
			count++;
		}
		// p5->p6
		if (binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
			count++;
		}
		// p6->p7
		if (binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
			count++;
		}
		// p7->p8
		if (binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
			count++;
		}
		// p8->p1
		if (binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
			count++;
		}

		return count;

	}

	/**
	 * returns the total number of neighbors with intensity 255 in the image.
	 * 
	 * @param binaryImage
	 * @param y
	 * @param x
	 * @return
	 */
	private int bP(int[][] binaryImage, int y, int x) {
		return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1]
				+ binaryImage[y][x + 1] + binaryImage[y + 1][x + 1]
				+ binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
				+ binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];

	}

	private int getA(int[][] binaryImage, int y, int x) {

		int count = 0;
		// p2 p3
		if (y - 1 >= 0 && x + 1 < binaryImage[y].length
				&& binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
			count++;
		}
		// p3 p4
		if (y - 1 >= 0 && x + 1 < binaryImage[y].length
				&& binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
			count++;
		}
		// p4 p5
		if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length
				&& binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
			count++;
		}
		// p5 p6
		if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length
				&& binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
			count++;
		}
		// p6 p7
		if (y + 1 < binaryImage.length && x - 1 >= 0
				&& binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
			count++;
		}
		// p7 p8
		if (y + 1 < binaryImage.length && x - 1 >= 0
				&& binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
			count++;
		}
		// p8 p9
		if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0
				&& binaryImage[y - 1][x - 1] == 1) {
			count++;
		}
		// p9 p2
		if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0
				&& binaryImage[y - 1][x] == 1) {
			count++;
		}

		return count;
	}

	private int getB(int[][] binaryImage, int y, int x) {

		return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1]
				+ binaryImage[y][x + 1] + binaryImage[y + 1][x + 1]
				+ binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
				+ binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
	}

	public static int[][] doThinning(int[][] m) {
		ThinningSuen ti = new ThinningSuen(m);
		// return ti.doThinning();
		return ti.suenThinning(m);
	}

	// public static void main(String[] args) throws IOException {
	// System.out.println("=================start==================");
	//
	// MatrixFile fMatrix = new MatrixFile(TrainingData.pathImageTraining);
	// int[][] m = (int[][]) fMatrix.getObject(4);
	//
	// m=Binarizor.toBinary(m, 100);
	//
	//
	// int[][] mm=ThinningDitchs.doThinning(m);
	//
	// //
	// //
	// // ImgTool.showImg(img, "original", 400);
	// ImgTool.showImg(mm, "ffff", 400, true);
	//
	// Normalizor n = new Normalizor(m);
	// System.out.println(n.getDimension());
	// n.scaleInnerObject(28, 28);
	// // ImgTool.showImg(n.scaleInnerObject(28, 28), "scale", 400);
	// // ImgTool.showSubImg(m, "", 400, n.x1, n.x2, n.y1, n.y2);
	//
	// System.out.println("===================end======================");
	// }
}
