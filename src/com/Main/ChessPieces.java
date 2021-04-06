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
import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3d;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChessPieces {
    public static  String textureNameBlack="mahogany";
    public static String textureNameWhite="gold"; // will be used later for texture picking
    private ArrayList<Piece> blackPieces;
    private ArrayList<Piece> whitePieces;
    public HashMap<String, Pair<Shape3D, Vector2f>> pieces;
    public static String [] objNames;
    public static HashMap<String, ImageIcon> icons;

    public ChessPieces(String black,String white ){
        blackPieces = new ArrayList<Piece>();
        whitePieces = new ArrayList<Piece>();
        objNames = new String[]{"Pawn", "Rook", "Knight", "Bishop", "Queen", "King"}; // string array to hold names
        pieces = new HashMap<>();
        icons = new HashMap<>();
        textureNameBlack = black;
        textureNameWhite = white;
    }

    public void makePieces(){
       String [] pieceNames = {"Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Pawn","Rook", "Knight", "Bishop", "Queen", "King", "Bishop",
       "Knight", "Rook"};
        createPieces(pieceNames, blackPieces, textureNameBlack, false);
        createPieces(pieceNames, whitePieces, textureNameWhite, true);
    }

    public void loadPieces() { // will load all the objects in called at the start of game
        float [] sizes = {0.8f, 0.9f, 1.025f, 1.15f, 1.275f, 1.35f};
        float [] yValues = {0.65f, 0.78f, 0.8f, 1.03f, 1.17f, 1.25f};
        for (int i = 0; i < objNames.length; i ++) {
            pieces.put(objNames[i], new Pair<>(loadPiece(objNames[i]), new Vector2f(sizes[i], yValues[i])));
            icons.put("Black_" + objNames[i], new ImageIcon("Assets/Icons/Black_" + objNames[i] + ".png"));
            icons.put("White_" + objNames[i], new ImageIcon("Assets/Icons/White_" + objNames[i] + ".png"));
        }
    }
    public void createPieces(String [] pieceList, ArrayList<Piece> list, String texture, boolean isWhite) {
        for (int i = 0; i < 16; i++) {
            float z = isWhite ? 1 : -1;
            Obj3D tmp = new Obj3D();
            tmp.duplicateNode(pieces.get(pieceList[i]).getFirst(), true);
            Vector3d vector3d = new Vector3d(-7 + (2f * (i % 8)), pieces.get(pieceList[i]).getSecond().y + 1/8f, z * (i / 8 * 2 + 5));
            Piece piece = new Piece(tmp, pieceList[i], isWhite ? "White" : "Black", vector3d, pieces.get(pieceList[i]).getSecond().x, isWhite ? Math.PI : 0, texture);
            list.add(piece);
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

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

}
