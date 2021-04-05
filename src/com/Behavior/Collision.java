package com.Behavior;
import com.Main.ChessBoard;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Vector3d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


public class Collision extends Behavior {
    private Shape3D piece1, piece2;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
    private TransformGroup sceneTG, positionTransform;
    private boolean isColliding, isWhite;
    private ArrayList<BranchGroup> currentPieces, oppositePieces;
    private BranchGroup removeBG;
    private PickBehavior pickBehavior;
    private ChessBoard chessBoard;

    public Collision(ChessBoard chessBoard, PickBehavior p, BranchGroup removeBG, TransformGroup sceneTG, ArrayList<BranchGroup> currentPieces, ArrayList<BranchGroup> oppositePieces, Shape3D piece, TransformGroup positionTransform, boolean isWhite){
        this.chessBoard = chessBoard;
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
        if(isColliding) {
            for (BranchGroup pieceBG : oppositePieces) {
                TransformGroup positionTG = (TransformGroup) pieceBG.getChild(0);
                Rectangle rect1 = createRect(positionTG);
                int index = currentPieces.indexOf((BranchGroup) positionTransform.getParent());
                Rectangle rect2 = createRect((TransformGroup) currentPieces.get(index).getChild(0));
                if (rect1.intersects(rect2)) {
                    TransformGroup scaledTG = (TransformGroup) positionTG.getChild(0);
                    piece2 = (Shape3D) scaledTG.getChild(0);
                    processCollision(pieceBG, piece2.getName());
                    return;
                }
            }
            processOwnPiece();
        }
    }

    public Rectangle createRect(TransformGroup targetTG){
        Vector3d vector3d = new Vector3d();
        Transform3D transform3D = new Transform3D();
        targetTG.getTransform(transform3D);
        transform3D.get(vector3d);
        return new Rectangle((int)vector3d.x, (int)vector3d.z, 2, 2);
    }

    public void processCollision(BranchGroup positionBG, String pieceRemoved){
        System.out.println("piece removed was " + pieceRemoved);
        oppositePieces.remove(positionBG);
        chessBoard.removeChessPiece(positionBG);
        pickBehavior.removeCollisionBehavior(removeBG);
    }

    public void processOwnPiece(){
        System.out.println("own piece");
        pickBehavior.removeCollisionBehavior(removeBG);
    }
}
