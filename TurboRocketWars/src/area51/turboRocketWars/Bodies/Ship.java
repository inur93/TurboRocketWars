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

import area51.turboRocketWars.Bodies.seperator.B2Separator;
import area51.turboRocketWars.Bodies.userData.FixtureViewProperties;

public class Ship{


//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f)
//	new Vec2(-1f,0f), new Vec2(1f,0f), new Vec2(0f,2f), new Vec2(0f,0.5f) ?
//	new Vec2(-1f,0f), new Vec2(1f,0f), new Vec2(0f,0.5f),new Vec2(0f,2f)  ?
//	new Vec2(-1f,0f), new Vec2(0f,0.5f), new Vec2(0f,2f), new Vec2(1f,0f) X
//	new Vec2(-1f,0f), new Vec2(0f,0.5f), new Vec2(1f,0f), new Vec2(0f,2f) ?
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(0f,0.5f), new Vec2(1f,0f) X
	
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f)
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f)
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f)
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f)
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f)
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f)
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f)
//	new Vec2(-1f,0f), new Vec2(0f,2f), new Vec2(1f,0f), new Vec2(0f,0.5f) 
	
	private static Vec2[] shapeVectors = new Vec2[]{new Vec2(-4f,0f), new Vec2(0f,3f), new Vec2(4f,0f), new Vec2(0f,10f)};
	private String id;
	private String type;
	private Body body;
	public Ship(World world, Vec2 position) {
		this.id = id;
		BodyDef bodyDef = new BodyDef();

		bodyDef.type = BodyType.DYNAMIC;
		FixtureViewProperties view = new FixtureViewProperties();
		view.fill = true;
		view.color = getNextColor();

		bodyDef.setPosition(position);
		body = world.createBody(bodyDef);

		body.setUserData(view);	    
//		PolygonShape dynamicBox = new PolygonShape();
//		dynamicBox.set(shapeVectors, shapeVectors.length);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 2;
		fixtureDef.friction = 0.3f;
		
//		body.getFixtureList()bodyDef
//		System.out.println("separated");
//			    fixtureDef.shape = dynamicBox;
			    B2Separator.seperate(body, fixtureDef, shapeVectors, 1);
//		body.createFixture(fixtureDef);
	}

	public String getId(){
		return this.id;
	}

	public String getType(){
		return this.type;
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
