package area51.turboRocketWars.Bodies;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.shots.Shot;
import area51.turboRocketWars.Bodies.shots.Shot.ShotType;
import area51.turboRocketWars.Bodies.userData.FixtureViewProperties;

public class Cannon {

	public enum CannonType{SINGLE, DUAL, TRIPLE, BOMBER};
	private Vec2[][] stdCannonPosOpt = new Vec2[][]{
			new Vec2[]{new Vec2(0, 7)},
			new Vec2[]{new Vec2(2, 5), new Vec2(-2,5)},
			new Vec2[]{new Vec2(2.5f, 4), new Vec2(0,7), new Vec2(-2.5f, 4)},
			new Vec2[]{new Vec2(0, -5)}
	};
	private Vec2[] stdDirOpt = new Vec2[]{new Vec2(0,5), new Vec2(0,-5)};
	private Vec2 stdDir;
	private Vec2[] stdCannonPos;
	private long reloadTime = 100; //msec
	private long lastShotTime = 0;
	private CannonType type;
	private ShotType shotType;
	private World world;
	private Ship ship;
	
	
	public Cannon(CannonType type, ShotType shotType,  World world, Ship ship) {
		this.type = type;
		this.world = world;
		this.ship = ship;
		this.shotType = shotType;
		switch(type){
		case DUAL:
			stdCannonPos = stdCannonPosOpt[1];
			stdDir = stdDirOpt[0];
			break;
		case SINGLE:
			stdCannonPos = stdCannonPosOpt[0];
			stdDir = stdDirOpt[0];
			break;
		case TRIPLE:
			stdCannonPos = stdCannonPosOpt[2];
			stdDir = stdDirOpt[0];
			break;
		case BOMBER:
			stdCannonPos = stdCannonPosOpt[3];
			stdDir = stdDirOpt[1];
		default:
			break;
		
		}

	}
	
	public synchronized void fire(){
		long newTime = System.currentTimeMillis();
		if(newTime-lastShotTime < reloadTime){
			return;
		}
		lastShotTime = newTime;
		Mat22 m = Mat22.createRotationalTransform(ship.getBody().getAngle());

		for(int i = 0; i < stdCannonPos.length; i++){
			Vec2 pos = ship.getBody().getPosition().add(m.mul(stdCannonPos[i]));
			Vec2 dir = m.mul(stdDir);		
			new Shot(shotType, pos, dir, world);
			
		}		
	}

}
