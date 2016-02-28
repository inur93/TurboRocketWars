package area51.turboRocketWars.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import area51.turboRocketWars.settings.KeyBoardConfigurations;

public abstract class KeyHandler implements KeyListener, ActionListener{

	protected KeyBoardConfigurations keyConfig;		
	protected KeyExecutor executor;

	public KeyHandler(KeyBoardConfigurations keyConfig, KeyExecutor executor) {
		this.keyConfig = keyConfig;
		this.executor = executor;
	}
	public void keyTyped(KeyEvent e) {}
	
	public abstract void keyPressed(KeyEvent e);
	
	public abstract void keyReleased(KeyEvent e);
	
	public abstract void actionPerformed(ActionEvent e);
	
	public void setKeyExecutor(KeyExecutor executor){
		this.executor = executor;
	}
	
	public KeyExecutor getKeyExecutor(){
		return this.executor;
	}
	
	public KeyBoardConfigurations getKeyBoardConfig(){
		return this.keyConfig;
	}
}
