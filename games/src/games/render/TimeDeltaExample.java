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

public class TimeDeltaExample extends JFrame implements Runnable {
	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameTread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	
	private Canvas canvas;
	private float angle;
	private float step;
	private long sleep;
	
	public TimeDeltaExample() {
	}

	protected void createAndShowGUI(){
		canvas = new Canvas();
		canvas.setSize(480, 480);
		canvas.setBackground(Color.WHITE);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Time Delta Example");
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
		long curTime = System.nanoTime();
		long lastTime = curTime;
		double nsPerFrame;
		while (running) {
			curTime = System.nanoTime();
			nsPerFrame = curTime - lastTime;
			gameLoop(nsPerFrame / 1.0e9);
			lastTime = curTime;
		}
	}

	private void render(Graphics g){
		g.setColor(Color.BLACK);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 30, 30);
		g.drawString("Up arrow increases sleep time", 30, 45);
		g.drawString("Down arrow decreases sleep time", 30, 60);
		g.drawString("Sleep time "+sleep+" ms", 30, 75);

		int x = canvas.getWidth()/4;
		int y = canvas.getHeight()/4;
		int w = canvas.getWidth()/2;
		int h = canvas.getHeight()/2;
		g.drawOval(x, y, w, h);
		
		float rw = w/2;
		float rh = h/2;
		int rx = (int)(rw * Math.cos(angle));
		int ry = (int)(rh * Math.sin(angle));
		int cx = (int)(rx + w);
		int cy = (int)(ry + h);
		g.drawLine(w, h, cx, cy);
		g.drawRect(cx-2, cy-2, 4, 4);
	}
	
	private void processInput(double delta){
		keyboard.poll();
		mouse.poll();
		if(keyboard.keyDownOnce(KeyEvent.VK_UP)){
			sleep += 10;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_DOWN)){
			sleep -= 10;
		}
		if(sleep > 1000){
			sleep = 1000;
		}
		if(sleep < 0){
			sleep = 0;
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
	
	private void gameLoop(double delta){
		processInput(delta);
		updateObjects(delta);
		renderFrame();
		sleep(sleep);
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
		final TimeDeltaExample app = new TimeDeltaExample();
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
		angle = 0.0f;
		step = (float)Math.PI / 2.0f;
	}
	
	private void updateObjects(double delta){
		angle += step * delta;
		if(angle > 2*Math.PI){
			angle -= 2*Math.PI;
		}
	}
}





















