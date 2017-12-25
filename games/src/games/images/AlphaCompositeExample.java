package games.images;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import games.util.SimpleFramework;

public class AlphaCompositeExample extends SimpleFramework {
	private String[] compositeName = {
			"SRC",
			"DST",
			"SRC_IN",
			"DST_IN",
			"SRC_OUT",
			"DST_OUT",
			"SRC_OVER",
			"DST_OVER",
			"SRC_ATOP",
			"DST_ATOP",
			"XOR",
			"CLEAR",
	};
	private int[] compositeRule = {
			AlphaComposite.SRC,
			AlphaComposite.DST,
			AlphaComposite.SRC_IN,
			AlphaComposite.DST_IN,
			AlphaComposite.SRC_OUT,
			AlphaComposite.DST_OUT,
			AlphaComposite.SRC_OVER,
			AlphaComposite.DST_OVER,
			AlphaComposite.SRC_ATOP,
			AlphaComposite.DST_ATOP,
			AlphaComposite.XOR,
			AlphaComposite.CLEAR,
	};
	private int compositeIndex;
	private float srcAlpha;
	private float dstAlpha;
	private float extAlpha;
	private BufferedImage sprite;
	private BufferedImage sourceImage;
	private BufferedImage destinationImage;
	
	public AlphaCompositeExample(){
		appBackground = Color.DARK_GRAY;
		appWidth = 640;
		appHeight = 480;
		appSleep = 10;
		appTitle = "Alpha Composite Example";
	}

	@Override
	protected void initialize() {
		super.initialize();
		srcAlpha = 1.0f;
		dstAlpha = 1.0f;
		extAlpha = 1.0f;
		destinationImage = new BufferedImage(320, 320, BufferedImage.TYPE_INT_ARGB);
		sourceImage = new BufferedImage(320, 320, BufferedImage.TYPE_INT_ARGB);
		sprite = new BufferedImage(320, 320, BufferedImage.TYPE_INT_ARGB);
		createImages();
	}

	private void createImages() {
		Graphics2D g2d = sourceImage.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g2d.fillRect(0, 0, sourceImage.getWidth(), sourceImage.getHeight());
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		Polygon polygon = new Polygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(sourceImage.getWidth(), 0);
		polygon.addPoint(sourceImage.getWidth(), (int)(sourceImage.getHeight()/1.5));
		g2d.setColor(new Color(1f, 1f, 1f, srcAlpha));
		g2d.fill(polygon);
		g2d.dispose();
		
		g2d = destinationImage.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g2d.fillRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		polygon = new Polygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(destinationImage.getWidth(), 0);
		polygon.addPoint(0, (int)(destinationImage.getHeight()/1.5));
		g2d.setColor(new Color(1f, 1f, 1f, dstAlpha));
		g2d.fill(polygon);
		int rule = compositeRule[compositeIndex];
		g2d.setComposite(AlphaComposite.getInstance(rule, extAlpha));
		g2d.drawImage(sourceImage, 0, 0, null);
		g2d.dispose();
		
		g2d = sprite.createGraphics();
		int dx = sprite.getWidth() / 8;
		int dy = sprite.getHeight() / 8;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				g2d.setColor((i+j)%2==0 ? Color.BLACK : Color.WHITE);
				g2d.fillRect(i*dx, j*dy, dx, dy);
			}
		}
		g2d.drawImage(destinationImage, 0, 0, null);
		g2d.dispose();
	}

	@Override
	protected void processInput(double delta) {
		super.processInput(delta);
		if(keyboard.keyDownOnce(KeyEvent.VK_UP)){
			compositeIndex--;
			if(compositeIndex < 0){
				compositeIndex = compositeRule.length-1;
			}
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_DOWN)){
			compositeIndex++;
			if(compositeIndex > compositeRule.length-1){
				compositeIndex = 0;
			}
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_A)){
			srcAlpha = dec(srcAlpha, delta);
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_Q)){
			srcAlpha = inc(srcAlpha, delta);
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_S)){
			dstAlpha = dec(dstAlpha, delta);
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_W)){
			dstAlpha = inc(dstAlpha, delta);
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_D)){
			extAlpha = dec(extAlpha, delta);
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_E)){
			extAlpha = inc(extAlpha, delta);
		}
		createImages();
	}

	private float inc(float val, double delta) {
		val += 0.5*delta;
		if(val > 1f){
			val = 1f;
		}
		return val;
	}

	private float dec(float val, double delta) {
		val -= 0.5*delta;
		if(val < 0f){
			val = 0f;
		}
		return val;
	}

	@Override
	protected void render(Graphics g) {
		super.render(g);
		Graphics2D g2d = (Graphics2D)g;
		int x = 20;
		int y = 35;
		g2d.drawString("", x, y);
		y += 15;
		g2d.drawString("UP/DOWN Arrow to select", x, y);
		y += 15;
		for(int i=0; i<compositeName.length; i++){
			if(i == compositeIndex){
				g2d.setColor(Color.RED);
			}else{
				g2d.setColor(Color.GREEN);
			}
			g2d.drawString(compositeName[i], x, y);
			y += 15;
		}
		g2d.drawString("", x, y);
		y += 15;
		g2d.setColor(Color.GREEN);
		g2d.drawString(String.format("Q | A : SRC_ALPHA=%.4f", srcAlpha), x, y);
		y += 15;
		g2d.drawString(String.format("W | S : DST_ALPHA=%.4f", dstAlpha), x, y);
		y += 15;
		g2d.drawString(String.format("E | D : EXT_ALPHA=%.4f", extAlpha), x, y);
		y += 15;
		int ix = canvas.getWidth() - destinationImage.getWidth() - 50;
		int iy = (canvas.getHeight() - destinationImage.getHeight()) / 2;
		g2d.drawImage(sprite, ix, iy, null);
	}
	
	public static void main(String[] args){
		launchApp(new AlphaCompositeExample());
	}
	
}


















