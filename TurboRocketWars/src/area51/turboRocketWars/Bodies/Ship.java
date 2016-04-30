package area51.turboRocketWars.Bodies;

import static java.awt.Color.black;
import static java.awt.Color.blue;
import static java.awt.Color.cyan;
import static java.awt.Color.darkGray;
import static java.awt.Color.gray;
import static java.awt.Color.green;
import static java.awt.Color.lightGray;
import static java.awt.Color.magenta;
import static java.awt.Color.orange;
import static java.awt.Color.pink;
import static java.awt.Color.red;
import static java.awt.Color.yellow;

import java.awt.Color;
import java.util.ArrayList;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.cannons.Cannon;
import area51.turboRocketWars.Bodies.seperator.B2Separator;
import area51.turboRocketWars.Bodies.shots.Bomb;
import area51.turboRocketWars.Bodies.shots.NormalShot;
import area51.turboRocketWars.Bodies.shots.NormalShot.NormalShotFactory;
import area51.turboRocketWars.Bodies.shots.Seeker;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.listeners.KeyExecutor;
import area51.turboRocketWars.regeneration.AmmoRegen;
import area51.turboRocketWars.regeneration.HPRegen;
import area51.turboRocketWars.settings.SettingsFinal;
import static area51.turboRocketWars.settings.SettingsEditable.*;

public class Ship implements KeyExecutor, Runnable {

	private static Vec2[] shapeVectors = new Vec2[] { new Vec2(-4f, -5f),
			new Vec2(0f, -2f), new Vec2(4f, -5f), new Vec2(0f, 5f) };
	private Vec2 boostVec = new Vec2(0, 50);

	private int lives = ShipLives();
	private final double maxHitPoints = ShipMaxHP();
	private double hitPoints = maxHitPoints;

	private String id;
	private String type;
	private volatile Body body;
	private volatile World world;
	private double regenHP = 1;
	private int regenAmmo = 1;
	private final int maxAmmo = 40;
	private int ammo = maxAmmo;
	@SuppressWarnings("rawtypes")
	private Cannon cannonStd;
	@SuppressWarnings("rawtypes")
	private Cannon cannon1;

	private boolean isRotationLocked = false;
	private boolean isWinner = false;

	private Vec2 spawnPoint;

	public static final ArrayList<Ship> ships = new ArrayList<Ship>();

	private long timeOfLastShot = 0;
	private long timeOfLastRegen = 0;
	private long hpRegenFrequence = HpRegenSpeed(); // msec until next regen
	private long ammoRegenFrequence = AmmoRegenSpeed();

	private HPRegen hpRegenerator = null;
	private AmmoRegen ammoRegen = null;

	public Ship(World world, Vec2 position) {
		this.spawnPoint = position;
		this.world = world;
		this.body = getNewBody(position.clone(), world);

		this.cannonStd = new Cannon<NormalShot>(new NormalShotFactory(), 1,
				true, 200, world, this);
		this.cannon1 = new Cannon<Seeker>(new Seeker.SeekerFactory(), 1, true, 1700,
				world, this);
		ships.add(this);

	}

	private Body getNewBody(Vec2 pos, World world) {
		BodyDef bodyDef = new BodyDef();

		bodyDef.type = BodyType.DYNAMIC;

		System.out.println("pos: " + pos);
		bodyDef.setPosition(pos.clone());
		Body body = null;

		body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 2;
		fixtureDef.friction = 0.3f;

		B2Separator.seperate(body, fixtureDef, shapeVectors, 1);
		body.setUserData(new UserDataProp(SettingsFinal.USER_DATA_SHIP,
				getNextColor(), 1, true, this));
		return body;
	}

	public void setCannonNormal(@SuppressWarnings("rawtypes") Cannon cannon) {
		this.cannonStd = cannon;
	}

	public void setCannonSpecial(@SuppressWarnings("rawtypes") Cannon cannon) {
		this.cannon1 = cannon;
	}

	public void lockRotation() {
		isRotationLocked = true;
	}

	public void unlockRotation() {
		isRotationLocked = false;
	}

	public String getId() {
		return this.id;
	}

	public int getMaxAmmo() {
		return this.maxAmmo;
	}

	public int getAmmoCount() {
		return this.ammo;
	}

	public String getType() {
		return this.type;
	}

	public Body getBody() {
		return this.body;
	}

	private boolean checkTime(long tolerance) {
		long newTime = System.currentTimeMillis();
		if (newTime - timeOfLastShot > tolerance) {
			timeOfLastShot = newTime;
			return true;
		}
		return false;
	}

	public void shootNormal() {
		if (this.lives <= 0)
			return;
		if (checkTime(cannonStd.getReloadTime()))
			cannonStd.fire();
	}

	public void shootSpecial() {
		if (this.lives <= 0)
			return;
		if (checkTime(cannon1.getReloadTime()))
			cannon1.fire();
	}

	public boolean hasAmmo(int amount) {
		if (this.ammo >= amount)
			return true;
		return false;
	}

	public void boost() {
		if (this.lives <= 0)
			return;
		float angle = body.m_sweep.a;
		body.setAngularVelocity(0); // easier to fly
		Mat22 m = Mat22.createRotationalTransform(angle);
		body.applyLinearImpulse(m.mul(boostVec), body.getPosition(), true);

	}

	public void left() {
		if (this.lives <= 0)
			return;
		if (isRotationLocked)
			return;
		body.setAngularVelocity(0);
		body.m_sweep.a += 0.1;
	}

	public void right() {
		if (this.lives <= 0)
			return;
		if (isRotationLocked)
			return;
		body.setAngularVelocity(0);
		body.m_sweep.a -= 0.1;
	}

	public double getMaxHitPoints() {
		return this.maxHitPoints;
	}

	public double getCurHitPoints() {
		return this.hitPoints;
	}

	public void regenerateHP() {
		if (hpRegenerator == null || hpRegenerator.hasStopped()) {
			hpRegenerator = new HPRegen(this);
		}
		if (hitPoints < maxHitPoints) {
			this.hitPoints += regenHP;
			if (hitPoints > maxHitPoints)
				hitPoints = maxHitPoints;
		}
	}

	public void regenerateAmmo() {
		if (ammoRegen == null || ammoRegen.hasStopped()) {
			ammoRegen = new AmmoRegen(this);
		}
		if (ammo < maxAmmo) {
			this.ammo += regenAmmo;
			if (ammo > maxAmmo)
				ammo = maxAmmo;
		}
	}

	public void stopRegenerateAmmo() {
		if (ammoRegen != null) {
			ammoRegen.stop();
			ammoRegen = null;
		}
	}

	public void stopRegenerateHP() {
		if (hpRegenerator != null) {
			hpRegenerator.stop();
			hpRegenerator = null;
		}
	}

	public long getHPRegenFrequency() {
		return this.hpRegenFrequence;
	}

	public long getAmmoRegenFrequency() {
		return this.ammoRegenFrequence;
	}

	public int getLives() {
		return this.lives;
	}

	public void attack(double dmg) {
		this.hitPoints -= dmg;
	}

	public Vec2 getSpawPoint() {
		return this.spawnPoint.clone();
	}

	public void setSpawnPoint(Vec2 point) {
		this.spawnPoint = point.clone();
		respawn();
	}

	public void die() {
		this.lives -= 1;
		if (this.lives <= 0) {
			ships.remove(this);
			this.lives = 0;
			this.hitPoints = 0;
			return;
		}
		respawn();
	}

	private void respawn() {
		world.destroyBody(body);
		body = getNewBody(spawnPoint.clone(), world);
		this.hitPoints = maxHitPoints;
		this.ammo = maxAmmo;
	}

	public boolean isWinner() {
		return this.isWinner;
	}

	public void setShipGameState() {

	}

	public void setWinner() {
		this.isWinner = true;
	}

	private static int curColor = 0;
	private static Color[] colors = new Color[] { black, red, blue, green,
			gray, cyan, lightGray, yellow, magenta, darkGray, orange, pink };

	private static Color getNextColor() {
		return colors[(curColor = (curColor + 1) % colors.length)];
	}

	public void useAmmo(int ammoCost) {
		this.ammo -= ammoCost;
	}

	public Object getHPRegenRatio() {
		return this.regenHP;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
