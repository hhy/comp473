package proj.mnist.gradient;

public class Gradient {
	public double angle, strength;
	public Gradient(double strength, double angle){
		this.angle=angle;
		this.strength=strength;
	}
	@Override
	public String toString() {
		return String.format("[%08.2f, %08.2f]", this.strength, this.angle);
	}
	
}
