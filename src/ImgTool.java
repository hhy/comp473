package proj;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ImgTool {

	static public BufferedImage scale(BufferedImage bi, int width, int height) {
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		double rw = (double) bi.getWidth() / width, rh = (double) bi
				.getHeight() / height;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				img.setRGB(x, y, bi.getRGB((int) (x * rw), (int) (y * rh)));
			}
		return img;
	}

	static public BufferedImage rotate(BufferedImage bi, double a) {
		/*
		 * normalize angle between [-PI, PI)
		 */
		while (a < 0)
			a += Math.PI * 2;
		while (a >= Math.PI * 2)
			a -= Math.PI * 2;
		a -= Math.PI;

		int wSrc = bi.getWidth(), hSrc = bi.getHeight();
		Point pUL = new Point(0, 0), pUR = new Point(wSrc - 1, 0), pDL = new Point(
				0, hSrc - 1), pDR = new Point(wSrc - 1, hSrc - 1);
		Point ppUL = pUL, ppUR = rotatePoint(pUR, a), ppDL = rotatePoint(pDL, a), ppDR = rotatePoint(
				pDR, a);
		int wDst = Math.max(Math.abs(ppUL.x - ppDR.x),
				Math.abs(ppUR.x - ppDL.x)), hDst = Math.max(
				Math.abs(ppUL.y - ppDR.y), Math.abs(ppUR.y - ppDL.y));
		BufferedImage img = new BufferedImage(wDst, hDst,
				BufferedImage.TYPE_INT_RGB);

		/*
		 * compute the offset of the output
		 */
		int top = 0, left = 0;
		if (a > Math.PI / 2) {
			top = ppDR.y;
			left = ppUR.x;
		} else if (a > 0) {
			top = ppUR.y;
			left = 0;
		} else if (a > -Math.PI / 2) {
			top = 0;
			left = ppDL.x;
		} else {
			top = ppDL.y;
			left = ppDR.x;
		}
		
		System.out.println(String.format(""));

		double cos = Math.cos(a), sin = Math.sin(a);
		for (int x = left; x < left + wDst; x++)
			for (int y = top; y < top + hDst; y++) {
				int xSrc = (int) (x * cos - y * sin), ySrc = (int) (x * sin + y
						* cos);
				if(xSrc<0)xSrc=0;if(xSrc>=wSrc) xSrc=wSrc-1;
				if(ySrc<0)ySrc=0;if(ySrc>=hSrc) ySrc=hSrc-1;
				
				img.setRGB(x - left, y - top, bi.getRGB(xSrc, ySrc));
			}

		return img;
	}

	static private Point rotatePoint(Point p, double a) {
		double cos = Math.cos(a), sin = Math.sin(a);
		return new Point((int) (p.x * cos + p.y * sin),
				(int) (p.x * (-sin) + p.y * cos));
	}

	static BufferedImage toBin(BufferedImage bi, int threadhold,
			boolean inverse) {
		BufferedImage img = new BufferedImage(bi.getWidth(), bi.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < bi.getWidth(); x++)
			for (int y = 0; y < bi.getHeight(); y++) {
				int rgb = bi.getRGB(x, y);
				int grayscale = ((rgb & 0x0ff) + ((rgb >> 8) & 0x0ff) + ((rgb >> 16) & 0x0ff)) / 3;
				if (grayscale > threadhold)
					img.setRGB(x, y, inverse ? Color.WHITE.getRGB()
							: Color.BLACK.getRGB());
				else
					img.setRGB(x, y, inverse ? Color.BLACK.getRGB()
							: Color.WHITE.getRGB());
			}
		return img;
	}
	
	static BufferedImage toGray(BufferedImage bi) {
		BufferedImage img = new BufferedImage(bi.getWidth(), bi.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < bi.getWidth(); x++)
			for (int y = 0; y < bi.getHeight(); y++) {
				int rgb = bi.getRGB(x, y);
				int grayscale = ((rgb & 0x0ff) + ((rgb >> 8) & 0x0ff) + ((rgb >> 16) & 0x0ff)) / 3;
				grayscale = (grayscale << 16) + (grayscale << 8) + grayscale;
				img.setRGB(x, y, grayscale);
			}
		return img;
	}

	public static void showImg(BufferedImage img, String title, int sideLength) {
		ImgComponent ic = new ImgComponent(img);
		ic.sideLength=sideLength;
		JFrame fr = new JFrame(String.format(
				"label: %s - [width: %d, height: %d]", title, img.getWidth(),
				img.getHeight()));
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.getContentPane().add(ic);
		fr.pack();
		fr.setVisible(true);
	}
	
	static double getSkewAngle(BufferedImage bi, int threadhold) {

		BufferedImage img = toBin(bi, threadhold, false);
		int w = bi.getWidth(), h = bi.getHeight();
		int[] yhist = new int[h];
		int rgbWhite = Color.white.getRGB();
		for (int y = 0; y < h; y++) {
			yhist[y] = 0;
			
				for (int x = 0; x < w; x++) {
				int rgb = img.getRGB(x, y);
				if (rgb == rgbWhite)
					yhist[x]++;
			}
		}
		return 0;
	}

	public static void main(String[] args) throws IOException {
		final String pathImage = "/home/bart/Pictures/a.png";
		BufferedImage ia = ImageIO.read(new File(pathImage));
		//BufferedImage ic=ImgTool.scale(ia, 800, 800);
		BufferedImage ib = rotate(ia, Math.PI / 6);
		
		ImgTool.showImg(ia, "", 800);
		
	}

}
