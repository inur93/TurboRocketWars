package area51.turboRocketWars.Bodies;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.FixtureViewProperties;

public class Cannon {

	public enum CannonType{SINGLE, DUAL, TRIPLE};
	private long reloadTime = 200; //msec
	private long lastShotTime = 0;
	private CannonType type;
	private World world;
	private Ship ship;
	
	
	public Cannon(CannonType type, World world, Ship ship) {
		this.type = type;
		this.world = world;
		this.ship = ship;
		
	

		

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1;
		fixtureDef.friction = 0.01f;
	}
	
	public synchronized void fire(){
		long newTime = System.currentTimeMillis();
		if(newTime-lastShotTime < 500){
			return;
		}
		lastShotTime = newTime;
		Vec2[] pos = null;
		Vec2[] dir = null;
		Mat22 m = Mat22.createRotationalTransform(ship.getBody().getAngle());
		Vec2 defDir = m.mul(new Vec2(0, 10));
		Vec2 posOffset = m.mul(new Vec2(0,5));
		switch(type){
		case DUAL:
//			pos = new Vec2[]{new Vec2(ship.getBody().getPosition(), y)
			break;
		case SINGLE:
			pos = new Vec2[]{ship.getBody().getPosition().add(posOffset)};
			dir = new Vec2[]{ship.getBody().getPosition().add(defDir)};
			break;
		case TRIPLE:
			break;
		default:
			break;
		
		}
		
		for(int i = 0; i < pos.length; i++){
			BodyDef bodyDef = new BodyDef();

			bodyDef.type = BodyType.DYNAMIC;
			bodyDef.setPosition(pos[i]);
			Body body = world.createBody(bodyDef);
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(0.2f, 0.2f);
		    body.createFixture(shape, 5);
			body.applyLinearImpulse(ship.getBody().getLinearVelocity().add(defDir.mul(100)), body.getPosition(), true);
		}
		
	}

}
