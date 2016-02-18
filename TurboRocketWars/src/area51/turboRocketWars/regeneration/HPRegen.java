package area51.turboRocketWars.regeneration;

import area51.turboRocketWars.Bodies.Ship;

public class HPRegen extends Regenerator {

	private Ship ship;
	public HPRegen(Ship ship) {
		super(ship.getHPRegenFrequency());
		this.ship = ship;
	}

	@Override
	public void regen() {
		ship.regenerateHP();
	}

}
