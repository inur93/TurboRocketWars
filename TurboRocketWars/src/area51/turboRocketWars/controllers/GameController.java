package area51.turboRocketWars.controllers;

import static area51.turboRocketWars.settings.SettingsFinal.LANDING_ANGLE_TOLERANCE;
import static area51.turboRocketWars.settings.SettingsFinal.MAX_DAMAGE;
import static area51.turboRocketWars.settings.SettingsFinal.USER_DATA_MAP;
import static area51.turboRocketWars.settings.SettingsFinal.USER_DATA_PLATFORM;
import static area51.turboRocketWars.settings.SettingsFinal.USER_DATA_SHIP;
import static area51.turboRocketWars.settings.SettingsFinal.USER_DATA_SHOT;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.MathUtils;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.shots.Shot;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.settings.SettingsFinal;
import area51.turboRocketWars.tasks.Task;

public class GameController implements Runnable, ContactListener{

	private MainController mainController;
	private World world;
	private boolean paused = false;
	
	//TODO figure out a better way to handle this. should not be this many queues and lists
	public static Queue<Body> bodiesToDelete = new LinkedList<Body>();
	public Queue<Ship> shipsToKill = new LinkedList<Ship>();
	public static ArrayList<Shot> shots = new ArrayList<Shot>();
	public static Queue<Task> tasks = new LinkedList<Task>();

	public GameController(MainController mainController, World world) {
		this.mainController = mainController;
		this.world = world;
		this.world.setContactListener(this);
		new Thread(this).start();
	}

	public void run() {
		paused = false;
		while(!paused){
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

			while(!tasks.isEmpty()) tasks.remove().executeTask();

			if(Ship.ships.size() == 1){
				Ship.ships.get(0).setWinner();
				break;
			}


			try {
				Thread.sleep((long) (SettingsFinal.TIME_STEP*1000));
			} catch (InterruptedException e) {}
		}
	}
	
	public void pause(){
		this.paused = true;
	}
	
	public void resume(){
		if(paused) new Thread(this).start();
	}
	
	public boolean isPaused(){
		return this.paused;
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
			ship.regenerateAmmo();
			ship.regenerateHP();
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
		Ship shipA = identifyShip(a);
		Ship shipB = identifyShip(b);
		
		if(aData.bodyType.equals(USER_DATA_SHIP)){
//			System.out.println("endcontact: " + aData.bodyType + "==" + bData.bodyType);
			shipA.unlockRotation();
			shipA.stopRegenerateAmmo();
			shipA.stopRegenerateHP();
		}
		else if(bData.bodyType.equals(USER_DATA_SHIP)){
//			System.out.println("endcontact: " + aData.bodyType + "==" + bData.bodyType);
			shipB.unlockRotation();
			shipB.stopRegenerateAmmo();
			shipB.stopRegenerateHP();
		}
		
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
