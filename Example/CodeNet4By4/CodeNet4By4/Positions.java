package CodeNet4By4;

// Positions.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/*
 *	@(#)Positions.java 1.11 01/01/11 07:32:12
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved

   The Positions class creates the spheres and boxes used to indicate
   a player's move. Initially small white spheres occupy the positions,
   but these are switched for red balls or blue cubes when a 
   player clicks on a position.

   Player 1 is represented by red balls.
   Player 2 is represented by blue cubes.

   A change is made by calling set().
 */

import java.util.BitSet;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.*;
import org.jogamp.vecmath.*;


public class Positions {
	private final static int NUM_SPOTS = 64;         // in the game board (4*4*4)
	private final static int PLAYER1 = 1;
	private final static int PLAYER2 = 2;                         // was MACHINE

	private Switch posSwitch, player1Switch, player2Switch;
	private BitSet posMask, player1Mask, player2Mask;

	private Appearance blueApp, redApp, whiteApp;

	private Vector3f[] points;
	private Group group;

	public Positions() {
		initAppearances();
		initLocations();

		group = new Group();
		                    // create the various markers and add them to a group
		makeWhiteSpheres();
		makeRedSpheres();
		makeBlueCubes();
	}
	
	/* a function to create the white, red and blue materials */
	private void initAppearances() { 
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f red = new Color3f(0.9f, 0.1f, 0.2f);
		Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
		Color3f ambRed = new Color3f(0.3f, 0.03f, 0.03f);
		Color3f ambBlue = new Color3f(0.03f, 0.03f, 0.3f);
		Color3f specular = new Color3f(1.0f, 1.0f, 1.0f);
		                                          // Create the red appearance node
		Material redMat = new Material(ambRed, black, red, specular, 100.f);
		redMat.setLightingEnable(true);
		redApp = new Appearance();
		redApp.setMaterial(redMat);
		                                         // Create the blue appearance node
		Material blueMat = new Material(ambBlue, black, blue, specular, 100.f);
		blueMat.setLightingEnable(true);
		blueApp = new Appearance();
		blueApp.setMaterial(blueMat);

	}

	/* Load point array with the offsets (coordinates) for each of the NUM_SPOTS
	 * positions.
	 */
	private void initLocations() {
		points = new Vector3f[NUM_SPOTS];
		int count = 0;
		for (int z = -30; z < 40; z += 20) {
			for (int y = -30; y < 40; y += 20) {
				for (int x = -30; x < 40; x += 20) {
					points[count] = new Vector3f((float) x, (float) y, (float) z);
					count++;
				}
			}
		}
	}
	
	/* Create the small white spheres that mark unoccupied positions. Place them in
	 * a switch with a bitset for turning each one on/off.
	 */
	private void makeWhiteSpheres() {
		// Create the switch nodes
		posSwitch = new Switch(Switch.CHILD_MASK);
		// Set the capability bits
		posSwitch.setCapability(Switch.ALLOW_SWITCH_READ);
		posSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		posMask = new BitSet(); // create the bit mask

		Sphere posSphere;
		for (int i = 0; i < NUM_SPOTS; i++) {
			Transform3D t3d = new Transform3D();
			t3d.set(points[i]); // set position
			TransformGroup tg = new TransformGroup(t3d);
			posSphere = new Sphere(2.0f, whiteApp);
			Shape3D shape = posSphere.getShape();
			shape.setUserData(i); // add board position ID to each shape
			tg.addChild(posSphere);
			posSwitch.addChild(tg);
			posMask.set(i); // make visible
		}
		// Set the positions mask
		posSwitch.setChildMask(posMask);

		group.addChild(posSwitch);
	}

	/* Create the red spheres that mark the user's positions.
	 * Place them in a switch with a bitset for turning each one on/off 
	 */
	private void makeRedSpheres() {
		// Create the switch nodes
		player1Switch = new Switch(Switch.CHILD_MASK);
		// Set the capability bits
		player1Switch.setCapability(Switch.ALLOW_SWITCH_READ);
		player1Switch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		player1Mask = new BitSet();                             // create the bit mask

		for (int i = 0; i < NUM_SPOTS; i++) {
			Transform3D t3d = new Transform3D();
			t3d.set(points[i]);                                        // set position
			TransformGroup tg = new TransformGroup(t3d);
			tg.addChild(new Sphere(5.5f, redApp));
			player1Switch.addChild(tg);
			player1Mask.clear(i);                                    // make invisible
		}
		                                                       // Set the positions mask
		player1Switch.setChildMask(player1Mask);

		group.addChild(player1Switch);
	}

	/* Create the blue cubes that mark the computer's positions.
	 * Place them in a switch with a bitset for turning each one on/off
	 */
	private void makeBlueCubes() {
		// Create the switch nodes
		player2Switch = new Switch(Switch.CHILD_MASK);
		// Set the capability bits
		player2Switch.setCapability(Switch.ALLOW_SWITCH_READ);
		player2Switch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		player2Mask = new BitSet(); // create the bit mask

		for (int i = 0; i < NUM_SPOTS; i++) {
			Transform3D t3d = new Transform3D();
			t3d.set(points[i]);                                        // set position
			TransformGroup tg = new TransformGroup(t3d);
			tg.addChild(new Box(4.0f, 5.0f, 5.0f, blueApp));
			player2Switch.addChild(tg);
			player2Mask.clear(i);                                       // make invisible
		}
		                                                         // Set the positions mask
		player2Switch.setChildMask(player2Mask);

		group.addChild(player2Switch);
	}

	public Group getChild() {
		return group;
	}

	/* a function called by Board to update the 3D scene */
	public void set(int pos, int player) {
		// turn off the white position marker for the given position
		posMask.clear(pos);
		posSwitch.setChildMask(posMask);

		// turn on one of the player markers
		if (player == PLAYER1) {
			player1Mask.set(pos);
			player1Switch.setChildMask(player1Mask); // show red ball
		} else if (player == PLAYER2) {
			player2Mask.set(pos);
			player2Switch.setChildMask(player2Mask); // show blue cube
		} else
			System.out.println("Positions set() call with illegal player value: " + player);
	}
}