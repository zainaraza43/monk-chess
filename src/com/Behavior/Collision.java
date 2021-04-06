package com.Behavior;
import Launcher.ImagePanel;
import com.Main.*;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Vector3d;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


public class Collision extends Behavior {
    private Piece piece;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
    private TransformGroup sceneTG, positionTransform;
    private boolean isColliding;
    private ArrayList<Piece> currentPieces, oppositePieces;
    private BranchGroup removeBG;
    private PickBehavior pickBehavior;
    private ChessBoard chessBoard;
    private Overlay overlay;

    public Collision(ChessBoard chessBoard, PickBehavior p, BranchGroup removeBG, TransformGroup sceneTG, ArrayList<Piece> whitePiece, ArrayList<Piece> blackPieces, Piece piece){
        this.chessBoard = chessBoard;
        this.pickBehavior = p;
        this.removeBG = removeBG;
        this.sceneTG = sceneTG;
        this.currentPieces = piece.isWhite() ? whitePiece : blackPieces;
        this.oppositePieces = piece.isWhite() ? blackPieces : whitePiece;
        this.positionTransform = piece.getPositionTransform();
        this.piece = piece;
        overlay = MONKEECHESS.overlay;
        isColliding = false;
    }

    @Override
    public void initialize() {
        wEnter = new WakeupOnCollisionEntry(piece.getPiece(), WakeupOnCollisionEntry.USE_BOUNDS);
        wakeupOn(wEnter);
    }

    @Override
    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        isColliding = !isColliding;
        if(isColliding) {
            for(Piece pieceBG : oppositePieces){
                Rectangle rect1 = createRect(pieceBG.getPositionTransform());
                Rectangle rect2 = createRect(piece.getPositionTransform());
                if(rect1.intersects(rect2)){
                    processCollision(pieceBG);
                    return;
                }
            }
            for(Piece pieceBG : currentPieces){
                if(piece == pieceBG){
                    continue;
                }
                Rectangle rect1 = createRect(pieceBG.getPositionTransform());
                Rectangle rect2 = createRect(piece.getPositionTransform());
                if(rect1.intersects(rect2)){
                    processOwnPiece(piece);
                    return;
                }
            }
        }
    }

    public Rectangle createRect(TransformGroup targetTG){
        Vector3d vector3d = new Vector3d();
        Transform3D transform3D = new Transform3D();
        targetTG.getTransform(transform3D);
        transform3D.get(vector3d);
        return new Rectangle((int)vector3d.x, (int)vector3d.z, 2, 2);
    }

    public void processCollision(Piece pieceObj){
        oppositePieces.remove(pieceObj);
        chessBoard.removeChessPiece(pieceObj);
        pickBehavior.removeCollisionBehavior(removeBG);
        OverlayPanels panels = pieceObj.isWhite() ? overlay.getRightPanel() : overlay.getLeftPanel();
        panels.addIcon(pieceObj.getColor() + "_" + pieceObj.getName());
        panels.repaint();

        if(pieceObj.getName().equals("King")){
            win(pieceObj);
        }
    }

    public void win(Piece pieceObj){
        if(pieceObj.isWhite())
            System.out.println("GAME OVER, Black team wins");
        else
            System.out.println("GAME OVER, White team wins");
    }

    public void processOwnPiece(Piece pieceObj){
        pieceObj.resetPos();
        pickBehavior.removeCollisionBehavior(removeBG);
    }
}
