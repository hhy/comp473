package proj.feature.gradient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import proj.misc.ImgTool;
import proj.mnist.MatrixFile;
import proj.mnist.TrainingData;

public class GradientVectorZoned {
	private double vectors[][];

	public GradientVectorZoned(int[][] img, int levels) {
		this.setVectors(img, levels);
		this.mergeZones();
		
	}
	
	public static double[] getGradient(int[][] img, int levels){
		GradientVectorZoned gv=new GradientVectorZoned(img, levels);
		return gv.mergeZones();
	}

	public double[] mergeZones(){
		int h=this.vectors.length, w=this.vectors[0].length;
		double[] all=new double[w*h];
		for(int y=0; y<h; y++){
			for(int x=0; x<w; x++)
				all[y*w+x]=vectors[y][x];
		}
		return all;
	}
	
	private void setVectors(int[][] img, int levels) {
		GradientOfMatrix mgz = new GradientOfMatrix(img, true);
		Gradient[][][][] gss = mgz.gss;

		this.vectors = new double[gss.length * gss[0].length][];
		// List<double[]> _vectors = new ArrayList<double[]>();

		// this.vectors=_vectors;
		//System.out.println(String.format("%d x %d", gss.length, gss[0].length));
		for (int yZone = 0; yZone < gss.length; yZone++) {
			for (int xZone = 0; xZone < gss[0].length; xZone++) {
				double[] dd = this.getFeatureVector(gss[yZone][xZone], levels);
				this.vectors[yZone * gss.length + xZone] = dd;

			}
		}
		

	}

	private double[] getFeatureVector(Gradient[][] g, int levels) {
		double[] v = new double[levels];
		double aa = Math.PI * 2 / levels;
		for (int i = 0; i < v.length; i++) {
			v[i] = 0;
		}
		for (int y = 0; y < g.length; y++)
			for (int x = 0; x < g[0].length; x++) {
				double a = g[y][x].angle;
				int ind = (int) ((a < 0 ? a + 2 * Math.PI : a) / aa);
				if (ind < 0 || ind > levels - 1)
					System.out.println("!!!!!");
				v[ind] += g[y][x].strength;
			}

		return v;
	}

	public static void main(String[] args) throws IOException {
		MatrixFile m = new MatrixFile(TrainingData.pathImageTraining);
		int[][] img = (int[][]) m.getObject(4);
		ImgTool.showImg(img, "", 400);

		for (int i = 0; i < img.length; i++) {
			System.out.println();
			for (int j = 0; j < img[0].length; j++) {
				System.out.print(String.format("%03d, ", img[i][j]));
			}
		}

		GradientVectorZoned mv = new GradientVectorZoned(img, 16);
		for (int y = 0; y < mv.vectors.length; y++) {
			System.out.print(String.format("---%02d---", y));
			for (int x = 0; x < mv.vectors[y].length; x++) {
				System.out.print(String.format("[%09.2f], ", mv.vectors[y][x]));
			}
			System.out.println();
		}

		GradientOfMatrix mg = new GradientOfMatrix(img);

		GradientOfMatrix mgz = new GradientOfMatrix(img, true);

		

	}
}
