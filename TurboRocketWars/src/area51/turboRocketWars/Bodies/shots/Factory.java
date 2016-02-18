package area51.turboRocketWars.Bodies.shots;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public interface Factory <T extends Shot>{

	T factory(Vec2 initialVel, Vec2 pos, Vec2 dir, World world);
}
