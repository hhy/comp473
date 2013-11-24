package proj.preprocess;

public class Binarizor {
	int[][] m;
	public Binarizor(int[][] m){
		this.m=m;
	}
	private int[][] toBinary(int threshold) {
		int w = m.length, h = m[0].length;

		int[][] mm = new int[h][];
		for (int y = 0; y < h; y++) {
			mm[y]=new int[w];
			for (int x = 0; x < w; x++) {

				if (m[y][x] > threshold)
					mm[y][x] = 1;

				else
					mm[y][x] = 0;
			}
		}
		return mm;
	}
	public static int[][] toBinary(int[][] m, int threshold){
		Binarizor b=new Binarizor(m);
		return b.toBinary(threshold);
	}
}
