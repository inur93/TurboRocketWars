package area51.turboRocketWars.Bodies.shots;

import java.awt.Color;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import area51.turboRocketWars.Bodies.Delegate;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.controllers.LocalMultiplayerController;
import area51.turboRocketWars.settings.SettingsFinal;

public abstract class Shot implements Delegate {

//	protected float[] velVecOpt = new float[]{50, 0.000000001f};
//	protected float velVec;
	protected World world;
	protected Body body;
	protected double damage = 5;
	protected Vec2 pos;
	protected Vec2 dir;
	
	
	public Shot(Vec2 pos, Vec2 dir, World world) {
		this.pos = pos;
		this.dir = dir.clone();
		this.dir.normalize();
		this.world = world;	
		LocalMultiplayerController.delegates.add(this);
	}
	
	public abstract void execute();

	public double getDamage(){
		return this.damage;
	}
	
	public Body getBody(){
		return this.body;
	}

}
