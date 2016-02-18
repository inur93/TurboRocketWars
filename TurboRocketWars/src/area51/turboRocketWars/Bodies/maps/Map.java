package area51.turboRocketWars.Bodies.maps;

import java.awt.Color;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;

public class Map {

	public static Map createDefaultMap(World world){	
		new Platform(world, -400, 0);
		new Platform(world, 400, 200);
		return new Map(world, new Vec2(-500, 0), 1000, 500);
	}
	
	public Map(World world, Vec2 pos, float width, float height) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0, 0);
		Body groundBody = world.createBody(groundBodyDef);
		ChainShape groundBox = new ChainShape();
		Vec2[] mapPoints = new Vec2[]{	pos, 									pos.clone().addLocal(width, 0),
										pos.clone().addLocal(width, height), 	pos.clone().addLocal(0, height)};
		groundBox.createLoop(mapPoints, mapPoints.length);
		
		groundBody.createFixture(groundBox, 0);
		groundBody.setUserData(new UserDataProp(SettingsFinal.USER_DATA_MAP, Color.YELLOW, 3, true, this));
	}

}
