package area51.turboRocketWars.Bodies.shots;

import java.awt.Color;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.controllers.LocalMultiplayerController;
import area51.turboRocketWars.settings.SettingsFinal;

public class Bomb  extends Shot implements Runnable{

	private float impFactor = 20;
	private long timeToDetonate = 1000; // msec
	private int numberFragments = 20;
	public Bomb(Vec2 pos, Vec2 dir, World world) {
		super(pos, dir, world);
		this.damage = 20;
	}

	@Override
	public void execute() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.setPosition(pos);
		body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.4f, 0.4f);
	    body.createFixture(shape, 15);
		body.applyLinearImpulse(dir.mul(impFactor), pos, true);
		body.setUserData(new UserDataProp(SettingsFinal.USER_DATA_SHOT, Color.WHITE, 1, true));
		new Thread(this).start();
	}

	public static class BombFactory implements Factory<Bomb>{

		public Bomb factory(Vec2 pos, Vec2 dir, World world) {
			return new Bomb(pos, dir, world);
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
			new Fragments(this.pos.add(m.mul(d).mul(7f)), m.mul(d), world);
		}
		LocalMultiplayerController.bodiesToDelete.add(this.body);
	}

}