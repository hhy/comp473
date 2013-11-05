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
	static int[] averageFilterForArray(int[] a, int w, int start, int end) {
		//ArrayChart.showArray(a, 800, 600, "befor");
		int[] b = new int[a.length];
		for(int i=0; i<start; i++) b[i]=a[i];
		for(int i=end; i<b.length; i++) b[i]=a[i];		
		for (int i = start+1; i < end-1; i++) {
			int s = a[i];
			for (int j = 1; j < w; j++) {
				int k1 = i - j, k2 = i + j;
				if (k1 < start)
					k1 = k2;
				if (k2 >= end)
					k2 = k1;
				s += a[k1] + a[k2];
			}
			b[i] = (int)(s / (2 * w + 1.0));
		}
		//ArrayChart.showArray(b, 800, 600, "after");
		return b;
	}

	static int[] averageFilterForArray(int[] a, int w) {
		return averageFilterForArray(a, w, 0, a.length);
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

	static int getWidthPeakDistribution(int[] yhist) {

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

	static private double getSkewingAngleByHistro(BufferedImage bi, double mid,
			double width) {
		double angle = 0;
		int h = -1, num = 5;

		for (int i = -num; i < num; i++) {
			double a = mid + i * (width / num);
			BufferedImage ie = ImgTool.rotate(bi, a);
			int[] yDist = SkewingHandler.getYDistribution(ie, 150, 4, false);
			// int[] xDist = SkewingHandler.getXDistribution(ie, 150, 4, false);
			int h1 = SkewingHandler.getWidthPeakDistribution(yDist);
			if (h1 < h || h < 0) {
				h = h1;
				angle = a;
			}
			// int w = SkewingHandler.getWidthPeakYDistribution(xDist);
			// String title = String.format("Angle: %d, height: %d ", (int) (180
			// * a / Math.PI), h);
			// System.out.println(title);
			//SkewingHandler.showImg(ie, title);
		}
		return angle;
	}

	static public double getSkewingAngleByHistro(BufferedImage bi) {

		double a = 0, aOld = 66.0, width = Math.PI / 2.0, errorTolerance = Math.PI / 180;
		for (int i = 0; i < 8; i++) {
			// System.out.println(i);
			a = getSkewingAngleByHistro(bi, a, width);
			if (Math.abs(aOld - a) < errorTolerance)
				return -a;
			width /= 5.0;
			aOld = a;
		}
		// System.out.println("dddddddd");
		return -a;
	}

	static public double getSkewAngleHough(BufferedImage bi, int threshold,
			boolean inverse) {
		BufferedImage img = ImgTool.toBin(bi, threshold, inverse);
		int h = bi.getHeight(), w = bi.getWidth();
		int[] ys = new int[w], xs = new int[h];
		int rgbWhite = Color.WHITE.getRGB();
		for (int i = 0; i < w; i++)
			ys[i] = -1;
		for (int i = 0; i < h; i++)
			xs[i] = -1;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				// if(xs[y]>=0 && ys[x]>=0) break;
				int rgb = img.getRGB(x, y);
				if (rgb != rgbWhite)
					continue;
				xs[y] = x;
				ys[x] = y;
				break;
			}
		}
		//ArrayChart.showArray(xs, 800, 600, "before: " + w + " x " + h);
		//ArrayChart.showArray(ys, 800, 600, "" + w + " x " + h);
		
		linearFilling(xs);
		linearFilling(ys);
		ArrayChart.showArray(ys, 800, 600, "" + w + " x " + h);
		ArrayChart.showArray(xs, 800, 600, "after: " + w + " x " + h);

	
		int[] wxs = positiveWidthArray(xs), wys = positiveWidthArray(ys);
		int[] xxs=null;
		int[] yys=null;
		for(int i=0; i<5; i++){
			yys=SkewingHandler.averageFilterForArray(ys, 4, wys[0], wys[1]);
			xxs=SkewingHandler.averageFilterForArray(xs, 4, wxs[0], wxs[1]);
			ys=yys;
			xs=xxs;
		}
		ArrayChart.showArray(yys, 800, 600, "---" + w + " x " + h);
		ArrayChart.showArray(xxs, 800, 600, "---" + w + " x " + h);
		 //wxs = positiveWidthArray(xs); wys = positiveWidthArray(ys);
		
		int offset=(wys[1]-wys[0])/30;
		return Math.atan((ys[wys[1]-offset]-ys[wys[0]+offset])/(double)(xs[wxs[1]-offset]-xs[wxs[0]+offset]));
	}

	static private int[] positiveWidthArray(int[] l) {
		
		int d[]=new int[2]; d[0]= -1;
		for (int i = 0; i < l.length; i++) {
			if (l[i] > 0 && d[0] < 0)
				d[0] = i;
			if (l[i] <= 0 && d[0]> 0){
				d[1]=i;
				return d;
			}
		}
		d[0]=0; d[1]=l.length;
		return d;
	}

	static private void linearFilling(int[] x) {
		int start, end;
		for (int i = 0; i < x.length; i = end) {
			start = -1;
			end = -1;
			for (int j = i; j < x.length && end < 0; j++) {
				if (x[j] < 0)
					continue;
				if (start < 0)
					start = j;
				else {
					end = j;
					break;
				}
			}
			if (end < 0)
				break;
			int step = (x[end] - x[start]) / (end - start);
			for (int j = start + 1; j < end; j++) {
				x[j] = x[start] + step * (j - start);
			}
		}
	}
	


	public static void main(String[] args) throws IOException {
		final String pathImage = "/home/bart/Pictures/c.png";
		BufferedImage ia = ImageIO.read(new File(pathImage));
		BufferedImage ic = ImgTool.scale(ia, 800, 800);
//		BufferedImage id = ImgTool.rotate(ic, Math.PI / 6);
		BufferedImage id = ImgTool.rotate(ic, 0.5);

		double a=getSkewAngleHough(id, 150, false);
		double b = SkewingHandler.getSkewingAngleByHistro(id);
		System.out.println(String.format("angle is : %f(correct answer), %f(hough), %f(histogram)", Math.toDegrees(1.0), Math.toDegrees(a), Math.toDegrees(b)));

	}

}
