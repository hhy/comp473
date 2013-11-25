package proj.feature;

import weka.core.Attribute;

public class HistroInfo {
	
	int[][] m;
	public int[] xs, ys, nes, ses;
	
	
	public HistroInfo(int[][] img){
		this.m=img;
		this.setHistInfo(img);
		this.mergeHistInfo();
	}
	
	
	public static double[] getHistro(int[][] m){
		HistroInfo hi=new HistroInfo(m);
		return hi.mergeHistInfo();
	}
	
	private double[] mergeHistInfo() {
		
		int xLen = xs.length, yLen = ys.length, neLen=nes.length, seLen=ses.length;
		int offset=0;
		double[] all=new double[xLen+yLen+neLen+seLen];
		for (int j=0; j < xLen; j++) {
			all[j+offset]=xs[j];
		}
		offset+=xLen;
		for (int j=0; j < yLen; j++) {
			all[j+offset]=ys[j];
			
		}
		offset+=yLen;
		for (int j=0; j < neLen; j++) {
			all[j+offset]=nes[j];
			
		}
		offset+=neLen;
		for (int j=0; j < seLen; j++) {
			all[j+offset]=ses[j];
			
		}
		return all;
	}

	

	private void setHistInfo(int[][] m) {

		int w = m.length, h = m[0].length;

		this.xs = new int[w];
		this.ys = new int[h];
		this.nes=new int[w+h-1];
		this.ses=new int[w+h-1];
		for (int i = 0; i < w; i++){
			ys[i] = 0;
			xs[i] = 0;
		}
		
		for(int i=0; i<w+h-1; i++){
			ses[i]=0;
			nes[i]=0;
		}
			
		

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (m[y][x] > 0) {
					xs[x]++;
					ys[y]++;
					nes[x+y]++;
					ses[x-y+w-1]++;
				}
			}
		}
		
	}
	
	@Override
	public String toString(){
		String s="";
		s+=String.format("x-axis: ");
		for(int i=0; i<xs.length; i++){
			s+=String.format("[%-3d]", xs[i]);
		}
		s+=String.format("\ny-axis: ");
		for(int i=0; i<ys.length; i++){
			s+=String.format("[%-3d]", ys[i]);
		}
		return s+"\n";
	}
}