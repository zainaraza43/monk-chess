/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * ChessBoard.java
 */

package com.Main;
import Launcher.Launcher;
import com.Behavior.MouseRotation;
import com.Behavior.PickBehavior;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

public class ChessBoard {
    private String name;
    private TransformGroup sceneTG;
    public static MouseRotation mouseRotation;
    private ChessPieces chessPieces;
    public Canvas3D canvas3D;
    public BranchGroup sceneBG;

    public ChessBoard(String name, Canvas3D canvas3D, BranchGroup sceneBG){
        this.name = name;
        this.sceneTG = new TransformGroup();
        this.sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.canvas3D = canvas3D;
        this.sceneBG = sceneBG;
    }

    public void createScene(TransformGroup sceneTG){
        generateBase(this.sceneTG, 8, name);
        this.sceneTG.addChild(addSides());
        BoundingSphere mouseBounds = new BoundingSphere(new Point3d(), 1000d);

        mouseRotation = new MouseRotation(this.sceneTG); // mouseRotation used for rotating the board
        mouseRotation.setSchedulingBounds(mouseBounds);

        PickBehavior pickBehavior = new PickBehavior(this.sceneBG, this.sceneTG, canvas3D); // pickBehaviour class
        pickBehavior.setSchedulingBounds(mouseBounds);
        this.sceneTG.addChild(pickBehavior);


        sceneTG.addChild(mouseRotation);
        addChessPieces(this.sceneTG);
        sceneTG.addChild(this.sceneTG);
    }

    public void addChessPieces(TransformGroup sceneTG){ // will add the pieces to the chess board
        chessPieces = Launcher.chessPieces;
        chessPieces.makePieces();
        for(int i = 0; i < 16; i ++){
           sceneTG.addChild(chessPieces.getBlackPieces().get(i));
           sceneTG.addChild(chessPieces.getWhitePieces().get(i));
        }
    }


    // function used to make bottom and sides of chess board
    private static Shape3D generateRectangle(Color3f color, Point3f size, Vector2f scale){ // function to generate rectangle QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        Point3f [] point3fs = new Point3f[4];
        point3fs[0] = new Point3f(-size.x * scale.x, -size.y * scale.y, size.z); // first point -x and -y
        point3fs[1] = new Point3f(size.x * scale.x, -size.y * scale.y, size.z); // second point +x and -y
        point3fs[2] = new Point3f(size.x * scale.x,  size.y * scale.y, size.z); // third point +x and +y
        point3fs[3] = new Point3f(-size.x * scale.x, size.y * scale.y, size.z); // last point in -x and +y
        for (int i = 0; i < 4; i ++) {
            quadArray.setCoordinate(i, point3fs[i]); // loop through and set the coordinates
            quadArray.setColor(i, color); //set the color
        }
        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.White));
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.2f);
        appearance.setTransparencyAttributes(transparencyAttributes);

        Shape3D shape3D = new Shape3D(quadArray, appearance);
        shape3D.setUserData(0);
        return shape3D;
    }

    //function used to make textured top of board
    private static Shape3D generateRectangle(String texture, Point3f size, Vector2f scale){ // function to generate rectangle QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        QuadArray quadArray = new QuadArray(4, QuadArray.TEXTURE_COORDINATE_2 | QuadArray.COORDINATES);
        Point3f [] point3fs = new Point3f[4];
        point3fs[0] = new Point3f(-size.x * scale.x, -size.y * scale.y, size.z); // first point -x and -y
        point3fs[1] = new Point3f(size.x * scale.x, -size.y * scale.y, size.z); // second point +x and -y
        point3fs[2] = new Point3f(size.x * scale.x,  size.y * scale.y, size.z); // third point +x and +y
        point3fs[3] = new Point3f(-size.x * scale.x, size.y * scale.y, size.z); // last point in -x and +y
        for (int i = 0; i < 4; i ++) {
            quadArray.setCoordinate(i, point3fs[i]); // loop through and set the coordinates
        }
        setAppearance(quadArray);
        Shape3D shape3D = new Shape3D(quadArray, texturedApp(texture));
        shape3D.setUserData(0);
        return shape3D;
    }

    //function used to make the entire board
    private static void generateBase(TransformGroup base, float scale, String texture){ // to scale down the base by factor of 0.06

        float x, z;
        Vector3f[] sides = new Vector3f[6];
        sides[4] = new Vector3f(0, 0, 0); // location of top of base
        sides[5] = new Vector3f(0, -(0.04f * scale), 0); // location of bottom of base
        for(int i = 0; i < 6; i ++){
            if(i < 4){ // calculate location of sides
                double a  = (Math.PI/2) * i;
                x = (float) Math.cos(a) * scale; // multiply by scale factor
                z = (float) Math.sin(a) * scale; // multiply by scale factor
                sides[i] = new Vector3f(x, -(0.04f * scale/2), z); // center the sides at -0.524f since they range from 0.024f to -0.24f (0.048 height diff) but 0.08 without scaling factor
            }
            Transform3D transform3D = new Transform3D(); // make a transform3d to move quad array
            if(i < 4){ // rotating the sides accordingly
                double angle = (i % 2 == 0) ? Math.cos(Math.PI/2 * i) * Math.PI/2 : ((i == 3) ? -Math.PI : 0);
                transform3D.rotY(angle); // rotate the quadArray by angle if necessary red and cyan rotated to show properly (front, back) and yellow rotated by -180 to show on left
            }
            else { // rotating the top and bottom of base
                transform3D.rotX(-Math.cos(Math.PI * (i % 4)) * Math.PI / 2); //rotate 90 degrees in positive for bottom and negative for top
            }
            transform3D.setTranslation(sides[i]); // apply the transition move it to the correct spot
            TransformGroup tg = new TransformGroup(); // make a transform group
            tg.setTransform(transform3D); // add the transformation to the group
            if(i < 4){ // sides
                tg.addChild(generateRectangle(MONKEECHESS.Grey, new Point3f(1, 0.04f, 0), new Vector2f(scale, scale/2)));
            }
            if(i == 4){ // textured top
               tg.addChild(generateRectangle(texture, new Point3f(1, 1, 0), new Vector2f(scale, scale)));
            }
            if(i == 5){ // bottom
                tg.addChild(generateRectangle(MONKEECHESS.Grey, new Point3f(1, 1, 0), new Vector2f(scale, scale)));
            }
            base.addChild(tg); // add the transform group to the baseTransformGroup
        }
    }

    //function used to make the side border
    private static Shape3D generateRectangle(Color3f color, Point3f size){ // function to generate rectangle
        QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        Point3f [] point3fs = new Point3f[4];
        point3fs[0] = new Point3f(-size.x, -size.y, size.z); // first point  and
        point3fs[1] = new Point3f(size.x  , -size.y  , size.z); // second point  and
        point3fs[2] = new Point3f(size.x  ,  size.y  , size.z); // third point  and
        point3fs[3] = new Point3f(-size.x  , size.y  , size.z); // last point in  and
        for (int i = 0; i < 4; i ++) {
            quadArray.setCoordinate(i, point3fs[i]); // loop through and set the coordinates
            quadArray.setColor(i, color); //set the color
        }
        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.White));
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.2f);
        appearance.setTransparencyAttributes(transparencyAttributes);

        Shape3D shape3D = new Shape3D(quadArray, appearance);
        shape3D.setUserData(0);
        return shape3D;
    }

    // function used to make the entire border
    public TransformGroup addSides(){ // will add sides that will hold a-h and 1 - 8 for coordinate system
        TransformGroup base = new TransformGroup();
        float x, z;
        Point3f [] point3fs = new Point3f[4];
        for (int i = 0; i < 4; i ++){
            double a = Math.PI / 2 * i;
            x = (float) Math.cos(a) * 8.5f;
            z = (float) Math.sin(a) * 8.5f;
            point3fs[i] = new Point3f(x, 0, z);
            Transform3D transform3D = new Transform3D();
            transform3D.rotX(-Math.PI / 2);
            transform3D.setTranslation(new Vector3d(point3fs[i]));
            TransformGroup tg = new TransformGroup(transform3D);
            if(i % 2 == 0){
                tg.addChild(generateRectangle(MONKEECHESS.Grey, new Point3f(0.5f, 9, 0)));
            }else{

                tg.addChild(generateRectangle(MONKEECHESS.Grey, new Point3f(8, 0.5f, 0)));
            }
            base.addChild(tg);
        }
        return base;
    }

    public static void setAppearance(QuadArray quadArray){ // will set the appearance of the chessBoard
        float [][] coords = {{0f, 0f}, {1f, 0f}, {1f, 1f}, {0f, 1f}};
        for(int i = 0; i < 4; i ++){
            quadArray.setTextureCoordinate(0, i, coords[i]);
        }
    }

    public static Texture setTexture(String name){
        TextureLoader loader = new TextureLoader("Assets/Textures/boardpics/" + name + ".jpg", null); // load in the image
        ImageComponent2D imageComponent2D = loader.getImage(); //get image
        if(imageComponent2D == null){ // if image is not found
            System.out.println("Error opening image");
        }

        Texture2D texture2D = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, imageComponent2D.getWidth(), imageComponent2D.getHeight());
        texture2D.setImage(0, imageComponent2D); //set the image on the texture
        texture2D.setEnable(true);
        return texture2D; // return the texture with the image
    }

    public static Appearance texturedApp(String name){
        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.White));

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTextureAttributes(ta);

        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.2f);
        appearance.setTransparencyAttributes(transparencyAttributes);
        appearance.setTexture(setTexture(name));
        return appearance;
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
}
