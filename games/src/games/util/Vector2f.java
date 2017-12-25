package games.util;

import java.awt.datatransfer.FlavorTable;

public class Vector2f {
	public float x;
	public float y;
	public float w;
	
	public Vector2f(){
		this.x = 0.0f;
		this.y = 0.0f;
		this.w = 1.0f;
	}
	
	public Vector2f(Vector2f v){
		this.x = v.x;
		this.y = v.y;
		this.w = v.w;
	}
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
		this.w = 1.0f;
	}
	
	public Vector2f(float x, float y, float w){
		this.x = x;
		this.y = y;
		this.w = w;
	}
	
	/**
	 * 平移
	 * @param tx
	 * @param ty
	 */
	public void translate(float tx, float ty){
		x += tx;
		y += ty;
	}
	
	/**
	 * 缩放
	 * @param sx
	 * @param sy
	 */
	public void scale(float sx, float sy){
		x *= sx;
		y *= sy;
	}
	
	/**
	 * 旋转
	 * @param rad
	 */
	public void rotate(float rad){
		float tmp = (float)(x*Math.cos(rad) - y*Math.sin(rad));
		y = (float)(x*Math.sin(rad) + y*Math.cos(rad));
		x = tmp;
	}
	
	/**
	 * 切变
	 * @param sx
	 * @param sy
	 */
	public void shear(float sx, float sy){
		float tmp = x+sx*y;
		y = y+sy*x;
		x = tmp;
	}
	
	/**
	 * 反转
	 * @return
	 */
	public Vector2f inv(){
		return new Vector2f(-x, -y);
	}
	
	public Vector2f add(Vector2f v){
		return new Vector2f(x+v.x,	y+v.y);
	}
	
	public Vector2f sub(Vector2f v){
		return new Vector2f(x-v.x,	y-v.y);
	}
	
	public Vector2f mul(float scalar){
		return new Vector2f(x*scalar,	y*scalar);
	}
	
	public Vector2f div(float scalar){
		return new Vector2f(x/scalar,	y/scalar);
	}
	
	public float len(){
		return (float)Math.sqrt(x*x+y*y);
	}
	
	public float lenSqr(){
		return x*x+y*y;
	}
	
	/**
	 * 单位向量
	 * @return
	 */
	public Vector2f norm(){
		return div(len());
	}
	
	/**
	 * 垂直向量
	 * @return
	 */
	public Vector2f perp(){
		return new Vector2f(-y, x);
	}
	
	/**
	 * 两个向量的点积
	 * @param v
	 * @return
	 */
	public float dot(Vector2f v){
		return x*v.x + y*v.y;
	}
	
	/**
	 * 弧度
	 * @return
	 */
	public float angle(){
		return (float)Math.atan2(y, x);
	}
	
	/**
	 * 使用一个角度值和一个半径 创建一个向量
	 * @param angle
	 * @param radius
	 * @return
	 */
	public static Vector2f polar(float angle, float radius){
		return new Vector2f(radius*(float)Math.cos(angle), radius*(float)Math.sin(angle));
	}
	
	public String toString(){
		return String.format("(%s, %s)", x, y);
	}
	
}













