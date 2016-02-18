package area51.turboRocketWars.Bodies.shots;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Delegate;
import area51.turboRocketWars.controllers.LocalMultiplayerController;

public abstract class Shot implements Delegate {

	protected World world;
	protected Body body;
	protected double damage = 5; // default value
	protected Vec2 initialVel;
	protected Vec2 pos;
	protected Vec2 dir;
	protected long startTime; // ms
	protected final long shotDuration = 3000; //ms
	
	public Shot(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
		this.initialVel = initialVel;
		this.pos = pos;
		this.dir = dir.clone();
		this.dir.normalize();
		this.world = world;	
		LocalMultiplayerController.delegates.add(this);
		LocalMultiplayerController.shots.add(this);
		this.startTime = System.currentTimeMillis();
	}
	
	public abstract void execute();
	
	public boolean hasTimedOut(){
		if(System.currentTimeMillis()-startTime > shotDuration){
			return true;
		}
		return false;
	}
	
	/**
	 * removes body from world and from controller shots list
	 */
	public void destroy(){
		world.destroyBody(getBody());
		LocalMultiplayerController.shots.remove(this);
	}

	public double getDamage(){
		return this.damage;
	}
	
	public Body getBody(){
		return this.body;
	}

}
