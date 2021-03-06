package area51.turboRocketWars.Bodies.shots;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.controllers.GameController;
import area51.turboRocketWars.tasks.Task;
import static area51.turboRocketWars.settings.SettingsEditable.*;

public abstract class Shot implements Task {

	protected World world;
	protected Body body;
	protected double damage = 5; // default value
	protected int ammoCost = 1;
	protected Vec2 initialVel;
	protected Vec2 pos;
	protected Vec2 dir;
	protected long startTime; // ms
	protected final long shotDuration = ShotDuration(); //ms
	
	public Shot(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
		this.initialVel = initialVel;
		this.pos = pos;
		this.dir = dir.clone();
		this.dir.normalize();
		this.world = world;	
		GameController.tasks.add(this);
		GameController.shots.add(this);
		this.startTime = System.currentTimeMillis();
	}
	
	public abstract void executeTask();
	
	/**
	 * lifetime for shot. 
	 * @return true if shot should be removed because of timing out. 
	 * Otherwise false
	 */
	public boolean hasTimedOut(){
		if(System.currentTimeMillis()-startTime > shotDuration){
			return true;
		}
		return false;
	}
	
	public int ammoCost(){
		return this.ammoCost;
	}
	
	/**
	 * removes body from world and from controller shots list
	 */
	public void destroy(){
		world.destroyBody(getBody());
		GameController.shots.remove(this);
	}

	public double getDamage(){
		return this.damage;
	}
	
	public Body getBody(){
		return this.body;
	}

}
