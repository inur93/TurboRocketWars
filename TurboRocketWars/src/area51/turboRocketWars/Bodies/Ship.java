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
import java.util.ArrayList;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Cannon.CannonType;
import area51.turboRocketWars.Bodies.Shot.ShotType;
import area51.turboRocketWars.Bodies.seperator.B2Separator;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;

public class Ship{

	private static Vec2[] shapeVectors = new Vec2[]{new Vec2(-4f,-5f), new Vec2(0f,-2f), new Vec2(4f,-5f), new Vec2(0f,5f)};
	private Vec2 boostVec = new Vec2(0,200);
	
	private int lives = 1;
	private double maxHitPoints = 10;
	private double hitPoints = maxHitPoints;
	
	private String id;
	private String type;
	private volatile Body body;
	private volatile World world;
	private Cannon cannonStd;
	private Cannon cannon1;
	private Cannon cannon2;
	
	private boolean isWinner = false;
	
	private Vec2 spawnPoint;
	
	public static ArrayList<Ship> ships = new ArrayList<Ship>();
	
	
	public Ship(World world, Vec2 position) {
		this.id = id;
		this.spawnPoint = position;
		this.world = world;
		this.body = getNewBody(position.clone(), world);
		this.cannonStd = new Cannon(CannonType.SINGLE, ShotType.NORMAL, world, this);
		this.cannon1 = new Cannon(CannonType.BOMBER, ShotType.BOMB, world, this);
		this.cannon2 = new Cannon(CannonType.DUAL, ShotType.NORMAL, world, this);
		
		
		ships.add(this);
		
	}
	
	private Body getNewBody(Vec2 pos, World world){
		BodyDef bodyDef = new BodyDef();

		bodyDef.type = BodyType.DYNAMIC;

		System.out.println("pos: " + pos);
		bodyDef.setPosition(pos.clone());
		Body body = null;
		
		body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 2;
		fixtureDef.friction = 0.3f;

		B2Separator.seperate(body, fixtureDef, shapeVectors, 1);
		body.setUserData(new UserDataProp(SettingsFinal.USER_DATA_SHIP, getNextColor(), 1, true));
		return body;
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
	
	public void shootStd(){
		if(this.lives <= 0) return;
		cannonStd.fire();
	}
	
	public void shoot1(){
		if(this.lives <= 0) return;
		cannon1.fire();
	}
	
	public void shoot2(){
		if(this.lives <= 0) return;
		cannon2.fire();
	}
	
	public void boost(){
		if(this.lives <= 0) return;
		float angle = body.m_sweep.a;
		body.setAngularVelocity(0);
		Mat22 m = Mat22.createRotationalTransform(angle);
		body.applyLinearImpulse(m.mul(boostVec), body.getPosition(), true);
		
	}
	
	public void yawLeft(){
		if(this.lives <= 0) return;
		body.setAngularVelocity(0);
		body.m_sweep.a += 0.1;
	}
	
	public void yawRight(){
		if(this.lives <= 0) return;
		body.setAngularVelocity(0);
		body.m_sweep.a -= 0.1;
	}
	
	public double getMaxHitPoints(){
		return this.maxHitPoints;
	}
	
	public double getCurHitPoints(){
		return this.hitPoints;
	}
	
	public int getLives(){
		return this.lives;
	}
	
	public void attack(double dmg){
		this.hitPoints -= dmg;
	}
	
	public Vec2 getSpawPoint(){
		return this.spawnPoint.clone();
	}
	
	public void die(){
		this.lives -= 1;
		if(this.lives <= 0){
			ships.remove(this);
			this.lives = 0;
			this.hitPoints = 0;
			this.body = null;
			return;
		}
		body = getNewBody(spawnPoint.clone(), world);
		this.hitPoints = maxHitPoints;
		
	}
	
	public boolean isWinner(){
		return this.isWinner;
	}
	
	public void setWinner(){
		this.isWinner = true;
	}

	private static int curColor = 0;
	private static Color[] colors = new Color[]{black, red, blue, green, gray, cyan, lightGray, yellow, magenta, darkGray, orange, pink};
	private static Color getNextColor(){
		return colors[(curColor = (curColor+1)%colors.length)];
	}
}
