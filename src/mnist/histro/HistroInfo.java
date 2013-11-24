package proj.mnist.histro;

public class HistroInfo {
	public int[] x, y;
	public int label;
	public HistroInfo(int[] xs, int[] ys, int label){
		this.x=xs;this.y=ys;this.label=label;
	}
	
	@Override
	public String toString(){
		String s="";
		s+=String.format("label: %d\n", this.label);
		s+=String.format("x-axis: ");
		for(int i=0; i<x.length; i++){
			s+=String.format("[%-3d]", x[i]);
		}
		s+=String.format("\ny-axis: ");
		for(int i=0; i<y.length; i++){
			s+=String.format("[%-3d]", y[i]);
		}
		return s+"\n";
	}
}