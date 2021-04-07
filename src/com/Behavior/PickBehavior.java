/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
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
import org.jogamp.vecmath.Color3f;
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
    private Transform3D currX, transformX, transformZ, imWorld3D;
    private Canvas3D canvas3D;
    private PickTool pickTool;
    private BranchGroup sceneBG, movementBG, collisionBG;
    private ChessPieces chessPieces;
    private ChessBoard chessBoard;

    public PickBehavior(ChessBoard chessBoard, BranchGroup sceneBG, TransformGroup sceneTG, Canvas3D canvas) {
        this.chessBoard = chessBoard;
        this.sceneTG = sceneTG;
        this.sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        this.sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.canvas3D = canvas;
        this.sceneBG = sceneBG;
        isMoving = false;
        currX = new Transform3D();
        transformX = new Transform3D();
        transformZ = new Transform3D();
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
            int mouseX = mouseEvent.getX();
            int mouseY = mouseEvent.getY();
            if (!isMoving && mouseEvent.getID() == MouseEvent.MOUSE_PRESSED && mouseEvent.getButton() == MouseEvent.BUTTON1) { // if left click
                pickBeh(mouseX, mouseY);
            }
        }
    }

    public void pickBeh(int mouseX, int mouseY) {
        canvas3D.getPixelLocationInImagePlate(mouseX, mouseY, mousePos); // calculate the position in 3D world
        canvas3D.getImagePlateToVworld(imWorld3D); // grab current 3D transform
        center = new Point3d();
        canvas3D.getCenterEyeInImagePlate(center); // grab the center
        imWorld3D.transform(mousePos); // apply position
        imWorld3D.transform(center); // apply center position

        Vector3d mouseVec = new Vector3d(); // mouse vector in 3D instead of 2d
        mouseVec.sub(mousePos, center); // get displacement from origin
        mouseVec.normalize();
        pickTool.setShapeRay(mousePos, mouseVec); // send pickArray

        if (pickTool.pickClosest() != null) { // if pickRay is not null
            PickResult pickResult = pickTool.pickClosest(); // get closest node
            if (pickResult.getNode(PickResult.SHAPE3D) instanceof Shape3D) { // if node is Shape3D
                Shape3D pickPiece = (Shape3D) pickResult.getNode(PickResult.SHAPE3D); // grab the Shape3D
                if (pickPiece != null) { // if it's not null
                    if ((int) pickPiece.getUserData() == 0 && pickPiece.getName() != null) { // if userData is 0
                        Piece piece = (Piece) pickPiece.getParent().getParent().getParent();
                        isMoving = true;
                        isWhite = piece.isWhite();
                        piece.makePieceGreen();

                        movementBG = new BranchGroup();
                        movementBG.setCapability(BranchGroup.ALLOW_DETACH);

                        collisionBG = new BranchGroup();
                        collisionBG.setCapability(BranchGroup.ALLOW_DETACH);

                        piece.oldPos(); // store old position before movement
                        setYValue(piece, 4);
                        piece.makeHighlight();
                        addKeyNav(piece);
                    }
                }
            }
        }
    }

    public void removeKeyNav(BranchGroup bg) {
        sceneTG.removeChild(bg);
        isMoving = false;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try{
                   Thread.sleep(150);
                }catch (InterruptedException e){
                    System.out.println(e);
                }
                if(!Collision.isColliding){
                    sceneTG.removeChild(collisionBG);
                    System.out.println("collision removed, did not happen");
                }
            }
        };
        Thread t = new Thread(r);
        t.start();

    }

    public void removeCollisionBehavior(BranchGroup bg){
        sceneTG.removeChild(bg);
    }

    public void addKeyNav(Piece piece) {
        KeyBoardInput keyBoardInput = new KeyBoardInput(piece, movementBG,this);
        keyBoardInput.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
        movementBG.addChild(keyBoardInput);
        sceneTG.addChild(movementBG);

        addCollisionBehavior(piece);
    }

    public void addCollisionBehavior(Piece piece) {
        Collision collision = new Collision(chessBoard, this, collisionBG, sceneTG, chessPieces.getWhitePieces(), chessPieces.getBlackPieces(), piece);
        collision.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
        collisionBG.addChild(collision);
        sceneTG.addChild(collisionBG);

    }

    public void setYValue(Piece piece, float amount) {
        Vector3d vector3d = piece.getPosition();
        vector3d.y += amount; // update
        piece.setPosition(vector3d); // set
    }

    public static Material setMaterial(Color3f clr) {
        int SH = 100;               // 10
        Material ma = new Material();
        ma.setAmbientColor(clr);
        ma.setEmissiveColor(clr);
        ma.setDiffuseColor(clr);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }
}
