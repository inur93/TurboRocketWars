package area51.turboRocketWars.Bodies.userData;

import java.awt.Color;

import area51.turboRocketWars.Bodies.shots.Shot;

public class UserDataProp {

	public String bodyType;
	public Color color = Color.black;
	public int stroke = 1;
	public boolean fill = false;
	public Shot shot;
	
	public UserDataProp(String bodyType, Color color, int stroke, boolean fill, Shot shot) {
		this(bodyType, color, stroke, fill);
		this.shot = shot;
	}
	public UserDataProp(String bodyType, Color color, int stroke, boolean fill) {
		this.bodyType = bodyType;
		this.color = color;
		this.stroke = stroke;
		this.fill = fill;
	}
	
	public String toString(){
		return "bodyType: " + bodyType + "; color: " + color + ";stroke: " + stroke + ";fill: " + fill;
	}

}
