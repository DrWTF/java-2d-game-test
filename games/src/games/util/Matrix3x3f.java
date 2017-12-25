package games.util;

public class Matrix3x3f {
	private float[][] m = new float[3][3];
	
	public Matrix3x3f(float[][] m){
		setMatrix(m);
	}
	
	public Matrix3x3f add(Matrix3x3f m1){
		return new Matrix3x3f(new float[][]{
			{
				m[0][0] + m1.m[0][0],
				m[0][1] + m1.m[0][1],
				m[0][2] + m1.m[0][2]
			},
			{
				m[1][0] + m1.m[1][0],
				m[1][1] + m1.m[1][1],
				m[1][2] + m1.m[1][2]
			},
			{
				m[2][0] + m1.m[2][0],
				m[2][1] + m1.m[2][1],
				m[2][2] + m1.m[2][2]
			},
		});
	}
	
	public Matrix3x3f sub(Matrix3x3f m1){
		return new Matrix3x3f(new float[][]{
			{
				m[0][0] - m1.m[0][0],
				m[0][1] - m1.m[0][1],
				m[0][2] - m1.m[0][2]
			},
			{
				m[1][0] - m1.m[1][0],
				m[1][1] - m1.m[1][1],
				m[1][2] - m1.m[1][2]
			},
			{
				m[2][0] - m1.m[2][0],
				m[2][1] - m1.m[2][1],
				m[2][2] - m1.m[2][2]
			},
		});
	}
	
	public Matrix3x3f mul(Matrix3x3f m1){
		return new Matrix3x3f(new float[][]{
			{
				m[0][0]*m1.m[0][0] + m[0][1]*m1.m[1][0] + m[0][2]*m1.m[2][0],
				m[0][0]*m1.m[0][1] + m[0][1]*m1.m[1][1] + m[0][2]*m1.m[2][1],
				m[0][0]*m1.m[0][2] + m[0][1]*m1.m[1][2] + m[0][2]*m1.m[2][2]
			},
			{
				m[1][0]*m1.m[0][0] + m[1][1]*m1.m[1][0] + m[1][2]*m1.m[2][0],
				m[1][0]*m1.m[0][1] + m[1][1]*m1.m[1][1] + m[1][2]*m1.m[2][1],
				m[1][0]*m1.m[0][2] + m[1][1]*m1.m[1][2] + m[1][2]*m1.m[2][2]
			},
			{
				m[2][0]*m1.m[0][0] + m[2][1]*m1.m[1][0] + m[2][2]*m1.m[2][0],
				m[2][0]*m1.m[0][1] + m[2][1]*m1.m[1][1] + m[2][2]*m1.m[2][1],
				m[2][0]*m1.m[0][2] + m[2][1]*m1.m[1][2] + m[2][2]*m1.m[2][2]
			},
		});
	}
	
	public void setMatrix(float[][] m){
		this.m = m;
	}
	
	public static Matrix3x3f zero(){
		return new Matrix3x3f(new float[][]{
			{0.0f, 0.0f, 0.0f},
			{0.0f, 0.0f, 0.0f},
			{0.0f, 0.0f, 0.0f},
		});
	}

	/**
	 * 平移
	 * @param x
	 * @param y
	 * @return
	 */
	public static Matrix3x3f translate(float x, float y){
		return new Matrix3x3f(new float[][]{
			{1.0f, 0.0f, 0.0f},
			{0.0f, 1.0f, 0.0f},
			{   x,    y, 1.0f},
		});
	}
	
	/**
	 * 平移
	 * @param v
	 * @return
	 */
	public static Matrix3x3f translate(Vector2f v){
		return translate(v.x, v.y);
	}
	
	/**
	 * 元矩阵
	 * @return
	 */
	public static Matrix3x3f identity(){
		return new Matrix3x3f(new float[][]{
			{1.0f, 0.0f, 0.0f},
			{0.0f, 1.0f, 0.0f},
			{0.0f, 0.0f, 1.0f},
		});
	}
	
	/**
	 * 缩放
	 * @param x
	 * @param y
	 * @return
	 */
	public static Matrix3x3f scale(float x, float y){
		return new Matrix3x3f(new float[][]{
			{   x, 0.0f, 0.0f},
			{0.0f,    y, 0.0f},
			{0.0f, 0.0f, 1.0f},
		});
	}
	
	/**
	 * 缩放
	 * @param v
	 * @return
	 */
	public static Matrix3x3f scale(Vector2f v){
		return scale(v.x, v.y);
	}
	
	/**
	 * 切变
	 * @param x
	 * @param y
	 * @return
	 */
	public static Matrix3x3f shear(float x, float y){
		return new Matrix3x3f(new float[][]{
			{1.0f,    y, 0.0f},
			{   x, 1.0f, 0.0f},
			{0.0f, 0.0f, 1.0f},
		});
	}

	/**
	 * 切变
	 * @param v
	 * @return
	 */
	public static Matrix3x3f shear(Vector2f v){
		return shear(v.x, v.y);
	}
	
	/**
	 * 旋转
	 * @param rad
	 * @return
	 */
	public static Matrix3x3f rotate(float rad){
		return new Matrix3x3f(new float[][]{
			{(float)Math.cos(rad), (float)Math.sin(rad), 0.0f},
			{(float)Math.sin(rad), (float)Math.cos(rad), 0.0f},
			{0.0f, 0.0f, 1.0f},
		});
	}
	
	public Vector2f mul(Vector2f v){
		return new Vector2f(
			v.x*m[0][0] + v.y*m[1][0] + v.w*m[2][0],//x
			v.x*m[0][1] + v.y*m[1][1] + v.w*m[2][1],//y
			v.x*m[0][2] + v.y*m[1][2] + v.w*m[2][2] //w
		);
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		for(int i=0; i<3; i++){
			b.append("[");
			b.append(m[i][0]);
			b.append(",\t");
			b.append(m[i][1]);
			b.append(",\t");
			b.append(m[i][2]);
			b.append("]\n");
		}
		return b.toString();
	}
}
