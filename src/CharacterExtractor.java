package proj;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CharacterExtractor {
	static void showImgs(BufferedImage[] imgs){
		JFrame jf=new JFrame("Images number: "+imgs.length);
		Container c=jf.getContentPane();
		JPanel jp=new JPanel();
		jp.setLayout(new GridLayout(1, imgs.length));
		for(int i=0; i<imgs.length; i++){
			
			
			jp.add(new ImgComponent(imgs[i]));
			
		}
		c.add(jp);
		jf.pack();
		jf.setVisible(true);
		
	}
	static int[] getEdges(int[] yhist) {
//		ArrayChart.showArray(yhist, 400, 300, "original");
//		int[] yyhist = SkewingCorrector.averageFilterForArray(yhist, 0);
//		ArrayChart.showArray(yhist, 400, 300, "processed");
//		yhist=yyhist;
		/*
		 * filter the maximum and minum, take the second maximum and minum for
		 * the threshold
		 */
		ArrayList<Integer> edges = new ArrayList<Integer>();
		
		boolean isBottom = true;
		for (int i = 0; i < yhist.length; i++) {
			if( (yhist[i] == 0 && isBottom )||((yhist[i] > 0 && !isBottom )))
				continue;
			else if(yhist[i]>0 && isBottom){
				edges.add(i);
				isBottom=!isBottom;
			}else if(yhist[i]==0 && !isBottom){
				edges.add(i-1);
				isBottom=!isBottom;
			}
			
			

		}

		int[] es = new int[edges.size()];
		for (int i = 0; i < es.length; i++)
			es[i] = edges.get(i);
		System.out.println("total characters: "+es.length/2+", "+"I am studying pattern recognition".length());
		return es;
	}

	static BufferedImage[] getCharacters(BufferedImage img) {
		int[] xs = SkewingCorrector.getPixelsXDistribution(img, 150, 0, false);
		// int[] ys = SkewingCorrector.getPixelsYDistribution(img, 150, 0,
		// false);
		int[] posEdges = getEdges(xs);
		ArrayChart.showArray(xs, 500, 400, "");
		ArrayChart.showArray(posEdges, 500, 400, "");
		
		
		HistroChart.showHistroDistrib(img, 400, 300, "");
		List<BufferedImage> bis = new ArrayList<BufferedImage>();
		for (int i=0; i<posEdges.length; i+=2) {
			BufferedImage bi = img.getSubimage(posEdges[i], 0, posEdges[i+1]-posEdges[i], img.getHeight());
			bis.add(bi);
		}
		BufferedImage[] imgs= bis.toArray(new BufferedImage[0]);
		showImgs(imgs);
		return imgs;
	}

	public static void main(String[] args) throws IOException {

		final String pathHome = System.getProperty("user.home");
		final String pathImage = pathHome + "/Pictures/a.png";
		BufferedImage ia = ImageIO.read(new File(pathImage));
		getCharacters(ia);
		// HistroChart.showHistroDistrib(ia, 800, 600, "Pixels Distribution");

	}

}
