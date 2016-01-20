package area51.turboRocketWars.gui;

public interface MainGamePanel extends Runnable{

		  public void grabFocus();

		  /**
		   * Renders the world
		   * @return if the renderer is ready for drawing
		   */
		  public boolean render();

		  /**
		   * Paints the rendered world to the screen
		   */
		  public void paintScreen();
		
}
