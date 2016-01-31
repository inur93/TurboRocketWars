package area51.turboRocketWars.Bodies.shots;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Seeker extends Shot {

	public Seeker(Vec2 pos, Vec2 dir, World world) {
		super(pos, dir, world);
		// TODO Auto-generated constructor stub
	}





	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
	public static class SeekerFactory implements Factory<Seeker>{

		public Seeker factory(Vec2 pos, Vec2 dir, World world) {
			return new Seeker(pos, dir, world);
		}
		
	}
}
