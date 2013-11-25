package proj.misc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

public class ArrayChart extends Component {

	int[] data;
	int w, h, xMax, xMin;
	//int[] xdata;

	public ArrayChart(int[] l, int w, int h) {
		super();
		this.data= l;
		this.w = w;
		this.h = h;
		this.xMin = this.xMax;
		for (int i = 0; i < data.length; i++) {
			if (data[i] > this.xMax)
				this.xMax = data[i];
			if (data[i] < this.xMin)
				this.xMin = data[i];
		}
		
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.width = this.w;
		d.height = this.h;
		return d;
	}

	@Override
	public void paint(Graphics g) {
		double interval = this.w /(double) data.length;
		for (int i = 0; i < data.length; i++) {
			g.drawLine((int)(interval * i), (data[i] - this.xMin) * this.h
					/ (this.xMax - this.xMin), (int)(i*interval) , this.h);
		}
	}

	public static void showArray(int[] xs, int w, int h, String title){
		
		JFrame jf=new JFrame(title);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ArrayChart ac=new ArrayChart(xs, w, h);
		jf.getContentPane().add(ac);
		jf.pack();
		jf.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] xs=new int[88];
		for(int i=0; i<xs.length; i++){xs[i]=i;}
		showArray(xs, 800, 600, "");

	}

}
