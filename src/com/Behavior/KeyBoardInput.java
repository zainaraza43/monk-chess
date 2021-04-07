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
import Launcher.Launcher;
import com.Main.Piece;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Vector3d;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class KeyBoardInput extends Behavior {
    private WakeupCriterion[] wakeupCriteria;
    private WakeupCondition wakeupCondition;
    private TransformGroup positionTG;
    private boolean isMoving;
    private Transform3D pieceTransform3D, highlightTransform3D;
    private int[] keyCodes;
    private float[][] moves;
    private PickBehavior pickBehavior;
    private BranchGroup removingBG;
    private Piece piece;

    public KeyBoardInput(Piece piece, BranchGroup removingBG, PickBehavior p){
        this.piece = piece;
        this.removingBG = removingBG;
        this.positionTG = piece.getPositionTransform();
        this.pickBehavior = p;
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
                        movePiece(moves[i][0], piece.isWhite() ? moves[i][1] : -moves[i][1]);
                        ArrayList<Piece> toCheck = piece.isWhite() ? Launcher.chessPieces.getWhitePieces() : Launcher.chessPieces.getBlackPieces();
                        for (Piece p:toCheck) {
                            if (piece.getPosition().x == p.getPosition().x && piece.getPosition().z == p.getPosition().z && piece.getPosition().y != p.getPosition().y) {
                                piece.makePieceRed();
                                return;
                            }
                        }
                        piece.makePieceGreen();
                    }
                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
                    pickBehavior.setYValue(piece, -4);
                    pickBehavior.removeKeyNav(removingBG);
                    piece.makePieceNormal();
                }
            }
        }
    }

    public void movePiece(float amount, float direction){
        positionTG.getTransform(pieceTransform3D);
        Vector3d vector3d = new Vector3d();
        pieceTransform3D.get(vector3d);

        switch ((int) direction){
            case 1:
                vector3d.z -= amount;
                break;
            case 2:
                vector3d.x -= amount;
                break;
            case -2:
                vector3d.x += amount;
                break;
            case -1:
                vector3d.z += amount;
                break;
        }
        // Make sure that piece can't go off the board
        bindCoords(vector3d, -7, 7);

        pieceTransform3D.setTranslation(vector3d);
        positionTG.setTransform(pieceTransform3D);
    }

    private static void bindCoords(Vector3d v, double min, double max) {
        v.x = Math.max(min, v.x);
        v.x = Math.min(max, v.x);

        v.z = Math.max(min, v.z);
        v.z = Math.min(max, v.z);
    }
}
