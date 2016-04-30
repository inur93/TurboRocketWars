package area51.turboRocketWars.Bodies.cannons;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.shots.Factory;
import area51.turboRocketWars.Bodies.shots.Shot;

public class Cannon<T extends Shot> {

	private Vec2 cannonDirection = new Vec2(0,5);
	private Vec2[] cannonPos;
	private long reloadTime = 100; //msec default
	private World world;
	private Ship ship;

	private Factory<T> shotFactory;

	public Cannon(Factory<T> shotFactory,int cannonNumber, boolean cannonPointForward, long reloadTime, World world, Ship ship) {
		this.shotFactory = shotFactory;	
		this.world = world;
		this.ship = ship;
		if(reloadTime <= 0) this.reloadTime = reloadTime;
		if(!cannonPointForward) cannonDirection = cannonDirection.mul(-1);

		cannonPos = new Vec2[cannonNumber];
		switch(cannonNumber){
		case 1 : 
			cannonPos = new Vec2[]{new Vec2(0, 7)};
			break;
		case 2:
			cannonPos = new Vec2[]{new Vec2(2, 5), new Vec2(-2,5)};
			break;
		case 3:
		default: 
			cannonPos = new Vec2[]{new Vec2(2.5f, 7), new Vec2(0,10), new Vec2(-2.5f, 7)};
			break;
		}

		if(!cannonPointForward){
			for(int i = 0; i < cannonPos.length; i++) cannonPos[i] = cannonPos[i].mul(-1);
		}
	}

	public synchronized void fire(){
		Mat22 m = Mat22.createRotationalTransform(ship.getBody().getAngle()%MathUtils.TWOPI);
		for(int i = 0; i < cannonPos.length; i++){
			if(ship.hasAmmo(shotFactory.getAmmoCost())){
				Vec2 pos = ship.getBody().getPosition().add(m.mul(cannonPos[i]));
				Vec2 dir = m.mul(cannonDirection);
				Shot s = shotFactory.factory(ship.getBody().getLinearVelocity(), pos, dir, world);
				ship.useAmmo(s.ammoCost());
			}
		}
	}

	public long getReloadTime(){
		return this.reloadTime;
	}



}
