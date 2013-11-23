package main;

import java.awt.image.BufferedImage;

public class PreProcessImage
{
	public static BufferedImage perform(BufferedImage img)
	{		
		BufferedImage processedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		// copy the original img to the processedImage
		for (int i = 0; i < img.getWidth(); i++)
		{
			for (int j = 0; j < img.getHeight(); j++)
			{
				processedImage.setRGB(i, j, img.getRGB(i, j));
			}
		}

		processedImage = PreProcessor.normalize(processedImage);
		
		processedImage = PreProcessor.thin(processedImage);		
		
		return processedImage;
	}
}
