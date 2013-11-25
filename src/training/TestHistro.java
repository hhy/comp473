package proj.training;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import proj.feature.FeatureType;
import proj.feature.Features;
import proj.feature.HistroInfo;
import proj.mnist.LabelsFileMnist;
import proj.mnist.MatrixFile;
import proj.mnist.TrainingData;
import proj.preprocess.PreProcessType;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class TestHistro {
	FastVector fv;
	Instances isTrainingSet, isTestSet;
	FeatureDB dbTrain, dbTest;
	int iTrainStart, iTrainEnd, iTestStart, iTestEnd;
	int lenVector;

	public TestHistro(FeatureDB dbTrain, FeatureDB dbTest, int iTrainStart,
			int iTrainEnd, int iTestStart, int iTestEnd, int lenVector) {

		this.dbTrain = dbTrain;
		this.dbTest = dbTest;
		this.iTrainStart = iTrainStart;
		this.iTrainEnd = iTrainEnd;
		this.iTestStart = iTestStart;
		this.iTestEnd = iTestEnd;
		this.lenVector = lenVector;

		this.fv = new FastVector(lenVector);
		for (int i = 0; i < lenVector - 1; i++) {
			fv.addElement(new Attribute("a" + i));
		}

		FastVector fc = new FastVector(10);
		for (int i = 0; i < 10; i++) {
			fc.addElement(i + "");
		}
		this.fv.addElement(new Attribute("digit", fc));

		this.isTrainingSet = new Instances("trainingset", this.fv, iTrainEnd
				- iTrainStart);
		this.isTrainingSet.setClassIndex(lenVector - 1);

		this.isTestSet = new Instances("trainingset", this.fv, iTrainEnd
				- iTrainStart);
		this.isTestSet.setClassIndex(lenVector - 1);
	}

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

	private void fillInstances(Instances instance, FeatureDB db, int iStart,
			int iEnd) throws Exception {

		for (int i = 0; i < iEnd - iStart; i++) {
			Instance in = new Instance(this.lenVector);

			int label = db.getLabel(i);
			Map<FeatureType, Object> features = db.getFeatures(i);
			double[] vector = this.mergeFeature(features);

//			System.out.println(vector.length);
			
			for (int j = 0; j < vector.length; j++) {
				in.setValue((Attribute) fv.elementAt(j), vector[j]);
				
			}

			if (label > 9 || label < 0) {
				System.out.println(String.format(
						"[warning] wrong label %d: %d", i, label));
			}
			in.setValue((Attribute) fv.elementAt(vector.length), label + "");

			instance.add(in);
		}
	}

	public void train() throws Exception {

		this.fillInstances(isTrainingSet, dbTrain, iTrainStart, iTrainEnd);
		System.out.println("Training data ready.");

		Classifier cModel = (Classifier) new NaiveBayes();
		cModel.buildClassifier(this.isTrainingSet);

		// Evaluation eTest = new Evaluation(isTrainingSet);

		Evaluation eTest = new Evaluation(isTrainingSet);
		System.out.println("Training finish");

		this.fillInstances(isTestSet, dbTest, this.iTestStart, this.iTestEnd);
		System.out.println("Testing data ready.");

		eTest.evaluateModel(cModel, isTestSet);
		// eTest.evaluateModel(cModel, isTrainingSet);
		System.out.println("Testing finish");

		String strSummary = eTest.toSummaryString();

		// Get the confusion matrix
		double[][] cmMatrix = eTest.confusionMatrix();

		strSummary += "\n[Confusion Matrix]\n";
		for (int i = 0; i < cmMatrix.length; i++) {
			for (int j = 0; j < cmMatrix[i].length; j++) {
				strSummary += String.format("[%04.0f]", cmMatrix[i][j]);
			}
			strSummary += "\n";
		}

		System.out.println(strSummary);

	}

	public void trainNeuro() throws Exception {

		this.fillInstances(isTrainingSet, dbTrain, iTrainStart, iTrainEnd);
		System.out.println("Training data ready.");

		Classifier cModel = (Classifier) new MultilayerPerceptron();
		// Classifier cModel = (Classifier) new NaiveBayes();
		// String[] options = {"-G"};
		// cModel.setOptions(options);
		cModel.buildClassifier(this.isTrainingSet);

		/* training */
		Evaluation eTest = new Evaluation(isTrainingSet);
		System.out.println("Training finish");

		/* testing */
		this.fillInstances(isTestSet, dbTest, this.iTestStart, this.iTestEnd);
		System.out.println("Testing data ready.");
		eTest.evaluateModel(cModel, isTestSet);

		System.out.println("Testing finish");

		String strSummary = eTest.toSummaryString();

		// Get the confusion matrix
		double[][] cmMatrix = eTest.confusionMatrix();

		strSummary += "\n[Confusion Matrix]\n";
		for (int i = 0; i < cmMatrix.length; i++) {
			for (int j = 0; j < cmMatrix[i].length; j++) {
				strSummary += String.format("[%04.0f]", cmMatrix[i][j]);
			}
			strSummary += "\n";
		}

		System.out.println(strSummary);

	}

	static public void main(String[] args) throws Exception {
		LabelsFileMnist fLabel = new LabelsFileMnist(
				TrainingData.pathLabelTraining);
		MatrixFile fMatrix = new MatrixFile(TrainingData.pathImageTraining);

		PreProcessType[] procedurePreProcess = new PreProcessType[] {
				PreProcessType.BINARIZATION, PreProcessType.THINNING_SUEN,
				PreProcessType.SLANTCORRECTION, PreProcessType.NORMALIZATION };
		
		FeatureType[] featureTypes = new FeatureType[] { FeatureType.GRADIENT, FeatureType.HISTROGRAM };

		FeatureDB dbTrain = new FeatureDB(fMatrix, fLabel, featureTypes,
				procedurePreProcess);

		fLabel = new LabelsFileMnist(TrainingData.pathLabelTest);
		fMatrix = new MatrixFile(TrainingData.pathImageTest);
		FeatureDB dbTest = new FeatureDB(fMatrix, fLabel, featureTypes,
				procedurePreProcess);

		TestHistro t = new TestHistro(dbTrain, dbTest, 0, 6000, 0, 1000, 423);
		t.trainNeuro();
//		t.train();

		System.out.println(t.fv.size());
	}
}
