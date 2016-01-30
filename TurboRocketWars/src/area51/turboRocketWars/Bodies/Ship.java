package area51.turboRocketWars.Bodies;

import static java.awt.Color.black;
import static java.awt.Color.blue;
import static java.awt.Color.cyan;
import static java.awt.Color.darkGray;
import static java.awt.Color.gray;
import static java.awt.Color.green;
import static java.awt.Color.lightGray;
import static java.awt.Color.magenta;
import static java.awt.Color.orange;
import static java.awt.Color.pink;
import static java.awt.Color.red;
import static java.awt.Color.yellow;

import java.awt.Color;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Cannon.CannonType;
import area51.turboRocketWars.Bodies.seperator.B2Separator;
import area51.turboRocketWars.Bodies.userData.FixtureViewProperties;

public class Ship{

	private static Vec2[] shapeVectors = new Vec2[]{new Vec2(-4f,-5f), new Vec2(0f,-2f), new Vec2(4f,-5f), new Vec2(0f,5f)};
	private Vec2 boostVec = new Vec2(0,200);
	private String id;
	private String type;
	private Body body;
	private Cannon cannon;
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

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 2;
		fixtureDef.friction = 0.3f;

		B2Separator.seperate(body, fixtureDef, shapeVectors, 1);
		
		this.cannon = new Cannon(CannonType.SINGLE, world, this);
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
	
	public void shoot(){
		cannon.fire();
	}
	
	public void boost(){
		float angle = body.m_sweep.a;
		body.setAngularVelocity(0);
		Mat22 m = Mat22.createRotationalTransform(angle);
		body.applyLinearImpulse(m.mul(boostVec), body.getPosition(), true);
		
	}
	
	public void yawLeft(){
		body.setAngularVelocity(0);
		body.m_sweep.a += 0.1;
	}
	
	public void yawRight(){
		body.setAngularVelocity(0);
		body.m_sweep.a -= 0.1;
	}

	private static int curColor = 0;
	private static Color[] colors = new Color[]{black, red, blue, green, gray, cyan, lightGray, yellow, magenta, darkGray, orange, pink};
	private static Color getNextColor(){
		return colors[(curColor = (curColor+1)%colors.length)];
	}
}
