/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Collision.java
 */
package com.Behavior;
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
    public static boolean isColliding, ownPiece;
    public static int collidingIndex = -1;
    private GameOver gameOver;

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
        gameOver = new GameOver();
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

    public void processCollision(Piece pieceObj) { // will process collision
        piece.sounds.validMove(); // play the valid move sound
        collidingIndex = oppositePieces.indexOf(pieceObj); // get the collidingIndex and update it
        oppositePieces.remove(pieceObj); // remove the piece that was captured from ArrayList
        chessBoard.removeChessPiece(pieceObj); // remove the piece that was collided with
        pickBehavior.removeCollisionBehavior(removeBG); // remove collision behaviour from the piecee
        chessBoard.addIcon(pieceObj); // update panel

        if (pieceObj.getName().equals("King") && Launcher.isMultiplayer) // if the piece captured is a king
            chessBoard.gameOver = true;
        if(pieceObj.getName().equals("King") && !Launcher.isMultiplayer)
            win();
        makeQueen(); // make a queen
    }

    public void win(){
        gameOver.endGame();
    }

    public void makeQueen(){ // will make a pawn a queen if a pawn captures a piece on the other end of the board
        if(piece.getName().equals("Pawn")){
            double valueToCheck = piece.isWhite() ? -7 : 7;
            Vector3d pos = piece.getPosition();
            if(pos.z == valueToCheck){
                Launcher.chessPieces.changePiece(piece);
            }
        }
        ChessPieces.isChangedPiece = true;
    }
    public void processOwnPiece(Piece pieceObj) { // will make sure if a piece tries to capture a peice of it's own color the collision is ignored
        ownPiece = true;
        pieceObj.sounds.inValidMove();
        pieceObj.resetPos();
        pickBehavior.removeCollisionBehavior(removeBG);
    }
}
