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
import com.Main.ChessBoard;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.Iterator;

public class MouseZoom extends Behavior {
    private TransformGroup targetTG;
    private Transform3D currX, transformX;
    private int direction;
    private double speed;
    private WakeupCriterion[] wakeupCriteria;
    private WakeupCondition wakeupCondition;

    public MouseZoom(TransformGroup tg) {
        this.direction = 0;
        this.currX = new Transform3D();
        this.transformX = new Transform3D();
        this.speed = 1;
        this.targetTG = tg;
    }

    @Override
    public void initialize() {
        this.wakeupCriteria = new WakeupCriterion[1];
        this.wakeupCriteria[0] = new WakeupOnAWTEvent(MouseWheelEvent.MOUSE_WHEEL);
        this.wakeupCondition = new WakeupOr(this.wakeupCriteria);
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
                mouseScroll(awtEvents);
            }
        }
        this.wakeupOn(wakeupCondition);
    }

    public void mouseScroll(AWTEvent[] events) {
        for (AWTEvent e : events) {
            MouseWheelEvent mouseWheelEvent = (MouseWheelEvent) e;
            if(mouseWheelEvent.getWheelRotation() < 0){
                moveViewer(-speed);
            }else{
                moveViewer(speed);
            }

        }
    }
    public void moveViewer(double speed){
        this.targetTG.getTransform(currX);
        Point3d center = new Point3d(0, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Vector3d v = new Vector3d();
        this.currX.get(v);
        Point3d eye = new Point3d(v);
        eye.z = ChessBoard.isWhite ? eye.z + speed : eye.z - speed;
        eye.y += speed;
        Transform3D look = new Transform3D();
        look.lookAt(eye, center, up);
        look.invert();
        this.targetTG.setTransform(look);
    }

}
