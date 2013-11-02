package proj.mnist;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ImagesFileMnist extends MnistFile {

	ImagesFileMnist(String name) throws IOException {
		super(name);
		FileChannel fc = this.getChannel();
		this.height = this.readInt();
		this.width = this.readInt();
	}

	int width, height;



	

	@Override
	int getObjectStoreSize() {
		// TODO Auto-generated method stub
		return this.width * this.height;
	}

	@Override
	int getOffsetObjectArrays() {
		// TODO Auto-generated method stub
		return 16;
	}

	@Override
	Object getObjectFromStream() throws IOException {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int r = 0; r < this.height; r++) {
			for (int c = 0; c < this.width; c++) {
				int b = this.read();
				b = (b << 16) + (b << 8) + b;
				img.setRGB(c, r, b);
			}
		}

		return img;
	}
	
	

}
