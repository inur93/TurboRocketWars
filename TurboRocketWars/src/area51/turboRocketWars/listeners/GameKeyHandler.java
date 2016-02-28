package area51.turboRocketWars.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import area51.turboRocketWars.settings.KeyBoardConfigurations;
import area51.turboRocketWars.settings.SettingsFinal;

public class GameKeyHandler extends KeyHandler{

	
	private volatile ArrayList<Integer> cmdQueue = new ArrayList<Integer>();
	public GameKeyHandler(KeyBoardConfigurations keyConfig, KeyExecutor executor) {
		super(keyConfig, executor);
		new Timer(SettingsFinal.KEY_UPDATE_SPEED, this).start();
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
				executor.boost();
			}else if(k == keyConfig.LEFT){
				executor.left();
			}else if(k == keyConfig.RIGHT){
				executor.right();
			}else if(k == keyConfig.SHOT_NORMAL){
				executor.shootNormal();
			}else if(k == keyConfig.SHOT_SPECIAL){
				executor.shootSpecial();
			}
		}
	}


}
