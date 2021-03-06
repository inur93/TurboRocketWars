//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.FlowLayout;
//import java.awt.event.KeyEvent;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import com.sun.java.swing.plaf.*;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import org.jbox2d.collision.AABB;
//import org.jbox2d.collision.shapes.ChainShape;
//import org.jbox2d.collision.shapes.CircleShape;
//import org.jbox2d.collision.shapes.EdgeShape;
//import org.jbox2d.collision.shapes.PolygonShape;
//import org.jbox2d.collision.shapes.Shape;
//import org.jbox2d.collision.shapes.ShapeType;
//import org.jbox2d.common.Mat22;
//import org.jbox2d.common.OBBViewportTransform;
//import org.jbox2d.common.Vec2;
//import org.jbox2d.dynamics.Body;
//import org.jbox2d.dynamics.BodyDef;
//import org.jbox2d.dynamics.BodyType;
//import org.jbox2d.dynamics.FixtureDef;
//import org.jbox2d.dynamics.World;
//import org.jbox2d.dynamics.joints.RevoluteJointDef;
//import org.jbox2d.pooling.arrays.IntArray;
//import org.jbox2d.pooling.arrays.Vec2Array;
//import org.jbox2d.serialization.JbDeserializer;
//import org.jbox2d.serialization.SerializationResult;
//import org.jbox2d.serialization.pb.PbDeserializer;
//
//import turboRocket.KeyPress;
//import area51.turboRocketWars.Bodies.Ship;
//import area51.turboRocketWars.Bodies.userData.FixtureViewProperties;
//import area51.turboRocketWars.gui.MainGamePanel;
//import area51.turboRocketWars.gui.impl.MainGamePanelImpl;
//import area51.turboRocketWars.maps.MapGenerator;
//
//
//
//public class TestMain implements Runnable {
//
//	private World world;
//	private Ship ship2;
//	private Ship ship1;
//	private MainGamePanel panel1;
//	private MainGamePanel panel2;
//	
//	private OBBViewportTransform camera1;
//	private OBBViewportTransform camera2;
//	public static void main(String[] args) {
//		new TestMain().run1();
//	}
//	
//	public void runProgram(){
//		// Setup world
//	    float timeStep = 1.0f/60.0f;
//	    int velocityIterations = 6;
//	    int positionIterations = 2;
//		// Run loop
//	   
//	    for (int i = 0; i < 6000; ++i) {
//	        world.step(timeStep, velocityIterations, positionIterations);
//	        world.clearForces();
//	        world.computeParticleCollisionEnergy();
//	        world.drawDebugData();
////	        System.out.println("step");
//	        Vec2  p1 = ship1.getBody().getPosition();
//	        Vec2  p2 = ship2.getBody().getPosition();
//	        camera1.setCamera(p1.x, p1.y, 10);
//	        camera2.setCamera(p2.x, p2.y, 10);
//	        System.out.println(ship1.getBody().getPosition());
//	        if(i < 100){
////	        ship1.getBody().applyForce(new Vec2(0, 1000), ship1.getBody().getPosition().add(new Vec2(30, 0)));
////	        ship1.getBody().applyForce(new Vec2(0, 1000), ship1.getBody().getPosition().add(new Vec2(-30, 0)));
////	        
//	        	ship1.getBody().m_sweep.a += 0.1f;
//	        float angle = ship1.getBody().getAngle();
//	        Mat22 mat = new Mat22(new Vec2((float) Math.cos(angle), (float) Math.sin(angle)) , new Vec2((float) -Math.sin(angle), (float) Math.cos(angle)));
//	        
//	        ship1.getBody().applyForceToCenter(mat.mul(new Vec2(0, 1000)));
//	        ship1.getBody().applyForce(new Vec2(10, 0), new Vec2(0, -5));
////	        world.
//	        }
////	        Vec2 position = body.getPosition();
////	        float angle = body.getAngle();
////	        System.out.println(position.x + "," + position.y + "," + angle);
//	        try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        panel1.paintScreen();
//	        panel2.paintScreen();
//	    }
//	}
//	public void setupGui(){
//		JFrame frame = new JFrame();
//		frame.setSize(650, 600);
//		frame.setLocationRelativeTo(null);
//		frame.setLayout(new FlowLayout());
//		
//		camera1 = new OBBViewportTransform();
//		camera1.setExtents(150, 300);
//		camera2 = new OBBViewportTransform();
//		camera2.setExtents(150, 300);
//		
//		panel1 = new MainGamePanelImpl(this.world, camera1);
//		panel2 = new MainGamePanelImpl(this.world, camera2);
//		
//		new Thread(panel1).start();
//		new Thread(panel2).start();
//		
////		flow.
////		frame.getLayout().addLayoutComponent("", (JPanel) panel1);
//		
////		frame.add(BorderLayout.LINE_START, (JPanel) panel1);
////		frame.add(BorderLayout.LINE_END, (JPanel) panel2);
//		frame.add((JPanel) panel1);
//		frame.add((JPanel) panel2);
//		
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
////		panel1.paintScreen();
////		panel2.paintScreen();
//		runProgram();
//		
//		
//	}
//	
//	public void run(){
//		InputStream is;
//		try {
//			is = s.getInputStream();
////			ObjectInputStream ois = new ObjectInputStream(is);
////			while(true)
//			System.out.println("reading: " + s.getLocalPort() + ";" + s.getPort());
////			SerializationResult res = (SerializationResult) ois.readObject();
//			JbDeserializer deSerialize = new PbDeserializer();
//			world = deSerialize.deserializeWorld(is);
//			System.out.println("world read" + world);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
////		catch (ClassNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//	}
//	Socket s = null;
//	public void begin(){
////		Socket s = null;
//		try {
//			s = new Socket("localhost", 5151);
//			OutputStream os = s.getOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(os);
//			oos.writeObject(new KeyPressData(KeyEvent.VK_UP, true));
//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
////		finally{
////			if(s != null)
////				try {
////					s.close();
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////		}
//		
//		System.out.println("start thread");
//		new Thread(this).start();
//		System.out.println("thread started");
////		world.getBodyList()
//		while(world == null)
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			};
//		System.out.println("setup gui");
//		setupGui();
//	}
//	public void run1(){
//	    // Static Body
//		{
//	    Vec2  gravity = new Vec2(0,-10);
//	    world = new World(gravity);
//	    
//	    BodyDef groundBodyDef = new BodyDef();
//	    groundBodyDef.position.set(10, 0);
//	    Body groundBody = world.createBody(groundBodyDef);
//	    
//	    PolygonShape groundBox = new PolygonShape();
//	    groundBox.setAsBox(100, 20);
//
//	    groundBody.createFixture(groundBox, 0);
//	    
//	    // Dynamic Body polygon
//	    ship1 = new Ship(world, new Vec2(-10, 20));
//	    ship2 = new Ship(world, new Vec2(10, 20));
//		}
//	    
//	    // dynamic body chain	
//	    BodyDef bodyDef1 = new BodyDef();
//	    bodyDef1.setType(BodyType.STATIC);
//
//	    bodyDef1.setPosition(new Vec2(30, 40));
//	    Body body1 = world.createBody(bodyDef1);
//
//	    
//	    ChainShape chain = new ChainShape();
//	    chain.createChain(new Vec2[]{new Vec2(-5, 0), new Vec2(0, 3), new Vec2(4, 3), new Vec2(7, 0), new Vec2(10,3)}, 5);
//
//	    FixtureDef fixtureDef1 = new FixtureDef();
//	    fixtureDef1.setShape(chain);
//	    fixtureDef1.setDensity(0);
//	    fixtureDef1.setFriction(0.3f);
//	    body1.createFixture(fixtureDef1);
//	    
//	    {
//	    	PolygonShape shape = new PolygonShape();
//	        shape.setAsBox(0.6f, 0.125f);
//
//	        FixtureDef fd = new FixtureDef();
//	        fd.shape = shape;
//	        fd.density = 20.0f;
//	        fd.friction = 0.2f;
//
//	        RevoluteJointDef jd = new RevoluteJointDef();
//	        jd.collideConnected = false;
//
//	        final float x = 45.0f;
//	        Body prevBody = body1;
//	        for (int i = 0; i < 30; ++i) {
//	          BodyDef bd = new BodyDef();
//	          bd.type = BodyType.DYNAMIC;
//	          bd.position.set(0.5f + i, x);
//	          Body body = world.createBody(bd);
//	          body.createFixture(fd);
//
//	          Vec2 anchor = new Vec2(i, x);
//	          jd.initialize(prevBody, body, anchor);
//	          world.createJoint(jd);
//
//	          prevBody = body;
//	        }
//	        
//	    }
//
//	    setupGui();
//	}
//}
