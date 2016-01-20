package area51.turboRocketWars.Bodies;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Ship extends Body{

	public Ship(BodyDef bd, World world) {
		super(bd, world);
		bd = new BodyDef();
		Shape shape = new Shape
		bd.setType(BodyType.DYNAMIC);
		// TODO Auto-generated constructor stub
	}

}
