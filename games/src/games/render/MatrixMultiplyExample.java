package games.render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import games.util.FrameRate;
import games.util.KeyboardInput;
import games.util.Matrix3x3f;
import games.util.RelativeMouseInput;
import games.util.SimpleMouseInput;
import games.util.Vector2f;

public class MatrixMultiplyExample extends JFrame implements Runnable {
	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameTread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;

	private static final int SCREEN_W = 640;
	private static final int SCREEN_H = 480;
	private float earthRot, earthDelta;
	private float moonRot, moonDelta;
	private boolean showStars;
	private int[] stars;
	private Random rand = new Random();
	
	public MatrixMultiplyExample() {
	}

	protected void createAndShowGUI(){
		Canvas canvas = new Canvas();
		canvas.setSize(640, 480);
		canvas.setBackground(Color.black);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Matrix Multiply Example");
		setIgnoreRepaint(true);
		pack();
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		setVisible(true);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		gameTread = new Thread(this);
		gameTread.start();
	}
	
	@Override
	public void run() {
		running = true;
		initialize();
		while (running) {
			gameLoop();
		}
	}

	private void render(Graphics g){
		g.setColor(Color.GREEN);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 30, 30);
		g.drawString("Press [SPACE] to toggle stars", 30, 45);

		if(showStars){
			g.setColor(Color.WHITE);
			for(int i=0; i<stars.length-1; i+=2){
				g.fillRect(stars[i], stars[i+1], 1, 1);
			}
		}
		
		Matrix3x3f sunMat = Matrix3x3f.identity();
		sunMat = sunMat.mul(Matrix3x3f.translate(SCREEN_W/2, SCREEN_H/2));
		Vector2f sun = sunMat.mul(new Vector2f());
		g.setColor(Color.RED);
		g.fillOval((int)sun.x-50, (int)sun.y-50, 100, 100);
		
		g.setColor(Color.WHITE);
		g.drawOval((int)sun.x-SCREEN_W/4, (int)sun.y-SCREEN_H/4, SCREEN_W/2, SCREEN_H/2);
		
		Matrix3x3f earthMat = Matrix3x3f.translate(SCREEN_W/4, 0);
		earthMat = earthMat.mul(Matrix3x3f.rotate(earthRot));
		earthMat = earthMat.mul(sunMat);
		earthRot += earthDelta;
		Vector2f earth = earthMat.mul(new Vector2f());
		g.setColor(Color.BLUE);
		g.fillOval((int)earth.x-10, (int)earth.y-10, 20, 20);
		
		Matrix3x3f moonMat = Matrix3x3f.translate(30, 0);
		moonMat = moonMat.mul(Matrix3x3f.rotate(moonRot));
		moonMat = moonMat.mul(earthMat);
		moonRot += moonDelta;
		Vector2f moon = moonMat.mul(new Vector2f());
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval((int)moon.x-5, (int)moon.y-5, 10, 10);
	}
	
	private void processInput(){
		keyboard.poll();
		mouse.poll();
		if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)){
			showStars = !showStars;
		}
	}
	
	private void sleep(long sleep){
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void renderFrame(){
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				} finally {
					if (g != null) {
						g.dispose();
					}
				}
				bs.show();
			} while (bs.contentsRestored());
		} while (bs.contentsLost());
	}
	
	private void gameLoop(){
		processInput();
		renderFrame();
		sleep(10L);
	}
	
	protected void onWindowClosing(){
		try {
			running = false;
			gameTread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public static void main(String[] args){
		final MatrixMultiplyExample app = new MatrixMultiplyExample();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
	
	private void initialize(){
		frameRate = new FrameRate();
		frameRate.initalize();
		earthDelta = (float)Math.toRadians(0.5);
		moonDelta = (float)Math.toRadians(2.5);
		showStars = true;
		stars = new int[1000];
		for(int i=0; i<stars.length-1; i+=2){
			stars[i] = rand.nextInt(SCREEN_W);
			stars[i+1] = rand.nextInt(SCREEN_H);
		}
	}
}





















