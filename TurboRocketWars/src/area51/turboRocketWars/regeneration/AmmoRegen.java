package area51.turboRocketWars.regeneration;

import area51.turboRocketWars.Bodies.Ship;

public class AmmoRegen extends Regenerator{

	private Ship ship;
	public AmmoRegen(Ship ship) {
		super(ship.getAmmoRegenFrequency());
		this.ship = ship;
	}

	@Override
	public void regen() {
		ship.regenerateAmmo();
	}

}
