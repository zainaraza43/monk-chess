package com.Main;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JFrame;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;


public class MONKEECRAFT extends JPanel {

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


    public static void CreateLight(BranchGroup TG) {// create light method
        AmbientLight AL = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));//set the ambient light
        AL.setEnable(true);//enable the light
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 1000.0);
        AL.setInfluencingBounds(bounds);
        TG.addChild(AL);//add the ambient light to the branchgroup

    }

    public static void PLight(BranchGroup State) {
        PointLight light = new PointLight();//define a pointLight
        light.setEnable(true);//enable the light
        light.setColor(new Color3f(1f, 1f, 1f));//set color according to instructions
        light.setPosition(new Point3f(2f, 2f, 2f));//set position according to instructions
        light.setAttenuation(new Point3f(1f, 0f, 0f));//set Attenuation according to instructions
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 1000.0);
        light.setInfluencingBounds(bounds);
        State.addChild(light);//add the light to the branch group

    }

    public static Material setMaterial(Color3f clr) {
        int SH = 128;               // 10
        Material ma = new Material();
        Color3f c = new Color3f(0.6f*clr.x, 0.6f*clr.y, 0.6f*clr.z);
        ma.setAmbientColor(c);
        ma.setEmissiveColor(new Color3f(0, 0, 0));
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
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
        BoundingSphere view_bounds = new BoundingSphere(new Point3d(), 20.0);
        keyNavBeh.setSchedulingBounds(view_bounds);
        return keyNavBeh;
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

    public static Sphere gen_sphere(Color3f color, float rad, int divisions) {
        Appearance ap = new Appearance();//define apperance
        Color3f Red = new Color3f(0f, 0f, 0f);
        ColoringAttributes ca = new ColoringAttributes(Red, ColoringAttributes.NICEST);
        ap.setColoringAttributes(ca);//apply attributes to apperance
        PolygonAttributes pa = new PolygonAttributes();
        ap.setPolygonAttributes(pa);
        ap.setMaterial(setMaterial(color));
        return new Sphere(rad, Sphere.GENERATE_NORMALS, divisions, ap);// generate new sphere and apply appearance to it, use rad for the radius
    }

    /* a function to create and return the scene BranchGroup */
    public static void createScene(BranchGroup sceneBG) {
        // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        float[] dist = {4.0f, 7.5f, 12f};// dist given in the lab
        Switch ST = new Switch();
        ST.setCapability(Switch.ALLOW_SWITCH_WRITE);
        ST.addChild(gen_sphere(Green, 0.75f, 60));
        ST.addChild(gen_sphere(Blue, 0.6f, 45));
        ST.addChild(gen_sphere(Orange, 0.5f, 30));
        ST.addChild(gen_sphere(Red, 0.35f, 15));
        DistanceLOD distanceLOD = new DistanceLOD(dist, new Point3f());
        distanceLOD.addSwitch(ST);//add the switches to the DistanceLOD
        BoundingSphere view_bounds = new BoundingSphere(new Point3d(), 100.0);

        distanceLOD.setSchedulingBounds(view_bounds);
        sceneBG.addChild(sceneTG);
        sceneTG.addChild(distanceLOD);
        sceneTG.addChild(ST);

        PLight(sceneBG);
        CreateLight(sceneBG);
        sceneBG.addChild(generateAxis(Yellow, 0.5f));

    }

    public MONKEECRAFT(){// contructor for lab7
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas_3D = new Canvas3D(config);//define a canvas
        SimpleUniverse su = new SimpleUniverse(canvas_3D);   //define simpile universe and put canvas in it
        // enable audio device
        defineViewer(su, new Point3d(1.35, 0.35, 2.0));    // set the viewer's location

        BranchGroup scene = new BranchGroup();
        createScene(scene);                           // add contents to the scene branch
        scene.addChild(keyNavigation(su));                   // allow key navigation

        scene.compile();                                     // compile the BranchGroup
        su.addBranchGraph(scene);                            // attach the scene to SimpleUniverse

        setLayout(new BorderLayout());
        add("Center", canvas_3D);
        setVisible(true);
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */

    public static class MyGUI extends JFrame {
        private static final long serialVersionUID = 1L;
        public MyGUI(String title) {
            JFrame frame = new JFrame("MONKECRAFT");
            frame.getContentPane().add(new MONKEECRAFT());
            frame.setSize(1920, 1080);    // set the size of the JFrame
            frame.setVisible(true);
            pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // will exit the program on close

        }
    }

}

