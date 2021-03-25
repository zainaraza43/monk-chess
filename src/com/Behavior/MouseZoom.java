package com.Behavior;

import com.Main.MONKEECHESS;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
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
    private SimpleUniverse simpleUniverse;

    public MouseZoom(TransformGroup tg, SimpleUniverse su) {
        this.direction = 0;
        this.currX = new Transform3D();
        this.transformX = new Transform3D();
        this.speed = 0.2;
        this.simpleUniverse = su;
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
        Vector3d v = new Vector3d();
        this.currX.get(v);
        Point3d translation = new Point3d(v);
        translation.z += speed;
        MONKEECHESS.viewerZoom(simpleUniverse, translation);
    }

}
