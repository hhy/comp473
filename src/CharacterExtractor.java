package proj;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class CharacterExtractor {

	static int[] getStartAndEndDistribution(int[] yhist) {

		int[] yyhist = SkewingCorrector.averageFilterForArray(yhist, 0);

		/*
		 * filter the maximum and minum, take the second maximum and minum for
		 * the threshold
		 */
		int max1 = yyhist[0], max2 = max1, min1 = max1, min2 = max1;
		for (int i = 0; i < yyhist.length; i++) {
			if (yyhist[i] > max1) {
				max2 = max1;
				max1 = yyhist[i];
			}
			if (yyhist[i] < min1) {
				min2 = min1;
				min1 = yyhist[i];
			}
		}

		int div = (int) ((min2 + max2) / (double) (8));

		int idxStart = -1, idxEnd = -1;

		for (int i = yyhist.length / 40; i < yyhist.length; i++) {
			int j = yyhist.length - i - 1;
			if (yyhist[i] > div && idxStart < 0)
				idxStart = i;
			if (yyhist[j] > div && idxEnd < 0)
				idxEnd = j;
			if (idxStart > 0 && idxEnd > 0)
				break;
		}

		// System.out.println(String
		// .format("start: %d, end: %d, width: %d", idxStart, idxEnd,
		// idxEnd-idxStart));

		return new int[] { idxEnd, idxStart };
	}

	static BufferedImage[] getCharacters(BufferedImage img) {
		int[] xs = SkewingCorrector.getPixelsXDistribution(img, 150, 0, false);
		int[] ys = SkewingCorrector.getPixelsYDistribution(img, 150, 0, false);
		int[] hs = getStartAndEndDistribution(ys);
		//System.out.println(String.format("get width is : %d - %d", hs[0], hs[1]));
		List<int[]> posChar=new ArrayList<int[]>();
		int start=-1, end=-1;
		for(int i=0; i<xs.length; i++){
			if(xs[i]==0 && end==-1) {
				end=i;
				start=-1;
			}
			if(xs[i]>0 && start==-1) {
				start=i;
				end=-1;
			}
			if(end>start && end>0)
				posChar.add(new int[]{start, end});
		}
		List<BufferedImage> bis = new ArrayList<BufferedImage>();
		for(int[] pos: posChar){
			BufferedImage bi=img.getSubimage(Math.min(pos[0], pos[1]), Math.min(hs[0], hs[1]), Math.abs(pos[0]-pos[1]), Math.abs(hs[0]-hs[1]));
			bis.add(bi);
		}
		
		

		return (BufferedImage[]) bis.toArray();
	}

	public static void main(String[] args) throws IOException {

		final String pathHome = System.getProperty("user.home");
		final String pathImage = pathHome + "/Pictures/a.png";
		BufferedImage ia = ImageIO.read(new File(pathImage));
		getCharacters(ia);
		// HistroChart.showHistroDistrib(ia, 800, 600, "Pixels Distribution");

	}

}
