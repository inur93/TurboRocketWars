package area51.turboRocketWars.Bodies;

import static java.awt.Color.*;

import java.awt.Color;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.FixtureViewProperties;

public class Ship{


	private static Vec2[] shapeVectors = new Vec2[]{new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f), new Vec2(-1f,0f)};
	private Body body;
	public Ship(World world, Vec2 position) {
		BodyDef bodyDef = new BodyDef();

		bodyDef.type = BodyType.DYNAMIC;
		FixtureViewProperties view = new FixtureViewProperties();
		view.fill = true;
		view.color = getNextColor();

		bodyDef.setPosition(position);
		//	    bodyDef.setAngle((float) Math.toRadians(0));
		body = world.createBody(bodyDef);

		body.setUserData(view);	    
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.set(shapeVectors, shapeVectors.length);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density = 2;
		fixtureDef.friction = 0.3f;
		//	    fixtureDef.shape.
		body.createFixture(fixtureDef);
	}

	public Body getBody(){
		return this.body;
	}

	private static int curColor = 0;
	private static Color[] colors = new Color[]{black, red, blue, green, gray, cyan, lightGray, yellow, magenta, darkGray, orange, pink};
	private static Color getNextColor(){
		return colors[(curColor = (curColor+1)%colors.length)];
	}
}
