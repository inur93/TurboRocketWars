import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.pooling.arrays.IntArray;
import org.jbox2d.pooling.arrays.Vec2Array;

import area51.turboRocketWars.Bodies.userData.FixtureViewProperties;
import area51.turboRocketWars.gui.MainGamePanel;
import area51.turboRocketWars.gui.impl.MainGamePanelImpl;
import area51.turboRocketWars.maps.MapGenerator;



public class TestMain {

	private World world;
	private Body body;
	private MainGamePanel panel;
	private OBBViewportTransform camera;
	public static void main(String[] args) {
		new TestMain().run();
	}
	
	public void runProgram(){
		// Setup world
	    float timeStep = 1.0f/60.0f;
	    int velocityIterations = 6;
	    int positionIterations = 2;
		// Run loop
	    for (int i = 0; i < 6000; ++i) {
	        world.step(timeStep, velocityIterations, positionIterations);
	        world.clearForces();
	        world.computeParticleCollisionEnergy();
	        world.drawDebugData();
	        Vec2 point = new Vec2();
	        
	        camera.setCenter(body.getPosition());
//	        world.
//	        Vec2 position = body.getPosition();
//	        float angle = body.getAngle();
//	        System.out.println(position.x + "," + position.y + "," + angle);
	        try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        panel.paintScreen();
	    }
	}
	public void setupGui(){
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		camera = new OBBViewportTransform();
		camera.setCamera(0, 0, 3);
		camera.setExtents(300, 300);
		panel = new MainGamePanelImpl(this.world, camera);
		new Thread(panel).start();
		frame.add((JPanel) panel, "Center");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		panel.paintScreen();
		runProgram();
		
		
	}
	public void run(){
	    // Static Body
		{
	    Vec2  gravity = new Vec2(0,-10);
	    world = new World(gravity);
	    
	    BodyDef groundBodyDef = new BodyDef();
	    groundBodyDef.position.set(10, 0);
	    Body groundBody = world.createBody(groundBodyDef);
	    
	    PolygonShape groundBox = new PolygonShape();
	    groundBox.setAsBox(100, 20);
//	    groundBox.set(new Vec2[]{new Vec2(20, 20), new Vec2(20, 30), new Vec2(60, 30), new Vec2(60, 20)}, 4, new Vec2Array(), new IntArray());
//	    ShapeType.CHAI

	    
//	    groundBox.m_vertices = ;
//	    groundBox.setAsBox(50, 10, new Vec2(10, 5), 1);
//	    groundBox.setAsBox(100, 10);
	    groundBody.createFixture(groundBox, 0);
	    
	    // Dynamic Body polygon
	    BodyDef bodyDef = new BodyDef();
	    
	    bodyDef.type = BodyType.DYNAMIC;
	    FixtureViewProperties view = new FixtureViewProperties();
	    view.fill = true;
	    view.color = Color.red;
	    
	    bodyDef.setPosition(new Vec2(55, 80));
	    bodyDef.setAngle((float) Math.toRadians(42));
	    body = world.createBody(bodyDef);
	    body.setUserData(view);	    
	    PolygonShape dynamicBox = new PolygonShape();
	    dynamicBox.setAsBox(5, 5);
	    
	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = dynamicBox;
	    fixtureDef.density = 1;
	    fixtureDef.friction = 0.3f;
//	    fixtureDef.shape.
	    body.createFixture(fixtureDef);
		}
	    
	    // dynamic body chain
		
		
	    BodyDef bodyDef1 = new BodyDef();
	    bodyDef1.setType(BodyType.STATIC);
//	    bodyDef1.setFixedRotation(false);
//	    bodyDef1.setAllowSleep(false);
//	    bodyDef1.setActive(true);
//	    bodyDef1.setAngle(30);
	    bodyDef1.setPosition(new Vec2(30, 40));
	    Body body1 = world.createBody(bodyDef1);
//	    body1.set
//	    body1.setActive(true);

	    
	    ChainShape chain = new ChainShape();
	    chain.createChain(new Vec2[]{new Vec2(-5, 0), new Vec2(0, 3), new Vec2(4, 3), new Vec2(7, 0), new Vec2(10,3)}, 5);

	    FixtureDef fixtureDef1 = new FixtureDef();
	    fixtureDef1.setShape(chain);
	    fixtureDef1.setDensity(0);
	    fixtureDef1.setFriction(0.3f);
	    body1.createFixture(fixtureDef1);
	    
	    {
	    	PolygonShape shape = new PolygonShape();
	        shape.setAsBox(0.6f, 0.125f);

	        FixtureDef fd = new FixtureDef();
	        fd.shape = shape;
	        fd.density = 20.0f;
	        fd.friction = 0.2f;

	        RevoluteJointDef jd = new RevoluteJointDef();
	        jd.collideConnected = false;

	        final float x = 45.0f;
	        Body prevBody = body1;
	        for (int i = 0; i < 30; ++i) {
	          BodyDef bd = new BodyDef();
	          bd.type = BodyType.DYNAMIC;
	          bd.position.set(0.5f + i, x);
	          Body body = world.createBody(bd);
	          body.createFixture(fd);

	          Vec2 anchor = new Vec2(i, x);
	          jd.initialize(prevBody, body, anchor);
	          world.createJoint(jd);

	          prevBody = body;
	        }
	        this.body = prevBody;
	    }
//	    body1.crea
	    // dynamic body circle
//	    BodyDef bodyDef2 = new BodyDef();
//	    bodyDef2.setType(BodyType.DYNAMIC);
//	    bodyDef2.setPosition(new Vec2(0, 5));
//	    Body body2 = world.createBody(bodyDef2);
//
//	    CircleShape circle = new CircleShape();
//	    circle.setRadius(3);
//	    FixtureDef fixtureDef2 = new FixtureDef();
//	    fixtureDef2.setShape(circle);
//	    fixtureDef2.setDensity(1);
//	    fixtureDef2.setFriction(0.3f);
//	    body2.createFixture(fixtureDef2);
//	    ShapeType.
	    setupGui();
	}
}
