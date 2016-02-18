package area51.turboRocketWars.Bodies.maps;

import java.awt.Color;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;

public class Platform {

	public Platform(World world, float px, float py) {
		BodyDef platformDef = new BodyDef();
		platformDef.position.set(px, py);
		Body platformBody = world.createBody(platformDef);
		ChainShape platformShape = new ChainShape();
		platformShape.createChain(new Vec2[]{new Vec2(0, 0), new Vec2(5,10), new Vec2(40,10), new Vec2(45,0)}, 4);
		platformBody.createFixture(platformShape, 0);
		platformBody.getFixtureList().setFriction(SettingsFinal.PLATFORM_FRICTION);
		platformBody.setUserData(new UserDataProp(SettingsFinal.USER_DATA_PLATFORM, Color.red, 3, false, this));
	}

}
