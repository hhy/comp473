package proj.mnist;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LabelsFileMnist extends MnistFile {

	public LabelsFileMnist(String name) throws IOException {
		super(name);
		// TODO Auto-generated constructor stub
	}

	List<Integer> getLabels() throws IOException {
		List<Integer> labels = new ArrayList<Integer>();

		for (int i = 0; i < this.numObjects; i++) {
			int b = this.read();
			labels.add(b);
		}
		return labels;
	}



	@Override
	int getObjectStoreSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	int getOffsetObjectArrays() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	Object getObjectFromStream() throws IOException {
		return new Integer(this.read() & 0x0ff);
	}
	
	public static void main(String[] args) throws IOException {
		LabelsFileMnist lb = new LabelsFileMnist(TrainingData.pathLabelTest);
		
		System.out.println(lb.numObjects);
		for(int i=0; i<lb.numObjects; i++){
			int a=(Integer)lb.getObject(i);
			if(a<0 || a>9) System.out.println(a);
		}
		
		//System.out.println(a);
	}
	

}
