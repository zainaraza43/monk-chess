package com.Main;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class ChessPieces {
    private TransformGroup sceneTG;
    private String textureNameBlack, textureNameWhite;
    private static ArrayList<TransformGroup> blackPieces;
    private static ArrayList<TransformGroup> whitePieces;

    public ChessPieces(TransformGroup sceneTG){
        this.sceneTG = sceneTG;
        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();

    }

    public void createPieces(){
        String [] black = {"Rook", "Knight", "Bishop", "King"};
        String [] black2 = {"Queen", "Bishop", "Knight", "Rook"};

        String [] white = {"Rook", "Knight", "Bishop", "King"};
        String [] white2 = {"Queen", "Bishop", "Knight", "Rook"};
        Pieces("black1", black, black2, 0, -7f, -5f, blackPieces);
        Pieces("white2", white, white2, Math.PI, 7f, 5f, whitePieces);


    }
    public void Pieces(String texture, String [] pieces, String [] pieces2, double rotation, float Z1, float Z2, ArrayList<TransformGroup> list){
        for(int i = 0; i < 16; i ++){
            if(i < 8){
                list.add(loadPiece(texture, "Pawn", 0.8f, new Vector3d(-7 + (2f * i), 0.9f, Z2), rotation));
            }
            if(i >= 8 && i < 12){
                list.add(loadPiece(texture, pieces[i - 8], 0.9f + (1/8f * (i - 8)), new Vector3d(-7 + (2f * (i - 8)), 0.9f + (1/8f * (i - 8)), Z1), rotation));
            }
            if(i >= 12){
                list.add(loadPiece(texture, pieces2[i - 12], (1.3f) - (1/8f * (i - 12)), new Vector3d(1 + (2f * (i - 12)), 1.35f - (1/8f * (i - 12)), Z1), rotation));
            }
        }
        for(TransformGroup t : list){
            this.sceneTG.addChild(t);
        }
    }
    public TransformGroup loadPiece(String texture, String fileName, float scale, Vector3d position, double rotation) {
        TransformGroup objTG = new TransformGroup();
        ObjectFile objectFile = new ObjectFile(ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE | ObjectFile.RESIZE);
        Scene scene = null;
        try{
            System.out.println("attempting to load in object");
            scene = objectFile.load(new File("Assets/Objects/obj_sht/" + fileName + ".obj").toURI().toURL());
        }catch (FileNotFoundException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }
        BranchGroup bg = scene.getSceneGroup();
        Shape3D piece = (Shape3D) bg.getChild(0);
        piece.setName(fileName); // piece name
        piece.setUserData(0); // piece is on the board
        setApp(piece, texture);
        bg.removeAllChildren();

        Transform3D scaleTran = new Transform3D();
        scaleTran.rotY(rotation);
        scaleTran.setScale(scale);
        scaleTran.setTranslation(position);
        objTG.setTransform(scaleTran);

        objTG.addChild(piece);
        return objTG;
    }

    public void setApp(Shape3D piece, String texture){
        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.White));
        PolygonAttributes polygonAttributes = new PolygonAttributes();
        polygonAttributes.setCapability(PolygonAttributes.CULL_NONE);
        appearance.setPolygonAttributes(polygonAttributes);

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.REPLACE);
        appearance.setTexture(setTexture(texture));
        appearance.setTextureAttributes(ta);
        piece.setAppearance(appearance);
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

    public static ArrayList<TransformGroup> getBlackPieces() {
        return blackPieces;
    }

    public static ArrayList<TransformGroup> getWhitePieces() {
        return whitePieces;
    }
}
