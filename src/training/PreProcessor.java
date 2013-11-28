package proj.training;

import proj.misc.ImgTool;
import proj.preprocess.Binarizor;
import proj.preprocess.Normalizor;
import proj.preprocess.PreProcessType;
import proj.preprocess.SlantCorrector;
import proj.preprocess.ThinningDitchs;
import proj.preprocess.ThinningSuen;

public class PreProcessor {
	
	private PreProcessType[] types;

	public PreProcessor(PreProcessType[] types) {
		
		this.types = types;
	}

	public int[][] process(int[][] img) {
		System.out.println("preprocess" +types.length);
		for (int i = 0; i < types.length; i++) {
			switch (this.types[i]) {
			case BINARIZATION:
				
				img=Binarizor.toBinary(img, 30);
				break;
			case NORMALIZATION:
				
				img=Normalizor.normalize(img);
				
				break;
			case SLANTCORRECTION:
				
				img=SlantCorrector.correctSlantProcess(img);
				
				break;
			case THINNING_DITCHS:
				
				img=ThinningDitchs.doThinning(img);
				
				break;	
			case THINNING_SUEN:
				
				img=ThinningSuen.doThinning(img);
				
				break;
			}
		}
		return img;
		
	}

	static public int[][] process(int[][] img, PreProcessType[] types) {
		PreProcessor p = new PreProcessor( types);
		return p.process(img);
	}

}
