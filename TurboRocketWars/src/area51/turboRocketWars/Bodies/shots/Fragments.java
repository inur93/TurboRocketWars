package area51.turboRocketWars.Bodies.shots;

import java.awt.Color;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;

public class Fragments extends Shot {

	private static int FRAGMENTS_AMMO_COST = 1;
	private float impFactor = 150;
	public Fragments(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
		super(initialVel, pos, dir, world);
		
	}

	@Override
	public void executeTask() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.setPosition(pos);
		body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.2f, 0.2f);
	    body.createFixture(shape, 15);
		body.applyLinearImpulse(dir.mul(impFactor), pos, true);
		body.setUserData(new UserDataProp(SettingsFinal.USER_DATA_SHOT, Color.WHITE, 1, true, this));
	}

	public static class FragmentFactory implements Factory<Fragments>{

		public Fragments factory(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
			return new Fragments(initialVel, pos, dir, world);
		}
		
		@Override
		public int getAmmoCost() {
			return FRAGMENTS_AMMO_COST;
		}
		
	}
}
