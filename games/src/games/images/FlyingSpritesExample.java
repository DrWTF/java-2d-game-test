package games.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import games.util.SimpleFramework;
import games.util.Vector2f;

public class FlyingSpritesExample extends SimpleFramework {
	int IMG_WIDTH = 256;
	int IMG_HEIGHT = 256;
	private enum Interpolation{
		NearestNeighbor,
		BiLinear,
		BiCubic;
	}
	private enum RotationMethod{
		AffineTransform,
		AffineTransformOp,
		TexturePaint;
	}
	
	boolean antialiased;
	boolean transparent;
	boolean greenBorder;
	Interpolation interpolation;
	RotationMethod rotationMethod;
	BufferedImage sprite;
	Vector2f[] positions;
	float[] angles;
	Vector2f[] velocities;
	float[] rotations;
	
	public FlyingSpritesExample() {
		appWidth = 640;
		appHeight = 640;
		appSleep = 0;
		appTitle = "Flying Sprites Example";
		appBackground = Color.DARK_GRAY;
	}
	
	

	@Override
	protected void initialize() {
		super.initialize();
		positions = new Vector2f[]{
				new Vector2f(-0.15f, 0.3f),
				new Vector2f(0.15f, 0.0f),
				new Vector2f(0.25f, -0.3f),
				new Vector2f(-0.25f, -0.6f),
		};
		velocities = new Vector2f[]{
				new Vector2f(-0.04f, 0.0f),
				new Vector2f(-0.05f, 0.0f),
				new Vector2f(0.06f, 0.0f),
				new Vector2f(0.07f, 0.0f),
		};
		angles = new float[]{
				(float)Math.toRadians(0),
				(float)Math.toRadians(0),
				(float)Math.toRadians(0),
				(float)Math.toRadians(0),
		};
		rotations = new float[]{
				1f, 0.75f, 0.5f, 0.25f
		};
		antialiased = false;
		transparent = false;
		greenBorder = false;
		interpolation = Interpolation.NearestNeighbor;
		rotationMethod = RotationMethod.AffineTransform;
		createSprite();
	}



	private void createSprite() {
		createCheckerboard();
		if(transparent){
			addTransparentBorder();
		}
		if(greenBorder){
			drawGreenBorder();
		}
	}



	private void drawGreenBorder() {
		Graphics2D g2d = sprite.createGraphics();
		g2d.setColor(Color.GREEN);
		g2d.drawRect(0, 0, sprite.getWidth()-1, sprite.getHeight()-1);
		g2d.dispose();
	}



	private void addTransparentBorder() {
		int borderWidth = IMG_WIDTH+8;
		int borderHeight  = IMG_HEIGHT+8;
		BufferedImage newSprite = new BufferedImage(borderWidth, borderHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newSprite.createGraphics();
		g2d.drawImage(sprite, 4, 4, null);
		g2d.dispose();
		sprite = newSprite;
	}



	private void createCheckerboard() {
		sprite = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = sprite.createGraphics();
		int dx = IMG_WIDTH / 8;
		int dy = IMG_HEIGHT / 8;
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				g2d.setColor((i+j)%2==0 ? Color.BLACK : Color.WHITE);
				g2d.fillRect(i*dx, j*dy, dx, dy);
			}
		}
		g2d.dispose();
	}



	@Override
	protected void processInput(double delta) {
		super.processInput(delta);
		if(keyboard.keyDownOnce(KeyEvent.VK_A)){
			antialiased = !antialiased;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_I)){
			Interpolation[] values = Interpolation.values();
			int index = (interpolation.ordinal() + 1) % values.length;
			interpolation = values[index];
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_T)){
			transparent = !transparent;
			createSprite();
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_R)){
			RotationMethod[] values = RotationMethod.values();
			int index = (rotationMethod.ordinal() + 1) % values.length;
			rotationMethod = values[index];
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_G)){
			greenBorder = !greenBorder;
			createSprite();
		}
	}



	@Override
	protected void updateObjects(double delta) {
		super.updateObjects(delta);
		for(int i=0; i<positions.length; i++){
			positions[i] = positions[i].add(velocities[i].mul((float)delta));
			if(positions[i].x >= 1f){
				positions[i].x = -1f;
			}else if(positions[i].x <= -1f){
				positions[i].x = 1f;
			}
			if(positions[i].y >= 1f){
				positions[i].y = -1f;
			}else if(positions[i].y <= -1f){
				positions[i].y = 1f;
			}
			angles[i] += rotations[i] * delta;
		}
	}



	@Override
	protected void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		setAntialasing(g2d);
		setInterpolation(g2d);
		switch (rotationMethod) {
			case AffineTransform: doAffineTransform(g2d); break;
			case AffineTransformOp: doAffineTransformOp(g2d); break;
			case TexturePaint: doTexturePaint(g2d); break;
		}
		super.render(g);
		g.drawString("(A)ntialiased: "+antialiased, 20, 35);
		g.drawString("(I)nterpolation: "+interpolation, 20, 50);
		g.drawString("(T)ransparent: "+transparent, 20, 65);
		g.drawString("(R)otationMethod: "+rotationMethod, 20, 80);
		g.drawString("(G)reenBorder: "+greenBorder, 20, 95);
	}

	private void doTexturePaint(Graphics2D g2d) {
		for(int i=0; i<positions.length; i++){
			Rectangle2D anchor = new Rectangle2D.Float(0, 0, sprite.getWidth(), sprite.getHeight());
			TexturePaint paint = new TexturePaint(sprite, anchor);
			g2d.setPaint(paint);
			AffineTransform transform = createTransform(positions[i], angles[i]);
			g2d.setTransform(transform);
			g2d.fillRect(0, 0, sprite.getWidth(), sprite.getHeight());
			//very important!!!
			g2d.setTransform(new AffineTransform());
		}
	}



	private void doAffineTransformOp(Graphics2D g2d) {
		for(int i=0; i<positions.length; i++){
			AffineTransformOp op = createTransformOp(positions[i], angles[i]);
			g2d.drawImage(op.filter(sprite, null), 0, 0, null);
		}
	}



	private AffineTransformOp createTransformOp(Vector2f position, float angle) {
		AffineTransform transform = createTransform(position, angle);
		if(interpolation == Interpolation.NearestNeighbor){
			return new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		}else if(interpolation == Interpolation.BiLinear){
			return new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		}else{
			return new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
		}
	}



	private void doAffineTransform(Graphics2D g2d) {
		for(int i=0; i<positions.length; i++){
			AffineTransform transform = createTransform(positions[i], angles[i]);
			g2d.drawImage(sprite, transform, null);
		}
	}



	private AffineTransform createTransform(Vector2f position, float angle) {
		Vector2f screen = getViewportTransform().mul(position);
		AffineTransform transform = AffineTransform.getTranslateInstance(screen.x, screen.y);
		transform.rotate(angle);
		transform.translate(-sprite.getWidth()/2, -sprite.getHeight()/2);
		return transform;
	}



	private void setInterpolation(Graphics2D g2d) {
		if(interpolation == Interpolation.NearestNeighbor){
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}else if(interpolation == Interpolation.BiLinear){
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}if(interpolation == Interpolation.BiCubic){
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
	}



	private void setAntialasing(Graphics2D g2d) {
		if(antialiased){
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}else{
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}



	public static void main(String[] args) {
		launchApp(new FlyingSpritesExample());
	}

}
