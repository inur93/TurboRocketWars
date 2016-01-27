package area51.turboRocketWars.gui.impl;

import java.awt.AWTError;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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

import area51.turboRocketWars.Bodies.userData.FixtureViewProperties;
import area51.turboRocketWars.gui.MainGamePanel;

public class MainGamePanelImpl extends JPanel implements MainGamePanel {

	public static final int SCREEN_DRAG_BUTTON = 3;

	public static final int INIT_WIDTH = 600;
	public static final int INIT_HEIGHT = 600;

	private World world;
	private Graphics2D dbg = null;
	private Image dbImage = null;

	private int panelWidth;
	private int panelHeight;
	private OBBViewportTransform camera;
	public volatile boolean stopped = false;

	public MainGamePanelImpl(World world, OBBViewportTransform camera) {
		this.world = world;
		this.camera = camera;
		setPreferredSize(new Dimension(300, 600));
		this.camera.setYFlip(true);
	}

	public void paintPolygon(Graphics2D g,Body body, PolygonShape poly){

		Vec2[] vertices = poly.getVertices();
		Polygon p = new Polygon();

		for(int i = 0; i <= poly.getVertexCount(); i++){						  
			Vec2 v1 = new Vec2(); 
			body.getWorldPointToOut((vertices[i%poly.getVertexCount()]), v1);
			camera.getWorldToScreen(v1, v1);
			p.addPoint((int) v1.x, (int) v1.y);
			
		}
		
//		System.out.println("drawing polygon");
		FixtureViewProperties viewProp = null;
		Object ud = body.getUserData();
		if(ud != null) viewProp = (FixtureViewProperties) ud;
		if(viewProp != null && viewProp.fill){
//			System.out.println("poly: " + p.xpoints);
			g.setStroke(new BasicStroke(viewProp.stroke));
			g.setColor(viewProp.color);
			g.fillPolygon(p);
		}
		else {
//			System.out.println("default polygon");
			g.setStroke(new BasicStroke(2));
			g.setColor(Color.black);
			g.drawPolygon(p);
		}
		
	}

	public void paintCircle(Graphics2D g, Body body, CircleShape circle){
		Vec2 pos = body.getPosition();
		for(int i = 0; i < circle.getVertexCount(); i++){
			System.out.println("drawing circle");
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
		
		FixtureViewProperties viewProp = null;
		if((viewProp = (FixtureViewProperties) body.getUserData()) != null){
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


	@Override
	protected synchronized void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		super.paintComponent(g);
		Body body = world.getBodyList();
		Image img = createImage(300, 600);
		g.drawImage(img, 0, 0, null);

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
	};



	public void run(){
		//		  while(!stopped){
		//			  
		//			  delay(30);
		//		  }
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
			//	      log.debug("dbImage is null, creating a new one");
			if (panelWidth <= 0 || panelHeight <= 0) {
				return false;
			}
			dbImage = createImage(panelWidth, panelHeight);
			if (dbImage == null) {
				//	        log.error("dbImage is still null, ignoring render call");
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
		//	    try {
		//	      Graphics g = this.getGraphics();
		//	      if ((g != null) && dbImage != null) {
		//	        g.drawImage(dbImage, 0, 0, null);
		//	        Toolkit.getDefaultToolkit().sync();
		//	        g.dispose();
		//	      }
		//	    } catch (AWTError e) {
		////	      log.error("Graphics context error", e);
		//	    }
	}

}
