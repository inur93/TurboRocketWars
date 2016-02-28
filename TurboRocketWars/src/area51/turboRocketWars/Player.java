package area51.turboRocketWars;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.listeners.GameKeyHandler;
import area51.turboRocketWars.listeners.KeyHandler;
import area51.turboRocketWars.listeners.MenuKeyHandler;
import area51.turboRocketWars.settings.KeyBoardConfigurations;

public class Player {

	private Ship ship;
	private GameKeyHandler gameKeyHandler;
	private MenuKeyHandler menuKeyHandler;
	
	public Player(Ship ship) {
		this.ship = ship;
		KeyBoardConfigurations config = new KeyBoardConfigurations();
		this.gameKeyHandler = new GameKeyHandler(config, ship);
		this.menuKeyHandler = new MenuKeyHandler(config, null);
	}
	
	public KeyHandler getMenuKeyHandler(){
		return this.menuKeyHandler;
	}
	
	public KeyHandler getGameKeyHandler(){
		return this.gameKeyHandler;
	}
	
	public Ship getShip(){
		return this.ship;
	}

}
