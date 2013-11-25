package proj.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import proj.feature.gradient.GradientVectorZoned;

public class Features {
	
	public HashMap<FeatureType, Object> features;
	private int[][] img;
	private FeatureType[] types;

	public Features(int[][] img, FeatureType[] types) throws Exception {
		this.img = img;
		this.features = new HashMap<FeatureType,Object>();
		this.types=types;
		this.setFeatureVector();
	}

	private void setFeatureVector() throws Exception {
		for (int i = 0; i < types.length; i++) {

			switch (types[i]) {
			case GRADIENT:
				double[] gv=GradientVectorZoned.getGradient(img, 16);
				features.put(FeatureType.GRADIENT, gv);
				break;
			case HISTROGRAM:
				double[] hist=HistroInfo.getHistro(img);
				features.put(FeatureType.HISTROGRAM, hist);
				break;
			default:
				throw new Exception("Unknown feature type");
				
			}

		}
	}
}
