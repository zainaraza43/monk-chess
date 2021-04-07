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
import com.Util.Sounds;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import java.awt.*;

public class ChessBoard {
    private String name;
    private TransformGroup sceneTG, objTG;
    public static MouseRotation mouseRotation;
    private ChessPieces chessPieces;
    public Canvas3D canvas3D;
    public BranchGroup sceneBG;
    public static boolean isWhite;
    public Sounds sounds;

    public ChessBoard(String name, Canvas3D canvas3D, BranchGroup sceneBG, TransformGroup sceneTG){
        this.sceneTG = sceneTG;
        this.name = name;
        this.canvas3D = canvas3D;
        this.sceneBG = sceneBG;
        sounds = new Sounds();
    }

    public void createScene(){
        objTG = new TransformGroup();
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE | TransformGroup.ALLOW_CHILDREN_WRITE | TransformGroup.ALLOW_CHILDREN_EXTEND | TransformGroup.ALLOW_CHILDREN_READ);
        objTG.addChild(makeBoard(name));
        objTG.addChild(addSides());
        addText(objTG);
        BoundingSphere mouseBounds = new BoundingSphere(new Point3d(), 1000d);

        mouseRotation = new MouseRotation(objTG); // mouseRotation used for rotating the board
        mouseRotation.setSchedulingBounds(mouseBounds);

        PickBehavior pickBehavior = new PickBehavior(this, this.sceneBG, objTG, canvas3D); // pickBehaviour class
        pickBehavior.setSchedulingBounds(mouseBounds);
        objTG.addChild(pickBehavior);

        sounds.playSound(sounds.getSoundNames()[0]);

        objTG.addChild(mouseRotation);
        addChessPieces(objTG);
        this.sceneTG.addChild(objTG);
    }

    public void addChessPieces(TransformGroup sceneTG){ // will add the pieces to the chess board
        chessPieces = Launcher.chessPieces;
        chessPieces.makePieces();
        for(int i = 0; i < 16; i ++){
            sceneTG.addChild(chessPieces.getBlackPieces().get(i));
            sceneTG.addChild(chessPieces.getWhitePieces().get(i));
        }
    }

    public void removeChessPiece(BranchGroup piece){
        objTG.removeChild(piece);
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

    public TransformGroup makeBoard(String name){
        Transform3D scalar = new Transform3D();
        scalar.rotX(-Math.PI / 2);
        scalar.setTranslation(new Vector3d(0, 0, 0));
        TransformGroup boardTG = new TransformGroup();
        boardTG.setTransform(scalar);
        Shape3D board = generateRectangle(name, new Point3f(1, 1, 0), new Vector2f(8, 8));
        board.setPickable(false);
        board.setCollidable(false);
        boardTG.addChild(board);
        return boardTG;
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

    public static TransformGroup generateText3d(String txt, double scl, Vector3f vector, Color3f clr, boolean isFlipped){ // function to generate text3D
        Font font = new Font("Arial", Font.PLAIN, 1);
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3d = new Font3D(font, myExtrude);
        Text3D text3D = new Text3D(font3d, txt); // will add the text at the point

        Transform3D scaler = new Transform3D();
        Transform3D scalar2 = new Transform3D();
        if(isFlipped){
            scaler.rotY(Math.PI);
            scalar2.rotX(-Math.PI / 2);
        }else {
            scaler.rotX(-Math.PI / 2);
        }
        scaler.setTranslation(vector);
        scaler.mul(scaler, scalar2);
        scaler.setScale(scl);
        TransformGroup scene_TG = new TransformGroup();
        scene_TG.setTransform(scaler);
        Appearance appearance = new Appearance();
        ColoringAttributes coloringAttributes = new ColoringAttributes(); // will add a colorAttribute to the text
        coloringAttributes.setColor(clr); // set the color
        coloringAttributes.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
        appearance.setColoringAttributes(coloringAttributes); // add the color attribute to appearance
        appearance.setMaterial(setMaterial(clr));
        scene_TG.addChild(new Shape3D(text3D, appearance));
        return scene_TG;
    }
    public void addText(TransformGroup objTG){
        String[] bottomText = {"a", "b", "c", "d", "e", "f", "g", "h", "1", "2", "3", "4", "5", "6", "7", "8"};
        for(int i = 0; i < 16; i ++){
            objTG.addChild(generateText3d(bottomText[i], 0.8f, new Vector3f(i < 8 ? -7 + 2f *( i % 8): -9 , 0, i < 8 ? 8.8f : 7 - 2f * (i % 8)), MONKEECHESS.White, false));
        }

        for(int i = 0; i < 16; i ++){
            objTG.addChild(generateText3d(bottomText[i], 0.8f, new Vector3f(i < 8 ? 7 - 2f *( i % 8): 9 , 0, i < 8 ? -8.8f : -7 + 2f * (i % 8)), MONKEECHESS.White, true));
        }
    }

}
