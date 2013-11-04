package proj;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SkewingHandler {
	static int[] averageFilterForArray(int[] a, int w) {
		int[] b = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			int s = a[i];
			for (int j = 1; j < w; j++) {
				int k1 = i - j, k2 = i + j;
				if (k1 < 0)
					k1 = k2;
				if (k2 >= a.length)
					k2 = k1;
				s += a[k1] + a[k2];
			}
			b[i] = s / (2 * w + 1);
		}
		return b;
	}

	public static void showData(int[][] data, int w, int h) {
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(new GridLayout(data.length, 1));
		for (int i = 0; i < data.length; i++)
			// jf.getContentPane().add(new HistroChart(data[i], w, h));
			jf.pack();
		jf.setVisible(true);
	}

	static int[] getYDistribution(BufferedImage bi, int threshold,
			int widthFilter, boolean inverse) {

		BufferedImage img = ImgTool.toBin(bi, threshold, inverse);
		int h = bi.getHeight(), w = bi.getWidth();
		int[] yhist = new int[h];
		int rgbWhite = Color.white.getRGB();
		for (int y = 0; y < h; y++) {
			yhist[y] = 0;
			for (int x = 0; x < w; x++) {
				int rgb = img.getRGB(x, y);
				if (rgb == rgbWhite)
					yhist[y]++;
			}
		}

		/*
		 * smooth the distribution
		 */
		int[] yyhist = averageFilterForArray(yhist, widthFilter);
		return yyhist;
	}

	static int[] getXDistribution(BufferedImage bi, int threshold,
			int widthFilter, boolean inverse) {

		BufferedImage img = ImgTool.toBin(bi, threshold, inverse);
		int h = bi.getHeight(), w = bi.getWidth();
		int[] xhist = new int[w];
		int rgbWhite = Color.white.getRGB();
		for (int x = 0; x < w; x++) {
			xhist[x] = 0;
			for (int y = 0; y < h; y++) {
				int rgb = img.getRGB(x, y);
				if (rgb == rgbWhite)
					xhist[x]++;
			}
		}

		/*
		 * smooth the distribution
		 */
		int[] xxhist = averageFilterForArray(xhist, widthFilter);
		return xxhist;
	}

	static void showImg(BufferedImage bi, String title) {
		HistroChart.showHistroDistrib(bi, 800, 500, title);
	}

	static int getWidthPeakYDistribution(int[] yhist) {

		int[] yyhist = averageFilterForArray(yhist, 3);

		/*
		 * filter the maximum and minum, take the second maximum and minum for
		 * the threshold
		 */
		int max1 = yyhist[0], max2 = max1, min1 = max1, min2 = max1;
		for (int i = 0; i < yyhist.length; i++) {
			if (yyhist[i] > max1) {
				max2 = max1;
				max1 = yyhist[i];
			}
			if (yyhist[i] < min1) {
				min2 = min1;
				min1 = yyhist[i];
			}
		}

		int div = (int) ((min2 + max2) / (double) (8));

		int idxStart = -1, idxEnd = -1;

		for (int i = yyhist.length / 40; i < yyhist.length; i++) {
			int j = yyhist.length - i - 1;
			if (yyhist[i] > div && idxStart < 0)
				idxStart = i;
			if (yyhist[j] > div && idxEnd < 0)
				idxEnd = j;
			if (idxStart > 0 && idxEnd > 0)
				break;
		}

		// System.out.println(String
		// .format("start: %d, end: %d, width: %d", idxStart, idxEnd,
		// idxEnd-idxStart));

		return idxEnd - idxStart;
	}

	static public double getSkewingAngle(BufferedImage bi) {
		return 0;
	}

	public static void main(String[] args) throws IOException {
		final String pathImage = "/home/bart/Pictures/c.png";
		BufferedImage ia = ImageIO.read(new File(pathImage));
		BufferedImage ic = ImgTool.scale(ia, 800, 800);
		BufferedImage id = ImgTool.rotate(ic, 0);

		for (int i = -20; i < 20; i += 5) {
			double a = i * Math.PI / (double) 80;
			BufferedImage ie = ImgTool.rotate(id, a);
			int[] yDist = SkewingHandler.getYDistribution(ie, 150, 4, false);
			int[] xDist = SkewingHandler.getXDistribution(ie, 150, 4, false);
			int h = SkewingHandler.getWidthPeakYDistribution(yDist);
			int w = SkewingHandler.getWidthPeakYDistribution(xDist);
			String title = String.format("Angle: %d, product: %d x %d = %d",
					(int) (180 * a / Math.PI), w, h, w * h);
			System.out.println(title);
			SkewingHandler.showImg(ie, title);
		}

	}

}
