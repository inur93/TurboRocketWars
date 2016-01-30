package area51.turboRocketWars.gui;

import java.awt.event.KeyListener;

import javax.swing.JPanel;

public interface MainWindow {

	void addWindowPanel(JPanel... panel);
	void clearWindow();
	void addKeyListener(KeyListener listener);
}
