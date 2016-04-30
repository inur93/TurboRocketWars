package area51.turboRocketWars.Bodies.maps;

import java.awt.Color;
import java.awt.Dimension;

import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.maps.objects.Entity;
import area51.turboRocketWars.Bodies.maps.objects.Island;
import area51.turboRocketWars.Bodies.maps.objects.Rock;
import area51.turboRocketWars.gui.MainWindow;
import area51.turboRocketWars.gui.views.MainGamePanel;
import area51.turboRocketWars.settings.SettingsFinal;

public class Map {

	private static int curSpawnIndex = 0;
	private Vec2[] spawnPoints;
	private World world;
	private float width;
	private float height;
	private String name;
	private Vec2 pos;
	private Platform[] platforms;
	private Entity[] entities;
	
	public static Map createRocksMap(World world){	
		Platform[] platforms = new Platform[]{
				new Platform(50, 0),
				new Platform(800, 200)
		};		
		
		return new Map("shooting rocks ", world, new Vec2(0, 0), 1000, 500, platforms, 
				new Rock(new Vec2(100, 100), Color.gray, true, 1, 3, 0, true),
				new Rock(new Vec2(750, 250), Color.gray.darker(), true, 1, 3, 0, true),
				new Island(new Vec2(800, 200), Color.orange.darker(), true, 1));
	}
	
	public static Map createDefaultMap(World world){	
		Platform[] platforms = new Platform[]{
				new Platform(50, 0),
				new Platform(800, 200)
		};		
		
		return new Map("default map", world, new Vec2(0, 0), 1000, 500, platforms, 
				new Island(new Vec2(800, 200), Color.orange.darker(), true, 1));
	}

	public Map(String name, World world, Vec2 pos, float width, float height, Platform[] platforms, Entity... entities) {
		this.name = name;
		this.world = world;
		this.width = width;
		this.height = height;
		this.pos = pos;
		this.platforms = platforms;
		this.spawnPoints = new Vec2[platforms.length];
		for(int i = 0; i < platforms.length; i++) spawnPoints[i] = platforms[i].getSpawnPoint();
		this.entities = entities;
	}
	
	public Map create(World world){
		new BaseMap(pos, Color.green, false, 3, width, height).create(world);
		if(entities != null) for(Entity e : entities) e.create(world);
		for(Platform p : platforms) p.create(world);
		return this;
	}
	
	public String getName(){ return this.name;}
	public float getWidth(){ return this.width;}
	public float getHeight(){ return this.height;}

	public Vec2 getNextSpawnPoint(){
		return spawnPoints[curSpawnIndex++%spawnPoints.length];
	}
	
	public int getNumSpawnPoints(){
		return spawnPoints.length;
	}
	
	public void addEntity(Entity entity){
		entity.create(world);
	}
	
	public void preview(MainWindow window, int width, int height, boolean showStatic){
		Ship s = new Ship(world, new Vec2(this.width/2, this.height/2));
		s.getBody().setActive(false);
		MainGamePanel p = new MainGamePanel(world, null, new OBBViewportTransform(), 1.8f);
		preview(p, width, height, showStatic);
	}
	public void preview(MainGamePanel panel, int width, int height, boolean showStatic){

		panel.setWorld(this.create(world).world, width/this.width, this.width/2, this.height/2);
		panel.setPreferredSize(new Dimension(width, height));
		
		// for test
		if(!showStatic)
		for(int i = 0; i < 4000; i++){
			delay(100);
			world.step(0.1f, SettingsFinal.velocityIterations, SettingsFinal.positionIterations);
		}

	}
	
	public void delay(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {}
	}
	
	public static void main(String[] args) {
		Map.createDefaultMap(new World(new Vec2(0, -10))).preview(new MainWindow(),-1, -1, false);
		
	}
}
