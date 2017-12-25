package games.render;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import games.util.FrameRate;
import games.util.KeyboardInput;
import games.util.RelativeMouseInput;

public class RelativeMouseExample extends JFrame implements Runnable {
	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameTread;
	private Canvas canvas;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	private Point point = new Point(0, 0);
	private boolean disableCursor = false;
	
	public RelativeMouseExample() {
		frameRate = new FrameRate();
	}

	protected void createAndShowGUI(){
		canvas = new Canvas();
		canvas.setSize(640, 480);
		canvas.setBackground(Color.black);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Relative Mouse Example");
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
		frameRate.initalize();
		while (running) {
			gameLoop();
		}
	}

	private void render(Graphics g){
		g.setColor(Color.GREEN);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 20, 20);
		g.drawString(mouse.getPosition().toString(), 20, 35);
		g.drawString("Press C to toggle cursor", 20, 60);
		g.drawString("Press Space to switch mouse modes", 20, 75);
		g.drawString("Relative "+ mouse.isRelative(), 20, 90);
		g.setColor(Color.WHITE);
		g.drawRect(point.x, point.y, 25, 25);
	}
	
	private void processInput(){
		keyboard.poll();
		mouse.poll();
		Point p = mouse.getPosition();
		if(mouse.isRelative()){
			point.x += p.x;
			point.y += p.y;
		}else{
			point.x = p.x;
			point.y = p.y;
		}
		if(point.x+25 < 0){
			point.x = canvas.getWidth() - 1;
		}else if(point.x > canvas.getWidth()-1){
			point.x = -25;
		}
		if(point.y+25 < 0){
			point.y = canvas.getHeight() - 1;
		}else if(point.y > canvas.getHeight()-1){
			point.y = -25;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)){
			mouse.setRelative(!mouse.isRelative());
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_C)){
			disableCursor = !disableCursor;
			if(disableCursor){
				disableCursor();
			}else{
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}
	
	private void disableCursor(){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = getToolkit().createImage("");
		Point point = new Point(0, 0);
		String name = "CanBeAnything";
		Cursor cursor = toolkit.createCustomCursor(image, point, name);
		setCursor(cursor);
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
		final RelativeMouseExample app = new RelativeMouseExample();
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
}





















