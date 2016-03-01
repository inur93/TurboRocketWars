package area51.turboRocketWars.gui.controllers;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import area51.turboRocketWars.Bodies.maps.Map;
import area51.turboRocketWars.gui.views.MapSelectPanel;
import area51.turboRocketWars.listeners.KeyExecutor;
import area51.turboRocketWars.listeners.KeyHandler;
import area51.turboRocketWars.settings.KeyBoardConfigurations;

public class MapselectController extends ViewController{

	private MapSelectPanel panel;
	private Map[] maps;
	public MapselectController(GUIController guiController, KeyHandler keyHandler, Map... maps) {
		super(guiController, keyHandler);
		this.panel = new MapSelectPanel(this, maps);
		this.maps = maps;
	}

	@Override
	public void boost() {
		this.panel.focusPrevButton();
	}

	@Override
	public void left() {
		guiController.cancel();
	}

	@Override
	public void right() {
		JButton b = this.panel.getSelectedButton();
		b.setEnabled(true);
		b.doClick();
		b.setEnabled(false);
	}

	@Override
	public void shootNormal() {
		this.panel.focusNextButton();
	}

	@Override
	public void shootSpecial() {}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case MapSelectPanel.RETURN_ACTION:
			guiController.cancel();
			break;
		default:
			for(Map m : maps){
				if(m.getName().equals(e.getActionCommand())){
					guiController.selectMap(m);
					panel.selectMap(m);
					break;
				}
			}
			break;
		}
	}

	@Override
	public JPanel getView() {
		return this.panel;
	}

}
