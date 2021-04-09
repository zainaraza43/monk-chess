package com.Behavior;

import com.Main.ChessBoard;
import com.Main.Piece;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.WakeupCriterion;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

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
    public void keyReleased(KeyEvent keyEvent) {
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
