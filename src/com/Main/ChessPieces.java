package com.Main;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class ChessPieces {
    private TransformGroup sceneTG;

    public ChessPieces(TransformGroup sceneTG){
        this.sceneTG = sceneTG;
    }

    public void CreatePieces(){
        this.sceneTG.addChild(loadPiece());

    }
    public TransformGroup loadPiece() {
        TransformGroup objTG = new TransformGroup();
        ObjectFile objectFile = new ObjectFile(ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE | ObjectFile.RESIZE);
        Scene scene = null;
        try{
            scene = objectFile.load(new File("Assets/Objects/obj_sht/Bishop.obj").toURI().toURL());
        }catch (FileNotFoundException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }
        BranchGroup bg = scene.getSceneGroup();
        Shape3D piece = (Shape3D) bg.getChild(0);
        setApp(piece);
        bg.removeAllChildren();

        Transform3D scale = new Transform3D();
        scale.setScale(1.8);
        scale.setTranslation(new Vector3d(0.8f, 1.8, 1f));
        objTG.setTransform(scale);


        objTG.addChild(piece);
        return objTG;
    }

    public void setApp(Shape3D piece){
        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.White));
        appearance.setTexture(setTexture("black1"));
        piece.setAppearance(appearance);
    }

    public static Texture setTexture(String name){
        TextureLoader loader = new TextureLoader("Assets/Textures/" + name + ".png", null); // load in the image
        ImageComponent2D imageComponent2D = loader.getImage(); //get image
        if(imageComponent2D == null){ // if image is not found
            System.out.println("Error opening image");
        }

        Texture2D texture2D = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, imageComponent2D.getWidth(), imageComponent2D.getHeight());
        texture2D.setImage(0, imageComponent2D); //set the image on the texture
        texture2D.setEnable(true);
        return texture2D; // return the texture with the image
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
