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
import com.Main.ChessPieces;
import com.Main.MONKEECHESS;
import org.jdesktop.j3d.examples.collision.CollisionDetector;
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
    private BranchGroup sceneBG;
    private ChessPieces chessPieces;
    static float zValue = -9f;


    public PickBehavior(BranchGroup sceneBG, TransformGroup sceneTG, Canvas3D canvas) {
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
                Shape3D piece = (Shape3D) pickResult.getNode(PickResult.SHAPE3D); // grab the Shape3D
                if (piece != null) { // if it's not null
                    if ((int) piece.getUserData() == 0 && piece.getName() != null) { // if userData is 0
                        isMoving = true;
                        isWhite = piece.getName().equals("White"); // check if piece selected is white

                        TransformGroup positionTransform = (TransformGroup) piece.getParent().getParent(); // get positionTransformGroup
                        TransformGroup highlightTransform = makeHighlight(positionTransform);

                        BranchGroup movementBG = new BranchGroup();
                        movementBG.setCapability(BranchGroup.ALLOW_DETACH);

                        setYValue(positionTransform, 3);
                        addKeyNav(piece, movementBG, positionTransform, highlightTransform, this.isWhite);
                        sceneTG.addChild(movementBG);
                    }
                }
            }
        }
    }

    public void removeKeyNav(BranchGroup bg, TransformGroup poisitionTransform, Shape3D piece, boolean isWhite) {
        sceneTG.removeChild(bg);
        isMoving = false;

        BranchGroup collisionBG = new BranchGroup();
        collisionBG.setCapability(BranchGroup.ALLOW_DETACH);
        addCollisionBehavior(collisionBG, poisitionTransform, piece, isWhite);
        sceneTG.addChild(collisionBG);
    }

    public void removeCollisionBehavior(BranchGroup bg){
        sceneTG.removeChild(bg);
    }

    public void addKeyNav(Shape3D piece, BranchGroup tmpBG, TransformGroup positionTransform, TransformGroup highlightTransform, boolean isWhite) {
        KeyBoardInput keyBoardInput = new KeyBoardInput(piece, tmpBG,this, positionTransform, highlightTransform, isWhite);
        keyBoardInput.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
        tmpBG.addChild(highlightTransform);
        tmpBG.addChild(keyBoardInput);
    }

    public void addCollisionBehavior(BranchGroup tmpBG, TransformGroup positionTransform, Shape3D piece, boolean isWhite) {

        if (isWhite) {
            Collision collision = new Collision(this, tmpBG, sceneTG, chessPieces.getWhitePieces(), chessPieces.getBlackPieces(), piece, positionTransform, isWhite);
            collision.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
            tmpBG.addChild(collision);
        }else{
            Collision collision = new Collision(this, tmpBG, sceneTG, chessPieces.getBlackPieces(), chessPieces.getWhitePieces(), piece, positionTransform, isWhite);
            collision.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000d));
            tmpBG.addChild(collision);
        }

    }

    public void setYValue(TransformGroup targetTG, float amount) {
        Transform3D tmp = new Transform3D();
        targetTG.getTransform(tmp);
        Vector3d vector3d = new Vector3d();
        tmp.get(vector3d);
        vector3d.y += amount;
        tmp.setTranslation(vector3d);
        targetTG.setTransform(tmp);
    }

    public TransformGroup makeHighlight(TransformGroup positionTG) {

        QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.NORMALS | QuadArray.COORDINATES);
        float[][] coords = {{-1, 0, -1}, {-1, 0, 1}, {1, 0, 1}, {1, 0, -1}};
        float[] normal = {0, 1, 0};
        for (int i = 0; i < 4; i++) {
            quadArray.setCoordinate(i, coords[i]);
            quadArray.setNormal(i, normal);
            quadArray.setColor(i, MONKEECHESS.Green);
        }
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D t3d = new Transform3D();
        positionTG.getTransform(t3d);
        Vector3d tmp = new Vector3d();
        t3d.get(tmp);
        tmp.y = 0.01;
        t3d.setTranslation(tmp);
        tg.setTransform(t3d);

        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.Green));
        tg.addChild(new Shape3D(quadArray, appearance));
        return tg;
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
