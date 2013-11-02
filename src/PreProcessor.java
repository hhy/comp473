package proj;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 
 * @author Haiyang Huang
 * 
 */

public abstract class PreProcessor {

	BufferedImage img;

	public PreProcessor(String pathImageFile) throws IOException {
		this.img = ImageIO.read(new File(pathImageFile));
	}

	abstract BufferedImage removeNoise();

	abstract BufferedImage correctSkewing();

	abstract List<BufferedImage> extract();

	abstract BufferedImage correctSlant();

	abstract BufferedImage normalize();

	abstract BufferedImage thin();

}
