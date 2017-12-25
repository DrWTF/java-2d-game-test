package games.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import games.util.SimpleFramework;

public class TransparentImageExample extends SimpleFramework {
	private BufferedImage img;
	private float shift;
	
	public TransparentImageExample(){
		appWidth = 400;
		appHeight = 300;
		appSleep = 10;
		appTitle = "Transparent Image Example";
		appBackground = Color.DARK_GRAY;
	}
	
	protected void initialize(){
		super.initialize();
		img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d =img.createGraphics();
		int w = 8;
		int h = 8;
		int dx = img.getWidth() / w;
		int dy = img.getHeight() / h;
		for(int i=0; i<w; i++){
			for(int j=0; j<h; j++){
				if((i+j)%2 == 0){
					g2d.setColor(Color.WHITE);
					g2d.fillRect(i*dx, j*dy, dx, dy);
				}
			}
		}
		g2d.dispose();
	}
	
	protected void updateObjects(double delta){
		super.updateObjects(delta);
		int ribbonHeight = canvas.getHeight() / 5;
		shift += delta * ribbonHeight;
		if(shift > ribbonHeight){
			shift -= ribbonHeight;
		}
	}
	
	protected void render(Graphics g) {
		super.render(g);
		int hx = canvas.getHeight() / 5;
		g.setColor(Color.LIGHT_GRAY);
		for(int i=-1; i<5; i++){
			g.fillRect(0, (int)shift+hx*i, canvas.getWidth(), hx/2);
		}
		int x = (canvas.getWidth() - img.getWidth()) / 2;
		int y = (canvas.getHeight() - img.getHeight()) / 2;
		g.drawImage(img, x, y, null);
	}
	
	public static void main(String[] args){
		launchApp(new TransparentImageExample());
	}
}
