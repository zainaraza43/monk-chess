/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Collision.java
 */
package com.Behavior;

import Launcher.GameEnd;
import Launcher.Launcher;
import com.Main.*;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Vector3d;

import java.util.ArrayList;
import java.util.Iterator;


public class Collision extends Behavior {
    private Piece piece;
    private WakeupCriterion[] wakeupCriteria;
    private WakeupCondition wakeupCondition;
    private TransformGroup sceneTG, positionTransform;
    private ArrayList<Piece> currentPieces, oppositePieces;
    private BranchGroup removeBG;
    private PickBehavior pickBehavior;
    private ChessBoard chessBoard;
    private Overlay overlay;
    public static boolean isColliding;
    public static int collidingIndex = -1;

    public Collision(ChessBoard chessBoard, PickBehavior p, BranchGroup removeBG, TransformGroup sceneTG, ArrayList<Piece> whitePiece, ArrayList<Piece> blackPieces, Piece piece) {
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
        wakeupCriteria = new WakeupCriterion[1];
        wakeupCriteria[0] = new WakeupOnCollisionEntry(piece.getPiece());
        wakeupCondition = new WakeupOr(wakeupCriteria);
        wakeupOn(wakeupCondition);
    }

    @Override
    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        WakeupCriterion wc = (WakeupCriterion) criteria.next();
        if (wc instanceof WakeupOnCollisionEntry) {
            Shape3D tmpPiece = (Shape3D) ((WakeupOnCollisionEntry) wc).getTriggeringPath().getObject();
            if (tmpPiece.getName() != null) {
                Piece piece2 = Obj3D.getPiece(tmpPiece);
                if (piece.getColor().equals(piece2.getColor())) {
                    processOwnPiece(piece);
                    isColliding = true;
                } else {
                    processCollision(piece2);
                    isColliding = true;
                }
            }
        }
    }

    public void processCollision(Piece pieceObj) {
        collidingIndex = oppositePieces.indexOf(pieceObj);
        oppositePieces.remove(pieceObj);
        chessBoard.removeChessPiece(pieceObj);
        pickBehavior.removeCollisionBehavior(removeBG);
        chessBoard.addIcon(pieceObj);
        if (pieceObj.getName().equals("King")) {
            chessBoard.gameOver = true;
        }
        makeQueen();

    }

    public void makeQueen(){
        if(piece.getName().equals("Pawn")){
            double valueToCheck = piece.isWhite() ? -7 : 7;
            Vector3d pos = piece.getPosition();
            if(pos.z == valueToCheck){
                Launcher.chessPieces.changePiece(piece);
            }
        }
        ChessPieces.isChangedPiece = true;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public BranchGroup getRemoveBG() {
        return removeBG;
    }

    public void processOwnPiece(Piece pieceObj) {
        pieceObj.sounds.inValidMove();
        pieceObj.resetPos();
        pickBehavior.removeCollisionBehavior(removeBG);
    }
}
