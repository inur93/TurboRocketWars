package area51.turboRocketWars.gui;

import javax.swing.JPanel;

import org.jbox2d.dynamics.World;

public interface MainGamePanel extends Runnable{

	void setWorld(World world);

	void grabFocus();

	/**
	 * Renders the world
	 * @return if the renderer is ready for drawing
	 */
	boolean render();

	/**
	 * Paints the rendered world to the screen
	 */
	void paintScreen();

	JPanel getPanel();

}
