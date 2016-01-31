package area51.turboRocketWars.Bodies.userData;

import java.awt.Color;

public class UserDataProp {

	public String bodyType;
	public Color color = Color.black;
	public int stroke = 1;
	public boolean fill = false;
	public UserDataProp(String bodyType, Color color, int stroke, boolean fill) {
		this.bodyType = bodyType;
		this.color = color;
		this.stroke = stroke;
		this.fill = fill;
	}

}
