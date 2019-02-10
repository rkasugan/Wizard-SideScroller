import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class VerticalEnemy extends Enemy {

	private double startX, startY, endX, endY, moveSpeed;
	private boolean directionUp = false;


	public VerticalEnemy(double startX, double startY, double endX, double endY) {
		super(endX, endY);
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		moveSpeed = 2;
	}

	public double getStartX() { return (int)startX; }
	public double getStartY() { return (int)startY; }
	public double getEndX() { return (int)endX; }
	public double getEndY() { return (int)endY; }
	public void setStartX(double changeTo) { startX = changeTo; }
	public void setStartY(double changeTo) { startY = changeTo; }
	public void setEndX(double changeTo) { endX = changeTo; }
	public void setEndY(double changeTo) { endY = changeTo; }
	public boolean getDirectionUp() { return directionUp; }
	public void setDirectionUp(boolean changeTo) { directionUp = changeTo; }
	public String toString() { return "Current: (" + getX() + "," + getY() + ")  Start: (" + startX + "," + startY + ")  End: (" + endX + "," + endY + ")  DirectionRight: " + directionUp; }
	public double getMoveSpeed() { return moveSpeed; }
}