package proj.misc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import proj.preprocess.Normalizor;
import proj.preprocess.SkewingCorrector;

public class PreProcessor {
	BufferedImage bi;

	PreProcessor(BufferedImage bi) {
		this.bi = bi;
	}

	

	BufferedImage removeNoise() {
		return null;
	}

	/**
	 * 
	 * @return
	 */
	static BufferedImage correctSkewing(BufferedImage bi) {
		return SkewingCorrector.correctSkewing(bi);

	}

	List<BufferedImage> extract() {
		return null;
	}

	BufferedImage correctSlant() {
		return null;
	}

//	static BufferedImage normalize(BufferedImage img)
//	{
//		return Normalizor.normalize(img, 100, 100);
//	}
//
//	static BufferedImage thin(BufferedImage img)
//	{
//		return Thinning.thin(img);
//		
//	}


	

	

	

}
