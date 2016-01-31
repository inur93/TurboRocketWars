package area51.turboRocketWars.Bodies.cannons;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.shots.Factory;
import area51.turboRocketWars.Bodies.shots.Shot;

public class Cannon<T extends Shot> {

	private Vec2 cannonDirection = new Vec2(0,5);
	private Vec2[] cannonPos;
	private long reloadTime = 100; //msec
	private World world;
	private Ship ship;

	private Factory<T> factory;

	public Cannon(Factory<T> factory,int number, boolean cannonPointForward, long reloadTime, World world, Ship ship) {
		this.factory = factory;	
		this.world = world;
		this.ship = ship;
		this.reloadTime = reloadTime;
		if(!cannonPointForward) cannonDirection = cannonDirection.mul(-1);

		cannonPos = new Vec2[number];
		switch(number){
		case 1 : 
			cannonPos = new Vec2[]{new Vec2(0, 7)};
			break;
		case 2:
			cannonPos = new Vec2[]{new Vec2(2, 5), new Vec2(-2,5)};
			break;
		case 3:
		default: 
			cannonPos = new Vec2[]{new Vec2(2.5f, 4), new Vec2(0,7), new Vec2(-2.5f, 4)};
			break;
		}

		if(!cannonPointForward){
			for(int i = 0; i < cannonPos.length; i++) cannonPos[i] = cannonPos[i].mul(-1);
		}
	}

	public synchronized void fire(){
		
		Mat22 m = Mat22.createRotationalTransform(ship.getBody().getAngle());

		for(int i = 0; i < cannonPos.length; i++){
			Vec2 pos = ship.getBody().getPosition().add(m.mul(cannonPos[i]));
			Vec2 dir = m.mul(cannonDirection);
			factory.factory(pos, dir, world);
		}		
	}
	
	public long getReloadTime(){
		return this.reloadTime;
	}



}
