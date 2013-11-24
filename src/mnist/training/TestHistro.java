package proj.mnist.training;

import java.io.IOException;

import proj.mnist.LabelsFileMnist;
import proj.mnist.MatrixFile;
import proj.mnist.TrainingData;
import proj.mnist.histro.HistroDB;
import proj.mnist.histro.HistroInfo;
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
	HistroDB dbTrain, dbTest;
	int iTrainStart, iTrainEnd, iTestStart, iTestEnd;
	int lenVector;

	public TestHistro(HistroDB dbTrain, HistroDB dbTest, int iTrainStart,
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

	private void fillInstances(Instances instance, HistroDB db, int iStart,
			int iEnd) throws IOException {

		for (int i = 0; i < iEnd - iStart; i++) {
			Instance in = new Instance(this.lenVector);
			HistroInfo hi = db.getHistro(i);
			int xLen = hi.x.length, yLen = hi.y.length;
			int j = 0;
			for (; j < xLen; j++) {
				in.setValue((Attribute) fv.elementAt(j), hi.x[j]);
			}

			for (; j < xLen + yLen; j++) {
				in.setValue((Attribute) fv.elementAt(j), hi.y[j - xLen]);
			}

			if (hi.label > 9 || hi.label < 0) {
				System.out.println(String.format(
						"[warning] wrong label %d: %d", i, hi.label));
			}
			in.setValue((Attribute) fv.elementAt(xLen + yLen), hi.label + "");

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

		 //Classifier cModel = (Classifier) new MultilayerPerceptron();
		Classifier cModel = (Classifier) new NaiveBayes();
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
		HistroDB dbTrain = new HistroDB(fLabel, fMatrix);

		fLabel = new LabelsFileMnist(TrainingData.pathLabelTest);
		fMatrix = new MatrixFile(TrainingData.pathImageTest);
		HistroDB dbTest = new HistroDB(fLabel, fMatrix);

		TestHistro t = new TestHistro(dbTrain, dbTest, 0, 60000, 0, 10000, 57);
		t.trainNeuro();

		System.out.println(t.fv.size());
	}
}
