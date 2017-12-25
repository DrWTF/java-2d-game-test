package games.render;

import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import games.render.DisplayModeExample.DisplayModeWrapper;

public class DisplayModeExample extends JFrame {

	class DisplayModeWrapper{
		private DisplayMode dm;
		
		public DisplayModeWrapper(DisplayMode dm){
			this.dm = dm;
		}
		
		public boolean equals(Object obj){
			DisplayModeWrapper other = (DisplayModeWrapper)obj;
			if(dm.getWidth() != other.dm.getWidth()){
				return false;
			}
			if(dm.getHeight() != other.dm.getHeight()){
				return false;
			}
			return true;
		}
		
		public String toString(){
			return ""+dm.getWidth()+" x "+dm.getHeight();
		}
	}
	
	private JComboBox<DisplayModeWrapper> displayModes;
	private GraphicsDevice graphicsDevice;
	private DisplayMode currentDisplayMode;
	
	public DisplayModeExample(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphicsDevice = ge.getDefaultScreenDevice();
		currentDisplayMode = graphicsDevice.getDisplayMode();
	}
	
	private JPanel getMainPanel(){
		JPanel panel = new JPanel();
		displayModes = new JComboBox<DisplayModeWrapper>(listDisplayModes());
		panel.add(displayModes);
		JButton enterButton = new JButton("Enter Full Screen");
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onEnterFullScreen();
			}
		});
		panel.add(enterButton);
		JButton exitButton = new JButton("Exit Full Screen");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onExitFullScreen();
			}
		});
		panel.add(exitButton);
		return panel;
	}
	
	private DisplayModeWrapper[] listDisplayModes(){
		ArrayList<DisplayModeWrapper> list = new ArrayList<DisplayModeWrapper>();
		for(DisplayMode mode : graphicsDevice.getDisplayModes()){
			if(mode.getBitDepth() == 32){
				DisplayModeWrapper wrapper = new DisplayModeWrapper(mode);
				if(!list.contains(wrapper)){
					list.add(wrapper);
				}
			}
		}
		return list.toArray(new DisplayModeWrapper[0]);
	}
	
	protected void createAndShowGUI(){
		Container canvas = getContentPane();
		canvas.add(getMainPanel());
		canvas.setIgnoreRepaint(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Display Mode Test");
		pack();
		setVisible(true);
	}
	
	protected void onEnterFullScreen() {
		if(graphicsDevice.isFullScreenSupported()){
			DisplayMode newMode = getSelectedMode();
			graphicsDevice.setFullScreenWindow(this);
			graphicsDevice.setDisplayMode(newMode);
		}
	}
	
	protected void onExitFullScreen() {
		graphicsDevice.setDisplayMode(currentDisplayMode);
		graphicsDevice.setFullScreenWindow(null);
	}
	
	protected DisplayMode getSelectedMode(){
		DisplayModeWrapper wrapper = (DisplayModeWrapper) displayModes.getSelectedItem();
		DisplayMode dm = wrapper.dm;
		int width = dm.getWidth();
		int height = dm.getHeight();
		int bitDepth = 32;
		int refreshRate = DisplayMode.REFRESH_RATE_UNKNOWN;
		return new DisplayMode(width, height, bitDepth, refreshRate);
	}
	
	public static void main(String[] args){
		final DisplayModeExample app = new DisplayModeExample();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
}


















