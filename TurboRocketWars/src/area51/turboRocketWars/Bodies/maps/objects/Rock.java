package area51.turboRocketWars.Bodies.maps.objects;

import java.awt.Color;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.seperator.B2Separator;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;

public class Rock extends Entity {

	private float scale;
	private float rotation;
	private boolean dynamic;
	public Rock(Vec2 position, Color color, boolean fill, int stroke, float scale, float rotation, boolean dynamic) {
		super(position, color, fill, stroke);
		this.scale = scale;
		this.rotation = rotation;
		this.dynamic = dynamic;
	}

	@Override
	public void create(World world) {
		BodyDef groundBodyDef = new BodyDef();
		if(dynamic) groundBodyDef.setType(BodyType.DYNAMIC);
		
		groundBodyDef.setPosition(position);
		Body groundBody = world.createBody(groundBodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1;
		fixtureDef.friction = 1f;
		
		float curStep = 0;
		int num = 9;
		float step = (float) ((Math.PI*2)/num);		
		Vec2[] points = new Vec2[num];

		for(int i = 0; i < num; i++){
			curStep = step*i;
			points[i] = new Vec2((float) Math.cos((double) curStep),(float) Math.sin((double) curStep)).mul(5*scale);
		}

		B2Separator.seperate(groundBody, fixtureDef, points, 1);		
		
		groundBody.setUserData(new UserDataProp(SettingsFinal.USER_DATA_MAP_ENTITY, color, stroke, fill, this));
		groundBody.m_sweep.a = this.rotation;
		
	}

}
