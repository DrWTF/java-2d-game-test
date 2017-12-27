package games.images;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.util.Hashtable;

import games.util.SimpleFramework;

public class ScaleImageExample extends SimpleFramework {
	int IMG_WIDTH = 320;
	int IMG_HEIGHT = 320;
	BufferedImage sprite;
	Image averaged;
	double averagedSpeed;
	Image nearestNeighbor;
	double nearestSpeed;
	BufferedImage nearest2;
	double nearest2Speed;
	BufferedImage bilinear;
	double bilinearSpeed;
	BufferedImage bicubic;
	double bicubicSpeed;
	BufferedImage stepDownBilinear;
	double stepDownBilinearSpeed;
	BufferedImage stepDownBicubic;
	double stepDownBicubicSpeed;
	
	public ScaleImageExample(){
		appWidth = 960;
		appHeight = 570;
		appBackground = Color.DARK_GRAY;
		appSleep = 1L;
		appTitle = "Scale Image Example";
	}
	
	
	
	@Override
	protected void initialize() {
		super.initialize();
		createTestImage();
		//
		long start = System.nanoTime();
		for(int i=0; i<100; i++){
			generateAveragedInstatnce();
		}
		long end = System.nanoTime();
		averagedSpeed = (end-start)/1.0E6;
		averagedSpeed /= 100;
		//
		start = System.nanoTime();
		for(int i=0; i<100; i++){
			generateNearestNeighbor();
		}
		end = System.nanoTime();
		nearestSpeed = (end-start)/1.0E6;
		nearestSpeed /= 100;
		//
		start = System.nanoTime();
		for(int i=0; i<100; i++){
			nearest2 = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
		end = System.nanoTime();
		nearest2Speed = (end-start)/1.0E6;
		nearest2Speed /= 100;
		//
		start = System.nanoTime();
		for(int i=0; i<100; i++){
			bilinear = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}
		end = System.nanoTime();
		bilinearSpeed = (end-start)/1.0E6;
		bilinearSpeed /= 100;
		//
		start = System.nanoTime();
		for(int i=0; i<100; i++){
			bicubic = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		end = System.nanoTime();
		bicubicSpeed = (end-start)/1.0E6;
		bicubicSpeed /= 100;
		//
		start = System.nanoTime();
		for(int i=0; i<100; i++){
			stepDownBilinear = scaleDownImage(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}
		end = System.nanoTime();
		stepDownBilinearSpeed = (end-start)/1.0E6;
		stepDownBilinearSpeed /= 100;
		//
		start = System.nanoTime();
		for(int i=0; i<100; i++){
			stepDownBicubic = scaleDownImage(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}
		end = System.nanoTime();
		stepDownBicubicSpeed = (end-start)/1.0E6;
		stepDownBicubicSpeed /= 100;
	}



	private BufferedImage scaleDownImage(Object hintValue) {
		BufferedImage ret = sprite;
		int targetWidth = sprite.getWidth()/4;
		int targetHeight = sprite.getHeight()/4;
		int w = sprite.getWidth();
		int h = sprite.getHeight();
		do {
			w = w/2;
			if(w < targetWidth){
				w = targetWidth;
			}
			h = h/2;
			if(h < targetHeight){
				h = targetHeight;
			}
			BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = tmp.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
			g2d.drawImage(ret, 0, 0, w, h, null);
			g2d.dispose();
			ret = tmp;
		} while (w!=targetWidth || h!=targetHeight);
		return ret;
	}



	private BufferedImage scaleWithGraphics(Object hintValue) {
		BufferedImage image = new BufferedImage(sprite.getWidth()/4, sprite.getHeight()/4, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
		g2d.drawImage(sprite, 0, 0, image.getWidth(), image.getHeight(), null);
		g2d.dispose();
		return image;
	}



	private void generateNearestNeighbor() {
		nearestNeighbor = sprite.getScaledInstance(sprite.getWidth()/4, sprite.getHeight()/4, Image.SCALE_REPLICATE);
		nearestNeighbor.getSource().startProduction(getConsumer());
	}



	private void generateAveragedInstatnce() {
		averaged = sprite.getScaledInstance(sprite.getWidth()/4, sprite.getHeight()/4, Image.SCALE_AREA_AVERAGING);
		averaged.getSource().startProduction(getConsumer());
	}



	private ImageConsumer getConsumer() {
		return new ImageConsumer() {
			
			@Override
			public void setProperties(Hashtable<?, ?> props) {
			}
			
			@Override
			public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setHints(int hintflags) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setDimensions(int width, int height) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setColorModel(ColorModel model) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void imageComplete(int status) {
				// TODO Auto-generated method stub
				
			}
		};
	}



	private void createTestImage() {
		sprite = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = sprite.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, IMG_WIDTH/2, IMG_HEIGHT/2);
		g2d.fillRect(IMG_WIDTH/2, IMG_HEIGHT/2, IMG_WIDTH, IMG_HEIGHT);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(IMG_WIDTH/2, 0, IMG_WIDTH, IMG_HEIGHT);
		g2d.fillRect(0, IMG_HEIGHT/2, IMG_WIDTH/2, IMG_HEIGHT);
		g2d.setColor(Color.RED);
		g2d.drawLine(0, sprite.getHeight()/2, sprite.getHeight()/2, 0);
		g2d.drawLine(sprite.getWidth(), sprite.getHeight()/2, sprite.getWidth()/2, sprite.getHeight());
		g2d.drawLine(sprite.getWidth()/2, sprite.getHeight(), 0, sprite.getHeight()/2);
		g2d.drawOval(0, 0, sprite.getWidth(), sprite.getHeight());
		
		g2d.setColor(Color.GREEN);
		int dx = sprite.getWidth()/18;
		for(int i=0; i<sprite.getWidth(); i+=dx){
			g2d.drawLine(i, 0, i, sprite.getHeight());
		}
		g2d.setColor(Color.GREEN);
		dx = sprite.getHeight()/18;
		for(int i=0; i<sprite.getWidth(); i+=dx){
			g2d.drawLine(0, i, sprite.getWidth(), i);
		}
		
		float x1 = sprite.getWidth()/4;
		float x2 = sprite.getWidth()*3/4;
		float y1 = sprite.getHeight()/4;
		float y2 = sprite.getHeight()*3/4;
		GradientPaint gp = new GradientPaint(x1, y1, Color.BLACK, x2, y2, Color.WHITE);
		g2d.setPaint(gp);
		g2d.fillOval(sprite.getWidth()/4, sprite.getHeight()/4, sprite.getWidth()/2, sprite.getHeight()/2);
		g2d.setFont(new Font("Arial", Font.BOLD, 42));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawString("Pg.1", sprite.getWidth()/2-40, sprite.getHeight()/2-20);
		g2d.drawString("Pg.2", sprite.getWidth()/2-40, sprite.getHeight()/2+40);
		g2d.dispose();
		
	}



	@Override
	protected void render(Graphics g) {
		super.render(g);
		g.drawImage(sprite, (canvas.getWidth()-sprite.getWidth())/2, 50, null);
		int sw = averaged.getWidth(null);
		int sh = averaged.getHeight(null);
		int pos = canvas.getHeight()-sh-50;
		int textPos = pos + sh;
		int imgPos = (sw+50)*0+50;
		g.drawImage(averaged, imgPos, pos, null);
		String time = String.format("%.4f ms", averagedSpeed);
		g.drawString("Area Avg", imgPos, textPos+15);
		g.drawString(time, 50, textPos+30);
		
		imgPos = (sw+50)*1+50;
		g.drawImage(nearestNeighbor, imgPos, pos, null);
		time = String.format("%.4f ms", nearestSpeed);
		g.drawString("Nearst", imgPos, textPos+15);
		g.drawString(time, imgPos, textPos+30);

		imgPos = (sw+50)*2+50;
		g.drawImage(nearest2, imgPos, pos, null);
		time = String.format("%.4f ms", nearest2Speed);
		g.drawString("Nearst2", imgPos, textPos+15);
		g.drawString(time, imgPos, textPos+30);

		imgPos = (sw+50)*3+50;
		g.drawImage(bilinear, imgPos, pos, null);
		time = String.format("%.4f ms", bilinearSpeed);
		g.drawString("Bilinear", imgPos, textPos+15);
		g.drawString(time, imgPos, textPos+30);

		imgPos = (sw+50)*4+50;
		g.drawImage(bicubic, imgPos, pos, null);
		time = String.format("%.4f ms", bicubicSpeed);
		g.drawString("Bicubic", imgPos, textPos+15);
		g.drawString(time, imgPos, textPos+30);

		imgPos = (sw+50)*5+50;
		g.drawImage(stepDownBilinear, imgPos, pos, null);
		time = String.format("%.4f ms", stepDownBilinearSpeed);
		g.drawString("Bilin-Step", imgPos, textPos+15);
		g.drawString(time, imgPos, textPos+30);

		imgPos = (sw+50)*6+50;
		g.drawImage(stepDownBicubic, imgPos, pos, null);
		time = String.format("%.4f ms", stepDownBicubicSpeed);
		g.drawString("Bicub-Step", imgPos, textPos+15);
		g.drawString(time, imgPos, textPos+30);
	}



	public static void main(String[] args) {
		launchApp(new ScaleImageExample());
	}

}
