package area51.turboRocketWars.gui.controllers;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

import area51.turboRocketWars.listeners.KeyExecutor;
import area51.turboRocketWars.listeners.KeyHandler;

public abstract class ViewController implements KeyExecutor, ActionListener   {

	protected GUIController guiController;
	protected KeyHandler keyHandler;
	
	public ViewController(GUIController guiController, KeyHandler keyHandler) {
		this.guiController = guiController;
		this.keyHandler = keyHandler;
	}
	
	public abstract JPanel getView();
	
	public KeyHandler setActive(){
		this.keyHandler.setKeyExecutor(this);
		return this.keyHandler;
	}

}
