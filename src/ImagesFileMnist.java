package proj;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImagesFileMnist extends MnistFile {

	ImagesFileMnist(String name) throws FileNotFoundException {
		super(name);
	}

	List<BufferedImage> getImages() throws IOException {
		List<BufferedImage> imgs = new ArrayList<BufferedImage>();
		this.init();
		// for(int i=0; i<this.numObjects; i++){
		for (int i = 0; i < 1; i++) {
			int rows = this.readInt(), cols = this.readInt();
			System.out.println(String.format(
					"get image, [rows: %d, columns: %d]", rows, cols));
			BufferedImage img = new BufferedImage(cols, rows,
					BufferedImage.TYPE_INT_BGR);
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					int b = this.read();
					img.setRGB(c, r, b);
				}
			}
			imgs.add(img);

		}
		return imgs;
	}

	void show(BufferedImage img) {
		JFrame jf = new JFrame();
		jf.setBounds(50, 50, 800, 600);
		JPanel jp = new JPanel();
		jf.add(jp);
		Graphics g = jp.getGraphics();
		
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		jf.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		ImagesFileMnist imgsFile = new ImagesFileMnist(Main.pathImageTraining);
		List<BufferedImage> imgs = imgsFile.getImages();
		System.out.println(imgsFile.info());
		BufferedImage img = imgs.get(0);
		imgsFile.show(img);

		imgsFile.close();

	}

}
