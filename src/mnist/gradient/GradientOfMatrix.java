package proj.mnist.gradient;

public class GradientOfMatrix {
	Gradient[][] gs;
	Gradient[][][][] gss;

	final static int SIZE_ZONE = 7;
	

	public GradientOfMatrix(int m[][]) {
		this.gs=this.getGradient(m, 1, m[0].length -1, 1, m.length -1);
	}
	
	public GradientOfMatrix(int m[][], boolean zone) {

		/*
		 * Padding the matrix;
		 */
		int[][] mm = new int[m.length + 2][];
		for (int y = 1; y < mm.length - 1; y++) {
			mm[y] = new int[m.length + 2];
			for (int x = 1; x < mm[1].length - 1; x++) {
				mm[y][x] = m[y - 1][x - 1];
			}
			mm[y][0] = mm[y][1];
			mm[y][mm[y].length - 1] = mm[y][mm[y].length - 2];
		}
		mm[0] = mm[1];
		mm[m.length + 1] = mm[m.length];

		/* compute all gradient of each zone */
		this.gss=new Gradient[m.length/SIZE_ZONE][][][];
		for (int y = 1; y < mm.length - 1; y += SIZE_ZONE) {
			int yy=(y-1)/SIZE_ZONE;
			int lenX=(mm[y-1].length-2)/SIZE_ZONE;
			this.gss[yy]=new Gradient[lenX][][];
			for (int x = 1; x < mm[0].length - 1; x += SIZE_ZONE) {
				this.gss[yy][(x-1)/SIZE_ZONE]=this.getGradient(mm, x, x+SIZE_ZONE, y, y+SIZE_ZONE);
			}
		}

	}
	
	private Gradient[][] getGradient(int[][] m, int xFrom, int xEnd,
			int yFrom, int yEnd) {
		
		Gradient[][] gs = new Gradient[yEnd - yFrom][];
		
		for (int y = yFrom; y < yEnd; y++) {
			gs[y-yFrom] = new Gradient[xEnd - xFrom];
			for (int x = xFrom; x < xEnd; x++) {
				double gx = m[y - 1][x + 1] + 2 * m[y][x + 1] + m[y + 1][x + 1]
						- m[y - 1][x - 1] + 2 * m[y][x - 1] + m[y + 1][x - 1];

				double gy = m[y + 1][x - 1] + 2 * m[y + 1][x] + m[y + 1][x + 1]
						- m[y - 1][x - 1] + 2 * m[x][y - 1] + m[y - 1][x + 1];
				double strengh = Math.sqrt(gx * gx + gy * gy);

				
				gs[y-yFrom][x-xFrom] = new Gradient(strengh, Math.atan2(gy, gx));
			}
		}
		return gs;
	}

	public static void main(String[] args) {
		System.out.println((int) (5 / 6));
	}

}
