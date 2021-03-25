package com.Behavior;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class MouseRotation extends Behavior {
    private WakeupCriterion[] wakeupCriteria;
    private WakeupCondition wakeupCondition;
    private TransformGroup targetTG;
    private boolean mouseClicked;
    private double xFactor, yFactor;
    private int xPrev, yPrev;
    private Transform3D currTrans, transformX, transformY;


    public MouseRotation(TransformGroup tg) { //will take in a transformGroup to apply rotation on
        this.targetTG = tg;
        this.currTrans = new Transform3D();
        this.transformX = new Transform3D(); //transformGroup for rotating in the x
        this.transformY = new Transform3D(); // transformGrpup for rotating in the y
        this.xPrev = 0;
        this.yPrev = 0;
        this.mouseClicked = true;
        this.xFactor = 0.002;
        this.yFactor = 0.002;
    }

    @Override
    public void initialize() {
        this.wakeupCriteria = new WakeupCriterion[3];
        this.wakeupCriteria[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
        this.wakeupCriteria[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
        this.wakeupCriteria[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
        this.wakeupCondition = new WakeupOr(wakeupCriteria);
        this.wakeupOn(wakeupCondition);

    }

    @Override
    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        AWTEvent[] awtEvents;
        WakeupCriterion wakeup;
        while (criteria.hasNext()) {
            wakeup = (WakeupCriterion) criteria.next();
            if (wakeup instanceof WakeupOnAWTEvent) {
                awtEvents = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                processMouseEvent(awtEvents);
            }
        }
        this.wakeupOn(wakeupCondition);
    }

    public void processMouseEvent(AWTEvent[] events) {
        int mouseX, mouseY;
        for (AWTEvent e : events) {
            MouseEvent mouseEvent = (MouseEvent) e;
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            if (mouseEvent.getID() == MouseEvent.MOUSE_DRAGGED) {
                processDrag(mouseX, mouseY);
            }
            else if(mouseEvent.getID() == MouseEvent.MOUSE_PRESSED){
                this.xPrev = mouseX;
                this.yPrev = mouseY;
            }
            else if (mouseEvent.getID() == MouseEvent.MOUSE_RELEASED) {
                mouseClicked = false;
            }
        }
    }

    public void processDrag(int mouseX, int mouseY) {
        if (mouseClicked) {
            mouseClicked = false;
        } else {
            int dx = mouseX - this.xPrev;
            int dy = mouseY - this.yPrev;
            transformX.rotX(dy * yFactor);
            transformY.rotY(dx * xFactor);
            this.targetTG.getTransform(currTrans);
            Matrix4d mat = new Matrix4d();
            this.currTrans.get(mat);
            currTrans.setTranslation(new Vector3d(0, 0, 0));
            currTrans.mul(currTrans, transformX);
            currTrans.mul(currTrans, transformY);
            Vector3d newTrans = new Vector3d(mat.m03, mat.m13, mat.m23);
            this.currTrans.setTranslation(newTrans);
            this.targetTG.setTransform(currTrans);
        }
        this.xPrev = mouseX;
        this.yPrev = mouseY;
    }

    public void setxFactor(double xFactor) {
        this.xFactor = xFactor;
    }

    public void setyFactor(double yFactor) {
        this.yFactor = yFactor;
    }
}
