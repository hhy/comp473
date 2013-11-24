package proj.mnist.histro;

public class HistroInfo {
	public int[] xs, ys, nes, ses;
	public int label;
	public HistroInfo(int[] xs, int[] ys, int[] nes, int[] ses, int label){
		this.xs=xs;this.ys=ys;this.label=label;
		this.nes=nes;
		this.ses=ses;
	}
	
	@Override
	public String toString(){
		String s="";
		s+=String.format("label: %d\n", this.label);
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