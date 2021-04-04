package com.Behavior;

import com.Main.ChessBoard;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Vector3d;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class KeyBoardInput extends Behavior {
    private WakeupCriterion[] wakeupCriteria;
    private WakeupCondition wakeupCondition;
    private TransformGroup targetTG, hg;
    private boolean isMoving;
    private Transform3D currTransform, transformX, transformZ;
    private int[] keyCodes;
    private float[][] moves;
    private PickBehavior pickBehavior;
    private boolean isWhite;

    public KeyBoardInput(PickBehavior p, TransformGroup tg,TransformGroup hg, boolean isWhite){
        this.targetTG = tg;
        this.pickBehavior = p;
        this.isWhite = isWhite;
        this.hg = hg;
        isMoving = true;
        currTransform = new Transform3D();
        transformX = new Transform3D();
        transformZ = new Transform3D();
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
                        System.out.println("key Pressed: " + keyCodes[i]);
                        movePiece(moves[i][0], moves[i][1]);

                    }
                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
                    System.out.println("piece was moved");
                    pickBehavior.setYValue(targetTG, -2);
                    pickBehavior.removeKeyNav();
                }
            }
        }
    }

    public void movePiece(float amount, float direction){
        hg.getTransform(transformX);
        Vector3d highlightVector = new Vector3d();
        transformX.get(highlightVector);

        targetTG.getTransform(currTransform);
        Vector3d vector3d = new Vector3d();
        currTransform.get(vector3d);

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
        transformX.setTranslation(highlightVector);
        hg.setTransform(transformX);
        currTransform.setTranslation(vector3d);
        targetTG.setTransform(currTransform);

    }
}
