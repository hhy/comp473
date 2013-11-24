package proj.preprocess;

import java.awt.List;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SlantCorrector {
	public int[][] pic;

	public SlantCorrector(int[][] pic) {
		this.pic = pic;
	}

	public double getMoment(int p, int q) {
		return this.getCenterMoment(p, q, 0, 0);
	}

	public double getCentroidX() {
		double m00 = this.getMoment(0, 0);
		return this.getMoment(1, 0) / m00;
	}

	public double getCentroidY() {
		double m00 = this.getMoment(0, 0);
		return this.getMoment(0, 1) / m00;
	}

	public double getCenterMoment(int p, int q, double xc, double yc) {
		double m = 0;
		final int XL = this.pic[0].length, YL = this.pic.length;

		for (int x = 0; x < XL; x++) {
			for (int y = 0; y < YL; y++) {
				m += Math.pow((double) x - xc, p)
						* Math.pow((double) y - yc, q)
						* (double) (this.pic[y][x]);
			}
		}
		return m;
	}

	public double getSlantTan() {
		double xc = this.getCentroidX(), yc = this.getCentroidY();
		return -this.getCenterMoment(1, 1, xc, yc)
				/ this.getCenterMoment(0, 2, xc, yc);

	}

	int[][] correctSlant() {

		final int XL = this.pic[0].length, YL = this.pic.length;
		int[][] result = new int[YL][];
		for (int y = 0; y < YL; y++) {
			result[y]=new int[XL];
			for (int x = 0; x < XL; x++) {
				result[y][x] = 0;
			}
		}
		double yc = this.getCentroidY();
		double tan = this.getSlantTan();

		for (int y = 0; y < YL; y++) {
			// System.out.println("-->" + (y - yc) * tan);
			for (int x = 0; x < XL; x++) {
				int xx = (Math.round(Math.round(x + (y - yc) * tan)) + XL) % XL;
				// System.out.println(y+","+xx+", "+x);
				result[y][xx] = this.pic[y][x];
			}
		}

		return result;
	}
	public static int[][] correctSlantProcess(int[][] m){
		SlantCorrector sc=new SlantCorrector(m);
		return sc.correctSlant();
	}

}
