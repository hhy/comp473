package main;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Normalization
{
	// normalize the size of the input image to the specified newWidth and newHeight
	public static BufferedImage normalize(BufferedImage img, int newWidth, int newHeight)
	{
		// initialize an object "newImage" of type BufferedImage that will contain the new normalized image
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		
		// variables to hold the four edges of an actual text area
		int imageLeftEdge = 0;
		int imageRightEdge = 0;
		int imageTopEdge = 0;
		int imageBottomEdge = 0;
		
		// variables to hold the actual dimension of the image
		int actualImageHight = 0;
		int actualImageWidth = 0;
		
		// variables to hold the aspect ratios between the new and original image size
		double widthAspectRatio = 0;
		double heightAspectRatio = 0;
		
		// flag to indicate that an edge is found
		boolean edgeFound  = false;
		
		// variables to hold the original image X-axis and Y-axis values of a pixel
		int originalPixelXLocation = 0;
		int originalPixelYLocation = 0;
		
		// find the top edge of the original image
		for (int i = 0; i < img.getHeight(); i++)
		{
			for (int j = 0; j < img.getWidth(); j++)
			{
				Color color = new Color(img.getRGB(j, i));
				int temp = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
				// if the color is not white, the edge is found
				if(temp != 255)
				{
					imageTopEdge = i;
					edgeFound  = true;
					break;
				}
			}
			if(edgeFound  == true)
			{
				break;
			}
		}
		
		// find the bottom edge of the original image
		edgeFound  = false;
		for (int i = img.getHeight() - 1; i >= 0; i--)
		{
			for (int j = 0; j < img.getWidth(); j++)
			{
				Color color = new Color(img.getRGB(j, i));
				int temp = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
				// if the color is not white, the edge is found
				if(temp != 255)
				{
					imageBottomEdge = i;
					edgeFound  = true;
					break;
				}
			}
			if(edgeFound  == true)
			{
				break;
			}
		}
		
		// find the left edge of the original image
		edgeFound  = false;
		for (int i = 0; i < img.getWidth(); i++)
		{
			for (int j = 0; j < img.getHeight(); j++)
			{
				Color color = new Color(img.getRGB(i, j));
				int temp = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
				// if the color is not white, the edge is found
				if(temp != 255)
				{
					imageLeftEdge = i;
					edgeFound  = true;
					break;
				}
			}
			if(edgeFound  == true)
			{
				break;
			}
		}		
		
		// find the right edge of the original image
		edgeFound  = false;
		for (int i = img.getWidth() - 1; i >= 0; i--)
		{
			for (int j = 0; j < img.getHeight(); j++)
			{
				Color color = new Color(img.getRGB(i, j));
				int temp = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
				// if the color is not white, the edge is found
				if(temp != 255)
				{
					imageRightEdge = i;
					edgeFound  = true;
					break;
				}
			}
			if(edgeFound  == true)
			{
				break;
			}
		}
		
		// calculate the actual size of the image
		actualImageHight = imageBottomEdge - imageTopEdge + 1;
		actualImageWidth =  imageRightEdge - imageLeftEdge + 1;
		
		// calculate the aspect ratio between the original image size and the new image size
		heightAspectRatio = (double) newHeight / actualImageHight;
		widthAspectRatio = (double) newWidth / actualImageWidth;

		// calculate the new image pixels values 
		for (int newPixelYLocation = 0; newPixelYLocation < newHeight; newPixelYLocation++)
		{
			for (int newPixelXLocation = 0; newPixelXLocation < newWidth; newPixelXLocation++)
			{
				// calculate the X-axis location of the pixel
				originalPixelXLocation = (int) (newPixelXLocation / widthAspectRatio) + imageLeftEdge;
				if(originalPixelXLocation < 0)
				{
					originalPixelXLocation = 0;
				}
				else if(originalPixelXLocation >= imageRightEdge)
				{
					originalPixelXLocation = imageRightEdge;
				}				
				
				// calculate the Y-axis location of the pixel
				originalPixelYLocation = (int) (newPixelYLocation / heightAspectRatio) + imageTopEdge;
				if(originalPixelYLocation < 0)
				{
					originalPixelYLocation = 0;
				}
				else if(originalPixelYLocation >= imageBottomEdge)
				{
					originalPixelYLocation = imageBottomEdge;
				}				

				newImage.setRGB(newPixelXLocation, newPixelYLocation, img.getRGB(originalPixelXLocation, originalPixelYLocation));
			}
		}
		return newImage;	
	}
}
