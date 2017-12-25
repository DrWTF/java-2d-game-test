package games.util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SimpleFramework extends JFrame implements Runnable {
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	
	protected FrameRate frameRate;
	protected Canvas canvas;
	
	protected RelativeMouseInput mouse;
	protected KeyboardInput keyboard;

	protected Color appBackground = Color.black;
	protected Color appBorder = Color.lightGray;
	protected Color appFPSColor = Color.green;
	
	protected Font appFont = new Font("Courier New", Font.PLAIN, 14);
	protected String appTitle = "TBD-Title";
	protected int appWidth = 640;
	protected int appHeight = 480;
	protected float appWorldWidth = 2.0f;
	protected float appWorldHeiht = 2.0f;
	protected long appSleep = 10L;
	
	protected boolean appMaintainRatio = false;
	protected float appBorderScale = 0.8f;
	
	protected void createAndShowGUI(){
		canvas = new Canvas();
		canvas.setBackground(appBackground);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setLocationByPlatform(true);
		setIgnoreRepaint(true);
		if(appMaintainRatio){
			getContentPane().setBackground(appBorder);
			setSize(appWidth, appHeight);
			setLayout(null);
			getContentPane().addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e){
					onComponentResized(e);
				}
			});
		}else{
			canvas.setSize(appWidth, appHeight);
			pack();
		}
		setTitle(appTitle);
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
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	protected void onComponentResized(ComponentEvent e){
		Dimension size = getContentPane().getSize();
		int vw = (int)(size.width * appBorderScale);
		int vh = (int)(size.height * appBorderScale);
		int vx = (int)(size.width - vw) / 2;
		int vy = (int)(size.height -vh) / 2;
		int newW = vw;
		int newH = (int)(vw * appWorldHeiht / appWorldWidth);
		if(newH > vh){
			newW = (int)(vh * appWorldHeiht / appWorldHeiht);
			newH = vh;
		}
		vx += (vw - newW) / 2;
		vy += (vh - newH) / 2;
		canvas.setLocation(vx, vy);
		canvas.setSize(newW, newH);
	}
	
	protected Matrix3x3f getViewportTransform(){
		return Utility.createViewport(appWorldWidth, appWorldHeiht, canvas.getWidth(), canvas.getHeight());
	}
	
	protected Matrix3x3f getReverseViewportTransform(){
		return Utility.createReverseViewport(appWorldWidth, appWorldHeiht, canvas.getWidth(), canvas.getHeight());
	}
	
	protected Vector2f getWorldMousePosition() {
		Matrix3x3f screenToWorld = getReverseViewportTransform();
		Point mousePos = mouse.getPosition();
		Vector2f screenPos = new Vector2f(mousePos.x, mousePos.y);
		return screenToWorld.mul(screenPos);
	}
	
	protected Vector2f getRelativeWorldMousePosition(){
		float sx = appWorldWidth / (canvas.getWidth() - 1);
		float sy = appWorldHeiht / (canvas.getHeight() - 1);
		Matrix3x3f viewport = Matrix3x3f.scale(sx, -sy);
		Point p = mouse.getPosition();
		return viewport.mul(new Vector2f(p.x, p.y));
	}
	
	protected void initialize(){
		frameRate = new FrameRate();
		frameRate.initalize();
	}
	
	protected void terminate(){
		
	}
	

	protected void processInput(double delta){
		keyboard.poll();
		mouse.poll();
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
	
	protected void render(Graphics g) {
		g.setFont(appFont);
		g.setColor(appFPSColor);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 20, 20);
	}

	protected void updateObjects(double delta){
		
	}
	
	private void gameLoop(double delta){
		processInput(delta);
		updateObjects(delta);
		renderFrame();
		sleep(appSleep);
	}
	
	
	
	protected void onWindowClosing(){
		try {
			running = false;
			gameThread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
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
		terminate();
	}

	protected static void launchApp(final SimpleFramework app){
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
