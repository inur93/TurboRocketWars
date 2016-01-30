package area51.turboRocketWars.gui.impl;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import area51.turboRocketWars.gui.MainWindow;

public class MainWindowImpl extends JFrame implements MainWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<JPanel> panels = new ArrayList<JPanel>();
	public MainWindowImpl(){
		this.setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setVisible(true);
	}
	public void addWindowPanel(JPanel... panel) {
		clearWindow();
		for(int i = 0; i < panel.length; i++){
			JPanel p = panel[i];
			int pX = getWidth() - getWidth()/(i+1);
			int pY = 0;
			int pW = getWidth()/panel.length;
			int pH = getHeight();
			p.setBounds(pX, pY, pW, pH);
			getContentPane().add(p);
			panels.add(p);
		}
	}
	
	private void resizePanels(){
		for(int i = 0; i < panels.size(); i++){
			int pX = getWidth() - getWidth()/(i+1);
			int pY = 0;
			int pW = getWidth()/panels.size();
			int pH = getHeight();
			panels.get(i).setBounds(pX, pY, pW, pH);
		}
	}

	public void clearWindow() {
		for(JPanel p : panels) getContentPane().remove(p);
	}
}
