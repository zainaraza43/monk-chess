package CodeNet4By4;

// WrapNetFBF.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Consists of 16 poles holding 4 spheres each. The player
   clicks on a sphere to make a turn, and the sphere turns
   into a large red sphere or blue box depending on the
   player.

   The game is shown using parallel projection so the poles 
   at the 'back' do not appear smaller than those at the front --
   there is no perspective effect.

   The game uses its own mouse behaviour, defined in 
   PickDragBehaviour, to select positions on the board, and to
   rotate the board.

   Changes for Net version:
       * make board global and added setPosn()
       * uses OverlayCanvas() instead of Canvas3D()
*/

import javax.swing.*;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;


import java.awt.*;

public class WrapNetFBF extends JPanel {
// Holds the 3D canvas where the game appears

	private static final long serialVersionUID = 1L;
	private static final int PWIDTH = 800;                   // size of panel
	private static final int PHEIGHT = 800;

	private static final int BOUNDSIZE = 100;             // larger than world

	private SimpleUniverse su;
	private BranchGroup sceneBG;
	private BoundingSphere bounds;                      // for environment nodes

	private Board board;
	private Appearance AppearanceTransparent;

	public WrapNetFBF(NetFourByFour fbf) {
		setLayout(new BorderLayout());
		setOpaque(false);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		OverlayCanvas canvas3D = new OverlayCanvas(config, fbf);
		add("Center", canvas3D);
		canvas3D.setFocusable(true);                    // give focus to the canvas
		canvas3D.requestFocus();

		su = new SimpleUniverse(canvas3D);

		createSceneGraph(canvas3D, fbf);
		initUserPosition();                                 // set user's viewpoint

		su.addBranchGraph(sceneBG);
	}

	private void createSceneGraph(OverlayCanvas canvas3D, NetFourByFour fbf) {
		sceneBG = new BranchGroup();
		bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

		// Create the transform group which moves the game
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sceneBG.addChild(tg);

		lightScene();                                             // add the lights
//		sceneBG.addChild(LightFog.createBkground(Commons.White, Commons.boundH));                                   // add the background

		tg.addChild(makePoles());                                      // add poles

		// posns holds the spheres/boxes which mark a player's turn
		// initially posns displays a series of small white spheres.
		Positions posns = new Positions();

		// board tracks the players moves on the game board
		board = new Board(posns, fbf);

		tg.addChild(posns.getChild());                      // add position markers

		mouseControls(canvas3D, fbf, tg);

		sceneBG.compile();                                         // fix the scene
	}

	// a function to create one ambient light and one directional light
	private void lightScene() {
		Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
		                                                  // create an ambient light
		AmbientLight ambLight = new AmbientLight(white);
		ambLight.setInfluencingBounds(bounds);
		sceneBG.addChild(ambLight);
		                                               // create a directional light
		Vector3f dir = new Vector3f(-1.0f, -1.0f, -1.0f);
		DirectionalLight dirLight = new DirectionalLight(white, dir);
		dirLight.setInfluencingBounds(bounds);
		sceneBG.addChild(dirLight);
	}

	/* a function to create the mouse pick and drag behavior */
	private void mouseControls(OverlayCanvas c, NetFourByFour fbf, TransformGroup tg) {
		PickDragBehavior mouseBeh = new PickDragBehavior(c, fbf, sceneBG, tg);
		mouseBeh.setSchedulingBounds(bounds);
		sceneBG.addChild(mouseBeh);
	} 

	/* a function to set the user's initial viewpoint. */
	private void initUserPosition() {
		View view = su.getViewer().getView();
		view.setProjectionPolicy(View.PARALLEL_PROJECTION);  // Use parallel projection

		// scale up and move back
		TransformGroup steerTG = su.getViewingPlatform().getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		t3d.set(65.0f, new Vector3f(0.0f, 0.0f, 400.0f));
		steerTG.setTransform(t3d);
	}

	/* Create 16 poles (cylinders) which will each appear to support 4 position
	 * markers (spheres).
	 */
	private BranchGroup makePoles() {
		BranchGroup bg = new BranchGroup();
		float x = -30.0f;
		float z = -30.0f;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				Transform3D t3d = new Transform3D();
				t3d.set(new Vector3f(x, 0.0f, z));
				TransformGroup tg = new TransformGroup(t3d);
				Appearance appearance = new Appearance();
				TransparencyAttributes ta = new TransparencyAttributes();
				ta.setCapability(TransparencyAttributes.FASTEST);
				ta.setTransparency(0.9f);
				appearance.setTransparencyAttributes(ta);
				Cylinder cyl = new Cylinder(1.0f, 60.0f, appearance);
				cyl.setPickable(false); // user cannot select the poles
				tg.addChild(cyl);
				bg.addChild(tg);
				x += 20.0f;
			}
			x = -30.0f;
			z += 20.0f;
		}
		return bg;
	} 
	
	// -------------------- position method

	/* a function called by top-level through JFrame */
	public void tryPosn(int posn, int pid) {
		board.tryPosn(posn, pid);
	}
}