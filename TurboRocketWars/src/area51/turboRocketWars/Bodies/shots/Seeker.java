package area51.turboRocketWars.Bodies.shots;

import static area51.turboRocketWars.settings.SettingsFinal.*;
import static area51.turboRocketWars.settings.SettingsEditable.*;

import java.awt.Color;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.userData.UserDataProp;

public class Seeker extends Shot implements Runnable{
	
	private static int SEEKER_AMMO_COST = 1;
	private float seekerSpeed = 10;
	private float initialSpeed = 20;
	private long timeBeforeSeeking = SeekerTimeBeforeSeeking(); // msec
	private boolean superSeekerOn = SuperSeeker();
	
	public Seeker(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
		super(initialVel, pos, dir, world);
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
		body.applyLinearImpulse(dir.mul(initialSpeed), pos, true);
		body.setUserData(new UserDataProp(USER_DATA_SHOT, Color.WHITE, 1, true, this));
		new Thread(this).start();	
	}
	
	public static class SeekerFactory implements Factory<Seeker>{

		public Seeker factory(Vec2 initialVel, Vec2 pos, Vec2 dir, World world) {
			return new Seeker(initialVel, pos, dir, world);
		}
		
		@Override
		public int getAmmoCost() {
			return SEEKER_AMMO_COST;
		}
		
	}

	@Override
	public void run() {
		
		delay(timeBeforeSeeking);
		Ship target = null;
		float shortest = 0;
		for(Ship s : Ship.ships){
			float curLength = pos.sub(s.getBody().getPosition()).length();
			if(target == null || curLength < shortest){
				target = s;
				shortest = curLength;
			}
		}
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - startTime < shotDuration){
			Vec2 direction = target.getBody().getPosition().sub(getBody().getPosition());
			direction.normalize();
			Vec2 directedSpeed = direction.mul(seekerSpeed*10);
			Vec2 targetSpeed = target.getBody().getLinearVelocity();
			
			if(superSeekerOn)this.getBody().setLinearVelocity(directedSpeed.add(targetSpeed));
			else this.getBody().applyForceToCenter(directedSpeed.add(targetSpeed).mul(50));
			
			delay(100);
		}
	}
	
	private void delay(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {}
	}
}
