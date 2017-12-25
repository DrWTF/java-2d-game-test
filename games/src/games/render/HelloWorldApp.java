package games.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import games.util.FrameRate;

public class HelloWorldApp extends JFrame {
	private FrameRate frameRate;
	
	public HelloWorldApp(){
		frameRate = new FrameRate();
	}
	
	protected void createAndShowGUI(){
		GamePanel gamePanel = new GamePanel();
		gamePanel.setBackground(Color.black);
		gamePanel.setPreferredSize(new Dimension(320, 240));
		getContentPane().add(gamePanel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Hello World");
		pack();
		frameRate.initalize();
		setVisible(true);
	}
	
	private class GamePanel extends JPanel {
		public void paint(Graphics g){
			super.paint(g);
			onPaint(g);
		}
	}
	
	protected void onPaint(Graphics g){
		frameRate.calculate();
		g.setColor(Color.white);
		g.drawString(frameRate.getFrameRate(), 30, 30);
		repaint();
	}
	
	public static void main(String[] args){
		final HelloWorldApp app = new HelloWorldApp();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
}
