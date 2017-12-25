package games.util;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class SimpleMouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final int BUTTON_COUNT = 3;
	private Point mousePos;
	private Point currentPos;
	private boolean[] mouse;
	private int[] polled;
	private int notches;
	private int polledNotches;
	
	public SimpleMouseInput() {
		mousePos = new Point(0, 0);
		currentPos = new Point(0, 0);
		mouse = new boolean[BUTTON_COUNT];
		polled = new int[BUTTON_COUNT];
	}
	
	public synchronized void poll(){
		mousePos = new Point(currentPos);
		polledNotches = notches;
		notches = 0;
		for(int i=0; i<mouse.length; i++){
			if(mouse[i]){
				polled[i]++;
			}else{
				polled[i] = 0;
			}
		}
	}
	
	public Point getPosition(){
		return mousePos;
	}
	
	public int getNotches() {
		return polledNotches;
	}
	
	public boolean buttonDown(int button){
		return polled[button-1]>0;
	}
	
	public boolean buttonDownOnce(int button){
		return polled[button-1]==1;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		notches += e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		currentPos = e.getPoint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton()-1;
		if(button>=0 && button<mouse.length){
			mouse[button] = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton()-1;
		if(button>=0 && button<mouse.length){
			mouse[button] = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseMoved(e);
	}

}
