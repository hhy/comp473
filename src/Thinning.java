package main;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Thinning
{
	private static int numberOfChanges = 0;
	
	// thinning process using Hilditch’s Thinning Algorithms
	public static BufferedImage thin(BufferedImage img)
	{
		// create an image that has the contour of one pixel
		BufferedImage contouredImage = new BufferedImage(img.getWidth() + 2, img.getHeight() + 2, BufferedImage.TYPE_INT_RGB);
		
		// reset contouredImage image to white
		for (int i = 0; i < contouredImage.getWidth(); i++)
		{
			for (int j = 0; j < contouredImage.getHeight(); j++)
			{
				contouredImage.setRGB(i, j, Color.WHITE.getRGB());
			}
		}

		// copy the original img to the center of the image with contour of one pixel
		for (int i = 0; i < img.getWidth(); i++)
		{
			for (int j = 0; j < img.getHeight(); j++)
			{
				contouredImage.setRGB(i + 1, j + 1, img.getRGB(i, j));
			}
		}

		while (true)
		{
			numberOfChanges = 0;
	
			// Hilditch’s Thinning Algorithms
			
			// the four conditions
			int numberOfOnes = 0;
			int numberOfTransition = 0;
			int directNeighborOne = 0;
			int directNeighborTwo = 0;
			
			// all eight neighbors
			int pixels[] = new int[8];
			
			// start looping over the image and do thinning
			for (int i = 1; i < contouredImage.getWidth() - 1; i++)
			{
				for (int j = 1; j < contouredImage.getHeight() - 1; j++) 
				{
					if(contouredImage.getRGB(i, j) != Color.WHITE.getRGB())
					{
						numberOfOnes = 0;
						numberOfTransition = 0;
						// calculate the 8-neighbor pixels
						pixels[0] = getPixelBinaryValue(contouredImage.getRGB(i, j - 1));
						pixels[1] = getPixelBinaryValue(contouredImage.getRGB(i + 1, j - 1));
						pixels[2] = getPixelBinaryValue(contouredImage.getRGB(i + 1, j));
						pixels[3] = getPixelBinaryValue(contouredImage.getRGB(i + 1, j + 1));
						pixels[4] = getPixelBinaryValue(contouredImage.getRGB(i, j + 1));
						pixels[5] = getPixelBinaryValue(contouredImage.getRGB(i - 1, j + 1));
						pixels[6] = getPixelBinaryValue(contouredImage.getRGB(i - 1, j));
						pixels[7] = getPixelBinaryValue(contouredImage.getRGB(i - 1, j - 1));
						
						// calculate the number of ones and the number of transitions in the 8-neighbor pixels
						for (int k = 0; k < pixels.length; k++)
						{
							numberOfOnes += pixels[k];
							// if there is a transition from zero to one
							if(pixels[k] < pixels[(k + 1) % pixels.length])
							{
								numberOfTransition++;
							}
						}
						
						// calculate the direct neighbors values
						directNeighborOne = pixels[0] * pixels[2] * pixels[4];
						directNeighborTwo = pixels[2] * pixels[4] * pixels[6];
						
						// if any of the four conditions fail the pixel stay as is
						if(numberOfOnes > 2 && numberOfOnes <= 6 && numberOfTransition == 1 && directNeighborOne == 0 && directNeighborTwo == 0)
						{
							numberOfChanges++;
							contouredImage.setRGB(i, j, Color.WHITE.getRGB());
						}
					}
				}
			}
			if (numberOfChanges == 0)
			{
				break;
			}			
		}
		// initialize an object "newImage" of type BufferedImage that will contain the new thin image
		BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		// copy the thin image to the returned object
		for (int i = 1; i < contouredImage.getWidth() - 1; i++)
		{
			for (int j = 1; j < contouredImage.getHeight() - 1; j++) 
			{
				newImage.setRGB(i - 1, j - 1, contouredImage.getRGB(i, j));
			}
		}		
		return newImage;
	}
	
	
	// return one if the pixel is not blank otherwise return zero
	private static int getPixelBinaryValue(int pixelColor)
	{
		Color color = new Color(pixelColor);
		// if the color is not white return one
		if (((color.getBlue() + color.getGreen() + color.getRed()) / 3) == 255)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	
}
