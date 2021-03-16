package com.Behavior;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.behaviors.mouse.MouseRotate;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Vector3d;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class MouseBeh extends MouseRotate {
    private WakeupCriterion [] wakeupCriteria;
    private WakeupCondition wakeupCondition;
    private double x_angle, y_angle, x_factor, y_factor;
    private Transform3D transformZ;

    public MouseBeh(TransformGroup transformGroup){
        wakeupCriteria = new WakeupCriterion[1];
        wakeupCriteria[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED);
        wakeupCondition = new WakeupOr(wakeupCriteria);
        this.x_angle = 0.0D;
        this.y_angle = 0.0D;
        this.x_factor = 0.0013D;
        this.y_factor = 0.0013D;
        if(((this.flags) & 2) == 2){
            this.invert = true;
            this.x_factor *= -1.0D;
            this.y_factor *= -1.0D;
        }
        this.setTransformGroup(transformGroup);
        transformZ = new Transform3D();

    }

    @Override
    public void initialize() {
        this.wakeupOn(wakeupCondition);


    }

    @Override
    public void processStimulus(Iterator<WakeupCriterion> iterator) {
        WakeupOnAWTEvent wakeup;
        WakeupCriterion genericEvent;
        AWTEvent [] events;

        while(iterator.hasNext()){
            genericEvent = (WakeupCriterion) iterator.next();

            if(genericEvent instanceof WakeupOnAWTEvent){
                wakeup = (WakeupOnAWTEvent) genericEvent;
                events = wakeup.getAWTEvent();
                processMouseEvent(events);
            }
        }
        this.wakeupOn(wakeupCondition);

    }

    public void processMouseEvent(AWTEvent [] events){
        for(AWTEvent event : events){
            MouseEvent mouseEvent = (MouseEvent) event;

            if(mouseEvent.getID() == MouseEvent.MOUSE_MOVED){
//                System.out.println("x: " + this.x + ", y: " + this.y + "\nx_last: " + this.x_last + ", y_last: " + this.y_last);
                this.x = mouseEvent.getX();
                this.y = mouseEvent.getY();
                int dx = this.x - this.x_last;
                int dy = this.y - this.y_last;
                if(!this.reset){
                    this.x_angle = (double) dy * this.y_factor;
                    this.y_angle = (double) dx * this.x_factor;

                    this.transformX.rotX(-this.x_angle);
                    this.transformY.rotY(-this.y_angle);
                    this.transformZ.rotZ(0);
                    this.transformGroup.getTransform(this.currXform);
                    Matrix4d mat = new Matrix4d();
                    this.currXform.get(mat);
                    this.currXform.setTranslation(new Vector3d(0d, 0d, 0d));
                    if(this.invert){
                        this.currXform.mul(this.currXform, this.transformX);
                        this.currXform.mul(this.currXform, this.transformY);
                        this.currXform.mul(this.currXform, this.transformZ);
                    }else{

                        this.currXform.mul(this.transformX, this.currXform);
                        this.currXform.mul(this.transformY, this.currXform);
                        this.currXform.mul(this.transformZ, this.currXform);
                    }
                    System.out.println(mat.m01 + ", " + mat.m13 + ", " + mat.m23);
                    Vector3d translation = new Vector3d(mat.m01, mat.m13, mat.m23);
                    currXform.setTranslation(translation);
                    this.transformGroup.setTransform(this.currXform);
                    transformChanged(this.currXform);

                }else{
                    this.reset = false;
                }
                this.x_last = this.x;
                this.y_last = this.y;
            }
        }
    }


    public void transformChanged(Transform3D transform) {
    }
}