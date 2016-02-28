package area51.turboRocketWars.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import area51.turboRocketWars.settings.KeyBoardConfigurations;

public class MenuKeyHandler extends KeyHandler{

	public MenuKeyHandler(KeyBoardConfigurations keyConfig, KeyExecutor executor) {
		super(keyConfig, executor);		
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
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
	public void keyReleased(KeyEvent e) {}
	public void actionPerformed(ActionEvent e) {}


}
