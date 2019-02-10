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

public class MagicBall {

	private double angle, xMoveSpeed, yMoveSpeed, x, y, moveSpeed;

	public MagicBall(double x, double y, double angle, double moveSpeed) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.moveSpeed = moveSpeed;

		xMoveSpeed = moveSpeed*Math.cos(Math.toRadians(angle));
		yMoveSpeed = moveSpeed*Math.sin(Math.toRadians(angle));

		yMoveSpeed *= -1;
	}

	public double getX() {return x;}
	public double getY() {return y;}
	public void setX(double changeTo) {x = changeTo;}
	public void setY(double changeTo) {y = changeTo;}
	public double getAngle() {return angle;}
	public double getMoveSpeed() {return moveSpeed;}
	public void setMoveSpeed(int changeTo) {moveSpeed = changeTo;}
	public String toString() {return "MagicalBall: x:" + x + ", y:" + y + ", angle:" + angle + ", speed: " + moveSpeed + ",xMove: " + xMoveSpeed + ", yMove:" + yMoveSpeed;}
	public double getXMoveSpeed() {return xMoveSpeed;}
	public double getYMoveSpeed() {return yMoveSpeed;}
}