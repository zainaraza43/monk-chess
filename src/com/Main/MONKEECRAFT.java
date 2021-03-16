package com.Main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.JFrame;

import com.Behavior.MouseBeh;
import com.jogamp.newt.event.MouseAdapter;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.behaviors.mouse.*;
import org.jogamp.java3d.utils.behaviors.vp.OrbitBehavior;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.scenegraph.io.state.org.jogamp.java3d.utils.behaviors.mouse.MouseBehaviorState;
import org.jogamp.java3d.utils.universe.PlatformGeometry;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;
import org.omg.CORBA.Object;
import sun.java2d.pipe.SpanShapeRenderer;


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


    private static Background generateBackground(){ // will return a background
        Background background = new Background(); // make a background
        background.setImageScaleMode(Background.SCALE_FIT_MAX); // scale it to max
        TextureLoader loader = new TextureLoader("Assets/Background/background.jpg", null); // load the image
        background.setApplicationBounds(new BoundingSphere(new Point3d(), 1000d)); // set the bounds
        background.setImage(loader.getImage()); // set the image
        return background;
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

        DirectionalLight directionalLight = new DirectionalLight(true ,White, new Vector3f(-0.3f, 0.2f, -1.0f));
        directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000d));
        State.addChild(directionalLight);

        DirectionalLight directionalLight1 = new DirectionalLight(true ,White, new Vector3f(0.3f, -0.2f, 1.0f));
        directionalLight1.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000d));
        State.addChild(directionalLight1);


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
        BoundingSphere view_bounds = new BoundingSphere(new Point3d(), 1000.0);
        keyNavBeh.setSchedulingBounds(view_bounds);
        return keyNavBeh;
    }

    private MouseBeh mouseNavigation(SimpleUniverse su){
        Viewer viewer = su.getViewer();
        TransformGroup view = viewer.getViewingPlatform().getViewPlatformTransform();
        MouseBeh mouseBeh = new MouseBeh(view);
        mouseBeh.setCapability(MouseBeh.INVERT_INPUT);
        mouseBeh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
        return mouseBeh;
    }

    private static BranchGroup createGround() throws FileNotFoundException {
        BranchGroup branchGroup = new BranchGroup();
        TransformGroup tg = new TransformGroup();
        Transform3D transform3D = new Transform3D();

        transform3D.setTranslation(new Vector3f(0, -7.0f, 0));
        transform3D.setScale(100);
        tg.setTransform(transform3D);
        int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
        ObjectFile f = new ObjectFile(flags);
        File file = new File("Assets/Background/back.obj");
        Scene s = null;
        try{
            s = f.load(file.toURI().toURL());
        }catch (FileNotFoundException | ParsingErrorException | IncorrectFormatException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }
        tg.addChild(s.getSceneGroup());
        branchGroup.addChild(tg);
        return branchGroup;
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

    public static TransformGroup addCubes(){
        TransformGroup sceneTg = new TransformGroup();
        Vector3f [] points = new Vector3f[6];
        points[4] = new Vector3f(0, 4, 0);
        points[5] = new Vector3f(0, -4, 0);
        float x, z, r = 4.0f;
        for(int i = 0; i < 6; i ++){
            if(i < 4){
                double a = (Math.PI / 2) * i;
                x = (float) Math.cos(a) * r;
                z = (float) Math.sin(a) * r;
                points[i] = new Vector3f(x, 0, z);
            }
            Transform3D transform3D = new Transform3D();
            transform3D.setTranslation(points[i]);
            TransformGroup tmp = new TransformGroup(transform3D);
            tmp.addChild(new ColorCube(0.35));
            sceneTg.addChild(tmp);
        }
        return sceneTg;
    }


    public static RotationInterpolator rotateBehavior(TransformGroup rotTG, Alpha rotAlpha) {
        rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
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
        PLight(sceneBG);
        CreateLight(sceneBG);
        try {
            sceneBG.addChild(createGround());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sceneBG.addChild(generateBackground());
        sceneBG.addChild(generateAxis(Yellow, 1f));
        sceneBG.addChild(addCubes());
        BoundingSphere mouseBounds = new BoundingSphere(new Point3d(), 10d);

        MouseTranslate mouseTranslate = new MouseTranslate(MouseTranslate.INVERT_INPUT);
        mouseTranslate.setTransformGroup(sceneTG);
        mouseTranslate.setSchedulingBounds(mouseBounds);
        sceneBG.addChild(mouseTranslate);

        MouseZoom mouseZoom = new MouseZoom(MouseZoom.INVERT_INPUT);
        mouseZoom.setTransformGroup(sceneTG);
        mouseZoom.setSchedulingBounds(mouseBounds);
        sceneBG.addChild(mouseZoom);



    }




    public MONKEECRAFT(){// contructor for lab7
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas_3D = new Canvas3D(config);//define a canvas

        OrbitBehavior orbitBehavior = new OrbitBehavior(canvas_3D, OrbitBehavior.REVERSE_ROTATE);
        orbitBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
        SimpleUniverse su = new SimpleUniverse(canvas_3D);   //define simpile universe and put canvas in it
//        defineViewer(su, new Point3d(1.35, 0.35, 2.0));    // set the viewer's location
        su.getViewingPlatform().setViewPlatformBehavior(orbitBehavior);

        orbitBehavior.setRotXFactor(2);
        orbitBehavior.setRotYFactor(2);

        BranchGroup scene = new BranchGroup();
        createScene(scene, su);                           // add contents to the scene branch
        scene.addChild(keyNavigation(su));                   // allow key navigation
        scene.addChild(mouseNavigation(su));
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
            frame.setSize(850, 700);    // set the size of the JFrame
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // will exit the program on close

        }
    }
}

