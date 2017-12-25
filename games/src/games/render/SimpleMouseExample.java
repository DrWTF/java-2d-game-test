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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import games.util.FrameRate;
import games.util.KeyboardInput;
import games.util.SimpleMouseInput;

public class SimpleMouseExample extends JFrame implements Runnable {
	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameTread;
	private SimpleMouseInput mouse;
	private KeyboardInput keyboard;
	private ArrayList<Point> lines = new ArrayList<Point>();
	private boolean drawingLine;
	private Color[] COLORS = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
	private int colorIndex;
	
	public SimpleMouseExample() {
		frameRate = new FrameRate();
	}

	protected void createAndShowGUI(){
		Canvas canvas = new Canvas();
		canvas.setSize(640, 480);
		canvas.setBackground(Color.black);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Simple Mouse Example");
		setIgnoreRepaint(true);
		pack();
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		mouse = new SimpleMouseInput();
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
		colorIndex += mouse.getNotches();
		Color color = COLORS[Math.abs(colorIndex % COLORS.length)];
		g.setColor(color);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 30, 30);
		g.drawString("Use mouse to draw lines", 30, 45);
		g.drawString("Press C to clear lines", 30, 60);
		g.drawString("Mouse Wheel cycles colors", 30, 75);
		g.drawString(mouse.getPosition().toString(), 30, 90);
		for(int i=0; i<lines.size()-1; i++){
			Point p1 = lines.get(i);
			Point p2 = lines.get(i+1);
			if(!(p1==null || p2==null)){
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
	}
	
	private void processInput(){
		keyboard.poll();
		mouse.poll();
		if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)){
			System.out.println("VK_SPACE");
		}
		if(mouse.buttonDownOnce(MouseEvent.BUTTON1)){
			drawingLine = true;
		}
		if(mouse.buttonDown(MouseEvent.BUTTON1)){
			lines.add(mouse.getPosition());
		}else if(drawingLine){
			lines.add(null);
			drawingLine = false;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_C)){
			lines.clear();
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
		final SimpleMouseExample app = new SimpleMouseExample();
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





















