/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * ChessBoard.java
 */
package com.Main;
import Launcher.Launcher;
import com.Behavior.CheckKeyboardBehaviour;
import com.Behavior.Collision;
import com.Behavior.MouseRotation;
import com.Behavior.PickBehavior;
import com.Util.Sounds;
import com.net.Client;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import java.awt.*;
import java.util.ArrayList;

public class ChessBoard {
    public static final int TURN_WHITE = 1;
    public static final int TURN_BLACK = 2;
    public MouseRotation mouseRotation;
    public OverlayCanvas3D overlayCanvas3D;
    public BranchGroup sceneBG;
    public boolean rotate, enablePicking, gameOver;
    public Sounds sounds;
    private String name;
    public TransformGroup sceneTG, objTG;
    public ChessPieces chessPieces;
    public Client client;
    public PickBehavior pickBehavior;
    private GameOver gameOverScreen;
    private CheckKeyboardBehaviour checkKeyboardBeh;
    public int turn = TURN_WHITE;

    public ChessBoard(String name, OverlayCanvas3D overlayCanvas3D, BranchGroup sceneBG, TransformGroup sceneTG) {
        this.sceneTG = sceneTG;
        this.name = name;
        this.overlayCanvas3D = overlayCanvas3D;
        this.sceneBG = sceneBG;
        sounds = new Sounds();
        rotate = false;
        gameOverScreen = new GameOver();

        if (Launcher.isMultiplayer) { // if multiplayer is selected

            overlayCanvas3D.setStatus("Waiting for players....");
            client = new Client(this);
            client.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            enablePicking = client.getPlayerID() == 1;
            overlayCanvas3D.setColor(turn == client.getPlayerID() ? Color.GREEN: Color.RED);
            overlayCanvas3D.setStatus(turn == client.getPlayerID() ? "Your move" : "Their move");

            checkKeyboardBeh = new CheckKeyboardBehaviour(this);
            overlayCanvas3D.addKeyListener(checkKeyboardBeh);
        }else{ // if single player is selected
            overlayCanvas3D.setStatus("Single player");
        }
    }

    public void createScene() {
        objTG = new TransformGroup();
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE | TransformGroup.ALLOW_CHILDREN_WRITE | TransformGroup.ALLOW_CHILDREN_EXTEND | TransformGroup.ALLOW_CHILDREN_READ);
        objTG.addChild(makeBoard(name));
        objTG.addChild(addSides());
        addText(objTG);
        BoundingSphere mouseBounds = new BoundingSphere(new Point3d(), 1000d);

        mouseRotation = new MouseRotation(this, objTG); // mouseRotation used for rotating the board
        mouseRotation.setSchedulingBounds(mouseBounds);

        pickBehavior = new PickBehavior(this, this.sceneBG, objTG, overlayCanvas3D); // pickBehaviour class
        pickBehavior.setSchedulingBounds(mouseBounds);
        objTG.addChild(pickBehavior);

        sounds.playSound(sounds.getSoundNames()[0]);

        objTG.addChild(mouseRotation);
        addChessPieces(objTG);
        this.sceneTG.addChild(objTG);
        if(Launcher.isMultiplayer && client.getPlayerID() == TURN_BLACK) {
            rotateBoard();
        }
    }

    public void addChessPieces(TransformGroup sceneTG) { // will add the pieces to the chess board
        chessPieces = Launcher.chessPieces;
        chessPieces.makePieces();
        for (int i = 0; i < 16; i++) {
            if (Launcher.isMultiplayer) {
                Piece piece = client.getPlayerID() == 1 ? chessPieces.getBlackPieces().get(i) : chessPieces.getWhitePieces().get(i);
                piece.getPiece().setPickable(false); // disable pick
            }
            sceneTG.addChild(chessPieces.getBlackPieces().get(i));
            sceneTG.addChild(chessPieces.getWhitePieces().get(i));
        }
    }

    public void swapTurn(){ // will swap the turn
        enablePicking = !enablePicking;
        turn = 3 - turn;
        overlayCanvas3D.setColor(turn == client.getPlayerID() ? Color.GREEN: Color.RED);
        overlayCanvas3D.setStatus(turn == client.getPlayerID() ? "Your move" : "Their move");
    }

    //  [isWhite]   [index of piece to move]   [new x pos]    [new z pos]    [piece to remove]
    public void updateBoard(boolean pieceIsWhite, int indexOfMovingPiece, double newX, double newZ, int collisionIndex, boolean isGameOver) {
        if(isGameOver){
            gameOver = isGameOver;
            String piece = pieceIsWhite ? "White" : "Black";
            overlayCanvas3D.setColor(Color.RED);
            overlayCanvas3D.setStatus("GameOver " + piece + " won!");
            System.exit(0);
        }
        swapTurn();
        ArrayList<Piece> pieceList = pieceIsWhite ? chessPieces.getWhitePieces() : chessPieces.getBlackPieces();
        ArrayList<Piece> oppList = pieceIsWhite ? chessPieces.getBlackPieces() : chessPieces.getWhitePieces();
        Piece movingPiece = pieceList.get(indexOfMovingPiece);
        Vector3d newPosition = movingPiece.getPosition();
        newPosition.x = newX;
        newPosition.z = newZ;
        movingPiece.setPosition(newPosition);

        if (collisionIndex > -1) {
            addIcon(oppList.get(collisionIndex));
            sounds.validMove();
            removeChessPiece(oppList.get(collisionIndex));
            oppList.remove(collisionIndex);
        }


        for (Piece p:pieceList) {
            p.makePieceNormal();
        }
    }

    public void sendData(int pieceIndex) { // data to send over to the other client
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ArrayList<Piece> pieceList = client.getPlayerID() == TURN_WHITE ? chessPieces.getWhitePieces() : chessPieces.getBlackPieces();
                ArrayList<Piece> oppList = client.getPlayerID() == TURN_WHITE ? chessPieces.getBlackPieces() : chessPieces.getWhitePieces();
                Piece pieceToMove = pieceList.get(pieceIndex);
                boolean pieceIsWhite = pieceToMove.isWhite();
                int index = pieceList.indexOf(pieceToMove);
                Vector3d pos = pieceToMove.getPosition();
                double newXPos = pos.x;
                double newZPos = pos.z;
                int newPieceIndex = ChessPieces.isChangedPiece ? chessPieces.pieceChangedIndex : -1;

                int collisionIndex = -1;
                if (Collision.isColliding) {
                    collisionIndex = Collision.collidingIndex;
                }
                //                        [piece color]       [piece index]  [new posX]     [new posZ]       [colliding index]      [if game ended]   [new piece for pawn-->queen]
                String toSend = "data " + pieceIsWhite + " " + index + " " + newXPos + " " + newZPos + " " + collisionIndex + " " + gameOver + " " + newPieceIndex;
                System.out.println("SENDING: " + toSend); // printing msg to send
                swapTurn();
                client.sendMessage(toSend);
                if(gameOver){ // if game ended and the current client won
                    overlayCanvas3D.setColor(Color.GREEN);
                    overlayCanvas3D.setStatus("You won!");
                    sounds.gameWon();
                    gameOverScreen.endGame();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    public void removeChessPiece(BranchGroup piece) { // will remove a chessPiece
        objTG.removeChild(piece);
    }

    public void addChessPiece(Piece piece) { // will add a chessPiece
        objTG.addChild(piece);
    }
    public void addIcon(Piece deadPiece) { // will update the iconPanels
        OverlayPanels panels = deadPiece.isWhite() ? MONKEECHESS.overlay.getRightPanel() : MONKEECHESS.overlay.getLeftPanel();
        panels.addIcon(deadPiece.getColor() + "_" + deadPiece.getName());
        panels.repaint();
    }

    //function used to make textured top of board
    private static Shape3D generateRectangle(String texture, Point3f size, Vector2f scale) { // function to generate rectangle QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        QuadArray quadArray = new QuadArray(8, QuadArray.TEXTURE_COORDINATE_2 | QuadArray.COORDINATES);
        Point3f[] point3fs = new Point3f[4];
        point3fs[0] = new Point3f(-size.x * scale.x, -size.y * scale.y, size.z); // first point -x and -y
        point3fs[1] = new Point3f(size.x * scale.x, -size.y * scale.y, size.z); // second point +x and -y
        point3fs[2] = new Point3f(size.x * scale.x, size.y * scale.y, size.z); // third point +x and +y
        point3fs[3] = new Point3f(-size.x * scale.x, size.y * scale.y, size.z); // last point in -x and +y
        for (int i = 0; i < 4; i++) {
            quadArray.setCoordinate(i, point3fs[i]); // loop through and set the coordinates
            quadArray.setCoordinate(4+3-i, point3fs[i]); // loop through and set the coordinates
        }
        setAppearance(quadArray);
        Shape3D shape3D = new Shape3D(quadArray, texturedApp(texture));
        shape3D.setUserData(0);
        return shape3D;
    }

    public TransformGroup makeBoard(String name) { // will make the chess board
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

    public void rotateBoard() { // will rotate the board if needed
        Transform3D tmp = new Transform3D();
        Transform3D tmp2 = new Transform3D();
        objTG.getTransform(tmp);

        tmp2.rotY(rotate ? -Math.PI : Math.PI);
        tmp.mul(tmp, tmp2);

        objTG.setTransform(tmp);
        rotate = !rotate;
    }

    //function used to make the side border
    private static Shape3D generateRectangle(Color3f color, Point3f size) { // function to generate rectangle
        QuadArray quadArray = new QuadArray(8, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        Point3f[] point3fs = new Point3f[4];
        point3fs[0] = new Point3f(-size.x, -size.y, size.z); // first point  and
        point3fs[1] = new Point3f(size.x, -size.y, size.z); // second point  and
        point3fs[2] = new Point3f(size.x, size.y, size.z); // third point  and
        point3fs[3] = new Point3f(-size.x, size.y, size.z); // last point in  and
        for (int i = 0; i < 4; i++) {
            quadArray.setCoordinate(i, point3fs[i]); // loop through and set the coordinates
            quadArray.setCoordinate(4+3-i, point3fs[i]); // loop through and set the coordinates
            quadArray.setColor(i, color); //set the color
        }
        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.White));
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.2f);
        appearance.setTransparencyAttributes(transparencyAttributes);

        Shape3D shape3D = new Shape3D(quadArray, appearance);
        shape3D.setUserData(0);
        shape3D.setCollidable(false);
        shape3D.setPickable(false);
        return shape3D;
    }

    // function used to make the entire border
    public TransformGroup addSides() { // will add sides that will hold a-h and 1 - 8 for coordinate system
        TransformGroup base = new TransformGroup();
        float x, z;
        Point3f[] point3fs = new Point3f[4];
        for (int i = 0; i < 4; i++) {
            double a = Math.PI / 2 * i;
            x = (float) Math.cos(a) * 8.5f;
            z = (float) Math.sin(a) * 8.5f;
            point3fs[i] = new Point3f(x, 0, z);
            Transform3D transform3D = new Transform3D();
            transform3D.rotX(-Math.PI / 2);
            transform3D.setTranslation(new Vector3d(point3fs[i]));
            TransformGroup tg = new TransformGroup(transform3D);
            if (i % 2 == 0) {
                tg.addChild(generateRectangle(MONKEECHESS.Grey, new Point3f(0.5f, 9, 0)));
            } else {

                tg.addChild(generateRectangle(MONKEECHESS.Grey, new Point3f(8, 0.5f, 0)));
            }
            base.addChild(tg);
        }
        return base;
    }

    public static void setAppearance(QuadArray quadArray) { // will set the appearance of the chessBoard
        float[][] coords = {{0f, 0f}, {1f, 0f}, {1f, 1f}, {0f, 1f}};
        for (int i = 0; i < 4; i++) {
            quadArray.setTextureCoordinate(0, i, coords[i]);
        }
    }

    public static Texture setTexture(String name) {
        TextureLoader loader = new TextureLoader("Assets/Textures/boardpics/" + name + ".jpg", null); // load in the image
        ImageComponent2D imageComponent2D = loader.getImage(); //get image
        if (imageComponent2D == null) { // if image is not found
            System.out.println("Error opening image");
        }

        Texture2D texture2D = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, imageComponent2D.getWidth(), imageComponent2D.getHeight());
        texture2D.setImage(0, imageComponent2D); //set the image on the texture
        texture2D.setEnable(true);
        return texture2D; // return the texture with the image
    }

    public static Appearance texturedApp(String name) {
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
        Color3f c = new Color3f(0.6f * clr.x, 0.6f * clr.y, 0.6f * clr.z);
        ma.setAmbientColor(c);
        ma.setEmissiveColor(new Color3f(0, 0, 0));
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

    public static TransformGroup generateText3d(String txt, double scl, Vector3f vector, Color3f clr, boolean isFlipped) { // function to generate text3D
        Font font = new Font("Arial", Font.PLAIN, 1);
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3d = new Font3D(font, myExtrude);
        Text3D text3D = new Text3D(font3d, txt); // will add the text at the point

        Transform3D scaler = new Transform3D();
        Transform3D scalar2 = new Transform3D();
        if (isFlipped) {
            scaler.rotY(Math.PI);
            scalar2.rotX(-Math.PI / 2);
        } else {
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
        Shape3D s = new Shape3D(text3D, appearance);
        s.setCollidable(false);
        s.setPickable(false);
        scene_TG.addChild(s);
        return scene_TG;
    }

    public void addText(TransformGroup objTG) {
        String[] bottomText = {"a", "b", "c", "d", "e", "f", "g", "h", "1", "2", "3", "4", "5", "6", "7", "8"};
        for (int i = 0; i < 16; i++) {
            objTG.addChild(generateText3d(bottomText[i], 0.8f, new Vector3f(i < 8 ? -7 + 2f * (i % 8) : -9, -0.15f, i < 8 ? 8.8f : 7 - 2f * (i % 8)), MONKEECHESS.Black, false));
        }

        for (int i = 0; i < 16; i++) {
            objTG.addChild(generateText3d(bottomText[i], 0.8f, new Vector3f(i < 8 ? 7 - 2f * (i % 8) : 9, -0.15f, i < 8 ? -8.8f : -7 + 2f * (i % 8)), MONKEECHESS.Black, true));
        }
    }
}
