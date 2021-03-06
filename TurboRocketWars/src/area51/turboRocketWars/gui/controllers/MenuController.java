package area51.turboRocketWars.gui.controllers;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import area51.turboRocketWars.gui.views.MainMenuPanel;
import area51.turboRocketWars.listeners.KeyHandler;

public class MenuController extends ViewController{

	private MainMenuPanel menuPanel;
	
	public MenuController(GUIController guiController, KeyHandler keyHandler) {
		super(guiController, keyHandler);
		this.menuPanel = new MainMenuPanel(this);
		
	}
	
	public KeyHandler getKeyHandler(){
		return this.keyHandler;
	}

	@Override
	public void boost() {
		this.menuPanel.focusPrevButton();
	}

	@Override
	public void left() {
		this.guiController.cancel();
	}

	@Override
	public void right() {
		this.menuPanel.getSelectedButton().doClick();
		
	}

	@Override
	public void shootNormal() {
		this.menuPanel.focusNextButton();
	}

	@Override
	public void shootSpecial() {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case MainMenuPanel.START_GAME_ACTION:
			this.guiController.goTostartGame();
			break;
		case MainMenuPanel.MAP_SELECT_ACTION:
			this.guiController.goToMapSelectMenu();
			break;
		case MainMenuPanel.EXIT_ACTION:
			this.guiController.exit();
			break;
		case MainMenuPanel.CANCEL_ACTION:
			this.guiController.cancel();
			break;
		case MainMenuPanel.SETTINGS_ACTION:
			this.guiController.goToSettings();
			break;
			default:
				System.err.println("unknown action command: " + e.getActionCommand());
		}
	}
	
	public JPanel getView(){
		return this.menuPanel.getPanel();
	}

}
