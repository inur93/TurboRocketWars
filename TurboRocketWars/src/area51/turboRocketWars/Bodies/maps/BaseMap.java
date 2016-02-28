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

public class BaseMap extends Entity{

	private float width;
	private float height;
	public BaseMap(Vec2 pos, Color color, boolean fill, int stroke, float width, float height) {
		super(pos, color, fill, stroke);
		this.width = width;
		this.height = height;
	}

	@Override
	public void create(World world) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.setPosition(this.position);
		Body groundBody = world.createBody(groundBodyDef);
		ChainShape groundBox = new ChainShape();
		Vec2[] mapPoints = new Vec2[]{	position, 									position.clone().addLocal(width, 0),
										position.clone().addLocal(width, height), 	position.clone().addLocal(0, height)};
		groundBox.createLoop(mapPoints, mapPoints.length);

		groundBody.createFixture(groundBox, 0);
		groundBody.setUserData(new UserDataProp(SettingsFinal.USER_DATA_MAP_ENTITY, color, stroke, fill, this));

	}

}
