package area51.turboRocketWars.controllers;

import static area51.turboRocketWars.settings.SettingsEditable.DEF_GRAVITY_X;
import static area51.turboRocketWars.settings.SettingsEditable.DEF_GRAVITY_Y;
import static area51.turboRocketWars.settings.SettingsFinal.LANDING_ANGLE_TOLERANCE;
import static area51.turboRocketWars.settings.SettingsFinal.MAX_DAMAGE;
import static area51.turboRocketWars.settings.SettingsFinal.USER_DATA_MAP;
import static area51.turboRocketWars.settings.SettingsFinal.USER_DATA_PLATFORM;
import static area51.turboRocketWars.settings.SettingsFinal.USER_DATA_SHIP;
import static area51.turboRocketWars.settings.SettingsFinal.USER_DATA_SHOT;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import area51.turboRocketWars.Bodies.Delegate;
import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.maps.Map;
import area51.turboRocketWars.Bodies.shots.Shot;
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
	
	//TODO figure out a better way to handle this. should not be this many queues and lists
	public static Queue<Body> bodiesToDelete = new LinkedList<Body>();
	public Queue<Ship> shipsToKill = new LinkedList<Ship>();
	public static ArrayList<Shot> shots = new ArrayList<Shot>();
	public static Queue<Delegate> delegates = new LinkedList<Delegate>();

	public LocalMultiplayerController() {
		window = new MainWindowImpl();
		world = new World(new Vec2(DEF_GRAVITY_X, DEF_GRAVITY_Y));

		Map.createDefaultMap(world);

		//TODO create convenience method in map to find ships starting positions
		Ship ship1 = new Ship(world, new Vec2(-390, 30));
		Ship ship2 = new Ship(world, new Vec2(410, 220));

		OBBViewportTransform camera1 = new OBBViewportTransform();
		OBBViewportTransform camera2 = new OBBViewportTransform();

		MainGamePanel panel1 = new MainGamePanelImpl(world, ship1, camera1);
		MainGamePanel panel2 = new MainGamePanelImpl(world, ship2, camera2);

		panels[0] = panel1.getPanel();
		panels[1] = panel2.getPanel();

		KeyHandler keyHandler1 = new KeyHandler(new KeyBoardConfigurations(KeyBoardConfigurations.ARROW_CONFIG), ship1);
		KeyHandler keyHandler2 = new KeyHandler(new KeyBoardConfigurations(KeyBoardConfigurations.WASD_CONFIG), ship2);

		window.addWindowPanel(panel1.getPanel(), panel2.getPanel());

		new Thread(panel1).start();
		new Thread(panel2).start();

		window.addKeyListener(keyHandler1);
		window.addKeyListener(keyHandler2);

		world.setContactListener(this);
		new Thread(this).start();
	}

	public void run() {

		while(true){
			world.step(SettingsFinal.TIME_STEP, SettingsFinal.velocityIterations, SettingsFinal.positionIterations);

			while(!bodiesToDelete.isEmpty()){
				world.destroyBody(bodiesToDelete.remove());
			}

			while(!shipsToKill.isEmpty()){
				Ship s = shipsToKill.remove();
				world.destroyBody(s.getBody());
				s.die();
			}
			
			removeTimedOutShots();

			while(!delegates.isEmpty()) delegates.remove().execute();

			if(Ship.ships.size() == 1){
				Ship.ships.get(0).setWinner();
				break;
			}


			try {
				Thread.sleep((long) (SettingsFinal.TIME_STEP*1000));
			} catch (InterruptedException e) {}
		}

		window.setMenu();
	}

	private void removeTimedOutShots() {
		Body parent = world.getBodyList();
		do{
			UserDataProp userData = (UserDataProp) parent.getUserData();
			Shot shot;
			if((shot = userData.shot) != null){
				if(shot.hasTimedOut()){
					shot.destroy();
				}
			}
		}while((parent = parent.getNext()) != null);
	}

	public static void main(String[] args) {
		new LocalMultiplayerController();
	}

	public Ship identifyShip(Body body){
		for(Ship s : Ship.ships){
			if(body.equals(s.getBody())){
				return s;
			}
		}
		return null;
	}
	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		UserDataProp aData = (UserDataProp) a.getUserData();
		UserDataProp bData = (UserDataProp) b.getUserData();
		
		switch(aData.bodyType){
		case USER_DATA_MAP:
			switch(bData.bodyType){
			case USER_DATA_SHIP:
				mapShipCollision(identifyShip(b), contact);
				break;
			case USER_DATA_SHOT:
				mapShotCollision(bData.shot, contact);
				break;
			default:
				System.err.println("collision not defined: " + aData.bodyType + " colliding " + bData.bodyType);
				break;
			}
			break;
		case USER_DATA_PLATFORM:
			switch(bData.bodyType){
			case USER_DATA_SHIP:
				attemptLanding(identifyShip(b), a);
				break;
			case USER_DATA_SHOT:
				mapShotCollision(bData.shot, contact);
				break;
			default:
				System.err.println("collision not defined: " + aData.bodyType + " colliding " + bData.bodyType);
				break;
			}
			break;
		case USER_DATA_SHIP:
			switch(bData.bodyType){
			case USER_DATA_SHIP:
				shipCollidingShip(identifyShip(a), identifyShip(b));
				break;
			case USER_DATA_SHOT:
				shotCollidingShip(identifyShip(a), bData.shot);
				break;
			case USER_DATA_PLATFORM:
				System.out.println("landing.... ship->platform");
				attemptLanding(identifyShip(a), b);
				break;
			case USER_DATA_MAP:
				mapShipCollision(identifyShip(a), contact);
				break;
			default:
				System.err.println("collision not defined: " + aData.bodyType + " colliding " + bData.bodyType);
				break;
			}
			break;
		case USER_DATA_SHOT:
			switch(bData.bodyType){
			case USER_DATA_SHIP:
				shotCollidingShip(identifyShip(b), aData.shot);
				break;
			case USER_DATA_SHOT:
				shotCollidingShot(aData.shot, bData.shot);
				break;
			case USER_DATA_MAP:
			case USER_DATA_PLATFORM:
				mapShotCollision(aData.shot, contact);
				break;
			default:
				System.err.println("collision not defined: " + aData.bodyType + " colliding " + bData.bodyType);
				break;
			}
			break;
		}
		
		for(Ship s : Ship.ships){
			if(s.getCurHitPoints() < 0){
				shipsToKill.add(s);
			}
		}
	}

	private void shotCollidingShot(Shot shota, Shot shotb) {
		bodiesToDelete.add(shota.getBody());
		bodiesToDelete.add(shotb.getBody());
	}

	private void shotCollidingShip(Ship ship, Shot shot) {
		ship.attack(shot.getDamage());
		bodiesToDelete.add(shot.getBody());
	}

	private void shipCollidingShip(Ship shipa, Ship shipb) {
		shipa.attack(MAX_DAMAGE);
		shipb.attack(MAX_DAMAGE);
	}

	private void attemptLanding(Ship ship, Body a) {
		float angle = Math.abs(ship.getBody().getAngle()%MathUtils.TWOPI);
		if(angle < LANDING_ANGLE_TOLERANCE || angle > MathUtils.TWOPI-LANDING_ANGLE_TOLERANCE){
			//TODO LAND
			ship.lockRotation();
		}else{
			ship.attack(MAX_DAMAGE);
		}
	}

	private void mapShotCollision(Shot shot, Contact contact) {
		bodiesToDelete.add(shot.getBody());
	}

	private void mapShipCollision(Ship ship, Contact contact) {
		ship.attack(MAX_DAMAGE);
	}

	public void endContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		UserDataProp aData = (UserDataProp) a.getUserData();
		UserDataProp bData = (UserDataProp) b.getUserData();
		if(aData.bodyType.equals(USER_DATA_SHIP)){
//			System.out.println("endcontact: " + aData.bodyType + "==" + bData.bodyType);
			identifyShip(a).unlockRotation();
		}
		else if(bData.bodyType.equals(USER_DATA_SHIP)){
//			System.out.println("endcontact: " + aData.bodyType + "==" + bData.bodyType);
			identifyShip(b).unlockRotation();
		}
		
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
