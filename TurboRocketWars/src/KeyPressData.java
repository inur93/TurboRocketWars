import java.io.Serializable;
import java.util.ArrayList;

import turboRocket.KeyPress;

public class KeyPressData implements Serializable, KeyPress{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4355118281068967115L;
//	public ArrayList<Character> keys = new ArrayList<Character>();
	private boolean isDown = false;
	private int key;
	public KeyPressData(int key, boolean isDown){
		this.key = key;
		this.isDown = isDown;
	}
	public String getShipId() {
		// TODO Auto-generated method stub
		return null;
	}
	public int getKey() {
		return this.key;
	}
	public boolean isKeyDown() {
		return isDown;
	}
	public boolean isKeyUp() {
		return !isDown;
	}

}
