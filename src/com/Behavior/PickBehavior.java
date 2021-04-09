/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * PickBehavior.java
 */
package com.Behavior;
import Launcher.Launcher;
import com.Main.*;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class PickBehavior extends Behavior {
    private WakeupCriterion[] wakeupCriteria;
    private WakeupCondition wakeupCondition;
    private TransformGroup sceneTG;
    private Point3d mousePos, center;
    private boolean isMoving, isWhite;
    private Transform3D imWorld3D;
    private OverlayCanvas3D OverlayCanvas3D;
    private PickTool pickTool;
    private BranchGroup sceneBG, movementBG, collisionBG;
    private ChessPieces chessPieces;
    private ChessBoard chessBoard;
    public Collision collision;
    public KeyBoardInput keyNav;

    public PickBehavior(ChessBoard chessBoard, BranchGroup sceneBG, TransformGroup sceneTG, OverlayCanvas3D canvas) {
        this.chessBoard = chessBoard;
        this.sceneTG = sceneTG;
        this.sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        this.sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.OverlayCanvas3D = canvas;
        this.sceneBG = sceneBG;
        isMoving = false;
        imWorld3D = new Transform3D();
        mousePos = new Point3d();
        pickTool = new PickTool(this.sceneBG);
        pickTool.setMode(PickTool.GEOMETRY);
        chessPieces = Launcher.chessPieces;
    }


    @Override
    public void initialize() {
        wakeupCriteria = new WakeupCriterion[2];
        this.wakeupCriteria[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED); // wakeup conditions
        this.wakeupCriteria[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
        this.wakeupCondition = new WakeupOr(wakeupCriteria);
        this.wakeupOn(wakeupCondition);
    }

    @Override
    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        AWTEvent[] awtEvents;
        WakeupCriterion wakeup;
        while (criteria.hasNext()) { // while a condition exists ie game is running
            wakeup = (WakeupCriterion) criteria.next(); // grab next condition mouse down or mouse up
            if (wakeup instanceof WakeupOnAWTEvent) {
                awtEvents = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                processEvent(awtEvents);
            }
        }
        this.wakeupOn(wakeupCondition);
    }

    public void processEvent(AWTEvent[] events) {
        for (AWTEvent e : events) {
            MouseEvent mouseEvent = (MouseEvent) e;
            int mouseX = mouseEvent.getX(); // used for pickBeh
            int mouseY = mouseEvent.getY();
            if (!isMoving && mouseEvent.getID() == MouseEvent.MOUSE_PRESSED && mouseEvent.getButton() == MouseEvent.BUTTON1) { // if left click
                if(Launcher.isMultiplayer && chessBoard.enablePicking){
                    pickBeh(mouseX, mouseY);
                }
                if(!Launcher.isMultiplayer){
                    pickBeh(mouseX, mouseY);
                }
            }
        }
    }

    public void pickBeh(int mouseX, int mouseY) {
        OverlayCanvas3D.getPixelLocationInImagePlate(mouseX, mouseY, mousePos); // calculate the position in 3D world
        OverlayCanvas3D.getImagePlateToVworld(imWorld3D); // grab current 3D transform
        center = new Point3d();
        OverlayCanvas3D.getCenterEyeInImagePlate(center); // grab the center
        imWorld3D.transform(mousePos); // apply position
        imWorld3D.transform(center); // apply center position

        Vector3d mouseVec = new Vector3d(); // mouse vector in 3D instead of 2d
        mouseVec.sub(mousePos, center); // get displacement from origin
        mouseVec.normalize();
        pickTool.setShapeRay(mousePos, mouseVec); // send pickArray

        if (!chessBoard.gameOver && pickTool.pickClosest() != null) { // if pickRay is not null
            PickResult pickResult = pickTool.pickClosest(); // get closest node
            if (pickResult.getNode(PickResult.SHAPE3D) instanceof Shape3D) { // if node is Shape3D
                Shape3D pickPiece = (Shape3D) pickResult.getNode(PickResult.SHAPE3D); // grab the Shape3D
                if (pickPiece != null) { // if it's not null
                    if ((int) pickPiece.getUserData() == 0 && pickPiece.getName() != null) { // if userData is 0
                        Piece piece = (Piece) pickPiece.getParent().getParent().getParent();
                        ChessPieces.isChangedPiece = false; // set isChanged to false
                        Collision.ownPiece = false; // set own collision to false
                        chessPieces.pieceChangedIndex = -1;
                        isMoving = true;
                        isWhite = piece.isWhite();
                        piece.makePieceGreen();

                        movementBG = new BranchGroup(); // branchGroup needed for movement
                        movementBG.setCapability(BranchGroup.ALLOW_DETACH);

                        collisionBG = new BranchGroup(); // branchGroup needed for collision
                        collisionBG.setCapability(BranchGroup.ALLOW_DETACH);

                        piece.oldPos(); // store old position before movement
                        piece.moveYPos(Piece.RAISE_AMOUNT); // move the piece up to make it hover
                        piece.makeHighlight();
                        addKeyNav(piece);
                    }
                }
            }
        }
    }

    public void removeKeyNav(BranchGroup bg, Piece p, int pieceIndex) {
        sceneTG.removeChild(bg);
        isMoving = false;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                if (!Collision.isColliding) { // if collision did not occure remove the collision behaviour
                    sceneTG.removeChild(collisionBG);
                    makeQueen(p);
                }
                if (Launcher.isMultiplayer && !Collision.ownPiece && !p.isSameSpot()) { //send msg to server to update board on client side
                    chessBoard.sendData(pieceIndex);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();

    }
    public void makeQueen(Piece piece){
        if(piece.getName().equals("Pawn")){
            double valueToCheck = piece.isWhite() ? -7 : 7;
            Vector3d pos = piece.getPosition();
            if(pos.z == valueToCheck){
                Launcher.chessPieces.changePiece(piece);
            }
        }
        ChessPieces.isChangedPiece = true;
    }

    public void removeCollisionBehavior(BranchGroup bg) {
        sceneTG.removeChild(bg);
    }

    public void addKeyNav(Piece piece) { // will add a keyNav
        keyNav = new KeyBoardInput(piece, movementBG, this, chessBoard);
        keyNav.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
        movementBG.addChild(keyNav);
        sceneTG.addChild(movementBG);

        addCollisionBehavior(piece);
    }

    public void addCollisionBehavior(Piece piece) { // will add a collision
        collision = new Collision(chessBoard, this, collisionBG, sceneTG, chessPieces.getWhitePieces(), chessPieces.getBlackPieces(), piece);
        collision.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
        collisionBG.addChild(collision);
        sceneTG.addChild(collisionBG);

    }
}
