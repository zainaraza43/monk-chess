package com.Main;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class ChessBoard {
    private String name;
    private TransformGroup sceneTG;
    public ChessBoard(String name){
        this.name = name;
        this.sceneTG = new TransformGroup();
    }

    public void createScene(BranchGroup sceneBG){
        createChessBoard(this.sceneTG);
        sceneBG.addChild(this.sceneTG);
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
}
