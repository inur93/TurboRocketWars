package area51.turboRocketWars.Bodies.maps.objects;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.seperator.B2Separator;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;

public class Island extends Entity {

	public Island(Vec2 position, Color color, boolean fill, int stroke) {
		super(position, color, fill, stroke);
	}

	@Override
	public void create(World world) {
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.setPosition(position);
		Body body = world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		
		Vec2[] points = new Vec2[]{new Vec2(0,0), new Vec2(-5,0), new Vec2(-7, 2), new Vec2(-6, -5), new Vec2(6,-5), new Vec2(8, 0)};

		for(int i = 0; i < points.length; i++){
			points[i] = points[i].mul(10);
		}

		B2Separator.seperate(body, fixtureDef, points, 1);		
		
		body.setUserData(new UserDataProp(SettingsFinal.USER_DATA_MAP_ENTITY, color, stroke, fill, this));
		
	}

}
