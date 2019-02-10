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

public class Enemy {

	private double x;
	private double y;
	private double xMoveSpeed;
	private double yMoveSpeed;
	private double moveSpeed;
	private BufferedImage image;
	private int frameCount;

	public Enemy(double x, double y) {
		this.x= x;
		this.y = y;
		//this.image = image;
		moveSpeed = 2;
		frameCount = 0;
	}

	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public int getXMoveSpeed() { return (int)xMoveSpeed; }
	public int getYMoveSpeed() { return (int)yMoveSpeed; }
	public BufferedImage getImage() { return image; }
	public int getFrameCount() { return frameCount/10; }

	public void setX(double changeTo) { x = changeTo; }
	public void setY(double changeTo) { y = changeTo; }
	public void setXMoveSpeed(double changeTo) {xMoveSpeed = changeTo;}
	public void setYMoveSpeed(double changeTo) {yMoveSpeed = changeTo;}
	public void setImage(BufferedImage changeTo) {image = changeTo;}
	public void setFrameCount(int changeTo) { frameCount = changeTo; }

	public void increment() {
		frameCount ++;
		if (frameCount == 50) {
			frameCount = 0;
		}
	}
}