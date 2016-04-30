package area51.turboRocketWars.settings;
import static java.awt.event.KeyEvent.*;

import java.awt.event.KeyEvent;
public class KeyBoardConfigurations {

//	public static final int WASD_CONFIG = 1;
//	public static final int ARROW_CONFIG = 2;
//	public static final int IJKL_CONFIG = 4;

	public final int BOOST;
	public final int LEFT;
	public final int RIGHT;
	public final int SHOT_NORMAL;
	public final int SHOT_SPECIAL;
	
	private static final int[] WASD_KEYS = new int[]{	VK_W, VK_A, 	VK_D, 		VK_S, VK_SHIFT};
	private static final int[] ARROW_KEYS = new int[]{	VK_UP,VK_LEFT, 	VK_RIGHT, 	VK_DOWN, VK_CONTROL};
	private static final int[] IJKL_KEYS = new int[]{		VK_I,VK_J, 	VK_L, 	VK_K, VK_H};

	private final int[] CONFIG_SELECTED;

	private static int CUR_KEY_CONFIG = 0;
	private static int[][] KEY_CONFIGS = new int[][]{ARROW_KEYS, WASD_KEYS, IJKL_KEYS};

	/**
	 * returns next default configuration
	 */
	public KeyBoardConfigurations(){
		this(null);
	}
	/**
	 * 
	 * @param cmds array of commands. should have a minimum length of 5. Commands are taken in this order: 
	 * 	BOOST, LEFT, RIGHT, SHOT_NORMAL, SHOT_SPECIAL
	 */
	public KeyBoardConfigurations(int[] cmds) {

		if(cmds == null || cmds.length < 5){
			cmds = getNext();
		}
			
		CONFIG_SELECTED = cmds;
		BOOST = cmds[0];
		LEFT = cmds[1];
		RIGHT = cmds[2];
		SHOT_NORMAL = cmds[3];
		SHOT_SPECIAL = cmds[4];
		
	}
	
	private int[] getNext(){
		return KEY_CONFIGS[CUR_KEY_CONFIG++%KEY_CONFIGS.length];
	}
	
	public boolean isValid(KeyEvent k){
		for(int val : CONFIG_SELECTED){
			if(val == k.getKeyCode()) return true;
		}
		return false;
	}
	
}
