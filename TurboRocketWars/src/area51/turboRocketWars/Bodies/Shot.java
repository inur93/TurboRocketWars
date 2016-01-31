package area51.turboRocketWars.Bodies;

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

import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;

public class Shot {

	public enum ShotType{NORMAL, BOMB, SEEKER}
	private float[] velVecOpt = new float[]{50, 0.000000001f};
	private float velVec;
	private World world;
	private Body body;
	private double damage = 5;
	
	private ShotType type;
	public Shot(ShotType type, Vec2 pos, Vec2 dir, World world) {
		this.type = type;
		switch (type) {
		case BOMB:
			velVec = velVecOpt[1];
			break;
		case NORMAL:
		case SEEKER:
			velVec = velVecOpt[0];
		default:
			break;
		}
		this.world = world;
//		FixtureDef fixtureDef = new FixtureDef();
//		fixtureDef.density = 1;
//		fixtureDef.friction = 0.01f;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.setPosition(pos);
		body = world.createBody(bodyDef);
		if(body == null) return;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.2f, 0.2f);
	    body.createFixture(shape, 15);
		body.applyLinearImpulse(dir.mul(velVec), pos, true);
		body.setUserData(new UserDataProp(SettingsFinal.USER_DATA_SHOT, Color.WHITE, 1, true));
		
//		world.getParticleBodyContacts()[0].
		
	}

	public double getDamage(){
		return this.damage;
	}

}
