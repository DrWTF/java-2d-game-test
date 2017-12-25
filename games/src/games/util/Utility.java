package games.util;

public class Utility {

	/**
	 * 创建视口转换矩阵
	 * @param worldWidth
	 * @param worldHeight
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static Matrix3x3f createViewport(float worldWidth, float worldHeight, float screenWidth, float screenHeight){
		float sx = (screenWidth-1) / worldWidth;
		float sy = (screenHeight-1) / worldHeight;
		float tx = (screenWidth-1) / 2.0f;
		float ty = (screenHeight-1) / 2.0f;
		Matrix3x3f viewport = Matrix3x3f.scale(sx, sy);
		viewport = viewport.mul(Matrix3x3f.translate(tx, ty));
		return viewport;
	}

	/**
	 * 创建反视口转换矩阵 可以将鼠标屏幕坐标转换为游戏世界坐标
	 * @param worldWidth
	 * @param worldHeight
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static Matrix3x3f createReverseViewport(float worldWidth, float worldHeight, float screenWidth, float screenHeight){
		float sx = worldWidth / (screenWidth-1);
		float sy = worldHeight / (screenHeight-1);
		float tx = (screenWidth-1) / 2.0f;
		float ty = (screenHeight-1) / 2.0f;
		Matrix3x3f viewport = Matrix3x3f.translate(-tx, -ty);
		viewport = viewport.mul(Matrix3x3f.scale(sx, -sy));
		return viewport;
	}
	
}
