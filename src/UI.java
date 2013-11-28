package proj;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import proj.feature.FeatureType;
import proj.feature.Features;
import proj.misc.ImgTool;
import proj.preprocess.PreProcessType;
import proj.training.PreProcessor;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class UI extends JFrame implements ActionListener {

	/**
	 * @param args
	 */
	int[][] img;
	MyCanvas can;
	JButton btnClean, btnRec, btnPreProc, btnLoadClassifier, btnGetImage,
			btnExtractFeatures;
	JTextArea txtContent;

	public UI(int w, int h) {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(50, 40, w, h);
		this.setLayout(new BorderLayout());
		this.can = new MyCanvas(400, 400);
		this.btnClean = new JButton("clean");
		this.btnPreProc = new JButton("preprocessing");

		this.btnRec = new JButton("recognize");
		this.btnLoadClassifier = new JButton("load classifier");
		this.btnGetImage = new JButton("get image");
		this.btnExtractFeatures = new JButton("extract Features");
		this.txtContent = new JTextArea(10, 80);
		JPanel jpCtrl = new JPanel();
		for (JButton btn : new JButton[] { btnClean, btnGetImage, btnPreProc,
				btnExtractFeatures, btnLoadClassifier, btnRec }) {
			btn.addActionListener(this);

			jpCtrl.add(btn);
		}

		this.add(jpCtrl, BorderLayout.NORTH);
		this.add(this.can, BorderLayout.CENTER);
		this.add(this.txtContent, BorderLayout.SOUTH);

		this.validateData();
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		UI ui = new UI(400, 400);

	}

	double[] features;

	private double[] mergeFeature(Map<FeatureType, Object> features) {

		double[] hist = (double[]) features.get(FeatureType.HISTROGRAM);
		int lenHist = hist == null ? 0 : hist.length;
		double[] grad = (double[]) features.get(FeatureType.GRADIENT);
		int lenGrad = grad == null ? 0 : grad.length;
		int len = lenHist + lenGrad;
		double[] vector = new double[len];

		int offset = 0;
		for (int i = 0; i < lenHist; i++) {
			vector[i + offset] = hist[i];
		}
		offset += lenHist;
		for (int i = 0; i < lenGrad; i++) {
			vector[i + offset] = grad[i];
		}

		return vector;
	}

	private double[] getFeatures(int[][] img) {

		FeatureType[] featureTypes = new FeatureType[] { FeatureType.GRADIENT,
				FeatureType.HISTROGRAM };
		Features f = null;
		try {
			f = new Features(img, featureTypes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int l = 0;
		for (FeatureType t : featureTypes) {
			if (FeatureType.HISTROGRAM == t)
				l += Features.vLenghtHist;
			if (FeatureType.GRADIENT == t)
				l += Features.vLengthGrad;
		}

		HashMap<FeatureType, Object> features = f.features;
		double[] v = this.mergeFeature(features);

		return v;

	}

	int[][] imgNormilized;

	private void preProcess() {

		PreProcessType[] procedurePreProcess = new PreProcessType[] {PreProcessType.NORMALIZATION,
				PreProcessType.SLANTCORRECTION, PreProcessType.THINNING_SUEN,
				PreProcessType.NORMALIZATION };
		PreProcessor processor = new PreProcessor(procedurePreProcess);
		this.imgNormilized = processor.process(this.img);

	}

	private Classifier loadClassfier() {
		JFileChooser jfc = new JFileChooser();
		File f = null;
		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			f = jfc.getSelectedFile();
		}
		if (f == null)
			return null;
		ObjectInputStream ois = null;
		Classifier cc = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(f));
			cc = (Classifier) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cc;

	}

	void validateData() {

		this.btnExtractFeatures.setEnabled(!(this.imgNormilized == null));
		this.btnRec.setEnabled(!((this.features == null || this.c == null)));
		this.btnPreProc.setEnabled(!(this.img == null));
	}

	Classifier c;

	String classify() {
		
		int lenVector=this.features.length+1;
		
		FastVector fv = new FastVector(lenVector);
		
		for (int i = 0; i < lenVector-1; i++) {
			fv.addElement(new Attribute("a" + i));
		}
		FastVector fc = new FastVector(10);
		for (int i = 0; i < 10; i++) {
			fc.addElement(i + "");
		}
		fv.addElement(new Attribute("digit", fc));
		Instances tt=new Instances("trainingset", fv, 0);
		
		Instance in=new Instance(lenVector);
		
		for (int j = 0; j < features.length; j++) {
			in.setValue((Attribute) fv.elementAt(j), features[j]);
		}
		
		tt.add(in);
		
		tt.setClassIndex(lenVector-1);
		
		
		double[] dis=null;
		try {
			dis=this.c.distributionForInstance(tt.firstInstance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String s="";
		for(int i=0; i<dis.length; i++){
			s+=String.format("%04f, ", dis[i]);
		}
		MultilayerPerceptron c=null;
		
		
		return s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnGetImage) {
			this.img = this.can.getMatrix();
			ImgTool.showImg(this.img, "" + this.can.lines.size(), 400, true);
			
		} else if (e.getSource() == this.btnPreProc) {
			this.preProcess();
			ImgTool.showImg(this.imgNormilized, "after preprocess", 400, true);
			
		} else if (e.getSource() == this.btnExtractFeatures) {
			this.features = this.getFeatures(this.imgNormilized);
			System.out.println("finish feature extraction. vector len:"+this.features.length);
			
		} else if (e.getSource() == this.btnLoadClassifier) {
			this.c = this.loadClassfier();
			System.out.println("classifier loaded: "+this.c.getClass().getCanonicalName());
			
			
		}else if (e.getSource() == this.btnRec) {

			
			this.txtContent.setText("" + this.classify());
			
			
		}else if (e.getSource() == this.btnClean) {
			this.can.repaint();
			if (!(this.img == null))
				for (int y = 0; y < this.img.length; y++) {
					Arrays.fill(this.img, this.img[0].length);
					this.can.lines.clear();
				}
			this.imgNormilized = null;
			this.features = null;
			
		} 

		this.validateData();

	}

}
