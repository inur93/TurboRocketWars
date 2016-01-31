package area51.turboRocketWars.gui;

import java.awt.Image;

import javax.swing.JPanel;

public interface MainMenuPanel {
	void setBackground(Image img);
	void setBounds(int x, int y, int width, int height);
	JPanel getPanel();
}
