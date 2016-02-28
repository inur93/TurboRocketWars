package area51.turboRocketWars.Bodies.maps;

import java.awt.Color;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.maps.objects.Entity;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;

public class Platform extends Entity{

	private final static Vec2 shipOffset = new Vec2(15,20); 
	public Platform(float px, float py) {
		super(new Vec2(px, py), Color.red, false, 1);
		
	}

	public Vec2 getSpawnPoint(){
		return position.add(shipOffset);
	}

	@Override
	public void create(World world) {
		BodyDef platformDef = new BodyDef();
		platformDef.setPosition(position);
		Body platformBody = world.createBody(platformDef);
		ChainShape platformShape = new ChainShape();
		platformShape.createChain(new Vec2[]{new Vec2(0, 0), new Vec2(5,10), new Vec2(40,10), new Vec2(45,0)}, 4);
		platformBody.createFixture(platformShape, 0);
		platformBody.getFixtureList().setFriction(SettingsFinal.PLATFORM_FRICTION);
		platformBody.setUserData(new UserDataProp(SettingsFinal.USER_DATA_PLATFORM, Color.red, 3, false, this));
	}
}
