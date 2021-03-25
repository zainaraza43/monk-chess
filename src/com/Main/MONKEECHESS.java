package com.Main;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import javax.swing.JPanel;
import javax.swing.JFrame;

import com.Behavior.MouseZoom;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

public class MONKEECHESS extends JPanel {

    public static final Color3f Red = new Color3f(1.0f, 0.0f, 0.0f);
    public static final Color3f Green = new Color3f(0.0f, 1.0f, 0.0f);
    public static final Color3f Blue = new Color3f(0.0f, 0.0f, 1.0f);
    public static final Color3f Yellow = new Color3f(1.0f, 1.0f, 0.0f);
    public static final Color3f Cyan = new Color3f(0.0f, 1.0f, 1.0f);
    public static final Color3f Orange = new Color3f(1.0f, 0.5f, 0.0f);
    public static final Color3f Magenta = new Color3f(1.0f, 0.0f, 1.0f);
    public static final Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
    public static final Color3f Grey = new Color3f(0.5f, 0.5f, 0.5f);
    public static final Color3f[] Clrs = {Blue, Green, Red, Yellow,
            Cyan, Orange, Magenta, Grey};
    public final static int clr_num = 8;

    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private static ChessBoard chessBoard;



    private static Background generateBackground(){ // will return a background
        Background background = new Background(); // make a background
        background.setImageScaleMode(Background.SCALE_FIT_MAX); // scale it to max
        TextureLoader loader = new TextureLoader("Assets/Background/background.jpg", null); // load the image
        background.setApplicationBounds(new BoundingSphere(new Point3d(), 1000d)); // set the bounds
        background.setImage(loader.getImage()); // set the image
        return background;
    }

    private static Shape3D generateAxis(Color3f yColor, float length) { // function to generate x, y, z axes
        LineArray lineArray = new LineArray(6, LineArray.COLOR_3 | LineArray.COORDINATES);
        Point3f[] coors = {new Point3f(length, 0,0), new Point3f(0,length,0), new Point3f(0,0,length)}; // array of the coordinates
        Color3f[] cols = {Green, yColor, Red}; // array of colors
        for (int i = 0; i < 6; i +=2){
            lineArray.setCoordinate(i, new Point3f(0,0,0)); // will make point at origin
            lineArray.setColor(i, cols[i % cols.length]); // will set the origin color % by length to get correct color and stay within size
            lineArray.setCoordinate(i + 1, coors[i % coors.length]); // will get the second point for x, y, z
            lineArray.setColor(i+ 1, cols[i % cols.length]); // will set the color for the point
        }
        return new Shape3D(lineArray);
    }

    private KeyNavigatorBehavior keyNavigation(SimpleUniverse simple_U) {
        //function for moving around using keys , can be used to move any direction
        //this allows us to move with the viewing platform
        ViewingPlatform view_platfm = simple_U.getViewingPlatform();
        TransformGroup view_TG = view_platfm.getViewPlatformTransform();
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(view_TG);
        BoundingSphere view_bounds = new BoundingSphere(new Point3d(), 10000.0);
        keyNavBeh.setSchedulingBounds(view_bounds);
        return keyNavBeh;
    }

    private MouseZoom mouseZoom(SimpleUniverse simpleUniverse) {
        ViewingPlatform view_platfm = simpleUniverse.getViewingPlatform();
        TransformGroup view_TG = view_platfm.getViewPlatformTransform();
        MouseZoom mouseZoom = new MouseZoom(view_TG, simpleUniverse);
        mouseZoom.setSchedulingBounds(new BoundingSphere(new Point3d(), 10000d));
        return mouseZoom;
    }


    private static void defineViewer(SimpleUniverse simple_U, Point3d eye) {
        TransformGroup viewTransform = simple_U.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(0, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);                 // set the TransformGroup of ViewingPlatform
    }

    public static void viewerZoom(SimpleUniverse su, Point3d eye){
        TransformGroup viewTransform = su.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(0, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);                 // set the TransformGroup of ViewingPlatform
    }


    public static RotationInterpolator rotateBehavior(TransformGroup rotTG, Alpha rotAlpha) {
        Transform3D yAxis = new Transform3D();                        // y-axis is the default
        RotationInterpolator rot_beh = new RotationInterpolator(
                rotAlpha, rotTG, yAxis, 0.0f, (float) Math.PI * 2.0f);  // 360 degrees of rotation
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rot_beh.setSchedulingBounds(bounds);
        return rot_beh;
    }

    /* a function to create and return the scene BranchGroup */
    public static void createScene(BranchGroup sceneBG, SimpleUniverse su) {
        // create 'objsBG' for content
        TransformGroup sceneTG = su.getViewingPlatform().getViewPlatformTransform();
        addLights(sceneBG, White);
        sceneBG.addChild(generateBackground());
        sceneBG.addChild(generateAxis(Yellow, 1f));

        chessBoard = new ChessBoard("chess");
        chessBoard.createScene(sceneBG);
    }

    public static void addLights(BranchGroup sceneBG, Color3f clr) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        AmbientLight amLgt = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
        amLgt.setInfluencingBounds(bounds);
        sceneBG.addChild(amLgt);
        Point3f pt  = new Point3f(2.0f, 2.0f, 2.0f);
        Point3f atn = new Point3f(1.0f, 0.0f, 0.0f);
        PointLight ptLight = new PointLight(clr, pt, atn);
        ptLight.setInfluencingBounds(bounds);
        sceneBG.addChild(ptLight);
    }
    public MONKEECHESS(){
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas_3D = new Canvas3D(config);//define a canvas
        SimpleUniverse su = new SimpleUniverse(canvas_3D);   //define simpile universe and put canvas in it
        defineViewer(su, new Point3d(0, 20, 20.0));    // set the viewer's location
        BranchGroup scene = new BranchGroup();
        createScene(scene, su);                           // add contents to the scene branch
        scene.addChild(keyNavigation(su));                   // allow key navigation
        scene.addChild(mouseZoom(su));
        scene.compile();                                     // compile the BranchGroup
        su.addBranchGraph(scene);                            // attach the scene to SimpleUniverse
        setLayout(new BorderLayout());
        add("Center", canvas_3D);
        setVisible(true);
    }

    public static class MyGUI extends JFrame {
        private static final long serialVersionUID = 1L;
        public MyGUI(String title) {
            JFrame frame = new JFrame(title);
            frame.getContentPane().add(new MONKEECHESS());
            frame.setSize(850, 700);    // set the size of the JFrame
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // will exit the program on close

        }
    }
}

