package proj.mnist.histro;

import java.awt.image.BufferedImage;
import java.io.IOException;

import proj.HistroChart;
import proj.ImgTool;
import proj.mnist.ImagesFileMnist;
import proj.mnist.LabelsFileMnist;
import proj.mnist.MatrixFile;
import proj.mnist.TrainingData;
import proj.preprocess.Binarizor;
import proj.preprocess.Normalizor;
import proj.preprocess.SlantCorrector;
import proj.preprocess.ThinningDitchs;

public class HistroDB {

	LabelsFileMnist fLabel;
	MatrixFile fMatrix;

	public HistroDB(LabelsFileMnist fLabel, MatrixFile fMatrix) {
		this.fLabel = fLabel;
		this.fMatrix = fMatrix;
	}

	public HistroInfo getHistro(int index) throws IOException {
		return getHistro(index, true, true, true, 10);
	}

	public HistroInfo getHistro(int index, boolean standardization,
			boolean slantCorrect, boolean toBinaryFirst, int threshold)
			throws IOException {

		int[][] m = (int[][]) this.fMatrix.getObject(index);
//		this.show(m, "original", 4);
		//ImgTool.showImg(m, "original", 400);
		if(toBinaryFirst){
			m=Binarizor.toBinary(m, threshold);
			//m=ThinningDitchs.doThinning(m);
		}
//		this.show(m, "after", 4);
//		ImgTool.showImg(m, "after", 400, true);

		if (slantCorrect) {
			m = SlantCorrector.correctSlantProcess(m);
		}
		if (standardization) {
			m = Normalizor.normalize(m);
		}

		int label = (Integer) this.fLabel.getObject(index);
		int[][] hs = this.getHistInfo(m);
		return new HistroInfo(hs[0], hs[1], hs[2], hs[3], label);

	}

	private int[][] getHistInfo(int[][] m) {
		return getHistInfo(m, 0);
	}

	

	private int[][] getHistInfo(int[][] m, int threshold) {

		int w = m.length, h = m[0].length;

		int[] xs = new int[w];
		int[] ys = new int[h];
		int[] nes=new int[w+h-1];
		int[] ses=new int[w+h-1];
		for (int i = 0; i < w; i++){
			ys[i] = 0;
			xs[i] = 0;
		}
		
		for(int i=0; i<w+h-1; i++){
			ses[i]=0;
			nes[i]=0;
		}
			
		

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (m[y][x] > threshold) {
					xs[x]++;
					ys[y]++;
					nes[x+y]++;
					ses[x-y+w-1]++;
				}
			}
		}
		return new int[][] { xs, ys, nes, ses};
	}
	
	public void show(int[][] mm, String title, int width) {
		System.out.println("--------------<" + title + ">----------");
		for (int n = 0; n <mm.length; n++) {
			for (int m = 0; m < mm[0].length; m++) {
				System.out.print(String.format("%" + (width==0?"":width) + "d",
						mm[n][m]));
			}
			System.out.println();
		}
		System.out.println("------------<" + title + ">(end)-------");
		System.out.println();
	}

	static public void main(String[] args) throws IOException {
		LabelsFileMnist fLabel = new LabelsFileMnist(
				TrainingData.pathLabelTraining);
		MatrixFile fMatrix = new MatrixFile(TrainingData.pathImageTraining);
		
		int[][] m=(int[][]) fMatrix.getObject(6);
		m=Binarizor.toBinary(m, 3);
		m=ThinningDitchs.doThinning(m);
		ImgTool.showImg(m, "", 400, true);
		
		
		
		
		

		HistroDB hs = new HistroDB(fLabel, fMatrix);
		for (int index = 0; index < 60000; index++) {
			HistroInfo hi = hs.getHistro(index);
			System.out.println(String.format("training %d, %d",index, hi.label));
		}

		fLabel = new LabelsFileMnist(TrainingData.pathLabelTest);
		fMatrix = new MatrixFile(TrainingData.pathImageTest);

		hs = new HistroDB(fLabel, fMatrix);
		for (int index = 0; index < 10000; index++) {
			
				
			HistroInfo hi=hs.getHistro(index);
			System.out.println(String.format("testing %d, %d",index, hi.label));
			
		}
		

		// ImagesFileMnist fImg=new
		// ImagesFileMnist(TrainingData.pathImageTraining);

		//HistroChart.showHistroDistrib((BufferedImage) fImg.getObject(index),
		// 300, 300, "");

	}

}
