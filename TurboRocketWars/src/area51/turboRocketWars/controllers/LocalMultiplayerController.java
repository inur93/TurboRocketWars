package area51.turboRocketWars.controllers;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.gui.MainGamePanel;
import area51.turboRocketWars.gui.MainWindow;
import area51.turboRocketWars.gui.impl.MainGamePanelImpl;
import area51.turboRocketWars.gui.impl.MainWindowImpl;
import area51.turboRocketWars.settings.KeyBoardConfigurations;
import area51.turboRocketWars.settings.SettingsFinal;

public class LocalMultiplayerController implements Runnable, ContactListener{

	private MainWindow window;
	private World world;
	private JPanel[] panels = new JPanel[2];
	Queue<Body> bodiesToDelete = new LinkedList<Body>();
	Queue<Ship> shipsToKill = new LinkedList<Ship>();
	public LocalMultiplayerController() {
		window = new MainWindowImpl();
		world = new World(new Vec2(0, -10));
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0, 0);
		Body groundBody = world.createBody(groundBodyDef);
		ChainShape groundBox = new ChainShape();
//		PolygonShape groundBox = new PolygonShape();
//		groundBox.setAsBox(100, 50);
		groundBox.createLoop(new Vec2[]{new Vec2(-250, 0), new Vec2(250, 0), new Vec2(250, 250), new Vec2(-250, 250)}, 4);
		
		groundBody.createFixture(groundBox, 0);
		groundBody.setUserData(new UserDataProp(SettingsFinal.USER_DATA_MAP, Color.YELLOW, 3, true));
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

		world.setContactListener(this);
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
			while(!bodiesToDelete.isEmpty()){
				world.destroyBody(bodiesToDelete.remove());
			}
			while(!shipsToKill.isEmpty()){
				Ship s = shipsToKill.remove();
				world.destroyBody(s.getBody());
				s.die();
			}
			if(Ship.ships.size() == 1){
				Ship.ships.get(0).setWinner();
			}
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

	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		if(((UserDataProp)a.getUserData()).bodyType.equals(SettingsFinal.USER_DATA_SHOT)){
			bodiesToDelete.add(a);
			if(((UserDataProp)b.getUserData()).bodyType.equals(SettingsFinal.USER_DATA_SHIP)){
				for(Ship s : Ship.ships){
					System.out.println(s.getCurHitPoints());
					if(b.equals(s.getBody())){
						s.attack(5);
						if(s.getCurHitPoints() <= 0){
//							bodiesToDelete.add(b);
							shipsToKill.add(s);
						}
					}
				}
			}
		}
		if(((UserDataProp)b.getUserData()).bodyType.equals(SettingsFinal.USER_DATA_SHOT)){
			bodiesToDelete.add(b);
			if(((UserDataProp)a.getUserData()).bodyType.equals(SettingsFinal.USER_DATA_SHIP)){
				for(Ship s : Ship.ships){
					if(a.equals(s.getBody())){
						s.attack(5);
						if(s.getCurHitPoints() <= 0){
//							bodiesToDelete.add(b);
							shipsToKill.add(s);
						}
					}
				}
			}
		}
	}

	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
