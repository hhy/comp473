package proj;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Label extends MnistFile {


	public Label(String name) throws FileNotFoundException {
		super(name);
		// TODO Auto-generated constructor stub
	}

	List<Integer> getLabels() throws IOException {
		List<Integer> labels = new ArrayList<Integer>();
		
		this.init();
		
		for (int i = 0; i < this.numObjects; i++) {
			int b=this.read();
			labels.add(b);
			//System.out.println(String.format("%05d: %d",i,  b));
			
		}
		return labels;
	}
	
	

	public static void main(String[] args) throws IOException  {
		Label lb = new Label(Main.pathLabelTraining);
		lb.getLabels();
		System.out.println(lb.info());
	}

}
