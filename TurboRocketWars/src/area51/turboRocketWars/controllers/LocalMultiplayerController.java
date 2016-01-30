package area51.turboRocketWars.controllers;

import javax.swing.JPanel;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.gui.MainGamePanel;
import area51.turboRocketWars.gui.MainWindow;
import area51.turboRocketWars.gui.impl.MainGamePanelImpl;
import area51.turboRocketWars.gui.impl.MainWindowImpl;
import area51.turboRocketWars.settings.KeyBoardConfigurations;

public class LocalMultiplayerController implements Runnable{

	private MainWindow window;
	private World world;
	private JPanel[] panels = new JPanel[2];
	public LocalMultiplayerController() {
		window = new MainWindowImpl();
		world = new World(new Vec2(0, -10));
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0, 0);
		Body groundBody = world.createBody(groundBodyDef);
		ChainShape groundBox = new ChainShape();
//		PolygonShape groundBox = new PolygonShape();
//		groundBox.setAsBox(100, 50);
		groundBox.createLoop(new Vec2[]{new Vec2(-150, 0), new Vec2(150, 0), new Vec2(150, 50), new Vec2(-150, 50)}, 4);
		
		groundBody.createFixture(groundBox, 0);
		Ship ship1 = new Ship(world, new Vec2(10, 10));
		Ship ship2 = new Ship(world, new Vec2(50, 10));
		
		OBBViewportTransform camera1 = new OBBViewportTransform();
		OBBViewportTransform camera2 = new OBBViewportTransform();
		
		MainGamePanel panel1 = new MainGamePanelImpl(world, ship1, camera1);
		MainGamePanel panel2 = new MainGamePanelImpl(world, ship2, camera2);
		
		panels[0] = panel1.getPanel();
		panels[1] = panel2.getPanel();
		
		KeyHandler keyHandler1 = new KeyHandler(new KeyBoardConfigurations(KeyBoardConfigurations.ARROW_CONFIG), ship1);
		KeyHandler keyHandler2 = new KeyHandler(new KeyBoardConfigurations(KeyBoardConfigurations.WASD_CONFIG), ship2);
		
		window.addWindowPanel(panel1.getPanel(), panel2.getPanel());
//		window.addWindowPanel(panel2.getPanel());
		
		window.addKeyListener(keyHandler1);
		window.addKeyListener(keyHandler2);

		new Thread(this).start();
	}

	long oldTime;
	long newTime;
	public void run() {
		oldTime = System.currentTimeMillis();
		float timeStep = 1.0f/60.0f;
		while(true){
//			newTime = System.currentTimeMillis();
			world.step(timeStep, 6, 3);
			oldTime = newTime;
			for(JPanel p : panels) p.repaint();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new LocalMultiplayerController();
	}

}
