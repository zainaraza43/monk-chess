/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * ChessPieces.java
 */
package com.Main;
import com.Util.Pair;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3d;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChessPieces {
    private String textureNameBlack, textureNameWhite; // will be used later for texture picking
    private ArrayList<TransformGroup> blackPieces;
    private ArrayList<TransformGroup> whitePieces;
    public HashMap<String, Pair<Shape3D, Vector2f>> pieces;
    private String [] objNames;
    public ChessPieces() {
        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
        objNames = new String[]{"Pawn", "Rook", "Knight", "Bishop", "Queen", "King"}; // string array to hold names
        pieces = new HashMap<>();

    }

    public void makePieces(){
       String [] pieceNames = {"Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Rook", "Knight", "Bishop", "Queen", "King", "Bishop",
       "Knight", "Rook"};
        createPieces(pieceNames, blackPieces, "mahogany", false);
        createPieces(pieceNames, whitePieces, "gold", true);
    }

    public void loadPieces() { // will load all the objects in called at the start of game
        float [] sizes = {0.8f, 0.9f, 1.025f, 1.15f, 1.275f, 1.35f};
        float [] yValues = {0.65f, 0.78f, 0.8f, 1.03f, 1.17f, 1.25f};
        for (int i = 0; i < objNames.length; i ++) {
            pieces.put(objNames[i], new Pair<>(loadPiece(objNames[i]), new Vector2f(sizes[i], yValues[i])));
        }
    }

    public TransformGroup designPieces(Shape3D piece, String name, Vector3d position, float scale, double rotation, String texture) {
        Transform3D scalar = new Transform3D();
        scalar.rotY(rotation);
        scalar.setScale(scale);
        scalar.setTranslation(position);
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        tg.setTransform(scalar);
        setApp(piece, texture);
        piece.setName(name);
        piece.setUserData(0);
        tg.addChild(piece);
        return tg;
    }

    public void createPieces(String [] pieceList, ArrayList<TransformGroup> list, String texture, boolean isWhite) {
        for (int i = 0; i < 16; i++) {
            float z = isWhite ? 1 : -1;
            Shape3D tmp = new Shape3D();
            tmp.duplicateNode(pieces.get(pieceList[i]).getFirst(), true);
            Vector3d vector3d = new Vector3d(-7 + (2f * (i % 8)), pieces.get(pieceList[i]).getSecond().y + 1/8f, z * (i / 8 * 2 + 5));
            list.add(designPieces(tmp, isWhite ? "White" : "Black", vector3d, pieces.get(pieceList[i]).getSecond().x, isWhite ? Math.PI : 0, texture));
        }
    }

    public Shape3D loadPiece(String fileName) { // function that loads a piece in
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
        appearance.setTextureAttributes(ta);

        appearance.setTexture(setTexture(texture));
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
}
