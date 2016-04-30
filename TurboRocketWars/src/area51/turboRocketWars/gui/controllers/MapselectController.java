package area51.turboRocketWars.gui.controllers;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import area51.turboRocketWars.Bodies.maps.Map;
import area51.turboRocketWars.gui.views.MapSelectPanel;
import area51.turboRocketWars.listeners.KeyExecutor;
import area51.turboRocketWars.listeners.KeyHandler;
import area51.turboRocketWars.settings.KeyBoardConfigurations;

public class MapselectController extends ViewController{

	private MapSelectPanel panel;
	private HashMap<String, Map> maps = new HashMap<String, Map>();

	public MapselectController(GUIController guiController, KeyHandler keyHandler, Map... maps) {
		super(guiController, keyHandler);
		String[] keys = new String[maps.length];
		for(int i = 0; i < maps.length; i++){
			keys[i] = maps[i].getName();
			this.maps.put(keys[i], maps[i]);
		}
		this.panel = new MapSelectPanel(this, keys);
		this.panel.selectMap(this.maps.get(keys[0]));
	}

	@Override
	public void boost() {
		String mapKey = this.panel.focusPrevButton();
		this.panel.showMap(maps.get(mapKey));
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
		String mapKey = this.panel.focusNextButton();
		this.panel.showMap(maps.get(mapKey));
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
			Map m = maps.get(e.getActionCommand());
			if(m != null){
				guiController.selectMap(m);
				panel.selectMap(m);
			}else {
				System.err.println("unknown action command. Expected map but no map was found with name: " + e.getActionCommand());
			}
			break;
		}
	}

	@Override
	public JPanel getView() {
		return this.panel;
	}

}
