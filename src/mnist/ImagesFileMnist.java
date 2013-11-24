package proj.mnist;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImagesFileMnist extends MatrixFile {

	public ImagesFileMnist(String name) throws IOException {
		super(name);
	}

	@Override
	Object getObjectFromStream() throws IOException {
		int[][] m = (int[][]) super.getObjectFromStream();
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int r = 0; r < this.height; r++) {
			for (int c = 0; c < this.width; c++) {
				img.setRGB(c, r, m[r][c]);
			}
		}

		return img;
	}

}
