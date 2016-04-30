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

	public static final String GAME_VIEW_ID = "game view controllers";
	public static final String MENU_VIEW_ID = "menu view controller";
	public static final String MAP_SELECT_VIEW_ID = "map select view controller";
	public static final String SETTINGS_VIEW_ID = "settings view controller";

	private MainController mainController;
	private MainWindow mainWindow;
	private Map[] maps;
	private String activeView;

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
		setActiveViews(MENU_VIEW_ID);
	}

	private void initializeMenuViews(KeyHandler menuListener){
		// create menu with KeyConfigs from player 1.
		ViewController[] menu = new ViewController[]{new MenuController(this, menuListener)};
		ViewController[] mapSelect = new ViewController[]{new MapselectController(this, menuListener, maps)};
		ViewController[] settings = new ViewController[]{new SettingsController(this, menuListener)};
		this.panels.put(MENU_VIEW_ID, menu);
		this.panels.put(MAP_SELECT_VIEW_ID, mapSelect);
		this.panels.put(SETTINGS_VIEW_ID, settings);
	}

	private void initializeGameView(Player[] players, World world){
		ViewController[] views = new GameViewController[players.length];
		if(this.panels.containsKey(GAME_VIEW_ID)) return;
		for(int i = 0; i < players.length; i++){
			Player player = players[i];
			OBBViewportTransform camera = new OBBViewportTransform();
			views[i] = new GameViewController(this, player.getGameKeyHandler(), world, player, camera);
		}
		this.panels.put(GAME_VIEW_ID, views);
	}

	/**
	 * Removes all views and sets the specified instead. 
	 * @param viewId activates views with this id. If view does not exist, you can add it with {@link #addView(String, ViewController[])}}
	 * @param extraKeyHandlers views have {@link KeyListener} attached, but might not be sufficient. In this you can add an extra listener here.
	 */
	public void setActiveViews(String viewId, KeyListener... extraKeyHandlers){
		ViewController[] views = panels.get(viewId);
		mainWindow.removeAll();
		activeView = viewId;
		KeyListener[] handlers = new KeyListener[views.length + (extraKeyHandlers == null ? 0 : extraKeyHandlers.length)];
		for(int i = 0; i < views.length; i++){
			int x = mainWindow.getWidth()/views.length * i;
			int y = 0;
			int width = mainWindow.getWidth()/views.length;
			int height = mainWindow.getHeight();
			this.mainWindow.addLayer(views[i].getView(), x, y, width, height);
			handlers[i] = views[i].setActive();
			System.out.println("adding handler: " + handlers[i]);
		}
		if(extraKeyHandlers != null){
			for(int i = views.length; i < handlers.length; i++){
				handlers[i] = extraKeyHandlers[i-views.length];
			}
		}
		setKeyHandlers(handlers);
	}

	public void goToMapSelectMenu(){
		setActiveViews(MAP_SELECT_VIEW_ID);
	}

	public void goTostartGame(){
		mainController.startGame();
	}

	/**
	 * initialize game view with world and players. Must be called before going to gameview
	 * @param players one view is created for each player. 
	 * @param world the world object containing each players ship (body) and map.
	 */
	public void initializeGameWindowAndStart(Player[] players, World world){
		initializeGameView(players, world);
		for(ViewController v : panels.get(GAME_VIEW_ID)) System.out.println("panel: " + v);
		setActiveViews(GAME_VIEW_ID);
	}

	private void setKeyHandlers(KeyListener... keyHandlers){
		if(mainWindow.getKeyListeners() != null){
			for(KeyListener k : mainWindow.getKeyListeners())mainWindow.removeKeyListener(k);
		}
		if(keyHandlers != null) for(KeyListener k : keyHandlers) mainWindow.addKeyListener(k);
	}

	public void addKeyListener(KeyListener keyhandler){
		mainWindow.addKeyListener(keyhandler);
	}

	public void selectMap(Map map) {
		this.mainController.selectMap(map);
	}

	public void exit() {
		mainController.exit();
	}

	public void cancel() {
		setActiveViews(MENU_VIEW_ID);
		mainWindow.giveFocus();
	}

	public void goToSettings() {
		setActiveViews(SETTINGS_VIEW_ID);
	}

	public void addView(String key, ViewController[] views){
		panels.put(key, views);
	}

	public void updateView(){
		setActiveViews(activeView);
		mainWindow.giveFocus();
	}

	//	public static void main(String[] args) {
	//		new GUIController(null, new MenuKeyHandler(new KeyBoardConfigurations(), null));
	//	}


}
