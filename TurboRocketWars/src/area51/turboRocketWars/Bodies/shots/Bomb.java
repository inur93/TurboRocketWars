package area51.turboRocketWars.Bodies.shots;

import java.awt.Color;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.controllers.GameController;
import static area51.turboRocketWars.settings.SettingsFinal.*;
import static area51.turboRocketWars.settings.SettingsEditable.*;


public class Bomb  extends Shot implements Runnable{

	private static int BOMB_AMMO_COST = BombAmmoCost();
	private float impFactor = 20;
	private long timeToDetonate = BombTimeToDetonate(); // msec
	private int numberFragments = BombNumberFragments();
	
	public Bomb(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
		super(initialVel, pos, dir, world);
		this.damage = BombDamage();
	}

	@Override
	public void executeTask() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.setPosition(pos);
		body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.4f, 0.4f);
		body.createFixture(shape, SHOT_DENSITY);
		body.applyLinearImpulse(dir.mul(impFactor), pos, true);
		body.setUserData(new UserDataProp(USER_DATA_SHOT, Color.WHITE, 1, true, this));
		new Thread(this).start();
	}

	public static class BombFactory implements Factory<Bomb>{

		public Bomb factory(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
			return new Bomb(initialVel, pos, dir, world);
		}
		@Override
		public int getAmmoCost() {
			return BOMB_AMMO_COST;
		}
	}

	public void run() {
		try {
			Thread.sleep(timeToDetonate);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Vec2 d = new Vec2(0, 1);
		double step = Math.PI*2/numberFragments;
		for(int i = 0; i < numberFragments; i++){
			Mat22 m = Mat22.createRotationalTransform((float) step*i);
			new Fragments(new Vec2(), this.pos.add(m.mul(d).mul(7f)), m.mul(d), world);
		}
		GameController.bodiesToDelete.add(this.body);
	}

}
