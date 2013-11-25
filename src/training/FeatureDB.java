package proj.training;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import proj.feature.FeatureType;
import proj.feature.Features;
import proj.misc.HistroChart;
import proj.misc.ImgTool;
import proj.mnist.ImagesFileMnist;
import proj.mnist.LabelsFileMnist;
import proj.mnist.MatrixFile;
import proj.mnist.TrainingData;
import proj.preprocess.Binarizor;
import proj.preprocess.Normalizor;
import proj.preprocess.PreProcessType;
import proj.preprocess.SlantCorrector;
import proj.preprocess.ThinningDitchs;

public class FeatureDB {

	LabelsFileMnist fLabel;
	MatrixFile fMatrix;
	FeatureType[] types;
	PreProcessor pre;
	int label;
	

	public FeatureDB(MatrixFile fMatrix, LabelsFileMnist fLabel, FeatureType[] types, PreProcessType[] pre) {
		this.fMatrix = fMatrix;
		this.fLabel=fLabel;
		this.types=types;
		this.pre=new PreProcessor(pre);
	}

	public Map<FeatureType, Object> getFeatures(int index) throws Exception {
		int[][] img=(int[][]) this.fMatrix.getObject(index);

		img=this.pre.process(img);
		
		Features f=new Features(img, types);
		
		return f.features;
	}
	public int getLabel(int index) throws IOException{
		 return (Integer) this.fLabel.getObject(index);

	}
	
	

//	public HistroInfo getFeatures(int index, boolean standardization,
//			boolean slantCorrect, boolean toBinaryFirst, int threshold)
//			throws IOException {
//
//		int[][] m = (int[][]) this.fMatrix.getObject(index);
////		this.show(m, "original", 4);

//
////		int label = (Integer) this.fLabel.getObject(index);
////		int[][] hs = this.getHistInfo(m);
////		return new HistroInfo(hs[0], hs[1], hs[2], hs[3], label);
//
//	}

	
	
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

	static public void main(String[] args) throws Exception {
		LabelsFileMnist fLabel = new LabelsFileMnist(
				TrainingData.pathLabelTraining);
		MatrixFile fMatrix = new MatrixFile(TrainingData.pathImageTraining);

		PreProcessType[] procedurePreProcess = new PreProcessType[] {
				PreProcessType.BINARIZATION, PreProcessType.THINNING_DITCHS,
				PreProcessType.SLANTCORRECTION, PreProcessType.NORMALIZATION };
		FeatureType[] featureTypes = new FeatureType[] { FeatureType.HISTROGRAM };

		FeatureDB dbTrain = new FeatureDB(fMatrix, fLabel, featureTypes,
				procedurePreProcess);
		
		dbTrain.getFeatures(4);
		
		
		

		fLabel = new LabelsFileMnist(TrainingData.pathLabelTest);
		fMatrix = new MatrixFile(TrainingData.pathImageTest);
		FeatureDB dbTest = new FeatureDB(fMatrix, fLabel, featureTypes,
				procedurePreProcess);

		
		//t.trainNeuro();
		

	}

}
