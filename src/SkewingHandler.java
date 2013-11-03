package proj;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SkewingHandler {

	static double getSkewAngle(BufferedImage bi, int threadhold) {

		BufferedImage img = ImgTool.toBin(bi, threadhold, false);
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
	public static void main(String[] args) {
		

	}

}
