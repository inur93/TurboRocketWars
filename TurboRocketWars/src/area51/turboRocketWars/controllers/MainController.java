package area51.turboRocketWars.controllers;

import static area51.turboRocketWars.settings.SettingsEditable.*;

import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Player;
import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.maps.Map;
import area51.turboRocketWars.gui.MainWindow;
import area51.turboRocketWars.gui.controllers.GUIController;
import area51.turboRocketWars.gui.controllers.MenuController;
import area51.turboRocketWars.gui.views.MainGamePanel;
import area51.turboRocketWars.listeners.GameKeyHandler;
import area51.turboRocketWars.listeners.KeyHandler;
import area51.turboRocketWars.listeners.MenuKeyHandler;
import area51.turboRocketWars.settings.KeyBoardConfigurations;

public class MainController implements IMainController{

	
//	private MenuController menuController;
	private GUIController guiController;
//	private GameController gameController;
	private World world;
	
	private Map[] maps = new Map[]{Map.createDefaultMap(new World(new Vec2()))};
	private Map selectedMap;
//	
//	private KeyHandler[] keyHandlers;
//	private MainGamePanel[] gamePanels;
	
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
