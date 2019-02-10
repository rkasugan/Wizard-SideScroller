public class Powerup {

	private double x, y;
	private String effect;

	public Powerup(double x, double y, String effect) {
		this.x = x;
		this.y = y;
		this.effect = effect;
	}
	public double getX() {return x;}
	public double getY() {return y;}
	public String getEffect() {return effect;}
	public void setX(double changeTo) {x = changeTo;}
	public void setY(double changeTo) {y = changeTo;}
}