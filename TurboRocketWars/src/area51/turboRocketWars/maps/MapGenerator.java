package area51.turboRocketWars.maps;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import static area51.turboRocketWars.settings.SettingsEditable.*;
import static area51.turboRocketWars.settings.SettingsFinal.*;

public class MapGenerator extends World{

	private Body map;
	private Body ship;
	public MapGenerator(){
	super(new Vec2(DEF_GRAVITY_X, DEF_GRAVITY_Y));
	map = createDefaultMap();
	ship = createDefaultShip();
	
	
	}
	
	public void run(){
		for (int i = 0; i < 60; ++i)
		{
		
		step(TIME_STEP, velocityIterations, positionIterations);
		Vec2 position = ship.getPosition();
		float angle = ship.getAngle();
		System.out.printf("%4.2f %4.2f %4.2f\n", position.x, position.y, angle);
		}
	}
	
	private Body createDefaultShip(){
		
		BodyDef shipBodyDef = new BodyDef();
		shipBodyDef.setType(BodyType.DYNAMIC);
		shipBodyDef.setPosition(new Vec2(50, 50));
		Body ship = createBody(shipBodyDef);
		
		PolygonShape shipShape = new PolygonShape();
		shipShape.setAsBox(1f, 1f, new Vec2(50, 50), (float) Math.toRadians(45));
		FixtureDef shipFixtureDef = new FixtureDef();
		shipFixtureDef.setShape(shipShape);
		shipFixtureDef.setDensity(1);
		shipFixtureDef.setFriction(0.3f);
		ship.createFixture(shipFixtureDef);
		return ship;
	}
	
	private Body createDefaultMap(){
		
		BodyDef mapBodyDef = new BodyDef();
		mapBodyDef.setPosition(new Vec2(10, 10));
		Body map = createBody(mapBodyDef);
		
		PolygonShape mapShape = new PolygonShape();
		mapShape.setAsBox(MAP_WIDTH, MAP_HEIGHT,MAP_CENTER, MAP_ANGLE);
		map.createFixture(mapShape, 0);
//		createBody(mapBodyDef)
		return map;
	}
	
	

}
