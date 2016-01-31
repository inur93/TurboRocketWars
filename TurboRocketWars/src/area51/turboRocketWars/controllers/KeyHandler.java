package area51.turboRocketWars.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Timer;

import org.jbox2d.dynamics.Body;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.settings.KeyBoardConfigurations;
import area51.turboRocketWars.settings.SettingsFinal;

public class KeyHandler implements KeyListener, ActionListener{

	private final KeyBoardConfigurations keyConfig;
	
	private volatile ArrayList<Integer> cmdQueue = new ArrayList<Integer>();
	private Ship ship;
	public KeyHandler(KeyBoardConfigurations keyConfig, Ship ship) {
		this.keyConfig = keyConfig;
		this.ship = ship;
		new Timer(SettingsFinal.KEY_UPDATE_SPEED, this).start();;
	}
	public void keyTyped(KeyEvent e) {	}
	
	public void keyPressed(KeyEvent e) {
		if(keyConfig.isValid(e)){
			if(!cmdQueue.contains(e.getKeyCode())){
				cmdQueue.add(e.getKeyCode());
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		cmdQueue.remove(new Integer(e.getKeyCode()));
	}
	public void actionPerformed(ActionEvent e) {
		for(int k : cmdQueue){
			if(k == keyConfig.BOOST){
				ship.boost();
			}else if(k == keyConfig.LEFT){
				ship.yawLeft();
			}else if(k == keyConfig.RIGHT){
				ship.yawRight();
			}else if(k == keyConfig.SHOT1){
				ship.shootStd();
			}else if(k == keyConfig.SHOT2){
				ship.shoot1();
			}else if(k == keyConfig.SHOT3){
				ship.shoot2();
			}
		}
	}

}
