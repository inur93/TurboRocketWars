package area51.turboRocketWars.gui.controllers;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Player;
import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.gui.views.MainGamePanel;
import area51.turboRocketWars.listeners.KeyHandler;
import area51.turboRocketWars.settings.SettingsFinal;

public class GameViewController extends ViewController{

	private MainGamePanel panel;
	private Player player;
	public GameViewController(GUIController guiController, KeyHandler keyHandler, World world, Player player, OBBViewportTransform camera) {
		super(guiController, keyHandler);
		System.out.println("new game view");
		this.player = player;
		this.panel = new MainGamePanel(world, player.getShip(), camera, SettingsFinal.CAMERA_ZOOM);
		
	}

	@Override
	public void boost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void left() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void right() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shootNormal() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shootSpecial() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public KeyHandler setActive() {
		this.keyHandler.setKeyExecutor(player.getShip());
		return this.player.getGameKeyHandler();
	};

	@Override
	public JPanel getView() {
		return this.panel;
	}

}
