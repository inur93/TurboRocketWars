package area51.turboRocketWars.settings;

import java.util.HashMap;
import java.util.LinkedHashMap;

import area51.turboRocketWars.exceptions.SettingTypeException;

public class SettingsEditable {


	// map setting names
	private static String GRAVITY_Y_NAME = "Gravity, vertical";// -9.82f;
	private static String GRAVITY_X_NAME = "Gravity horizontal"; //0;
	private static String NUM_PLAYERS_NAME = "Number of players"; //2;

	// ship setting names
	private static String SHIP_LIVES_NAME = "Player Lives"; //5;
	private static String SHIP_MAX_HITPOINTS_NAME = "Ship maximum hitpoints"; //100;
	private static String HP_REGEN_SPEED_NAME = "HP regeneration frequency (1/ms)"; //100;
	private static String AMMO_REGEN_SPEED_NAME = "Ammo regeneration frequency (1/ms)"; //150;

	// ammo properties names
	private static String SHOT_DURATION_TIME_NAME = "Shot duration";
	private static String BOMB_AMMO_COST_NAME = "Bomb ammo cost";
	private static String BOMB_TIME_TO_DETONATE_NAME = "Bomb detonate time";
	private static String BOMB_NUMBER_FRAGMENTS_NAME = "Number of fragments when exploding";
	private static String BOMB_DAMAGE_NAME = "Bomb damage before detonation";
	private static String SEEKER_TIME_BEFORE_SEEKING_NAME = "Seeker delay before seeking";
	private static String SUPER_SEEKER_NAME = "Use precicer seeker missile";
	
	// map setting values
	private static Float GRAVITY_Y = -9.82f;
	private static Float GRAVITY_X = 0f;
	private static Integer NUM_PLAYERS = 2;

	// ship setting values
	private static int SHIP_LIVES = 5;
	private static float SHIP_MAX_HITPOINTS = 100;
	private static long HP_REGEN_SPEED = 100;
	private static long AMMO_REGEN_SPEED = 150;

	// ammo properties names
	private static Long SHOT_DURATION_TIME = 6000l;
	private static Integer BOMB_AMMO_COST = 1;
	private static Long BOMB_TIME_TO_DETONATE = 1000l;
	private static Integer BOMB_NUMBER_FRAGMENTS = 20;
	private static Double BOMB_DAMAGE = 20d;
	private static Long SEEKER_TIME_BEFORE_SEEKING = 1000l;
	private static Boolean SUPER_SEEKER = false;

	// regex
	private static String regExFloat = "-?(\\d+\\.)?\\d+(f|F)?";
	private static String regExInt = "-?\\d+";
	private static String regExLong = "\\d+(l|L)?";
	private static String regExPosInt = "\\d+";
	private static String regExPosFloat = "(\\d+\\.)?\\d+(f|F)?";
	private static String regExDouble = "(\\d+\\.)?\\d+(d|D)?";
	private static String regExBoolean = "true|false";

	private static boolean initialized = false;
	
	private static LinkedHashMap<String, Setting> ammoSettings;
	private static LinkedHashMap<String, Setting> shipSettings;
	private static LinkedHashMap<String, Setting> mapSettings;
	
	private static LinkedHashMap<String, Setting> settings;

	public static HashMap<String, Setting> getAllSettings(){
		if(!initialized) initialize();
		return settings;
	}
	
	public static LinkedHashMap<String, Setting> getAmmoSettings(){
		if(!initialized) initialize();
		return ammoSettings;
	}
	
	public static LinkedHashMap<String, Setting> getMapSettings(){
		if(!initialized) initialize();
		return mapSettings;
	}
	
	public static LinkedHashMap<String, Setting> getShipSettings(){
		if(!initialized) initialize();
		return shipSettings;
	}

	public static <T> T getSetting(String key, Class<T> c){
		if(!initialized) initialize();
		try{
			return settings.get(key).getValue(c);
		}catch(SettingTypeException e){
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		}
	}

	private static void initialize(){
		initialized = true;
		settings = new LinkedHashMap<String, Setting>();
		
		initializeAmmoSettings();
		initializeMapSettings();
		initializeShipSettings();
		
		settings.putAll(shipSettings);
		settings.putAll(ammoSettings);
		settings.putAll(mapSettings);
		}
	
	private static void initializeShipSettings(){
		shipSettings = new LinkedHashMap<String, Setting>();
		shipSettings.put(SHIP_LIVES_NAME, new Setting(SHIP_LIVES_NAME, SHIP_LIVES, regExPosInt));
		shipSettings.put(SHIP_MAX_HITPOINTS_NAME, new Setting(SHIP_MAX_HITPOINTS_NAME, SHIP_MAX_HITPOINTS, regExPosFloat));
		shipSettings.put(HP_REGEN_SPEED_NAME, new Setting(HP_REGEN_SPEED_NAME, HP_REGEN_SPEED, regExLong));
		
	}
	
	private static void initializeMapSettings(){
		mapSettings = new LinkedHashMap<String, Setting>();
		mapSettings.put(GRAVITY_Y_NAME, new Setting(GRAVITY_Y_NAME, GRAVITY_Y, regExFloat));
		mapSettings.put(GRAVITY_X_NAME, new Setting(GRAVITY_X_NAME, GRAVITY_X, regExFloat));
		mapSettings.put(NUM_PLAYERS_NAME, new Setting(NUM_PLAYERS_NAME, NUM_PLAYERS, regExPosInt));
	}
	
	private static void initializeAmmoSettings(){
		ammoSettings = new LinkedHashMap<String, Setting>();
		ammoSettings.put(SHOT_DURATION_TIME_NAME, new Setting(SHOT_DURATION_TIME_NAME, SHOT_DURATION_TIME, regExLong));
		ammoSettings.put(AMMO_REGEN_SPEED_NAME, new Setting(AMMO_REGEN_SPEED_NAME, AMMO_REGEN_SPEED, regExLong));		
		ammoSettings.put(BOMB_AMMO_COST_NAME, new Setting(BOMB_AMMO_COST_NAME, BOMB_AMMO_COST, regExInt));
		ammoSettings.put(BOMB_DAMAGE_NAME, new Setting(BOMB_DAMAGE_NAME, BOMB_DAMAGE, regExDouble));
		ammoSettings.put(BOMB_NUMBER_FRAGMENTS_NAME, new Setting(BOMB_NUMBER_FRAGMENTS_NAME, BOMB_NUMBER_FRAGMENTS, regExInt));
		ammoSettings.put(BOMB_TIME_TO_DETONATE_NAME, new Setting(BOMB_TIME_TO_DETONATE_NAME, BOMB_TIME_TO_DETONATE, regExLong));
		ammoSettings.put(SEEKER_TIME_BEFORE_SEEKING_NAME, new Setting(SEEKER_TIME_BEFORE_SEEKING_NAME, SEEKER_TIME_BEFORE_SEEKING, regExLong));
		ammoSettings.put(SUPER_SEEKER_NAME, new Setting(SUPER_SEEKER_NAME, SUPER_SEEKER, regExBoolean));
	}

	public static float GravityX(){return getSetting(GRAVITY_X_NAME, Float.class);}
	public static float GravityY(){return getSetting(GRAVITY_Y_NAME, Float.class);}
	public static int NumPlayers(){return getSetting(NUM_PLAYERS_NAME, Integer.class);}
	public static int ShipLives(){return getSetting(SHIP_LIVES_NAME, Integer.class);}
	public static float ShipMaxHP(){return getSetting(SHIP_MAX_HITPOINTS_NAME, Float.class);}
	public static long HpRegenSpeed(){return getSetting(HP_REGEN_SPEED_NAME, Long.class);}
	
	public static long ShotDuration(){return getSetting(SHOT_DURATION_TIME_NAME, Long.class);}
	public static long AmmoRegenSpeed(){return getSetting(AMMO_REGEN_SPEED_NAME, Long.class);}
	public static int BombAmmoCost(){return getSetting(BOMB_AMMO_COST_NAME, Integer.class);}
	public static long BombTimeToDetonate(){return getSetting(BOMB_TIME_TO_DETONATE_NAME, Long.class);}
	public static int BombNumberFragments(){return getSetting(BOMB_NUMBER_FRAGMENTS_NAME, Integer.class);}
	public static double BombDamage(){return getSetting(BOMB_DAMAGE_NAME, Double.class);}	
	public static long SeekerTimeBeforeSeeking(){return getSetting(SEEKER_TIME_BEFORE_SEEKING_NAME, Long.class);}
	public static boolean SuperSeeker(){return getSetting(SUPER_SEEKER_NAME, Boolean.class);}
}
