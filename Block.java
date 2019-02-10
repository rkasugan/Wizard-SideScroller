public class Block {

	private double x, y;

	public Block(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public void setX(double changeTo) { x = changeTo; }
	public void setY(double changeTo) { y = changeTo; }
	public String toString() {
		return "Block - X: " + x + ", Y: " + y;
	}
}