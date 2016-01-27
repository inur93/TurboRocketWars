package area51.turboRocketWars.Bodies.seperator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.particle.StackQueue;
import org.jbox2d.pooling.arrays.Vec2Array;

/* Convex Separator for Box2D Flash
 *
 * This class has been written by Antoan Angelov. 
 * It is designed to work with Erin Catto's Box2D physics library.
 *
 * Everybody can use this software for any purpose, under two restrictions:
 * 1. You cannot claim that you wrote this software.
 * 2. You can not remove or alter this notice.
 *
 */


public class B2Separator{


	/**
	 * Separates a non-convex polygon into convex polygons and adds them as fixtures to the <code>body</code> parameter.<br/>
	 * There are some rules you should follow (otherwise you might get unexpected results) :
	 * <ul>
	 * <li>This class is specifically for non-convex polygons. If you want to create a convex polygon, you don't need to use this class - Box2D's <code>b2PolygonShape</code> class allows you to create convex shapes with the <code>setAsArray()</code>/<code>setAsVector()</code> method.</li>
	 * <li>The vertices must be in clockwise order.</li>
	 * <li>No three neighbouring points should lie on the same line segment.</li>
	 * <li>There must be no overlapping segments and no "holes".</li>
	 * </ul> <p/>
	 * @param body The b2Body, in which the new fixtures will be stored.
	 * @param fixtureDef A b2FixtureDef, containing all the properties (friction, density, etc.) which the new fixtures will inherit.
	 * @param verticesVec The vertices of the non-convex polygon, in clockwise order.
	 * @param scale <code>[optional]</code> The scale which you use to draw shapes in Box2D. The bigger the scale, the better the precision. The default value is 30. 
	 * @see b2PolygonShape
	 * @see b2PolygonShape.SetAsArray()
	 * @see b2PolygonShape.SetAsVector()
	 * @see b2Fixture
	 * */

	public static void seperate(Body body, FixtureDef fixtureDef, Vec2[] verticesVec, float scale){
		
		int n = verticesVec.length;
		int j, m;
		Vec2[] vec = new Vec2[n];
		System.out.println(validate(verticesVec));
		PolygonShape polyShape = new PolygonShape();

		for (int i=0; i<n; i++) {
			vec[i] = new Vec2(verticesVec[i].x*scale,verticesVec[i].y*scale);
		}


		Vec2[][] figsVec=calcShapes(vec);
		n=figsVec.length;

		for (int i=0; i<n; i++) {
			vec=figsVec[i];
			m=vec.length;
			verticesVec= new Vec2[m];
			System.out.println("new vertex");
			for (j=0; j<m; j++) {

				verticesVec[j] = new Vec2(vec[j].x/scale,vec[j].y/scale);
				System.out.println(verticesVec[j]);
			}

			polyShape=new PolygonShape();
			polyShape.set(verticesVec, verticesVec.length);
			fixtureDef.shape=polyShape;
			body.createFixture(fixtureDef);
		}
	}

	/**
	 * Checks whether the vertices in <code>verticesVec</code> can be properly distributed into the new fixtures (more specifically, it makes sure there are no overlapping segments and the vertices are in clockwise order). 
	 * It is recommended that you use this method for debugging only, because it may cost more CPU usage.
	 * <p/>
	 * @param verticesVec The vertices to be validated.
	 * @return An integer which can have the following values:
	 * <ul>
	 * <li>0 if the vertices can be properly processed.</li>
	 * <li>1 If there are overlapping lines.</li>
	 * <li>2 if the points are <b>not</b> in clockwise order.</li>
	 * <li>3 if there are overlapping lines <b>and</b> the points are <b>not</b> in clockwise order.</li>
	 * </ul> 
	 * */

	public static int validate(Vec2[] verticesVec){
		int i, n = verticesVec.length;
		int j, j2, i2, i3;
		float d;
		int ret = 0;
		boolean fl;
		boolean fl2 = false;

	for (i=0; i<n; i++) {
		i2=(i<n-1)?i+1:0;
		i3=(i>0)?i-1:n-1;

		fl=false;
		for (j=0; j<n; j++) {
			if (((j!=i)&&j!=i2)) {
				if (! fl) {
					d=det(verticesVec[i].x,verticesVec[i].y,verticesVec[i2].x,verticesVec[i2].y,verticesVec[j].x,verticesVec[j].y);
					if ((d>0)) {
						fl=true;
					}
				}

				if ((j!=i3)) {
					j2=(j<n-1)?j+1:0;
					if (hitSegment(verticesVec[i].x,verticesVec[i].y,verticesVec[i2].x,verticesVec[i2].y,verticesVec[j].x,verticesVec[j].y,verticesVec[j2].x,verticesVec[j2].y) != null) {
						ret=1;
					}
				}
			}
		}

		if (! fl) {
			fl2=true;
		}
	}

	if (fl2) {
		if ((ret==1)) {
			ret=3;
		}
		else {
			ret=2;
		}

	}
	return ret;
	}
	private static Vec2[][] calcShapes(Vec2[] verticesVec){
		Vec2[] vec;
		int i,n,j = 0;
		float d,t,dx,dy,minLen;
		int i1, i2, i3;
		int j1, j2;
		Vec2 p1, p2, p3;
		Vec2 v1, v2;
		int k = 0, h = 0;
		ArrayList<Vec2> vec1, vec2;
		Vec2 v, hitV = null;
		boolean isConvex;
		LinkedList<Vec2[]> queue = new LinkedList<Vec2[]>();
		ArrayList<Vec2[]> figsVec = new ArrayList<Vec2[]>();

		queue.addLast(verticesVec);
		
		while (!queue.isEmpty()) {
			vec= queue.getFirst();//queue[0];
			n=vec.length;
			isConvex=true;

			for (i=0; i<n; i++) {
				i1=i;
				i2=(i<n-1)?i+1:i+1-n;
				i3=(i<n-2)?i+2:i+2-n;

				p1=vec[i1];// [i1];
				p2=vec[i2];
				p3=vec[i3];

				d=det(p1.x,p1.y,p2.x,p2.y,p3.x,p3.y);
				if ((d<0)) {
					isConvex=false;
					minLen=Float.MAX_VALUE;

					for (j=0; j<n; j++) {
						if (((j!=i1)&&j!=i2)) {
							j1=j;
							j2=(j<n-1)?j+1:0;

							v1= vec[j1];
							v2= vec[j2];

							v=hitRay(p1.x,p1.y,p2.x,p2.y,v1.x,v1.y,v2.x,v2.y);

							if (v != null) {
								dx=p2.x-v.x;
								dy=p2.y-v.y;
								t=dx*dx+dy*dy;

								if ((t<minLen)) {
									h=j1;
									k=j2;
									hitV=v;
									minLen=t;
								}
							}
						}
					}

					if ((minLen==Float.MAX_VALUE)) {
						err();
					}

					vec1= new ArrayList<Vec2>();
					vec2= new ArrayList<Vec2>();

					j1=h;
					j2=k;
					v1= vec[j1];
					v2= vec[j2];

					if (! pointsMatch(hitV.x,hitV.y,v2.x,v2.y)) {
						vec1.add(hitV);
//						vec1.addElement(hitV);
//						vec1.push(hitV);
					}
					if (! pointsMatch(hitV.x,hitV.y,v1.x,v1.y)) {
//						vec2.addElement(hitV);
						vec2.add(hitV);
//						vec2.push(hitV);
					}

					h=-1;
					k=i1;
					while (true) {
						if ((k!=j2)) {
							vec1.add(vec[k]);
						}
						else {
							if (((h<0)||h>=n)) {
								err();
							}
							if (! isOnSegment(v2.x,v2.y,vec[h].x,vec[h].y,p1.x,p1.y)) {
								vec1.add(vec[k]);
							}
							break;
						}

						h=k;
						if (((k-1)<0)) {
							k=n-1;
						}
						else {
							k--;
						}
					}

					ArrayList<Vec2> temp = new ArrayList<Vec2>();
					temp.ensureCapacity(vec1.size());
//					System.out.println("check reverse");
//					for(Vec2 vec1test : vec1) System.out.println(vec1test);
//					System.out.println("second");
					for(int index = vec1.size()-1; index >= 0; index--){
//						System.out.println(vec1.get(index));
						temp.add(vec1.get(index));
					}
//					System.out.println("third");
//					for(Vec2 testing : temp) System.out.println(testing);
					vec1= temp;

					h=-1;
					k=i2;
					while (true) {
						if ((k!=j1)) {
							vec2.add(vec[k]);
						}
						else {
							if (((h<0)||h>=n)) {
								err();
							}
							if (((k==j1)&&! isOnSegment(v1.x,v1.y,vec[h].x,vec[h].y,p2.x,p2.y))) {
								vec2.add(vec[k]);
							}
							break;
						}

						h=k;
						if (((k+1)>n-1)) {
							k=0;
						}
						else {
							k++;
						}
					}

					queue.addLast(vec1.toArray( new Vec2[vec1.size()]));
					queue.addLast(vec2.toArray(new Vec2[vec2.size()]));
					queue.remove(0);
					System.out.println("pop1 push 2");

					break;
				}
			}

			if (isConvex) {
				System.out.println("pop only");
				figsVec.add(queue.remove(0));
			}
		}
		
		for(Vec2[] test : figsVec){
			System.out.println("new");
			for(Vec2 test1 : test){
				System.out.println(test1);
			}
		}
		
		return figsVec.toArray(new Vec2[figsVec.size()][]);
	}
	
	private static Vec2 hitRay(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
		float t1 = x3-x1;
		float t2 = y3-y1;
		float t3 = x2-x1;
		float t4 = y2-y1;
		float t5 = x4-x3;
		float t6 = y4-y3;
		float t7 = t4*t5-t3*t6;
		float a = (((t5*t2)-t6*t1)/t7);
	float px = x1+a*t3;
	float py = y1+a*t4;
	boolean b1 = isOnSegment(x2,y2,x1,y1,px,py);
	boolean b2 = isOnSegment(px,py,x3,y3,x4,y4);

	if ((b1&&b2)) {
		return new Vec2(px,py);
	}
	return null;	
	}

	private static Vec2 hitSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
		float t1 = x3-x1;
		float t2 = y3-y1;
		float t3 = x2-x1;
		float t4 = y2-y1;
		float t5 = x4-x3;
		float t6 = y4-y3;
		float t7 = t4*t5-t3*t6;
		float a=(((t5*t2)-t6*t1)/t7);

		float px = x1+a*t3;
		float py = y1+a*t4;
		boolean b1 = isOnSegment(px,py,x1,y1,x2,y2);
		boolean b2 = isOnSegment(px,py,x3,y3,x4,y4);
		if ((b1&&b2)) {
			return new Vec2(px,py);
		}

		return null;
	}


	private static boolean isOnSegment(float px, float py, float x1, float y1, float x2, float y2){
		boolean b1 = ((((x1+0.1)>=px)&&px>=x2-0.1)||(((x1-0.1)<=px)&&px<=x2+0.1));
		boolean b2 = ((((y1+0.1)>=py)&&py>=y2-0.1)||(((y1-0.1)<=py)&&py<=y2+0.1));
		return ((b1&&b2)&&isOnLine(px,py,x1,y1,x2,y2));
	}

	private static boolean pointsMatch(float x1, float y1, float x2, float y2){
		float dx = (x2>=x1)?x2-x1:x1-x2;
		float dy = (y2>=y1)?y2-y1:y1-y2;
		return ((dx<0.1)&&dy<0.1);
	}

	private static boolean isOnLine(float px, float py, float x1, float y1, float x2, float y2){
		if ((((x2-x1)>0.1)||x1-x2>0.1)) {
			float a = (y2-y1)/(x2-x1);
			float possibleY =a*(px-x1)+y1;
			float diff = (possibleY>py)?possibleY-py:py-possibleY;
			return (diff<0.1);
		}
		return (((px-x1)<0.1)||x1-px<0.1);
	}

	private static float det(float x1, float y1, float x2, float y2, float x3, float y3){
		return x1*y2+x2*y3+x3*y1-y1*x2-y2*x3-y3*x1;
	}

	private static void err() {
		throw new Error("A problem has occurred. Use the Validate() method to see where the problem is.");
	}

}
