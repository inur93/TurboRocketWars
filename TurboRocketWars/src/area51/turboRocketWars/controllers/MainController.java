package area51.turboRocketWars.controllers;

import static area51.turboRocketWars.settings.SettingsEditable.DEF_GRAVITY_X;
import static area51.turboRocketWars.settings.SettingsEditable.DEF_GRAVITY_Y;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Player;
import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.maps.Map;
import area51.turboRocketWars.gui.controllers.GUIController;

public class MainController implements IMainController{

	
	private GUIController guiController;
	private World world;
	
	private Map[] maps = new Map[]{Map.createDefaultMap(new World(new Vec2())), Map.createRocksMap(new World(new Vec2()))} ;
	private Map selectedMap;	
	private Player[] players;
	
	public MainController() {
		
		this.world = new World(new Vec2(DEF_GRAVITY_X, DEF_GRAVITY_Y));
		this.players = new Player[]{new Player(new Ship(world, new Vec2())), new Player(new Ship(world, new Vec2()))};
		this.guiController = new GUIController(this, players[0].getMenuKeyHandler(), maps);

	}

	public void selectMap(Map map){
		this.selectedMap = map;
	}
	
	public Map[] getMaps(){
		return this.maps;
	}
	
	public void startGame(){
		for(Player p : players) p.getShip().setSpawnPoint(selectedMap.getNextSpawnPoint());
		selectedMap.create(world);

		guiController.initializeGameWindowAndStart(players, world);
		new GameController(this, world);
	}
	
	public static void main(String[] args) {
		new MainController();
	}

	public void exit() {
		System.exit(0);
	}
}
