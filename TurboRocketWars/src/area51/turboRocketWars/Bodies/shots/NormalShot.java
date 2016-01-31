package area51.turboRocketWars.Bodies.shots;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class NormalShot extends Shot {

	public NormalShot(ShotType type, Vec2 pos, Vec2 dir, World world) {
		super(type, pos, dir, world);
	}

}
