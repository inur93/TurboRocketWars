package area51.turboRocketWars.gui.controllers;

import static area51.turboRocketWars.settings.SettingsEditable.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;

import area51.turboRocketWars.gui.views.SettingsPanel;
import area51.turboRocketWars.listeners.KeyHandler;
import area51.turboRocketWars.settings.Setting;

public class SettingsController extends ViewController {

	private HashMap<String, Setting> settings;

	private final String ammoSettings = "Ammonition settings";
	private final String shipSettings = "Ship settings";
	private final String mapSettings = "Map settings";
	private final String mainSettings = "Settings";
	
	private final String SAVE_AND_RETURN_ACTION = "Save and return";

	private SettingsPanel currentView = null;
	private final HashMap<String, SettingsPanel> settingViews = new LinkedHashMap<String, SettingsPanel>();


	public SettingsController(GUIController guiController, KeyHandler keyHandler) {
		super(guiController, keyHandler);
		settings = getAllSettings();
		ArrayList<String> list = new ArrayList<String>();
		list.add(ammoSettings);
		list.add(mapSettings);
		list.add(shipSettings);
//		list.add(mainSettings);

		SettingsPanel ammoView = new SettingsPanel(ammoSettings, getAmmoSettings().entrySet().iterator(), this, keyHandler, SAVE_AND_RETURN_ACTION);
		SettingsPanel mainView = (currentView = new SettingsPanel(mainSettings, list, this));
		SettingsPanel mapView = new SettingsPanel(mapSettings, getMapSettings().entrySet().iterator(), this, keyHandler, SAVE_AND_RETURN_ACTION);
		SettingsPanel shipView = new SettingsPanel(shipSettings, getShipSettings().entrySet().iterator(), this, keyHandler, SAVE_AND_RETURN_ACTION);
		
		settingViews.put(mainSettings, mainView);
		settingViews.put(ammoSettings, ammoView);
		settingViews.put(mapSettings, mapView);
		settingViews.put(shipSettings, shipView);
		
//		guiController.addView(ammoSettings, mainView);
		
	}

	@Override
	public void boost() {
		String value = this.currentView.getSelectedValue();
		if(value != null && !currentView.equals(settingViews.get(mainSettings)))
			settings.get(this.currentView.getSelectedButton().getText()).setValue(value);
		this.currentView.focusPrevButton();
	}

	@Override
	public void left() {}

	@Override
	public void right() {
		this.currentView.getSelectedButton().doClick();
	}

	@Override
	public void shootNormal() {
		String value = this.currentView.getSelectedValue();
		if(value != null && !currentView.equals(settingViews.get(mainSettings)))
			settings.get(this.currentView.getSelectedButton().getText()).setValue(value);
		this.currentView.focusNextButton();
	}

	@Override
	public void shootSpecial() {}

	@Override
	public void actionPerformed(ActionEvent e) {
		Iterator<Entry<String, Setting>> it = settings.entrySet().iterator();
		switch(e.getActionCommand()){
		case SettingsPanel.RETURN_ACTION:
			while(it.hasNext()){
				it.next().getValue().cancel();
			}
			if(currentView.equals(settingViews.get(mainSettings))){
				this.guiController.cancel();
			}else{
				this.currentView = settingViews.get(mainSettings);
				this.guiController.updateView();
			}
			break;
		case SAVE_AND_RETURN_ACTION:
			while(it.hasNext()){
				it.next().getValue().save();
			}
			if(currentView.equals(settingViews.get(mainSettings))){
				this.guiController.cancel();
			}else{
				this.currentView = settingViews.get(mainSettings);
				this.guiController.updateView();
			}
			break;
		default:
			SettingsPanel panel = settingViews.get(e.getActionCommand());
			if(panel != null){
				this.currentView = panel;
				this.guiController.updateView();
			}else{
				System.out.println("unknown command in settings controller. Command: " + e.getActionCommand());
			}
		}
	}

	@Override
	public JPanel getView() {
		System.out.println("current view: " + currentView);
		return currentView;
	}

}
