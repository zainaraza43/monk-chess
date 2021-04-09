/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * CheckKeyboardBehaviour.java
 */
package com.Behavior;
import com.Main.ChessBoard;
import com.Main.Piece;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class CheckKeyboardBehaviour implements KeyListener {

    private ChessBoard chessBoard;
    public CheckKeyboardBehaviour(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { // process keyboard press and send check data
        if (keyEvent.getKeyCode() == KeyEvent.VK_C) {
            chessBoard.sounds.check();
            chessBoard.client.sendMessage("check " + chessBoard.client.getPlayerID());
            int id = chessBoard.client.getPlayerID();
            ArrayList<Piece> list = id==1 ? chessBoard.chessPieces.getBlackPieces() : chessBoard.chessPieces.getWhitePieces();
            Piece king = null;
            for (Piece p:list) {
                if (p.getName().equals("King")) {
                    king = p;
                    break;
                }
            }
            king.makePieceRed();
        }
    }
}
