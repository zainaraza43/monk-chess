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
    private TransformGroup targetTG, sceneTG;
    private static Vector3d inVector = new Vector3d(0f, 0f, -1f);
    private Point3d mousePos, center;
    private double xFactor, zFactor;
    private int xPrev, zPrev;
    private boolean isDrag;
    private Transform3D currX, transformX, transformZ, imWorld3D;
    private Canvas3D canvas3D;
    private PickTool pickTool;
    private BranchGroup sceneBG;


    public PickBehavior(BranchGroup sceneBG, TransformGroup sceneTG, Canvas3D canvas){
       this.sceneTG = sceneTG;
       this.canvas3D = canvas;
       this.sceneBG = sceneBG;
       isDrag = true;
       currX = new Transform3D();
       transformX = new Transform3D();
       transformZ = new Transform3D();
       imWorld3D = new Transform3D();
       mousePos = new Point3d();
       xFactor = 0.002;
       zFactor = 0.002;
       xPrev = 0;
       zPrev = 0;
       pickTool = new PickTool(this.sceneBG);
       pickTool.setMode(PickTool.GEOMETRY);

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

    public void processEvent(AWTEvent [] events){
        for (AWTEvent e : events) {
            MouseEvent mouseEvent = (MouseEvent) e;
            int mouseX = mouseEvent.getX();
            int mouseY = mouseEvent.getY();
            if(mouseEvent.getID() == MouseEvent.MOUSE_PRESSED && mouseEvent.getButton() == MouseEvent.BUTTON1){ // if left click
                this.xPrev = mouseX; // record mouse x and y
                this.zPrev = mouseY;
                pickBeh(mouseX, mouseY);
            }
        }
    }

    public void pickBeh(int mouseX, int mouseY){
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

        if(pickTool.pickClosest() != null){ // if pickRay is not null
            PickResult pickResult = pickTool.pickClosest(); // get closest node
            if(pickResult.getNode(PickResult.SHAPE3D) instanceof Shape3D){ // if node is Shape3D
                Shape3D piece = (Shape3D) pickResult.getNode(PickResult.SHAPE3D); // grab the Shape3D
                if(piece != null){ // if it's not null
                    if((int) piece.getUserData() == 0){ // if userData is 0
                        System.out.println("Piece name: " + piece.getName());
                    }
                }
            }
        }
    }
}
