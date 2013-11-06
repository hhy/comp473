package proj;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HistroChart extends JPanel {
	int[] xdata, ydata;
	int w, h;
	int xMax, xMin, yMax, yMin;
	BufferedImage bi;
	public HistroChart(BufferedImage bi,int w, int h) {
		this.bi=bi;
		
		this.xdata =SkewingCorrector.getXDistribution(this.bi, 150, 2, true); 
		this.ydata =SkewingCorrector.getYDistribution(this.bi, 150, 2, true); 
		this.w = w;
		this.h = h;
		this.xMax = xdata[0];
		this.xMin = this.xMax;
		for (int i = 0; i < xdata.length; i++) {
			if (xdata[i] > this.xMax)
				this.xMax = xdata[i];
			if (xdata[i] < this.xMin)
				this.xMin = xdata[i];
		}
		this.yMin=ydata[0]; this.yMax=this.yMin;
		for (int i = 0; i < ydata.length; i++) {
			if (ydata[i] > this.yMax)
				this.yMax = ydata[i];
			if (ydata[i] < this.yMin)
				this.yMin = ydata[i];
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.width = this.w+this.padding;
		d.height = this.h+this.padding;
		return d;
	}

	int padding=250;
	@Override
	public void paint(Graphics g) {
		g.drawImage(this.bi, 0, 0, this.w, this.h, null);
		double interval = this.w /(double) xdata.length;
		for (int i = 0; i < xdata.length; i++) {
			g.drawLine((int)(interval * i), (xdata[i] - this.xMin) * this.padding
					/ (this.xMax - this.xMin) + this.h, (int)(i*interval) , this.h+this.padding-2);
		}
		interval = this.h / (double)ydata.length;
		for (int i = 0; i < ydata.length; i++) {
			g.drawLine((ydata[i] - this.yMin) * this.padding
					/ (this.yMax - this.yMin) + this.w, (int)(interval *i), this.w+this.padding-2, (int)(interval * i));
		}
	}
	
	public static void showHistroDistrib(BufferedImage bi, int w, int h, String title){
		HistroChart hc=new HistroChart(bi, w, h);
		JFrame jf = new JFrame(title);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().add(hc);
		jf.pack();
		jf.setVisible(true);
	}



	static public void main(String[] args) throws IOException {
		final String pathImage = "/home/bart/Pictures/c.png";
		BufferedImage ia = ImageIO.read(new File(pathImage));
		BufferedImage ib=ImgTool.rotate(ia, Math.PI/50);
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().add(new HistroChart(ib, 800, 500));
		jf.pack();
		jf.setVisible(true);
	}

}
