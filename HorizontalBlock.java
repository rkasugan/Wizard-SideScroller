public class HorizontalBlock extends Block {

	private double startX, startY, endX, endY, moveSpeed;
	private boolean directionRight = true;


	public HorizontalBlock(double startX, double startY, double endX, double endY) {
		super(startX, startY);
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		moveSpeed = 2;
	}

	public double getStartX() { return startX; }
	public double getStartY() { return startY; }
	public double getEndX() { return endX; }
	public double getEndY() { return endY; }
	public void setStartX(double changeTo) { startX = changeTo; }
	public void setStartY(double changeTo) { startY = changeTo; }
	public void setEndX(double changeTo) { endX = changeTo; }
	public void setEndY(double changeTo) { endY = changeTo; }
	public boolean getDirectionRight() { return directionRight; }
	public void setDirectionRight(boolean changeTo) { directionRight = changeTo; }
	public String toString() { return "Current: (" + getX() + "," + getY() + ")  Start: (" + startX + "," + startY + ")  End: (" + endX + "," + endY + ")  DirectionRight: " + directionRight; }
	public double getMoveSpeed() { return moveSpeed; }
}