public class Explosion {

	private double x, y;
	private int count;
	public Explosion(double x, double y) {
		count = 0;
		this.x = x;
		this.y = y;



	}
	public int getCount() {return count;}
	public void increment() {count ++;}
	public int getX() {return (int)x;}
	public int getY() {return (int)y;}
	public void setX(int changeTo) {x = changeTo;}
	public void setY(int changeTo) {y = changeTo;}
}