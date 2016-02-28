package area51.turboRocketWars.Bodies.maps.objects;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public abstract class Entity {

	public Vec2 position;
	public Color color;
	public boolean fill;
	public int stroke;
	public Entity(Vec2 position, Color color, boolean fill, int stroke) {
		this.position = position;
		this.color = color;
		this.fill = fill;
		this.stroke = stroke;
	}
	
	public abstract void create(World world);
}
