/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * KeyBoardInput.java
 */
package com.Behavior;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Vector3d;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class KeyBoardInput extends Behavior {
    private WakeupCriterion[] wakeupCriteria;
    private WakeupCondition wakeupCondition;
    private TransformGroup targetTG, highlightTransform;
    private boolean isMoving;
    private Transform3D pieceTransform3D, highlightTransform3D;
    private int[] keyCodes;
    private float[][] moves;
    private PickBehavior pickBehavior;
    private boolean isWhite;

    public KeyBoardInput(PickBehavior p, TransformGroup tg,TransformGroup highlightTransform, boolean isWhite){
        this.targetTG = tg;
        this.pickBehavior = p;
        this.isWhite = isWhite;
        this.highlightTransform = highlightTransform;
        isMoving = true;
        pieceTransform3D = new Transform3D();
        highlightTransform3D = new Transform3D();
        keyCodes = new int[]{KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D};
        moves = new float[][]{{2f, 1}, {-2f, -2}, {2f, -1}, {-2f, 2}};
    }



    @Override
    public void initialize() {
        wakeupCriteria = new WakeupCriterion[1];
        wakeupCriteria[0] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        wakeupCondition = new WakeupOr(wakeupCriteria);
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

    public void processEvent(AWTEvent[] events){
        for(AWTEvent awtEvent : events){
            KeyEvent keyEvent = (KeyEvent) awtEvent;
            if(keyEvent.getID() == KeyEvent.KEY_PRESSED){
                for(int i = 0; i < keyCodes.length; i ++){
                    if(keyEvent.getKeyCode() == keyCodes[i]){
                        movePiece(moves[i][0], isWhite ? moves[i][1] : -moves[i][1]);

                    }
                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
                    pickBehavior.setYValue(targetTG, -2);
                    pickBehavior.removeKeyNav();
                }
            }
        }
    }

    public void movePiece(float amount, float direction){
        highlightTransform.getTransform(highlightTransform3D);
        Vector3d highlightVector = new Vector3d();
        highlightTransform3D.get(highlightVector);

        targetTG.getTransform(pieceTransform3D);
        Vector3d vector3d = new Vector3d();
        pieceTransform3D.get(vector3d);

        switch ((int) direction){
            case 1:
                vector3d.z -= amount;
                highlightVector.z -= amount;
                break;
            case 2:
                vector3d.x -= amount;
                highlightVector.x -= amount;
                break;
            case -2:
                vector3d.x += amount;
                highlightVector.x += amount;
                break;
            case -1:
                vector3d.z += amount;
                highlightVector.z += amount;
                break;
        }
        highlightTransform3D.setTranslation(highlightVector);
        highlightTransform.setTransform(highlightTransform3D);
        pieceTransform3D.setTranslation(vector3d);
        targetTG.setTransform(pieceTransform3D);

    }
}