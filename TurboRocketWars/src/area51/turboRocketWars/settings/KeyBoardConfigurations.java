package area51.turboRocketWars.settings;
import static java.awt.event.KeyEvent.*;

import java.awt.event.KeyEvent;
public class KeyBoardConfigurations {

	public static final int WASD_CONFIG = 1;
	public static final int ARROW_CONFIG = 2;
	public static final int SP_CONFIG = 4;


	public final int BOOST;
	public final int LEFT;
	public final int RIGHT;
	public final int SHOT1;
	public final int SHOT2;
	public final int SHOT3;
	
	private int[] WASD_KEYS = new int[]{	VK_W, VK_A, 	VK_D, 		VK_S, VK_2, VK_3};
	private int[] ARROW_KEYS = new int[]{	VK_UP,VK_LEFT, 	VK_RIGHT, 	VK_DOWN, VK_N, VK_M};
	private int[] SP_KEYS = new int[]{		VK_UP,VK_LEFT, 	VK_RIGHT, 	VK_DOWN, VK_2, VK_3};

	private static int CONFIG_OCCUPIED;
	private final int[] CONFIG_SELECTED;

	public KeyBoardConfigurations(int config) {

		if((config & CONFIG_OCCUPIED) > 0){
			System.err.println("config already selected. This result in unexpected behaviour");
		}
		CONFIG_OCCUPIED += config;
		int[] cmds;
		switch(config){
		case WASD_CONFIG:
			cmds = WASD_KEYS;
			break;
		case ARROW_CONFIG:
			cmds = ARROW_KEYS;
			break;
		case SP_CONFIG:
		default:
			cmds = SP_KEYS;
			break;
		}
		CONFIG_SELECTED = cmds;
		BOOST = cmds[0];
		LEFT = cmds[1];
		RIGHT = cmds[2];
		SHOT1 = cmds[3];
		SHOT2 = cmds[4];
		SHOT3 = cmds[5];
	}
	
	public boolean isValid(KeyEvent k){
		for(int val : CONFIG_SELECTED){
			if(val == k.getKeyCode()) return true;
		}
		return false;
	}
	
}
