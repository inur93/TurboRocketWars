package area51.turboRocketWars.gui.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JPanel;

import area51.turboRocketWars.gui.MainMenuPanel;

public class MainMenuPanelImpl extends JPanel implements MainMenuPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image background;
	public MainMenuPanelImpl(){
//		setBackground(new Color(0, 0, 1f, 0.5f));
		setBounds(0, 0, 500, 500);
		JButton b = new JButton("come woooork");
		b.setBounds(400, 400, 400, 300);
		add(b);
		setVisible(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
	};

	public void setBackground(Image img) {
		this.background = img;
	}
	
	public JPanel getPanel(){
		return this;
	}
}
