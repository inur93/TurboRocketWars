package area51.turboRocketWars.gui.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;

import javax.swing.JPanel;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.Ship;
import area51.turboRocketWars.Bodies.userData.UserDataProp;
import area51.turboRocketWars.gui.MainGamePanel;
import area51.turboRocketWars.settings.SettingsFinal;

public class MainGamePanelImpl extends JPanel implements MainGamePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int WIN_LOOSE_FONT_SIZE = 140;

	public static final int SCREEN_DRAG_BUTTON = 3;
	public static final String HEART_ASCII_CODE = "\u2764";

	private final static int BAR_LENGTH = 100;
	private final static int BAR_THICKNESS = 10;

	private final static int TXT_COL1_OFFSET = 15;
	private final static int TXT_ROW1_OFFSET = 15;

	private final static int TXT_COL2_OFFSET = 60;
	private final static int TXT_ROW2_OFFSET = 35;

	private final static int TXT_COL3_OFFSET = 180;
	private final static int TXT_ROW3_OFFSET = 35;

	private final static int TXT_COL4_OFFSET = 240;
	private final static int TXT_ROW4_OFFSET = 35;

	private volatile World world;
	private volatile Ship ship;
	private Graphics2D dbg = null;
	private Image dbImage = null;

	private int panelWidth;
	private int panelHeight;
	private OBBViewportTransform camera;
	public volatile boolean stopped = false;



	public MainGamePanelImpl(World world, Ship ship, OBBViewportTransform camera) {
		this.ship = ship;
		this.world = world;
		this.camera = camera;
		this.camera.setYFlip(true);
		setBackground(Color.black);
	}

	public void setWorld(World world){
		this.world = world;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		camera.setExtents(width/2, height/2);
	};

	public void paintPolygon(Graphics2D g,Body body, PolygonShape poly){

		Vec2[] vertices = poly.getVertices();
		Polygon p = new Polygon();

		for(int i = 0; i <= poly.getVertexCount(); i++){						  
			Vec2 v1 = new Vec2(); 
			body.getWorldPointToOut((vertices[i%poly.getVertexCount()]), v1);
			camera.getWorldToScreen(v1, v1);
			p.addPoint((int) v1.x, (int) v1.y);

		}
		UserDataProp viewProp = null;
		Object ud = body.getUserData();
		if(ud != null) viewProp = (UserDataProp) ud;
		if(viewProp != null && viewProp.fill){
			g.setStroke(new BasicStroke(viewProp.stroke));
			g.setColor(viewProp.color);
			g.fillPolygon(p);
		}
		else {
			g.setStroke(new BasicStroke(2));
			g.setColor(Color.black);
			g.drawPolygon(p);
		}

	}

	public void paintCircle(Graphics2D g, Body body, CircleShape circle){
		Vec2 pos = body.getPosition();
		for(int i = 0; i < circle.getVertexCount(); i++){
			Vec2 v1 = circle.getVertex(i%circle.getVertexCount());
			Vec2 v2 = circle.getVertex((i+1)%circle.getVertexCount());
			camera.getWorldToScreen(pos.add(v1), v1);
			camera.getWorldToScreen(pos.add(v2), v2);
			g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
		}
		Vec2 v2 = new Vec2(); 		
		camera.getWorldToScreen(pos, v2);

		g.drawOval((int) v2.x, (int) v2.y, (int) circle.getRadius(), (int) circle.getRadius());
	}

	private void paintChain(Graphics2D g, Body body, ChainShape chain) {

		UserDataProp viewProp = null;
		if((viewProp = (UserDataProp) body.getUserData()) != null){
			g.setColor(viewProp.color);
			g.setStroke(new BasicStroke(viewProp.stroke));
		}else{

		}
		for(int i = 1; i < chain.m_count; i++){
			Vec2 v1 = new Vec2();
			Vec2 v2 = new Vec2();	

			body.getWorldPointToOut(chain.m_vertices[i-1], v1);
			body.getWorldPointToOut(chain.m_vertices[i], v2);

			camera.getWorldToScreen(v1, v1);
			camera.getWorldToScreen(v2, v2);

			g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
		}
	}

	private void paintEdge(Graphics2D g, Body body, EdgeShape edge){
		Vec2 v1 = new Vec2();
		Vec2 v2 = new Vec2();
		body.getWorldPointToOut(edge.m_vertex1, v1);
		body.getWorldPointToOut(edge.m_vertex2, v2);

		camera.getWorldToScreen(v1, v1);
		camera.getWorldToScreen(v2, v2);
		g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
	}

	private void drawTestGrid(Graphics2D g){

		for(int i = 0; i<40; i++){
			if(i%2 > 0) {
				g.setColor(Color.red.darker());
			}else{
				g.setColor(Color.blue.darker());
			}
			Vec2 v1 = new Vec2(-500, i*30);
			Vec2 v2 = new Vec2(500, i*30);

			Vec2 v3 = new Vec2(-500+i*30, -500);
			Vec2 v4 = new Vec2(-500+i*30, 500);

			camera.getWorldToScreen(v1, v1);
			camera.getWorldToScreen(v2, v2);

			camera.getWorldToScreen(v3, v3);
			camera.getWorldToScreen(v4, v4);

			g.drawLine((int)v1.x, (int)v1.y, (int) v2.x,(int)v2.y);
			g.drawLine((int)v3.x, (int)v3.y, (int) v4.x,(int)v4.y);

		}
	}

	@Override
	protected synchronized void paintComponent(Graphics graphics) {

		Graphics2D g = (Graphics2D) graphics;

		super.paintComponent(g);
		if(world == null) return;

		drawTestGrid(g);

		Body body = world.getBodyList();

		drawHPBar(g);

		dawLivesInfo(g);

		drawAmmoInfo(g);

		if(ship.getLives() <= 0){
			g.setFont(new Font(Font.SERIF, Font.BOLD, WIN_LOOSE_FONT_SIZE));
			g.drawString("GAME OVER", 50, getHeight()/2);;
		}else
			if(ship.isWinner()){
				g.setFont(new Font(Font.SERIF, Font.BOLD, WIN_LOOSE_FONT_SIZE));
				g.drawString("YOU WIN", 50, getHeight()/2);
			}
		if(ship.getLives() > 0){
			camera.setCamera(ship.getBody().getPosition().x, ship.getBody().getPosition().y, SettingsFinal.CAMERA_ZOOM);
		} else{
			camera.setCamera(ship.getSpawPoint().x, ship.getSpawPoint().y, 5);
		}
		do{
			if(body == null) break;
			Fixture fix = body.getFixtureList();
			do{
				if(fix == null) break;
				Shape shape = fix.getShape();
				switch(shape.getType()){
				case CHAIN:
					paintChain(g, body, (ChainShape) shape);
					break;
				case CIRCLE:
					paintCircle(g, body, (CircleShape) shape); 
					break;
				case EDGE:
					paintEdge(g, body, (EdgeShape) shape);
					break;
				case POLYGON:
					paintPolygon(g, body, (PolygonShape) shape); 
					break;
				}
			}while((fix = fix.getNext()) != null);	
		}while((body = body.getNext()) != null);
	}

	private void drawAmmoInfo(Graphics2D g) {

		double ratio = BAR_LENGTH/ship.getMaxHitPoints();
		double hp = ship.getCurHitPoints();

		g.setStroke(new BasicStroke(BAR_THICKNESS));
		g.setFont(new Font(TOOL_TIP_TEXT_KEY, Font.BOLD, 12));
		g.setColor(Color.red);

		int BAR_Y = TXT_ROW1_OFFSET-BAR_THICKNESS/2;
		g.drawString("Ammo: ", TXT_COL3_OFFSET, TXT_ROW1_OFFSET);

		g.drawLine(	TXT_COL4_OFFSET, 			BAR_Y, 
					TXT_COL4_OFFSET+BAR_LENGTH, BAR_Y);

		g.setColor(Color.yellow);
		if(hp > 0){
			g.drawLine(	TXT_COL4_OFFSET, 					BAR_Y,
						TXT_COL4_OFFSET+(int)(hp *ratio) , 	BAR_Y);
		}
	}

	private void dawLivesInfo(Graphics2D g) {
		g.setColor(Color.red);
		g.drawString("Lives: ", TXT_COL1_OFFSET, TXT_ROW2_OFFSET);
		g.setColor(Color.green);
		String lives = "";
		for(int i = 0; i < ship.getLives(); i++){
			lives += HEART_ASCII_CODE;
		}
		g.drawString(lives, TXT_COL2_OFFSET, TXT_ROW2_OFFSET);
	}

	private void drawHPBar(Graphics2D g){
		// draw hitpoint bar hpleft/hptotal and text
		double ratio = BAR_LENGTH/ship.getMaxHitPoints();
		double hp = ship.getCurHitPoints();
		
		g.setStroke(new BasicStroke(BAR_THICKNESS));
		g.setFont(new Font(TOOL_TIP_TEXT_KEY, Font.BOLD, 12));
		g.setColor(Color.red);
		int BAR_Y = TXT_ROW1_OFFSET-BAR_THICKNESS/2;

		g.drawString("HP: ", TXT_COL1_OFFSET, TXT_ROW1_OFFSET);

		g.drawLine(	TXT_COL2_OFFSET, 			BAR_Y, 
					TXT_COL2_OFFSET+BAR_LENGTH, BAR_Y);

		g.setColor(Color.green);
		if(hp > 0){
			g.drawLine(	TXT_COL2_OFFSET, 					BAR_Y,
						TXT_COL2_OFFSET+(int)(hp *ratio) , 	BAR_Y);
		}
	}

	public void delay(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {}
	}

	public Graphics2D getDBGraphics() {
		return dbg;
	}


	public boolean render() {
		if (dbImage == null) {
			if (panelWidth <= 0 || panelHeight <= 0) {
				return false;
			}
			dbImage = createImage(panelWidth, panelHeight);
			if (dbImage == null) {
				return false;
			}
			dbg = (Graphics2D) dbImage.getGraphics();
			dbg.setFont(new Font("Courier New", Font.PLAIN, 12));
		}
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, panelWidth, panelHeight);
		return true;
	}

	public synchronized void paintScreen() {
		repaint();
	}

	public JPanel getPanel() {
		return this;
	}

	public void run() {
		while(true){
			repaint();
			try {
				Thread.sleep(SettingsFinal.GRAPHICS_UPDATE_RATE);
			} catch (InterruptedException e) {}
		}
	}

}
