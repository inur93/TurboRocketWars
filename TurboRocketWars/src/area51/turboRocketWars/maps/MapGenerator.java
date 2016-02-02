package area51.turboRocketWars.maps;

import java.awt.Color;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;
import static area51.turboRocketWars.settings.SettingsEditable.*;
import static area51.turboRocketWars.settings.SettingsFinal.*;

public class MapGenerator extends World{

//	private Body map;
//	private Body ship;
	public MapGenerator(){
	super(new Vec2(DEF_GRAVITY_X, DEF_GRAVITY_Y));
//	map = createDefaultMap();
//	ship = createDefaultShip();
	
	
	}

public static void createMap(int ind, World world){
	switch(ind){
	default:
		defMap(world);
		break;
	}
}

private static void defMap(World world){
	BodyDef groundBodyDef = new BodyDef();
	groundBodyDef.position.set(0, 0);
	Body groundBody = world.createBody(groundBodyDef);
	ChainShape groundBox = new ChainShape();
//	PolygonShape groundBox = new PolygonShape();
//	groundBox.setAsBox(100, 50);
	groundBox.createLoop(new Vec2[]{new Vec2(-500, 0), new Vec2(500, 0), new Vec2(500, 500), new Vec2(-500, 500)}, 4);
	
	groundBody.createFixture(groundBox, 0);
	groundBody.setUserData(new UserDataProp(SettingsFinal.USER_DATA_MAP, Color.YELLOW, 3, true));
	
	createPlatform(world, -400, 0);
	createPlatform(world, 400, 200);
}
	
private static void createPlatform(World world, float px, float py){
	BodyDef platformDef = new BodyDef();
	platformDef.position.set(px, py);
	Body platformBody = world.createBody(platformDef);
	ChainShape platformShape = new ChainShape();
	platformShape.createChain(new Vec2[]{new Vec2(0, 0), new Vec2(5,10), new Vec2(40,10), new Vec2(45,0)}, 4);
	platformBody.createFixture(platformShape, 0);
	platformBody.getFixtureList().setFriction(SettingsFinal.PLATFORM_FRICTION);
	platformBody.setUserData(new UserDataProp(SettingsFinal.USER_DATA_PLATFORM, Color.red, 3, false));
}
	

}
