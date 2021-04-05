package com.Behavior;
import Launcher.Launcher;
import com.Main.ChessBoard;
import com.Main.ChessPieces;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class Collision extends Behavior {
    private Shape3D piece1, piece2;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
    private TransformGroup sceneTG, positionTransform;
    private boolean isColliding, isWhite;
    private ArrayList<TransformGroup> currentPieces, oppositePieces;
    private BranchGroup removeBG;
    private PickBehavior pickBehavior;

    public Collision(PickBehavior p, BranchGroup removeBG, TransformGroup sceneTG, ArrayList<TransformGroup> currentPieces, ArrayList<TransformGroup> oppositePieces, Shape3D piece, TransformGroup positionTransform, boolean isWhite){
        this.isWhite = isWhite;
        this.pickBehavior = p;
        this.removeBG = removeBG;
        this.sceneTG = sceneTG;
        this.currentPieces = currentPieces;
        this.oppositePieces = oppositePieces;
        this.positionTransform = positionTransform;
        this.piece1 = piece;
        isColliding = false;
    }

    @Override
    public void initialize() {
        wEnter = new WakeupOnCollisionEntry(piece1, WakeupOnCollisionEntry.USE_BOUNDS);
        wExit = new WakeupOnCollisionExit(piece1, WakeupOnCollisionExit.USE_BOUNDS);
        wakeupOn(wEnter);
    }

    @Override
    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        isColliding = !isColliding;
        if(isColliding){
            for(TransformGroup positionTG : oppositePieces){
                Rectangle rect1 = createRect(positionTG);
                int index = currentPieces.indexOf(positionTransform);
                Rectangle rect2 = createRect(currentPieces.get(index));
                if(rect1.intersects(rect2)){
                    TransformGroup scaledTG = (TransformGroup) positionTG.getChild(0);
                    piece2 = (Shape3D) scaledTG.getChild(0);
                    if(!piece1.getName().equals(piece2.getName())){
                        if(isWhite){
                            processCollision(positionTG, -1);
                        }else{
                            processCollision(positionTG, 1);
                        }
                    }
                    break;
                }
            }
            wakeupOn(wExit);
        }else {
            wakeupOn(wEnter);
        }
    }

    public Rectangle createRect(TransformGroup targetTG){
        Vector3d vector3d = new Vector3d();
        Transform3D transform3D = new Transform3D();
        targetTG.getTransform(transform3D);
        transform3D.get(vector3d);
        return new Rectangle((int)vector3d.x, (int)vector3d.z, 2, 2);
    }

    public void processCollision(TransformGroup positionTG, float x){
        oppositePieces.remove(positionTG);
        Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3d(x * 9.5f, 1, PickBehavior.zValue));
        positionTG.setTransform(transform3D);
        PickBehavior.zValue += 1.5;
        pickBehavior.removeCollisionBehavior(removeBG);
    }
}
