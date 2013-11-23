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


public class MatrixFile extends MnistFile {

	public MatrixFile(String name) throws IOException {
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
		//BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[][] img=new int[this.height][];
		for (int r = 0; r < this.height; r++) {
			img[r]=new int[width];
			for (int c = 0; c < this.width; c++) {
				int b = this.read();
				//b = (b << 16) + (b << 8) + b;
				//img.setRGB(c, r, b);
				img[r][c]=b;
			}
		}

		return img;
	}
	
	

}
