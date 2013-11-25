package proj.preprocess;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;

import proj.misc.ImgTool;
import proj.mnist.ImagesFileMnist;
import proj.mnist.MatrixFile;
import proj.mnist.TrainingData;

public class ThinningDitchs {
	int[][] binaryImage;
	public ThinningDitchs(int[][] m){
		this.binaryImage=m;
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
	
	public static int[][] doThinning(int[][] m){
		ThinningDitchs ti=new ThinningDitchs(m);
		return ti.doThinning();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("=================start==================");

		MatrixFile fMatrix = new MatrixFile(TrainingData.pathImageTraining);
		int[][] m = (int[][]) fMatrix.getObject(4);
		
		m=Binarizor.toBinary(m, 100);

		
		int[][] mm=ThinningDitchs.doThinning(m);
		
		//
		//
		// ImgTool.showImg(img, "original", 400);
		ImgTool.showImg(mm, "ffff", 400, true);

		Normalizor n = new Normalizor(m);
		System.out.println(n.getDimension());
		n.scaleInnerObject(28, 28);
		// ImgTool.showImg(n.scaleInnerObject(28, 28), "scale", 400);
		// ImgTool.showSubImg(m, "", 400, n.x1, n.x2, n.y1, n.y2);

		System.out.println("===================end======================");
	}
}
