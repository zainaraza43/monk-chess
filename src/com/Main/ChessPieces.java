package com.Main;

import com.Util.Pair;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChessPieces {
    private String textureNameBlack, textureNameWhite;
    private ArrayList<TransformGroup> blackPieces;
    private ArrayList<TransformGroup> whitePieces;
    public HashMap<String, Pair<Shape3D, Float>> pieces;
    private String [] objNames;
    public ChessPieces() {
        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
        objNames = new String[]{"Pawn", "Rook", "Knight", "Bishop", "King", "Queen"};
        pieces = new HashMap<>();

    }

    public void makePieces(){
       String [] blackSide = {"Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Rook", "Knight", "Bishop", "Queen", "King", "Bishop",
       "Knight", "Rook"};

        String [] whiteSide = {"Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Rook", "Knight", "Bishop", "King", "Queen", "Bishop",
                "Knight", "Rook"};
        createPieces(blackSide, blackPieces, "mahogany", false);
        createPieces(whiteSide, whitePieces, "gold", true);
    }

    public void loadPieces() {
        float [] sizes = {0.8f, 0.9f, 1.025f, 1.15f, 1.275f, 1.4f};
        for (int i = 0; i < objNames.length; i ++) {
            pieces.put(objNames[i], new Pair<Shape3D, Float>(loadPiece(objNames[i]),sizes[i]));
        }
    }

    public TransformGroup designPieces(Shape3D piece, Vector3d position, float scale, double rotation, String texture) {
        Transform3D scalar = new Transform3D();
        scalar.rotY(rotation);
        scalar.setScale(scale);
        scalar.setTranslation(position);
        TransformGroup tg = new TransformGroup();
        tg.setTransform(scalar);
        setApp(piece, texture);
        tg.addChild(piece);
        return tg;
    }

    public void createPieces(String [] pieceList, ArrayList<TransformGroup> list, String texture, boolean isWhite) {
        for (int i = 0; i < 16; i++) {
            float z = isWhite ? 1 : -1;
            Shape3D tmp = new Shape3D();
            tmp.duplicateNode(pieces.get(pieceList[i]).getFirst(), true);
            Vector3d vector3d = new Vector3d(-7 + (2f * (i % 8)), pieces.get(pieceList[i]).getSecond(), z * (i / 8 * 2 + 5));
            list.add(designPieces(tmp, vector3d, pieces.get(pieceList[i]).getSecond(), isWhite ? Math.PI : 0, texture));
        }
    }

    public Shape3D loadPiece(String fileName) {
        ObjectFile objectFile = new ObjectFile(ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE | ObjectFile.RESIZE);
        Scene scene = null;
        try {
            System.out.println("attempting to load in object");
            scene = objectFile.load(new File("Assets/Objects/obj_sht/" + fileName + ".obj").toURI().toURL());
        } catch (FileNotFoundException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }
        BranchGroup bg = scene.getSceneGroup();
        Shape3D piece = (Shape3D) bg.getChild(0);
        piece.setName(fileName); // piece name
        piece.setUserData(0); // piece is on the board

        bg.removeAllChildren();

        return piece;
    }

    public ArrayList<TransformGroup> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<TransformGroup> getBlackPieces() {
        return blackPieces;
    }
    public void setApp(Shape3D piece, String texture) {
        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.White));

        ColoringAttributes ca = new ColoringAttributes(MONKEECHESS.Black, ColoringAttributes.SHADE_GOURAUD);
        ca.setColor(0, 0, 0);
        appearance.setColoringAttributes(ca);

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTexture(setTexture(texture));
        appearance.setTextureAttributes(ta);
        piece.setAppearance(appearance);
    }

    public static Texture setTexture(String name) {
        TextureLoader loader = new TextureLoader("Assets/Textures/" + name + ".jpg", null); // load in the image
        ImageComponent2D imageComponent2D = loader.getImage(); //get image
        if (imageComponent2D == null) { // if image is not found
            System.out.println("Error opening image");
        }

        Texture2D texture2D = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, imageComponent2D.getWidth(), imageComponent2D.getHeight());
        texture2D.setImage(0, imageComponent2D); //set the image on the texture
        texture2D.setEnable(true);
        return texture2D; // return the texture with the image
    }

    public static Material setMaterial(Color3f clr) {
        int SH = 100;               // 10
        Material ma = new Material();
        Color3f c = new Color3f(0.6f * clr.x, 0.6f * clr.y, 0.6f * clr.z);
        ma.setAmbientColor(c);
        ma.setEmissiveColor(new Color3f(0, 0, 0));
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

//    public static ArrayList<TransformGroup> getBlackPieces() {
//        return blackPieces;
//    }
//
//    public static ArrayList<TransformGroup> getWhitePieces() {
//        return whitePieces;
//    }
}
