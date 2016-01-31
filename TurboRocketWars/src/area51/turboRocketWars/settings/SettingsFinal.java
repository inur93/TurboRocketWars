package area51.turboRocketWars.settings;

import org.jbox2d.common.Vec2;

public class SettingsFinal {

	public static final float MAP_HEIGHT = 100;
	public static final float MAP_WIDTH = 100;
	public static final Vec2 MAP_CENTER = new Vec2(MAP_WIDTH/2, MAP_HEIGHT/2);
	public static final float MAP_ANGLE = 0;
	public static final float TIME_STEP = 1.0f/60.0f;
	public static final int velocityIterations = 6;
	public static final int positionIterations = 2;
	public static final int KEY_UPDATE_SPEED = 1000/50;
	public static final String USER_DATA_SHOT = "shot";
	public static final String USER_DATA_SHIP = "ship";
	public static final String USER_DATA_MAP = "map";
	
	public static final int GRAPHICS_UPDATE_RATE = 1000/60;
	public static final float CAMERA_ZOOM = 2;
}
