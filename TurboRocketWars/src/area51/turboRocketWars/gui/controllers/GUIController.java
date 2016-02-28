package area51.turboRocketWars.gui.controllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Player;
import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.maps.Map;
import area51.turboRocketWars.controllers.MainController;
import area51.turboRocketWars.gui.MainWindow;
import area51.turboRocketWars.gui.views.MainGamePanel;
import area51.turboRocketWars.gui.views.MainMenuPanel;
import area51.turboRocketWars.listeners.GameKeyHandler;
import area51.turboRocketWars.listeners.KeyHandler;
import area51.turboRocketWars.listeners.MenuKeyHandler;
import area51.turboRocketWars.settings.KeyBoardConfigurations;

public class GUIController {

	private static final String GAME_VIEW_ID = "game view controllers";
	private static final String MENU_VIEW_ID = "menu view controller";
	private static final String MAP_SELECT_VIEW_ID = "map select view controller";

	private MainController mainController;
	private MainWindow mainWindow;
	private Map[] maps;

	// fancy way to save all views
	private HashMap<String, ViewController[]> panels = new HashMap<String, ViewController[]>();

	public GUIController(MainController mainController, KeyHandler menuListener, Map[] maps) {
		this.mainController = mainController;
		this.maps = maps;

		this.mainWindow = new MainWindow();

		initializeMenuViews(menuListener);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
		}

		try {
			this.mainWindow.setBackground(ImageIO.read(new File("images/startbackground.jpg")));
		} catch (IOException e) {
			System.err.println("no background");
		}

		setActiveViews(panels.get(MENU_VIEW_ID));
	}

	private void initializeMenuViews(KeyHandler menuListener){

		// create menu with KeyConfigs from player 1.
		ViewController[] menu = new ViewController[]{new MenuController(this, menuListener)};
		ViewController[] mapSelect = new ViewController[]{new MapselectController(this, menuListener, maps)};
		this.panels.put(MENU_VIEW_ID, menu);
		this.panels.put(MAP_SELECT_VIEW_ID, mapSelect);
	}

	private void initializeGameView(Player[] players, World world){
		ViewController[] views = new GameViewController[players.length];
		for(int i = 0; i < players.length; i++){
			Player player = players[i];
			OBBViewportTransform camera = new OBBViewportTransform();
			views[i] = new GameViewController(this, player.getGameKeyHandler(), world, player, camera);
		}
		this.panels.put(GAME_VIEW_ID, views);
	}

	private void setActiveViews(ViewController[] views){
		mainWindow.removeAll();
		KeyHandler[] handlers = new KeyHandler[views.length];
		for(int i = 0; i < views.length; i++){
			int x = mainWindow.getWidth()/views.length * i;
			int y = 0;
			int width = mainWindow.getWidth()/views.length;
			int height = mainWindow.getHeight();
			this.mainWindow.addLayer(views[i].getView(), x, y, width, height);
			handlers[i] = views[i].setActive();
		}
		setKeyHandlers(handlers);

	}

	public void goToMapSelectMenu(){
		this.mainWindow.removeAll();
		setActiveViews(panels.get(MAP_SELECT_VIEW_ID));
	}

	public void goTostartGame(){
		mainController.startGame();
	}

	public void initializeGameWindowAndStart(Player[] players, World world){
		this.mainWindow.removeAll();
		initializeGameView(players, world);
		for(ViewController v : panels.get(GAME_VIEW_ID)) System.out.println("panel: " + v);
		setActiveViews(panels.get(GAME_VIEW_ID));
	}

	public void setKeyHandlers(KeyHandler... keyHandlers){
		if(mainWindow.getKeyListeners() != null){
			for(KeyListener k : mainWindow.getKeyListeners())mainWindow.removeKeyListener(k);
		}
		if(keyHandlers != null) for(KeyListener k : keyHandlers) mainWindow.addKeyListener(k);
	}

	public void selectMap(Map map) {
		this.mainController.selectMap(map);
	}

	public void exit() {
		mainController.exit();
	}

	public void cancel() {
		setActiveViews(panels.get(MENU_VIEW_ID));
	}



	//	public static void main(String[] args) {
	//		new GUIController(null, new MenuKeyHandler(new KeyBoardConfigurations(), null));
	//	}


}
