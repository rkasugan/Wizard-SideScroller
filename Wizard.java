/*
	have an array of frames for each action


	"wizard.attack" in the runner will call the "attack()" method which will change currentState to 2, will set currentStateFrames to "attackStateFrames" (which will be the
	interval distance for the attack animation chain, i gotta make xxxStateFrames which act as final storages for interval lengths for each action)

	it will have 5 frames, once the fifth frame is done everything will revert back to the same values as the running animation set



	add an idle animation that isnt the run, which would just be him standing there, and make that the default when he's not doing anything
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import javax.swing.border.*;

public class Wizard
{

	private double x = 200;
	private double y = 590;
	private double xSpeed = 0;
	private double ySpeed = 0;
	private BufferedImage wizardSpriteSheet, img;

	private int currentState = 0;  //0 - run,   1 - jump,   2 - attack
	private int currentStateFrames;

	private final int runStateFrames = 20;
	private int runAnimCount = 0;
	private BufferedImage[] runAnim = new BufferedImage[4];

	boolean currentlyJumping = false;
	boolean falling = false;
	private final int jumpStateFrames = 5;
	private int jumpAnimCount = 0;
	private BufferedImage[] jumpAnim = new BufferedImage[5];

	public Wizard()
	{
		currentStateFrames = runStateFrames;

		try
		{
			wizardSpriteSheet = ImageIO.read(new File("Sprites/WizardSpriteSheet2.png"));

			runAnim[0] = wizardSpriteSheet.getSubimage(  0, 256, 140, 109);
			runAnim[1] = wizardSpriteSheet.getSubimage(214, 256, 140, 109);
			runAnim[2] = wizardSpriteSheet.getSubimage(417, 256, 140, 109);
			runAnim[3] = wizardSpriteSheet.getSubimage(830, 256, 140, 109);
			img = runAnim[0];


			jumpAnim[0] = wizardSpriteSheet.getSubimage( 24, 399, 119, 114);
			jumpAnim[1] = wizardSpriteSheet.getSubimage(221, 381, 116, 128);
			jumpAnim[2] = wizardSpriteSheet.getSubimage(418, 368, 122, 135);
			jumpAnim[3] = wizardSpriteSheet.getSubimage(617, 370, 121, 133);
			jumpAnim[4] = wizardSpriteSheet.getSubimage(821, 377, 116, 126);



		}
		catch (Exception e)
		{}


	}

	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public void setX(int changeTo) { x = changeTo; }
	public void setY(int changeTo) { y = changeTo; }
	public double getXSpeed() { return xSpeed; }
	public double getYSpeed() { return ySpeed; }
	public void setXSpeed(double changeTo) { xSpeed = changeTo; }
	public void setYSpeed(double changeTo) { ySpeed = changeTo; }
	public BufferedImage getImage() { return img; }
	public void setImage(BufferedImage changeTo) { img = changeTo; }
	public int getCurrentState() { return currentState; }
	public int getCurrentStateFrames() { return currentStateFrames; }
	public void setCurrentState(int changeTo) { currentState = changeTo; }
	public void setCurrentStateFrames(int changeTo) { currentStateFrames = changeTo; }
	public int getJumpStateFrames() { return jumpStateFrames; }
	public void setJumpAnimCount(int changeTo) { jumpAnimCount = changeTo; }
	public BufferedImage[] getJumpAnim() { return jumpAnim; }
	public int getJumpAnimCount() { return jumpAnimCount; }
	public double getWidth() { return img.getWidth(); }
	public double getHeight() { return img.getHeight(); }
	public boolean getFalling() { return falling; }
	public void setFalling(boolean changeTo) { falling = changeTo; }
	public int getRunStateFrames() { return runStateFrames; }
	public int getRunAnimCount()  {return runAnimCount; }
	public void setRunAnimCount(int changeTo) { runAnimCount = changeTo; }
	public BufferedImage[] getRunAnim() { return runAnim; }
	public void increment()
	{
		currentStateFrames --;
		if (falling) {
			currentlyJumping = true;
		}
		if (!currentlyJumping)
		{
			if (currentStateFrames == 0)
			{
				currentStateFrames = runStateFrames;
				runAnimCount ++;
				if (runAnimCount == 4)
				{
					runAnimCount = 0;
				}
				img = runAnim[runAnimCount];
			}
		}
		else   //if jumping
		{
			y += ySpeed;
			ySpeed += 0.75;

			if (currentStateFrames == 0)
			{
				currentStateFrames = jumpStateFrames;
				jumpAnimCount ++;
				if (jumpAnimCount == 5)
				{
					jumpAnimCount = 4;
				}
				img = jumpAnim[jumpAnimCount];
			}

			if (y >= 590) {  //or whatever the end condition for the jump will be in the future (like actually checking if it hits a block or something)
				y = 590;
				currentlyJumping = false;
				ySpeed = 0;
				jumpAnimCount = 0;
				runAnimCount = 0;
				currentStateFrames = runStateFrames;
				img = runAnim[0];
			}


		}
		/*if (!falling) {
			currentlyJumping = false;
			ySpeed = 0;
			jumpAnimCount = 0;
			runAnimCount = 0;
			currentStateFrames = runStateFrames;
			img = runAnim[0];
		}*/
	}
	public void jump(int superJumpTimeLeft)
	{
		currentlyJumping = false;
		if (!currentlyJumping)
		{
			currentlyJumping = true;
			ySpeed = -20;
			currentStateFrames = jumpStateFrames;
			jumpAnimCount = 0;
			if (superJumpTimeLeft > 0) {
				ySpeed = -29;
			}
		}

	}

	public static void main(String[]args) {
		SideScroller app = new SideScroller();    //yea i put this here in every single java file i create because im too lazy to alt-tab over
	}
}