package proj;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;

import proj.mnist.ImagesFileMnist;
import proj.mnist.TrainingData;

public class Normalizer {
	// normalize the size of the input image to the specified newWidth and
	// newHeight
	static public BufferedImage normalize(BufferedImage img, int newWidth,
			int newHeight) {
		// initialize an object "newImage" of type BufferedImage that will
		// contain the new normalized image
		BufferedImage newImage = new BufferedImage(newWidth, newHeight,
				BufferedImage.TYPE_INT_RGB);

		// variables to hold the four edges of an actual text area
		int imageLeftEdge = 0;
		int imageRightEdge = 0;
		int imageTopEdge = 0;
		int imageBottomEdge = 0;

		// variables to hold the actual dimension of the image
		int actualImageHight = 0;
		int actualImageWidth = 0;

		// variables to hold the aspect ratios between the new and original
		// image size
		double widthAspectRatio = 0;
		double heightAspectRatio = 0;

		// flag to indicate that an edge is found
		boolean edgeFound = false;

		// variables to hold the original image X-axis and Y-axis values of a
		// pixel
		int originalPixelXLocation = 0;
		int originalPixelYLocation = 0;

		// find the top edge of the original image
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int temp = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
				// if the color is not white, the edge is found
				if (temp != 255) {
					imageTopEdge = i;
					edgeFound = true;
					break;
				}
			}
			if (edgeFound == true) {
				break;
			}
		}

		// find the bottom edge of the original image
		edgeFound = false;
		for (int i = img.getHeight() - 1; i >= 0; i--) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int temp = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
				// if the color is not white, the edge is found
				if (temp != 255) {
					imageBottomEdge = i;
					edgeFound = true;
					break;
				}
			}
			if (edgeFound == true) {
				break;
			}
		}

		// find the left edge of the original image
		edgeFound = false;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color color = new Color(img.getRGB(i, j));
				int temp = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
				// if the color is not white, the edge is found
				if (temp != 255) {
					imageLeftEdge = i;
					edgeFound = true;
					break;
				}
			}
			if (edgeFound == true) {
				break;
			}
		}

		// find the right edge of the original image
		edgeFound = false;
		for (int i = img.getWidth() - 1; i >= 0; i--) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color color = new Color(img.getRGB(i, j));
				int temp = (color.getBlue() + color.getGreen() + color.getRed()) / 3;
				// if the color is not white, the edge is found
				if (temp != 255) {
					imageRightEdge = i;
					edgeFound = true;
					break;
				}
			}
			if (edgeFound == true) {
				break;
			}
		}

		// calculate the actual size of the image
		actualImageHight = imageBottomEdge - imageTopEdge + 1;
		actualImageWidth = imageRightEdge - imageLeftEdge + 1;

		// calculate the aspect ratio between the original image size and the
		// new image size
		heightAspectRatio = (double) newHeight / actualImageHight;
		widthAspectRatio = (double) newWidth / actualImageWidth;

		// calculate the new image pixels values
		for (int newPixelYLocation = 0; newPixelYLocation < newHeight; newPixelYLocation++) {
			for (int newPixelXLocation = 0; newPixelXLocation < newWidth; newPixelXLocation++) {
				// calculate the X-axis location of the pixel
				originalPixelXLocation = (int) (newPixelXLocation / widthAspectRatio)
						+ imageLeftEdge;
				if (originalPixelXLocation < 0) {
					originalPixelXLocation = 0;
				} else if (originalPixelXLocation >= imageRightEdge) {
					originalPixelXLocation = imageRightEdge;
				}

				// calculate the Y-axis location of the pixel
				originalPixelYLocation = (int) (newPixelYLocation / heightAspectRatio)
						+ imageTopEdge;
				if (originalPixelYLocation < 0) {
					originalPixelYLocation = 0;
				} else if (originalPixelYLocation >= imageBottomEdge) {
					originalPixelYLocation = imageBottomEdge;
				}

				newImage.setRGB(newPixelXLocation, newPixelYLocation, img
						.getRGB(originalPixelXLocation, originalPixelYLocation));
			}
		}
		return newImage;
	}

	// thinning process using Hilditch’s Thinning Algorithms
	public BufferedImage thin(BufferedImage img) {
		// initialize an object "newImage" of type BufferedImage that will
		// contain the new thin image
		BufferedImage newImage = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);

		// white color value
		Color whiteColor = new Color(255, 255, 255);

		// reset newImage to white color
		for (int i = 0; i < newImage.getWidth(); i++) {
			for (int j = 0; j < newImage.getHeight(); j++) {
				newImage.setRGB(i, j, whiteColor.getRGB());
			}
		}

		// temporary object to hold the original image during the calculation
		BufferedImage tempImage = new BufferedImage(img.getWidth() + 2,
				img.getHeight() + 2, BufferedImage.TYPE_INT_RGB);

		// reset temporary image to white
		for (int i = 0; i < tempImage.getWidth(); i++) {
			for (int j = 0; j < tempImage.getHeight(); j++) {
				tempImage.setRGB(i, j, whiteColor.getRGB());
			}
		}

		// copy the original image to the center of the temporary image
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				tempImage.setRGB(i + 1, j + 1, img.getRGB(i, j));
			}
		}

		// Hilditch’s Thinning Algorithms

		// the four conditions
		int numberOfOnes = 0;
		int numberOfTransition = 0;
		int directNeighborOne = 0;
		int directNeighborTwo = 0;

		// all eight neighbors
		int pixels[] = new int[8];

		// start looping over the image and do thinning
		for (int i = 1; i < tempImage.getWidth() - 1; i++) {
			for (int j = 1; j < tempImage.getHeight() - 1; j++) {
				numberOfOnes = 0;
				numberOfTransition = 0;

				// calculate the 8-neighbor pixels
				pixels[0] = getPixelBinaryValue(tempImage.getRGB(i, j - 1));
				pixels[1] = getPixelBinaryValue(tempImage.getRGB(i + 1, j - 1));
				pixels[2] = getPixelBinaryValue(tempImage.getRGB(i + 1, j));
				pixels[3] = getPixelBinaryValue(tempImage.getRGB(i + 1, j + 1));
				pixels[4] = getPixelBinaryValue(tempImage.getRGB(i, j + 1));
				pixels[5] = getPixelBinaryValue(tempImage.getRGB(i - 1, j + 1));
				pixels[6] = getPixelBinaryValue(tempImage.getRGB(i - 1, j));
				pixels[7] = getPixelBinaryValue(tempImage.getRGB(i - 1, j - 1));

				// calculate the number of ones and the number of transitions in
				// the 8-neighbor pixels
				for (int k = 0; k < pixels.length; k++) {
					numberOfOnes += pixels[k];
					// if there is a transition from zero to one
					if (pixels[k] < pixels[(k + 1) % pixels.length]) {
						numberOfTransition++;
					}
				}

				// calculate the direct neighbors values
				directNeighborOne = pixels[0] * pixels[2] * pixels[4];
				directNeighborTwo = pixels[2] * pixels[4] * pixels[6];

				// if any of the four conditions fail the pixel stay as is
				if (!(numberOfOnes >= 2 && numberOfOnes <= 6
						&& numberOfTransition == 1 && directNeighborOne == 0 && directNeighborTwo == 0)) {
					newImage.setRGB(i - 1, j - 1, img.getRGB(i - 1, j - 1));
				}
			}
		}
		return newImage;
	}

	// return one if the pixel is not blank otherwise return zero
	private int getPixelBinaryValue(int pixelColor) {
		Color color = new Color(pixelColor);
		// if the color is not blank return one
		if (((color.getBlue() + color.getGreen() + color.getRed()) / 3) != 255) {
			return 1;
		} else {
			return 0;
		}
	}

	public static void main(String[] args) throws IOException {
		ImagesFileMnist fImg = new ImagesFileMnist(
				TrainingData.pathImageTraining);
		BufferedImage im=Normalizer.normalize((BufferedImage)fImg.getObject(6), 400, 400);
		ImgTool.showImg(im,"",  400);
		
	}
}
