/*


get sprite for enemy







*/


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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.MalformedURLException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.geom.Line2D;
import java.lang.Object;
import javafx.scene.shape.Line;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

public class SideScroller extends JPanel implements KeyListener,Runnable,MouseListener
{
	private JFrame frame;
	Thread t;
	private double scale = 1.0;

	boolean toggleInfo = false;  //normally false

	private BufferedImage background1, background2, background3;
	private double background1A = 0;    //x position of layer 1, A copy
	private double background1B = 1920; //x position of layer 2, B copy
	private double background2A = 0;
	private double background2B = 1920;
	private double background3A = 0;
	private double background3B = 1920;

	private double moveSpeed = 0;
	private double tempMoveSpeed = 0;
	private String lastDirection = "right";
	private int lastDirectionNum = 1;   //1 - right,      -1 - left

	Wizard wizard;
	private ArrayList<MagicBall> magicBalls;
	private BufferedImage magicBall;
	private boolean right = false;
	private boolean left = false;

	private BufferedImage block;
	private ArrayList<Block> blockList;
	private ArrayList<HorizontalBlock> horizontalBlockList;
	private ArrayList<Powerup> powerupList;
	private BufferedImage questionBlock;
	String[][] blockMap;

	private ArrayList<HorizontalEnemy> horizontalEnemyList;
	private ArrayList<VerticalEnemy> verticalEnemyList;
	private BufferedImage enemyPic;
	BufferedImage enemySpriteSheet;
	BufferedImage[] enemySprites = new BufferedImage[5];

	/*boolean checkYTop = false;
	boolean checkYBot = false;
	boolean checkYCenter = false;
	boolean checkXLeft = false;
	boolean checkXRight = false;
	boolean checkXCenter = false;*/

	int blockOnTopOfIndex = 0;
	boolean onTopOfRegular = false;
	int onTopOfRegularNum = 0;    //0 = not on block,    1 = onRegularBlock,     2 = onHorizontalBlock

	int superJumpTimeLeft = 0;
	final int superJumpTimeSafe = 750;

	int gameState = 0;   //0 = game active,     1 = lose,      2 = win
	int lives = 3;
	//Block winBlock;
	int winBlockIndex = 0;
	BufferedImage winBlock;

	ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	BufferedImage enemyExplosionSheet;
	BufferedImage[] enemyExplosion = new BufferedImage[16];

	private int score = 0;
	private final int loseLifeFrames = 100;
	private int loseLifeFramesCount = 0;
	private int timeRemaining = 1000;









	/*private BufferedImage temp[];         //for cutting out and testing sprites
	private BufferedImage spriteSheet;
	final int setFrameCount = 20;
	int frameCount = setFrameCount;
	int animCount = 0;*/

	public SideScroller()
	{
		wizard = new Wizard();
		magicBalls = new ArrayList<MagicBall>();
		blockList = new ArrayList<Block>();
		horizontalBlockList = new ArrayList<HorizontalBlock>();
		powerupList = new ArrayList<Powerup>();
		horizontalEnemyList = new ArrayList<HorizontalEnemy>();
		verticalEnemyList = new ArrayList<VerticalEnemy>();

		try { //image stuff

			background1 = ImageIO.read(new File("BackgroundLayers/layer1.png"));
			background1 = resize(background1, 1500, 844);
			background1B = 1500;
			background2 = ImageIO.read(new File("BackgroundLayers/layer2.png"));
			background2 = resize(background2, 1500, 844);
			background2B = 1500;
			background3 = ImageIO.read(new File("BackgroundLayers/layer3.png"));
			background3 = resize(background3, 1500, 844);
			background3B = 1500;

			magicBall = resize(ImageIO.read(new File("Sprites/magicBall.png")), 35, 35);
			magicBall = resize(magicBall, scale);

			block = resize(ImageIO.read(new File("Sprites/block.png")), 50, 50);
			questionBlock = resize(ImageIO.read(new File("Sprites/questionBlock.png")), 50, 50);
			winBlock = resize(ImageIO.read(new File("Sprites/winSquare.jpg")), 50, 50);

			enemyPic = block;
			enemySpriteSheet = ImageIO.read(new File("Sprites/Enemy/EnemySpriteSheet.png"));
			for (int i = 0; i < enemySprites.length; i ++) {
				String path = "Sprites/Enemy/enemy" + (i+1) + ".png";
				enemySprites[i] = resize(ImageIO.read(new File(path)), 125, 125);
			}
			enemyPic = enemySprites[0];


			enemyExplosionSheet = ImageIO.read(new File("Sprites/enemyExplosion.png"));
			//x: 0-64, 64-128, 128-192, 192-256
			int count = 0;
			for (int row = 0; row < 4; row ++) {
				for (int col = 0; col < 4; col ++) {
					enemyExplosion[count] = enemyExplosionSheet.getSubimage(80*col, 80*row, 80, 80);
					count ++;
				}
			}

			readBlockMap();


			/*spriteSheet = ImageIO.read(new File("Sprites/WizardSpriteSheet2.png"));         //for cutting out and testing sprites
			temp = new BufferedImage[5];
			temp[0] = spriteSheet.getSubimage( 24, 399, 119, 114);
			temp[1] = spriteSheet.getSubimage(221, 381, 116, 128);
			temp[2] = spriteSheet.getSubimage(418, 368, 122, 135);
			temp[3] = spriteSheet.getSubimage(617, 370, 121, 133);
			temp[4] = spriteSheet.getSubimage(821, 377, 116, 126);*/
		}
		catch (Exception e) {}

		frame=new JFrame();
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.add(this);
		frame.setSize(1500,800);  //1920, 1000
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		t=new Thread(this);
		t.start();
	}
	public void run()
	{
		while(gameState == 0)
		{
			if (left || right) {
				if ((right && !leftBlock()) || (left && !rightBlock())) {
					moveSpeed = 8;
					tempMoveSpeed = moveSpeed;
				}
				else {
					moveSpeed = 0;
					tempMoveSpeed = 0;
				}
			}
			else {
				moveSpeed = 0;
				tempMoveSpeed = 0;
			}

			onBlock();
			if (onTopOfRegularNum == 2) { //on top of a horizontalBlock
				HorizontalBlock tempBlock = horizontalBlockList.get(blockOnTopOfIndex);
				double temp = tempBlock.getMoveSpeed();
				if (tempBlock.getDirectionRight()) {                  //block is moving to the right
					moveSpeed += tempBlock.getMoveSpeed();
					moveSpeed = Math.abs(moveSpeed);
					tempMoveSpeed = -1*moveSpeed;
					if (left) {
						moveSpeed = 6;
						tempMoveSpeed = 6;
					}
				}
				else {                                                //block is moving to the left
					moveSpeed += tempBlock.getMoveSpeed();
					moveSpeed = Math.abs(moveSpeed);
					tempMoveSpeed = moveSpeed;
					if (right) {
						moveSpeed = 6;
						tempMoveSpeed = -6;
					}
				}
			}


			if (lastDirection.equals("right")) {
				if (background1A == 0) {   //cycles the two background pictures for one layer
					background1B = 1500*scale;
				}
				if (background1B == 0) {
					background1A = 1500*scale;
				}
				if (background2A == 0) {   //same thing for other 3 layers
					background2B = 1500*scale;
				}
				if (background2B == 0) {
					background2A = 1500*scale;
				}
				if (background3A == 0) {
					background3B = 1500*scale;
				}
				if (background3B == 0) {
					background3A = 1500*scale;
				}
			}
			else if (lastDirection.equals("left")) {
				if (background1A == 0) {   //cycles the two background pictures for one layer
					background1B = -1500*scale;
				}
				if (background1B == 0) {
					background1A = -1500*scale;
				}
				if (background2A == 0) {   //same thing for other 3 layers
					background2B = -1500*scale;
				}
				if (background2B == 0) {
					background2A = -1500*scale;
				}
				if (background3A == 0) {
					background3B = -1500*scale;
				}
				if (background3B == 0) {
					background3A = -1500*scale;
				}
			}

			if (right && !left) {
				tempMoveSpeed = -1 * moveSpeed;
			}

			if (moveSpeed == 8) {
				background1A -= (0.5 * lastDirectionNum);
				background1B -= (0.5 * lastDirectionNum);
				background2A -= (1 * lastDirectionNum);
				background2B -= (1 * lastDirectionNum);
				background3A -= (3 * lastDirectionNum);
				background3B -= (3 * lastDirectionNum);
			}

			//onBlock();
			if (underBlock() && wizard.getYSpeed() < 0) {
				wizard.setYSpeed(0);
			}

			wizard.setFalling(onBlock());
			incrementWizard();
			//wizard.increment();   //frame/animation stuff for the wizard




			//move magicballs
			for (int i = magicBalls.size()-1; i >= 0; i --) {
				MagicBall tempMagicalBall = magicBalls.get(i);
				tempMagicalBall.setX(tempMagicalBall.getX() + tempMagicalBall.getXMoveSpeed() + tempMoveSpeed);
				tempMagicalBall.setY(tempMagicalBall.getY() + tempMagicalBall.getYMoveSpeed());

				//kill horizontalEnemies
				for (int j = horizontalEnemyList.size()-1; j >= 0; j --) {
					HorizontalEnemy tempEnemy = horizontalEnemyList.get(j);

					Rectangle magicRec = new Rectangle((int)tempMagicalBall.getX(), (int)tempMagicalBall.getY(), magicBall.getWidth(), magicBall.getHeight());
					Rectangle enemyRec = new Rectangle(tempEnemy.getX()+10, tempEnemy.getY()+20, enemyPic.getWidth()-50, enemyPic.getHeight()-50);

					if (magicRec.intersects(enemyRec)) {
						explosions.add(new Explosion(tempEnemy.getX(), tempEnemy.getY()));
						horizontalEnemyList.remove(j);
						try {
							playSound("Sprites/explosion.wav");
						} catch (MalformedURLException ex) {
						} catch (LineUnavailableException ex) {
						} catch (UnsupportedAudioFileException ex) {
						} catch (IOException ex) {
						}
						score += 100;
					}
				}
				//kill verticalEnemies
				for (int j = verticalEnemyList.size()-1; j >= 0; j --) {
					VerticalEnemy tempEnemy = verticalEnemyList.get(j);

					Rectangle magicRec = new Rectangle((int)tempMagicalBall.getX(), (int)tempMagicalBall.getY(), magicBall.getWidth(), magicBall.getHeight());
					Rectangle enemyRec = new Rectangle(tempEnemy.getX()+10, tempEnemy.getY()+20, enemyPic.getWidth()-50, enemyPic.getHeight()-50);

					if (magicRec.intersects(enemyRec)) {
						explosions.add(new Explosion(tempEnemy.getX(), tempEnemy.getY()));
						verticalEnemyList.remove(j);
						try {
							playSound("Sprites/explosion.wav");
						} catch (MalformedURLException ex) {
						} catch (LineUnavailableException ex) {
						} catch (UnsupportedAudioFileException ex) {
						} catch (IOException ex) {
						}
						score += 100;
					}
				}


				//remove far-offscreen magicballs
				if (tempMagicalBall.getX() < -200 || tempMagicalBall.getX() > 2100 || tempMagicalBall.getY() < -200 || tempMagicalBall.getY() > 1200) {
					magicBalls.remove(i);
				}
			}

			//move blocks
			for (int i = 0; i < blockList.size(); i ++) {
				Block tempBlock = blockList.get(i);
				tempBlock.setX(tempBlock.getX() + tempMoveSpeed);
			}
			//move horizontalBlocks
			for (int i = 0; i < horizontalBlockList.size(); i ++) {
				HorizontalBlock tempBlock = horizontalBlockList.get(i);
				tempBlock.setStartX(tempBlock.getStartX() + tempMoveSpeed);
				tempBlock.setEndX(tempBlock.getEndX() + tempMoveSpeed);


				if (tempBlock.getDirectionRight()) {
					tempBlock.setX(tempBlock.getX() + tempBlock.getMoveSpeed() + tempMoveSpeed);
					if (tempBlock.getX() >= tempBlock.getEndX()) {
						tempBlock.setDirectionRight(false);
					}
				}
				else {
					tempBlock.setX(tempBlock.getX() - tempBlock.getMoveSpeed() + tempMoveSpeed);
					if (tempBlock.getX() <= tempBlock.getStartX()) {
						tempBlock.setDirectionRight(true);
					}
				}
				//System.out.println(tempBlock);
			}

			//move horizontalEnemies
			for (int i = horizontalEnemyList.size()-1; i >= 0; i --) {
				HorizontalEnemy tempEnemy = horizontalEnemyList.get(i);
				tempEnemy.increment();

				tempEnemy.setStartX(tempEnemy.getStartX() + tempMoveSpeed);
				tempEnemy.setEndX(tempEnemy.getEndX() + tempMoveSpeed);

				if (tempEnemy.getDirectionRight()) {
					tempEnemy.setX(tempEnemy.getX() + tempEnemy.getMoveSpeed() + tempMoveSpeed);
					if (tempEnemy.getX() >= tempEnemy.getEndX()) {
						tempEnemy.setDirectionRight(false);
					}
				}
				else {
					tempEnemy.setX(tempEnemy.getX() - tempEnemy.getMoveSpeed() + tempMoveSpeed);
					if (tempEnemy.getX() <= tempEnemy.getStartX()) {
						tempEnemy.setDirectionRight(true);
					}
				}

				//wizard hits enemy
				Rectangle wizardRectangle = new Rectangle(wizard.getX(), wizard.getY(), wizard.getImage().getWidth(), wizard.getImage().getHeight());
				Rectangle enemyRectangle = new Rectangle(tempEnemy.getX(), tempEnemy.getY(), enemyPic.getWidth(), enemyPic.getHeight());
				if (wizardRectangle.intersects(enemyRectangle)) {
					explosions.add(new Explosion(tempEnemy.getX(), tempEnemy.getY()));
					horizontalEnemyList.remove(i);
					lives--;
					if (lives == 0) {
						gameState = 1;
					}
					try {
						playSound("Sprites/explosionGetHit.wav");
					} catch (MalformedURLException ex) {
					} catch (LineUnavailableException ex) {
					} catch (UnsupportedAudioFileException ex) {
					} catch (IOException ex) {
					}
				}
			}

			//move verticalEnemies
			for (int i = verticalEnemyList.size()-1; i >= 0; i --) {
				VerticalEnemy tempEnemy = verticalEnemyList.get(i);
				tempEnemy.increment();

				tempEnemy.setStartX(tempEnemy.getStartX() + tempMoveSpeed);
				tempEnemy.setEndX(tempEnemy.getEndX() + tempMoveSpeed);

				if (tempEnemy.getDirectionUp()) {
					tempEnemy.setX(tempEnemy.getX() + tempMoveSpeed);
					tempEnemy.setY(tempEnemy.getY() + tempEnemy.getMoveSpeed());
					if (tempEnemy.getY() >= tempEnemy.getEndY()) {
						tempEnemy.setDirectionUp(false);
					}
				}
				else {
					tempEnemy.setX(tempEnemy.getX() + tempMoveSpeed);
					tempEnemy.setY(tempEnemy.getY() - tempEnemy.getMoveSpeed());
					if (tempEnemy.getY() <= tempEnemy.getStartY()) {
						tempEnemy.setDirectionUp(true);
					}
				}

				//wizard hits enemy
				Rectangle wizardRectangle = new Rectangle(wizard.getX(), wizard.getY(), wizard.getImage().getWidth(), wizard.getImage().getHeight());
				Rectangle enemyRectangle = new Rectangle(tempEnemy.getX()+10, tempEnemy.getY()+20, enemyPic.getWidth()-50, enemyPic.getHeight()-50);
				if (wizardRectangle.intersects(enemyRectangle)) {
					explosions.add(new Explosion(tempEnemy.getX(), tempEnemy.getY()));
					verticalEnemyList.remove(i);
					lives--;
					if (lives == 0) {
						gameState = 1;
					}
					try {
						playSound("Sprites/explosionGetHit.wav");
					} catch (MalformedURLException ex) {
					} catch (LineUnavailableException ex) {
					} catch (UnsupportedAudioFileException ex) {
					} catch (IOException ex) {
					}
				}
			}

			//move powerups
			for (int i = 0; i < powerupList.size(); i ++) {
				Powerup tempPowerup = powerupList.get(i);
				tempPowerup.setX(tempPowerup.getX() + tempMoveSpeed);

				if (new Rectangle(wizard.getX(), wizard.getY(), wizard.getImage().getWidth(), wizard.getImage().getHeight()).intersects(new Rectangle((int)powerupList.get(i).getX(), (int)powerupList.get(i).getY(), 50, 50))) {
					activatePowerup(powerupList.get(i));
					powerupList.remove(i);
					score += 100;
				}
			}

			//increment and move explosions
			for (int i = explosions.size()-1; i >= 0; i --) {
				Explosion tempExplosion = explosions.get(i);
				tempExplosion.setX(tempExplosion.getX() + (int)tempMoveSpeed);

				tempExplosion.increment();
				if (tempExplosion.getCount() == 16) {
					explosions.remove(i);
				}
			}








			if (superJumpTimeLeft > 0) {
				superJumpTimeLeft --;
			}

			onBlock();
			if (blockOnTopOfIndex == winBlockIndex) {
				gameState = 2;
			}
			if (wizard.getX() > blockList.get(winBlockIndex).getX()) {
				gameState = 2;
			}

			if (loseLifeFramesCount > 0) {
				loseLifeFramesCount --;
			}


			/*frameCount--;                 //for checking animations and cutting from sprite sheet
			if (frameCount == 0)
			{
				frameCount = 20;
				animCount ++;
				if (animCount > 4)
				{
					animCount = 0;
				}
			}*/

			//10 frame pause
			try {
				t.sleep(10);
			} catch(InterruptedException e) {
			}

			repaint();
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		//background layers
		if (background1A < background1B)
		{
			g2d.drawImage(background1, (int)background1A, 0, null);      //ok so basically if i draw imageA first and imageB second when A is to the left of B it works fine,
			g2d.drawImage(background1, (int)background1B, 0, null);      //but if A moves to the right of B and I'm still drawing imageB first then the white line shows up.
		}                                                                //So basically I have to change the order that I draw the A and B images depending on which one is
		else                                                             //on the left and right. i did this whole thing cause i was sick of that tiny stupid white line in my
		{																 //last game project and idk why its doing this but this seems to fix it so whatev
			g2d.drawImage(background1, (int)background1B, 0, null);
			g2d.drawImage(background1, (int)background1A, 0, null);
		}
		if (background2A < background2B)
		{
			g2d.drawImage(background2, (int)background2A, 0, null);
			g2d.drawImage(background2, (int)background2B, 0, null);
		}
		else
		{
			g2d.drawImage(background2, (int)background2B, 0, null);
			g2d.drawImage(background2, (int)background2A, 0, null);
		}
		if (background3A < background3B)
		{
			g2d.drawImage(background3, (int)background3A, 0, null);
			g2d.drawImage(background3, (int)background3B, 0, null);
		}
		else
		{
			g2d.drawImage(background3, (int)background3B, 0, null);
			g2d.drawImage(background3, (int)background3A, 0, null);
		}

		//info
		if (toggleInfo) {
			g2d.drawString("MagicBall Count: " + magicBalls.size(), 50, 100);
			g2d.drawString("Background3A: " + background3A, 50, 125);
			g2d.drawString("Background3B: " + background3B, 50, 150);
			g2d.drawString("MoveSpeed: " + moveSpeed, 50, 175);
			g2d.drawString("TempMoveSpeed: " + tempMoveSpeed, 50, 200);
			g2d.drawString("X: " + wizard.getX(), 50, 225);
			g2d.drawString("Y: " + wizard.getY(), 50, 250);
			g2d.drawString("YSpeed: " + wizard.getYSpeed(), 50, 275);
			g2d.drawString("OnBlock: " + onBlock(), 50, 300);
			g2d.drawString("UnderBlock: " + underBlock(), 50, 325);
			g2d.drawString("LeftBlock: " + leftBlock(), 50, 350);
			g2d.drawString("RightBlock: " + rightBlock(), 50, 375);
			g2d.drawString("JumpTimeLeft: " + superJumpTimeLeft, 50, 400);
			g2d.drawString("Lives: " + lives, 50, 425);
			g2d.drawString("GameState: " + gameState, 50, 450);
		}

		//wizard
		g2d.drawImage(wizard.getImage(), wizard.getX(), wizard.getY(), null);

		//blocks
		for (int i = 0; i < blockList.size(); i ++) {

			if (i == winBlockIndex) {
				g2d.drawImage(winBlock, (int)blockList.get(i).getX(), (int)blockList.get(i).getY(), null);
			}
			else {
				g2d.drawImage(block, (int)blockList.get(i).getX(), (int)blockList.get(i).getY(), null);
			}
		}
		//horizontalBlocks
		for (int i = 0; i < horizontalBlockList.size(); i ++) {
			g2d.drawImage(block, (int)horizontalBlockList.get(i).getX(), (int)horizontalBlockList.get(i).getY(), null);
			//System.out.println("Drawing: " + horizontalBlockList.get(i).toString());
		}

		//horizontalEnemies
		for (int i = 0; i < horizontalEnemyList.size(); i ++) {
			//g2d.drawImage(enemyPic, (int)horizontalEnemyList.get(i).getX(), (int)horizontalEnemyList.get(i).getY(), null);
			//HorizontalEnemy tempEnemy = horizontalEnemyList.get(i);
			g2d.drawImage(enemySprites[horizontalEnemyList.get(i).getFrameCount()], (int)horizontalEnemyList.get(i).getX(), (int)horizontalEnemyList.get(i).getY(), null);
			//g2d.drawRect(tempEnemy.getX()+10, tempEnemy.getY()+20, enemyPic.getWidth()-50, enemyPic.getHeight()-50);
		}
		//verticalEnemies
		for (int i = 0; i < verticalEnemyList.size(); i ++) {
			//g2d.drawImage(enemyPic, (int)verticalEnemyList.get(i).getX(), (int)verticalEnemyList.get(i).getY(), null);
			g2d.drawImage(enemySprites[verticalEnemyList.get(i).getFrameCount()], (int)verticalEnemyList.get(i).getX(), (int)verticalEnemyList.get(i).getY(), null);
		}

		//powerups
		for (int i = 0; i < powerupList.size(); i++) {
			g2d.drawImage(questionBlock, (int)powerupList.get(i).getX(), (int)powerupList.get(i).getY(), null);
		}

		//magicballs
		for (int i = magicBalls.size()-1; i >= 0; i --) {
			AffineTransform backup = g2d.getTransform();
			AffineTransform trans = new AffineTransform();
			trans.rotate( Math.toRadians(360-magicBalls.get(i).getAngle()), magicBalls.get(i).getX(), magicBalls.get(i).getY()); //the points to rotate around
			g2d.transform( trans );
			g2d.drawImage( magicBall, (int)magicBalls.get(i).getX(), (int)magicBalls.get(i).getY(), null );  // the actual location of the sprite
			g2d.setTransform( backup ); // restore previous transform
		}

		//explosions
		for (int i = 0; i < explosions.size(); i ++) {
			g2d.drawImage(enemyExplosion[explosions.get(i).getCount()], explosions.get(i).getX(), explosions.get(i).getY(), null);
		}



		//gameOver
		if (gameState > 0) {
			g2d.setColor(Color.BLACK);

			if (gameState == 1) {
				g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, 70));
				g2d.setColor(Color.RED);
				g2d.drawString("You Lost!", 600, 350);
			}
			else if (gameState == 2) {
				g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, 70));
				g2d.setColor(Color.GREEN);
				g2d.drawString("You Won!", 600, 350);
				score += timeRemaining;
				g2d.drawString("Score: " + score, 600, 450);
			}
		}

		//actual info
		g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, 20));
		g2d.setColor(Color.BLACK);
		g2d.drawString("Lives: " + lives, 50, 50);
		g2d.drawString("Score: " + score, 200, 50);


		//g2d.drawImage(block, (int)(wizard.getX() + wizard.getWidth() - 50), wizard.getY(), null);
		//g2d.drawImage(block, (int)(wizard.getX() + 45), wizard.getY(), null);


		//g2d.drawImage(temp[animCount], 700, 500, null);       //for cutting from sprite sheet
	}



	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 68) { //d
			//moveSpeed = 0;
			right = false;
		}
		else if (e.getKeyCode() == 65) { //a
			//moveSpeed = 0;
			left = false;
		}
	}
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());

		if (e.getKeyCode() == 32) { //spacebar
			//wizard.jump();
			if (onBlock()) {
				System.out.println("Jump!");
				wizard.jump(superJumpTimeLeft);
			}
		}
		else if (e.getKeyCode() == 73) { //i
			toggleInfo = !toggleInfo;
		}
		else if (e.getKeyCode() == 68) { //d
			setDirection("right");
			right = true;
			//moveSpeed = 8;
		}
		else if (e.getKeyCode() == 65) { //a
			setDirection("left");
			left = true;
			//moveSpeed = 8;
		}
		else if (e.getKeyCode() == 66) { //d
			for (int i = 0; i < blockList.size(); i ++) {
				System.out.println(blockList.get(i));
			}
		}
	}
	public void keyTyped(KeyEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
	public void mouseEntered(MouseEvent e) {

	}
	public void mouseReleased(MouseEvent e) {

	}
	public void mousePressed(MouseEvent e) {
		System.out.println("X: " + e.getX() + ", Y: " + e.getY());

		//there's a bit of offset between the g2d drawings and the mouseEvents coordinates, so this is to adjust for that
		double mouseX = e.getX()-10;
		double mouseY = e.getY()-40;
		System.out.println("ADJUSTED - X: " + mouseX + ", Y: " + mouseY);

		System.out.println("Wizard X: " + wizard.getX() + ", Y: " + wizard.getY());

		//locates the screen location of the wizards staff
		double spawnPosX = (double)(wizard.getX() + 122 - 23);    //(122, 64) is the screen location of where I want the orbs to come out, but the orb picture starts at the
		double spawnPosY = (double)(wizard.getY() +  64 - 23);    //top left corner, so the spawnPos are moved over exactly half the picture length/height

		//calculates the angle between the mouse click and the staff, and adjusts angle depending on variable circumstances
		double angle = Math.toDegrees(Math.atan((spawnPosY - mouseY)/(mouseX-spawnPosX)));
		System.out.println("Initial Angle: " + angle);
		if (angle < 0) {
			angle += 180;
		}
		//if (angle == -0.0) {
			//angle += 180;
		//}
		if (mouseY > (wizard.getY()+62)) {   //about the horizontal middle of wizard's magic stick
			angle += 180;
		}
		if (mouseX < (wizard.getX()+87)  && angle < 90) {
			angle += 180;
		}
		if (mouseX > (wizard.getX()+119)) {
			if (angle < 0) {
				angle += 360;
			}
			if (angle > 90 && angle < 270) {
				angle += 180;
			}
		}
		System.out.println("Final angle: " + angle);

		//adds all the completed components of the magicBall to the arrayList
		magicBalls.add(new MagicBall(spawnPosX, spawnPosY, angle, 12));
		try {
			playSound("Sprites/laserPew.wav");
		} catch (MalformedURLException ex) {
		} catch (LineUnavailableException ex) {
		} catch (UnsupportedAudioFileException ex) {
		} catch (IOException ex) {
		}
	}
	public void mouseClicked(MouseEvent e) {

	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
	public static BufferedImage resize(BufferedImage img, double scale) {
		int height = img.getHeight();
		int width = img.getWidth();
		return resize(img, (int)(scale*width), (int)(scale*height));
	}
	public void setDirection(String direction) {
		if (direction.equals("right")) {
			lastDirection = "right";
			lastDirectionNum = 1;
		}
		else if (direction.equals("left")) {
			lastDirection = "left";
			lastDirectionNum = -1;
		}
	}
	public void readBlockMap() {

		File blockMapFile = new File("Map.txt");

		String str = "";
		int rows = 0;
		int cols = 0;
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(blockMapFile));
			String text = "";
			while( (text=input.readLine())!= null)
			{
				cols = text.length();
				rows ++;
			}
			blockMap = new String[rows][cols];
			System.out.println(blockMap.length + ", " + blockMap[0].length);

			System.out.println("break");


			BufferedReader input2 = new BufferedReader(new FileReader(blockMapFile));
			text = "";
			int rowCount = 0;
			while( (text=input2.readLine())!= null)
			{
				for (int i = 0; i < text.length(); i ++) {
					blockMap[rowCount][i] = Character.toString(text.charAt(i));
				}
				rowCount++;
			}

			int blockX = 0;
			int blockY = 0;
			for (int row = 0; row < blockMap.length; row ++) {
				for (int col = 0; col < blockMap[row].length; col ++) {
					System.out.print(blockMap[row][col] + " ");
					if (blockMap[row][col].equals("x")) {
						blockList.add(new Block(blockX, blockY));
					}
					if (blockMap[row][col].equals("h")) {
						horizontalBlockList.add(new HorizontalBlock(blockX, blockY, blockX+200, blockY));
					}
					if (blockMap[row][col].equals("p")) {
						powerupList.add(new Powerup(blockX, blockY, "jump"));
					}
					if (blockMap[row][col].equals("w")) {
						blockList.add(new Block(blockX, blockY));
						//winBlock = blockList.get(blockList.size()-1);
						winBlockIndex = blockList.size()-1;
					}
					if (blockMap[row][col].equals("e")) {
						horizontalEnemyList.add(new HorizontalEnemy(blockX-200, blockY, blockX, blockY));
					}
					if (blockMap[row][col].equals("v")) {
						verticalEnemyList.add(new VerticalEnemy(blockX, blockY, blockX, blockY + 200));
					}
					blockX += 50;
				}
				System.out.println();
				blockY += 50;
				blockX = 0;
			}
		}
		catch (IOException io)
		{
			System.err.println("File does not exist");
		}
	}

	public boolean onBlock() {

		boolean onRegular = false;
		boolean onHorizontal = false;

		//check regular
		for (int i = 0; i < blockList.size(); i ++) {

			boolean checkYTop = (wizard.getY() >= blockList.get(i).getY() && wizard.getY() <= blockList.get(i).getY()+50);
			boolean checkYBot = (wizard.getY() + 110 >= blockList.get(i).getY() && wizard.getY() + 110 <= blockList.get(i).getY()+40);
			boolean checkYCenter = (wizard.getY() <= blockList.get(i).getY() && wizard.getY()+110 >= blockList.get(i).getY()+50);

			boolean checkXLeft = (wizard.getX() >= blockList.get(i).getX() && wizard.getX()+25 <= blockList.get(i).getX()+50);
			//boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 >= blockList.get(i).getX()+10 && wizard.getX()+wizard.getWidth()- 50 <= blockList.get(i).getX()+50);
			boolean checkXCenter = (wizard.getX()-10 <= blockList.get(i).getX()-10 && wizard.getX()+wizard.getWidth()-25 >= blockList.get(i).getX()+60);
			boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 <= blockList.get(i).getX() + 50 && wizard.getX()+wizard.getWidth()-50-25 >= blockList.get(i).getX());

			if ( checkYBot && (checkXLeft || checkXCenter || checkXRight)) {
				blockOnTopOfIndex = i;
				onTopOfRegular = true;
				onTopOfRegularNum = 1;
				onRegular = true;
				//return true
			}
		}
		//return false;

		//check horizontal
		for (int i = 0; i < horizontalBlockList.size(); i ++) {

			boolean checkYTop = (wizard.getY() >= horizontalBlockList.get(i).getY() && wizard.getY() <= horizontalBlockList.get(i).getY()+50);
			boolean checkYBot = (wizard.getY() + 110 >= horizontalBlockList.get(i).getY() && wizard.getY() + 110 <= horizontalBlockList.get(i).getY()+40);
			boolean checkYCenter = (wizard.getY() <= horizontalBlockList.get(i).getY() && wizard.getY()+110 >= horizontalBlockList.get(i).getY()+50);

			boolean checkXLeft = (wizard.getX() >= horizontalBlockList.get(i).getX() && wizard.getX()+25 <= horizontalBlockList.get(i).getX()+50);
			//boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 >= blockList.get(i).getX()+10 && wizard.getX()+wizard.getWidth()- 50 <= blockList.get(i).getX()+50);
			boolean checkXCenter = (wizard.getX()-10 <= horizontalBlockList.get(i).getX()-10 && wizard.getX()+wizard.getWidth()-25 >= horizontalBlockList.get(i).getX()+60);
			boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 <= horizontalBlockList.get(i).getX() + 50 && wizard.getX()+wizard.getWidth()-50-25 >= horizontalBlockList.get(i).getX());

			if ( checkYBot && (checkXLeft || checkXCenter || checkXRight)) {
				blockOnTopOfIndex = i;
				onHorizontal = true;
				onTopOfRegular = false;
				onTopOfRegularNum = 2;
			}
		}

		if (onRegular || onHorizontal) {
			return true;
		}
		onTopOfRegularNum = 0;
		return false;

	}

	public boolean underBlock() {
		boolean regular = false;
		boolean horizontal = false;

		//regular
		for (int i = 0; i < blockList.size(); i ++) {

			boolean checkYTop = (wizard.getY() >= blockList.get(i).getY() && wizard.getY()+10 <= blockList.get(i).getY()+50);


			boolean checkXLeft = (wizard.getX() >= blockList.get(i).getX() && wizard.getX()+45 <= blockList.get(i).getX()+50);
			boolean checkXCenter = (wizard.getX()-10 <= blockList.get(i).getX()-10 && wizard.getX()+wizard.getWidth()-25 >= blockList.get(i).getX()+60);

			//boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 >= blockList.get(i).getX()+20 && wizard.getX()+wizard.getWidth()- 50 <= blockList.get(i).getX()+50);
			boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 <= blockList.get(i).getX() + 50 && wizard.getX() + wizard.getWidth() - 50 - 45 >= blockList.get(i).getX());


			if ( checkYTop && (checkXLeft || checkXCenter || checkXRight)) {
				regular = true;
			}
		}

		//horizontal
		for (int i = 0; i < horizontalBlockList.size(); i ++) {

			boolean checkYTop = (wizard.getY() >= horizontalBlockList.get(i).getY() && wizard.getY()+10 <= horizontalBlockList.get(i).getY()+50);


			boolean checkXLeft = (wizard.getX() >= horizontalBlockList.get(i).getX() && wizard.getX()+45 <= horizontalBlockList.get(i).getX()+50);
			boolean checkXCenter = (wizard.getX()-10 <= horizontalBlockList.get(i).getX()-10 && wizard.getX()+wizard.getWidth()-25 >= horizontalBlockList.get(i).getX()+60);

			//boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 >= blockList.get(i).getX()+20 && wizard.getX()+wizard.getWidth()- 50 <= blockList.get(i).getX()+50);
			boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 <= horizontalBlockList.get(i).getX() + 50 && wizard.getX() + wizard.getWidth() - 50 - 45 >= horizontalBlockList.get(i).getX());


			if ( checkYTop && (checkXLeft || checkXCenter || checkXRight)) {
				horizontal = true;
			}
		}

		if (regular || horizontal) {
			return true;
		}
		return false;
	}
	public boolean leftBlock() {
			for (int i = 0; i < blockList.size(); i ++) {

				boolean checkYTop = (wizard.getY() >= blockList.get(i).getY() && wizard.getY() <= blockList.get(i).getY()+50);
				boolean checkYBot = (wizard.getY() + 110 >= blockList.get(i).getY() && wizard.getY() + 110 <= blockList.get(i).getY()+40);
				boolean checkYCenter = (wizard.getY() <= blockList.get(i).getY() && wizard.getY()+110 >= blockList.get(i).getY()+50);

				boolean checkXLeft = (wizard.getX()-10 >= blockList.get(i).getX() - 10 && wizard.getX()+25 <= blockList.get(i).getX()+50);
				boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 >= blockList.get(i).getX()+10 && wizard.getX()+wizard.getWidth()- 50 <= blockList.get(i).getX()+50);
				boolean checkXCenter = (wizard.getX()-10 <= blockList.get(i).getX()-10 && wizard.getX()+wizard.getWidth()-25 >= blockList.get(i).getX()+60);

				if ( checkXRight && (checkYCenter)) {

					return true;
				}
			}
			return false;
	}
	public boolean rightBlock() {
		for (int i = 0; i < blockList.size(); i ++) {

			boolean checkYTop = (wizard.getY() >= blockList.get(i).getY() && wizard.getY() <= blockList.get(i).getY()+50);
			boolean checkYBot = (wizard.getY() + 110 >= blockList.get(i).getY() && wizard.getY() + 110 <= blockList.get(i).getY()+40);
			boolean checkYCenter = (wizard.getY() <= blockList.get(i).getY() && wizard.getY()+110 >= blockList.get(i).getY()+50);

			boolean checkXLeft = (wizard.getX() >= blockList.get(i).getX() && wizard.getX()+25 <= blockList.get(i).getX()+50);
			boolean checkXRight = (wizard.getX() + wizard.getWidth() - 50 >= blockList.get(i).getX()+10 && wizard.getX()+wizard.getWidth()- 50 <= blockList.get(i).getX()+50);
			boolean checkXCenter = (wizard.getX()-10 <= blockList.get(i).getX()-10 && wizard.getX()+wizard.getWidth()-25 >= blockList.get(i).getX()+60);

			if ( checkXLeft && (checkYCenter)) {

				return true;
			}
		}
		return false;
	}



	public void incrementWizard() {

			wizard.setCurrentStateFrames(wizard.getCurrentStateFrames()-1);

			wizard.setY((int)(wizard.getY() + wizard.getYSpeed()));

			if (!onBlock()) { //if falling
				wizard.setYSpeed(wizard.getYSpeed() + 0.75);


				if (wizard.getCurrentStateFrames() == 0)
				{
					wizard.setCurrentStateFrames(wizard.getJumpStateFrames());
					wizard.setJumpAnimCount(wizard.getJumpAnimCount() + 1);
					if (wizard.getJumpAnimCount() == 5)
					{
						wizard.setJumpAnimCount(4);
					}
					wizard.setImage(wizard.getJumpAnim()[wizard.getJumpAnimCount()]);
				}
			}

			if (onBlock()) {  //if hits block
				//wizard.setY(590);
				//System.out.println("Hit block");
				//currentlyJumping = false;
				wizard.setYSpeed(0);
				//wizard.setY((int)(blockList.get(blockOnTopOfIndex).getY()-110));
				if (onTopOfRegular) {
					wizard.setY((int)(blockList.get(blockOnTopOfIndex).getY()-110));
				}
				else {
					wizard.setY((int)(horizontalBlockList.get(blockOnTopOfIndex).getY()-110));
				}
				wizard.setJumpAnimCount(0);
				//jumpAnimCount = 0;
				//runAnimCount = 0;
				//currentStateFrames = runStateFrames;
				//img = runAnim[0];

				if (wizard.getCurrentStateFrames() == 0) {
					wizard.setCurrentStateFrames(wizard.getRunStateFrames());
					wizard.setRunAnimCount(wizard.getRunAnimCount() + 1);
					if (wizard.getRunAnimCount() == 4) {
						wizard.setRunAnimCount(0);
					}
					wizard.setImage(wizard.getRunAnim()[wizard.getRunAnimCount()]);
				}



			}


		if (wizard.getY() >= 2000) {
			wizard.setY(590);
			wizard.setX(200);
			wizard.setYSpeed(0);
			lives--;
			restart();
			if (lives == 0) {
				gameState = 1;
			}
		}



	}

	public void activatePowerup(Powerup powerup) {
		if (powerup.getEffect().equals("jump")) {
			superJumpTimeLeft = superJumpTimeSafe;
		}
		try {
			playSound("Sprites/pickupSound.wav");
		} catch (MalformedURLException ex) {
		} catch (LineUnavailableException ex) {
		} catch (UnsupportedAudioFileException ex) {
		} catch (IOException ex) {
		}
	}
	public static void playSound(String fileName) throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException{
		    File url = new File(fileName);
		    Clip clip = AudioSystem.getClip();

		    AudioInputStream ais = AudioSystem.getAudioInputStream( url );
		    clip.open(ais);
		    clip.start();
	}
	public void restart() {



			score = 0;
			scale = 1.0;

			toggleInfo = false;  //normally false

			 BufferedImage background1, background2, background3;
			 background1A = 0;    //x position of layer 1, A copy
			 background1B = 1920; //x position of layer 2, B copy
			 background2A = 0;
			 background2B = 1920;
			 background3A = 0;
			 background3B = 1920;

			 moveSpeed = 0;
			 tempMoveSpeed = 0;
			 lastDirection = "right";
			 lastDirectionNum = 1;   //1 - right,      -1 - left




			 right = false;
			 left = false;





			blockOnTopOfIndex = 0;
			onTopOfRegular = false;
			onTopOfRegularNum = 0;    //0 = not on block,    1 = onRegularBlock,     2 = onHorizontalBlock

			superJumpTimeLeft = 0;


			gameState = 0;   //0 = game active,     1 = lose,      2 = win


		winBlockIndex = 0;












				wizard = new Wizard();
				magicBalls = new ArrayList<MagicBall>();
				blockList = new ArrayList<Block>();
				horizontalBlockList = new ArrayList<HorizontalBlock>();
				powerupList = new ArrayList<Powerup>();
				horizontalEnemyList = new ArrayList<HorizontalEnemy>();
				verticalEnemyList = new ArrayList<VerticalEnemy>();

				try { //image stuff


					readBlockMap();



				}
				catch (Exception e) {}






	}

	public static void main(String[]args) {
		SideScroller app = new SideScroller();
	}
}