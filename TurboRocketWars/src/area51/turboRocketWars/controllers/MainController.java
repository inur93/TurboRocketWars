package area51.turboRocketWars.controllers;

import static area51.turboRocketWars.settings.SettingsEditable.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Player;
import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.maps.Map;
import area51.turboRocketWars.gui.controllers.GUIController;
import area51.turboRocketWars.listeners.KeyHandler;
import area51.turboRocketWars.settings.KeyBoardConfigurations;

public class MainController implements IMainController, KeyListener{

	
	private GUIController guiController;
	private World world;
	private GameController gameController;
	
	private Map[] maps = new Map[]{Map.createDefaultMap(new World(new Vec2())), Map.createRocksMap(new World(new Vec2()))} ;
	private Map selectedMap;	
	private Player[] players;
	private boolean gameStarted = false;
	
	public MainController() {
		this.world = new World(new Vec2(GravityX(), GravityY()));
		this.players = new Player[]{new Player(new Ship(world, new Vec2())), new Player(new Ship(world, new Vec2()))};
		this.guiController = new GUIController(this, players[0].getMenuKeyHandler(), maps);

	}

	public void selectMap(Map map){
		gameStarted = false;
		this.selectedMap = map;
	}
	
	public Map[] getMaps(){
		return this.maps;
	}
	
	public void startGame(){
		if(gameStarted){
			resume();
			return;
		}
		if(selectedMap == null) selectedMap = maps[0]; // if no map selected take default
		
		this.world = new World(new Vec2(GravityX(), GravityY()));
		for(Player p : players) p.setShip(new Ship(world, selectedMap.getNextSpawnPoint()));
		selectedMap.create(world);

		guiController.initializeGameWindowAndStart(players, world);
		gameController = new GameController(this, world);
		guiController.addKeyListener(this);
		gameStarted = true;
	}

	public static void main(String[] args) {
		new MainController();
	}

	public void exit() {
		System.exit(0);
	}
	
	private void resume(){
		gameController.resume();
		guiController.setActiveViews(GUIController.GAME_VIEW_ID, this);
		System.out.println("resuming...");
	}
	
	private void pause(){
		gameController.pause();
		guiController.setActiveViews(GUIController.MENU_VIEW_ID, this);
		System.out.println("pausing...");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			if(gameStarted && gameController.isPaused()){
				resume();
			}else if(gameStarted && !gameController.isPaused()){
				pause();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}
