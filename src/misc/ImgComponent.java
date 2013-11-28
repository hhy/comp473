package proj.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * 
 * @author Haiyang Huang
 * 
 */

public class ImgComponent extends Component {

	BufferedImage img;

	public ImgComponent(String pathImageFile) throws IOException {
		this.img = ImageIO.read(new File(pathImageFile));
	}

	public ImgComponent(BufferedImage bi) {
		this.img = bi;
	}
	public ImgComponent(int[][] m){
		this.img=new BufferedImage(m[0].length, m.length, BufferedImage.TYPE_INT_RGB);
		for(int y=0; y<m.length; y++)for(int x=0; x<m[0].length; x++){
			int rgb=m[y][x];
			int grayscale = (rgb  + (rgb << 8) + (rgb << 16));
			this.img.setRGB(x, y, grayscale);
		}
	}
	
	public ImgComponent(int[][] m, boolean binary){
		this.img=new BufferedImage(m[0].length, m.length, BufferedImage.TYPE_INT_RGB);
		for(int y=0; y<m.length; y++)for(int x=0; x<m[0].length; x++){
			int rgb=m[y][x];
			
			int grayscale = (rgb==1? Color.WHITE.getRGB(): Color.BLACK.getRGB());
			this.img.setRGB(x, y, grayscale);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.width = sideLength==0? this.img.getWidth(): sideLength;
		d.height = sideLength==0? this.img.getHeight(): sideLength;
		return d;
	}

	@Override
	public void paint(Graphics g) {
		if (sideLength == 0)
			g.drawImage(img, 0, 0, null);
		else
			g.drawImage(img, 0, 0, sideLength, sideLength, null);
	}

	int sideLength;

	public void showImg(String title) {
		ImgTool.showImg(this.img, title, this.img.getWidth());
	}

	

	static public void main(String[] args) throws IOException {
		String pathImg = "/home/bart/course/soen423/class.png";
		ImgComponent t = new ImgComponent(pathImg);
		t.showImg("class.png");
	}

}
