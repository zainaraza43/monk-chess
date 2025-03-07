/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * MONKEECHESS.java
 */

package com.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.*;

import com.Behavior.MouseZoom;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.PlatformGeometry;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

public class MONKEECHESS extends JPanel {
    // defining colors
    public static Overlay overlay;
    //public static String status;
    public static String board = "chess0";
    public static final Color3f Red = new Color3f(1.0f, 0.0f, 0.0f);
    public static final Color3f Green = new Color3f(0.0f, 1.0f, 0.0f);
    public static final Color3f Blue = new Color3f(0.0f, 0.0f, 1.0f);
    public static final Color3f Yellow = new Color3f(1.0f, 1.0f, 0.0f);
    public static final Color3f Cyan = new Color3f(0.0f, 1.0f, 1.0f);
    public static final Color3f Orange = new Color3f(1.0f, 0.5f, 0.0f);
    public static final Color3f Magenta = new Color3f(1.0f, 0.0f, 1.0f);
    public static final Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
    public static final Color3f Grey = new Color3f(0.5f, 0.5f, 0.5f);
    public static final Color3f Black = new Color3f(0, 0, 0);
    public static ChessBoard chessBoard;
    public static OverlayCanvas3D overlayCanvas3D;
    public static SimpleUniverse su;
    public static Point3d position;
    public static String ground = "background0";

    private static Background generateBackground() { // will return a background
        Background background = new Background(); // make a background
        background.setImageScaleMode(Background.SCALE_FIT_MAX); // scale it to max
        TextureLoader loader = new TextureLoader("Assets/Images/" + ground + ".jpg", null); // load the image
        background.setApplicationBounds(new BoundingSphere(new Point3d(), 1000d)); // set the bounds
        background.setImage(loader.getImage()); // set the image
        return background;
    }

    private KeyNavigatorBehavior keyNavigation(SimpleUniverse simple_U) {
        ViewingPlatform view_platfm = simple_U.getViewingPlatform();
        TransformGroup view_TG = view_platfm.getViewPlatformTransform();
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(view_TG);
        BoundingSphere view_bounds = new BoundingSphere(new Point3d(), 1000.0);
        keyNavBeh.setSchedulingBounds(view_bounds);
        return keyNavBeh;
    }

    private MouseZoom mouseZoom(SimpleUniverse simpleUniverse) { // used for zooming the viewer in and out of the board
        ViewingPlatform view_platfm = simpleUniverse.getViewingPlatform();
        TransformGroup view_TG = view_platfm.getViewPlatformTransform();
        MouseZoom mouseZoom = new MouseZoom(view_TG);
        mouseZoom.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
        return mouseZoom;
    }

    public static void changeViewer(SimpleUniverse su, Point3d eye) { // used to move the viewer to a top down view
        TransformGroup viewTransform = su.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(1, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);                 // set the TransformGroup of ViewingPlatform
    }

    public static void resetViewer(SimpleUniverse su, Point3d eye) { // function used to reset the viewer
        TransformGroup viewTransform = su.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(0, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);                 // set the TransformGroup of ViewingPlatform
    }

    private static void defineViewer(SimpleUniverse simple_U, Point3d eye) { // function used to define viewer original position
        TransformGroup viewTransform = simple_U.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(0, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);                 // set the TransformGroup of ViewingPlatform
    }

    /* a function to create and return the scene BranchGroup */
    public void createScene(BranchGroup sceneBG) {
        // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE | TransformGroup.ALLOW_CHILDREN_WRITE | TransformGroup.ALLOW_CHILDREN_EXTEND);
        sceneBG.addChild(sceneTG);
        addLights(sceneBG, White);
        sceneBG.addChild(generateBackground());
        chessBoard = new ChessBoard(board, overlayCanvas3D, sceneBG, sceneTG); // pass in texture name, canvas and sceneBG
        chessBoard.createScene(); // sceneTG is what all pieces and board will be on
    }

    public static void addLights(BranchGroup sceneBG, Color3f clr) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
        AmbientLight amLgt = new AmbientLight(new Color3f(0.7f, 0.7f, 0.7f));
        amLgt.setInfluencingBounds(bounds);
        sceneBG.addChild(amLgt);
        Point3f pt = new Point3f(0f, 2f, 0f);
        Point3f atn = new Point3f(1, 0, 0);
        PointLight ptLight = new PointLight(clr, pt, atn);
        ptLight.setInfluencingBounds(bounds);
        sceneBG.addChild(ptLight);
    }

    public MONKEECHESS() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        overlayCanvas3D = new OverlayCanvas3D(config);//define a canvas
        Viewer viewer = new Viewer(overlayCanvas3D);
        viewer.getView().setFrontClipDistance(0.05);
        viewer.getView().setBackClipDistance(100);

        ViewingPlatform viewingPlatform = new ViewingPlatform();
        viewingPlatform.setCapability(ViewingPlatform.ALLOW_BOUNDS_WRITE);

        su = new SimpleUniverse(viewingPlatform, viewer);   //define simpile universe and put canvas in it
        position = new Point3d(0, 25, 25);
        defineViewer(su, position);    // set the viewer's location random for black piece or white piece
        BranchGroup scene = new BranchGroup();
        createScene(scene);                           // add contents to the scene branch
        scene.addChild(keyNavigation(su));                   // allow key navigation
        scene.addChild(mouseZoom(su));
        scene.compile();                                     // compile the BranchGroup
        su.addBranchGraph(scene);                            // attach the scene to SimpleUniverse
        overlayCanvas3D.setBounds(10, 0, 850, 700);
        add("Center", overlayCanvas3D);
        setVisible(true);
    }

    public static class MyGUI extends JFrame {
        private static final long serialVersionUID = 1L;
        public static JFrame frame;
        public MyGUI(String title) {
            frame = new JFrame(title);
            frame.setSize(1010, 850);    // set the size of the JFrame
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            JPanel panel = new JPanel();
            panel.setBounds(70, 2, 865, 718);
            panel.setBackground(Color.black);
            panel.setVisible(true);
            panel.add(new MONKEECHESS());
            frame.add(panel);
            overlay = new Overlay(frame);
            overlay.createPanels();

            frame.setLayout(new BorderLayout());
            frame.setVisible(true);
            pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // will exit the program on close
        }
    }
}

