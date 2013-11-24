package proj.mnist;

import java.awt.image.BufferedImage;
import java.io.IOException;

import proj.ImgComponent;
import proj.ImgTool;

public class TrainingData {
	public static final String pathFile="/tmp/tt";
	public static final String pathLabelTraining=pathFile+"/train-labels-idx1-ubyte";
	public static final String pathImageTraining=pathFile+"/train-images-idx3-ubyte";
	public static final String pathLabelTest=pathFile+"/t10k-labels-idx1-ubyte";
	public static final String pathImageTest=pathFile+"/t10k-images-idx3-ubyte";
	
	
	LabelsFileMnist labels;
	ImagesFileMnist images;
	MatrixFile matrixs;
	
	public TrainingData(String pathLabels, String pathImages) throws IOException{
		labels=new LabelsFileMnist(pathLabels);
		images=new ImagesFileMnist(pathImages);
	}
	void showElement(int index, int sideLength) throws IOException {
		BufferedImage img=(BufferedImage) images.getObject(index);
		int lbl=((Integer) labels.getObject(index)).intValue();
		ImgTool.showImg(img, ""+lbl, sideLength);
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		TrainingData t=new TrainingData(pathLabelTraining, pathImageTraining);
		t.showElement(9, 300);

	}

}
