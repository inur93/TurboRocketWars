package area51.turboRocketWars.Bodies.shots;

import java.awt.Color;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.UserDataProp;
import static area51.turboRocketWars.settings.SettingsFinal.*;

public class NormalShot extends Shot {

	private static int NORMAL_AMMO_COST = 1;
	private final float NORMAL_SHOT_SPEED = 1000;
	
	/**
	 * 
	 * @param initialVel velocity of parent body, fx ship
	 * @param pos start position of shot, fx somewhere in front of ship
	 * @param dir firing direction, fx same direction as ship
	 * @param world, the one and only world
	 */
	public NormalShot(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
		super(initialVel, pos, dir, world);
	}

	private void createBody(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.setPosition(pos.clone());
		body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.2f, 0.2f);
	    body.createFixture(shape, SHOT_DENSITY);
	    
	}
	
	@Override
	public void executeTask() {
		// create body and adds it to the world at given position
	    createBody();
	    
	    // add first velocity of ship given by initialVel then normal shots speed with ship direction
	    body.setLinearVelocity(initialVel);
	    body.applyLinearImpulse(dir.mul(NORMAL_SHOT_SPEED).clone(), pos, true);
	    
	    // important to add userdata otherwise no way to determine type of body at collision detection
	    body.setUserData(new UserDataProp(USER_DATA_SHOT, Color.WHITE, 1, true, this));
	}
	
	public static class NormalShotFactory implements Factory<NormalShot>{

		public NormalShot factory( Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
			return new NormalShot(initialVel, pos, dir, world);
		}
		
		@Override
		public int getAmmoCost() {
			return NORMAL_AMMO_COST;
		}
	}

}
