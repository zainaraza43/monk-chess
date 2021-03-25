/*
 * Class that will load in the game chess board provided a string which is the texture name
 */

package com.Main;
import com.Behavior.MouseRotation;
import com.Behavior.MouseZoom;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.mouse.MouseTranslate;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class ChessBoard {
    private String name;
    private TransformGroup sceneTG;
    private MouseRotation mouseRotation;
    public ChessBoard(String name){
        this.name = name;
        this.sceneTG = new TransformGroup();
        this.sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    }

    public void createScene(TransformGroup sceneTG){
        createChessBoard(this.sceneTG);
        this.sceneTG.addChild(addSides());
        BoundingSphere mouseBounds = new BoundingSphere(new Point3d(), 1000d);
        mouseRotation = new MouseRotation(this.sceneTG);
        mouseRotation.setSchedulingBounds(mouseBounds);
        sceneTG.addChild(mouseRotation);

        ChessPieces chessPieces = new ChessPieces(this.sceneTG);
        chessPieces.CreatePieces();
        sceneTG.addChild(this.sceneTG);
    }

    public void createChessBoard(TransformGroup objectTG){
        Transform3D rotation3D = new Transform3D(); // rotationTransform3D
        rotation3D.setScale(8);
        TransformGroup rotationGroup = new TransformGroup(rotation3D);
        ObjectFile objectFile = new ObjectFile(ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE | ObjectFile.RESIZE);
        Scene scene = null;
        try{
            scene = objectFile.load(new File("Assets/Objects/ChessBoard/back.obj").toURI().toURL());
        }catch (FileNotFoundException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }
        BranchGroup branchGroup = scene.getSceneGroup();
        Shape3D chessBoard = (Shape3D) branchGroup.getChild(0);
        setAppearance(chessBoard);
        rotationGroup.addChild(branchGroup);
        objectTG.addChild(rotationGroup);
    }

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
        return new Shape3D(quadArray);
    }

    public TransformGroup addSides(){
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

    public void setAppearance(Shape3D board){
        Appearance app = texturedApp(this.name);
        app.setTransparencyAttributes(setTransparency(TransparencyAttributes.FASTEST, 0.2f));
        board.setAppearance(app);
        board.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    }

    public static Texture setTexture(String name){
        TextureLoader loader = new TextureLoader("Assets/Textures/" + name + ".jpg", null); // load in the image
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
        appearance.setTexture(setTexture(name));

        PolygonAttributes polygonAttributes = new PolygonAttributes();
        polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.REPLACE);


        float scale = 4f;
        Transform3D t = new Transform3D();
        t.setScale(scale);
        ta.setTextureTransform(t);

        appearance.setPolygonAttributes(polygonAttributes);
        appearance.setTextureAttributes(ta);
        return appearance;
    }

    public static TransparencyAttributes setTransparency(int mode, float value){
        return new TransparencyAttributes(mode, value);
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
