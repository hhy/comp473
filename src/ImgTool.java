package proj;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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

	static public BufferedImage rotate(BufferedImage bi, double aa, boolean lib) {
		double a = -aa;
		while (a < -Math.PI)
			a += Math.PI * 2;
		while (a >= Math.PI)
			a -= Math.PI * 2;
		AffineTransform at = new AffineTransform();
		at.rotate(-a, bi.getWidth() / 2, bi.getHeight() / 2);

		// at.scale(2, 2);
		AffineTransformOp op = new AffineTransformOp(at,
				AffineTransformOp.TYPE_BICUBIC);
		// BufferedImage _bi = new BufferedImage(bi.getWidth() ,
		// bi.getHeight(), BufferedImage.TYPE_INT_RGB);
		//
		// op.filter(bi, _bi);
		// return _bi;
		BufferedImage _bi = op.filter(bi, null);
		for (int x = 0; x < _bi.getWidth(); x++)
			for (int y = 0; y < _bi.getHeight(); y++)
				if (((_bi.getRGB(x, y) >> 24) & 0x0ff) == 0)
					_bi.setRGB(x, y, Color.white.getRGB());
		return _bi;
	}

	static public BufferedImage rotate(BufferedImage bi, double a) {
		/*
		 * normalize angle between [-PI, PI)
		 */
		// System.out.print((int)(180*a/Math.PI));
		while (a < -Math.PI)
			a += Math.PI * 2;
		while (a >= Math.PI)
			a -= Math.PI * 2;

		// System.out.println("---->  "+(int)(180*a/Math.PI));

		int wSrc = bi.getWidth(), hSrc = bi.getHeight();
		Point pUL = new Point(0, 0), pUR = new Point(wSrc - 1, 0), pDL = new Point(
				0, hSrc - 1), pDR = new Point(wSrc - 1, hSrc - 1);
		Point ppUL = pUL, ppUR = rotatePoint(pUR, a), ppDL = rotatePoint(pDL, a), ppDR = rotatePoint(
				pDR, a);

		// System.out.print(String.format("%30s", "original vertex: "));
		// for(Point p: new Point[]{pUL, pUR, pDL, pDR}){
		// System.out.print(String.format("[%3d, %3d], ", p.x, p.y));
		// }
		// System.out.println();
		// System.out.print(String.format //BufferedImage ib=f.normalize(ia,
		// 500, 500);

		// BufferedImage ib=ImgTool.rotate(ia, -Math.PI/3);
		// BufferedImage ib=ImgTool.rotate(ia,0);
		// ("%30s", String.format("after rotate %d, vertex:",
		// (int)(180*a/Math.PI))));
		// for(Point p: new Point[]{ppUL, ppUR, ppDL, ppDR}){
		// System.out.print(String.format("[%3d, %3d], ", p.x, p.y));
		// }
		// System.out.println();

		int wDst = Math.max(Math.abs(ppUL.x - ppDR.x),
				Math.abs(ppUR.x - ppDL.x)) + 1, hDst = Math.max(
				Math.abs(ppUL.y - ppDR.y), Math.abs(ppUR.y - ppDL.y)) + 1;
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
			top = ppUR.y; // BufferedImage ib=f.normalize(ia, 500, 500);

			// BufferedImage ib=ImgTool.rotate(ia, -Math.PI/3);
			// BufferedImage ib=ImgTool.rotate(ia,0);

			left = 0;
		} else if (a > -Math.PI / 2) {
			top = 0;
			left = ppDL.x;
		} else {
			top = ppDL.y;
			left = ppDR.x;
		}

		// System.out.println(String.format("info: top: %d, left: %d, width: %d, height: %d",
		// top, left, wDst, hDst));

		double cos = Math.cos(a), sin = Math.sin(a);
		for (int x = left; x < left + wDst; x++)
			for (int y = top; y < top + hDst; y++) {
				int xSrc = (int) (x * cos - y * sin), ySrc = (int) (x * sin + y
						* cos);
				if (xSrc < 0)
					xSrc = 0;
				if (xSrc >= wSrc)
					xSrc = wSrc - 1;
				if (ySrc < 0)
					ySrc = 0;
				if (ySrc >= hSrc)
					ySrc = hSrc - 1;

				img.setRGB(x - left, y - top, bi.getRGB(xSrc, ySrc));
			}

		return img;
	}

	static private Point rotatePoint(Point p, double a) {
		double cos = Math.cos(a), sin = Math.sin(a);
		return new Point((int) (p.x * cos + p.y * sin),
				(int) (p.x * (-sin) + p.y * cos));
	}

	static BufferedImage toBin(BufferedImage bi, int threshold, boolean inverse) {
		BufferedImage img = new BufferedImage(bi.getWidth(), bi.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < bi.getWidth(); x++)
			for (int y = 0; y < bi.getHeight(); y++) {
				int rgb = bi.getRGB(x, y);

				int grayscale = ((rgb & 0x0ff) + ((rgb >> 8) & 0x0ff) + ((rgb >> 16) & 0x0ff)) / 3;
				if (grayscale > threshold || (((rgb >> 24) & 0x0ff) == 0))
					img.setRGB(x, y, Color.WHITE.getRGB());
				else
					img.setRGB(x, y, Color.BLACK.getRGB());
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
		ic.sideLength = sideLength;
		JFrame fr = new JFrame(String.format(
				"label: %s - [width: %d, height: %d]", title, img.getWidth(),
				img.getHeight()));
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.getContentPane().add(ic);
		fr.pack();
		fr.setVisible(true);
	}
	
	public static void showImg(int[][] img, String title, int sideLength) {
		ImgComponent ic = new ImgComponent(img);
		ic.sideLength = sideLength;
		JFrame fr = new JFrame(String.format(
				"label: %s - [width: %d, height: %d]", title, img[0].length,
				img.length));
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.getContentPane().add(ic);
		fr.pack();
		fr.setVisible(true);
	}
	
	

	public static void main(String[] args) throws IOException {
		final String pathImage = "/home/bart/Pictures/a.png";
		BufferedImage ia = ImageIO.read(new File(pathImage));
		// BufferedImage ic=ImgTool.scale(ia, 800, 800);
		ia = ImgTool.scale(ia, 100, 100);
		BufferedImage ib = rotate(ia, 0);
		// ib = rotate(ia, Math.PI / 6);
		ib = rotate(ia, -Math.PI / 6);
		// ib = ImgTool.toBin(ib, 150, false);

		ImgTool.showImg(ib, "", 800);

	}

}
