package com.net;

import com.Main.ChessBoard;
import com.Main.MONKEECHESS;
import com.Main.Piece;
import com.Util.SoundUtilityJOAL;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client extends Thread {
    private static final int PORT = 6969;
//    private static final String HOST = "homeserver.bigboisinc.me";
    private static final String HOST = "localhost";

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isConnecting;
    private ChessBoard chessBoard;
    private int id;

    public Client(ChessBoard chessBoard) {
        isConnecting = true;
        this.chessBoard = chessBoard;
        id = -1;
    }

    private void parseData(String line) {
        String[] parts = line.split(" ");
        boolean isWhite = Boolean.parseBoolean(parts[1]);
        int pieceIndex = Integer.parseInt(parts[2]);
        double newX = Double.parseDouble(parts[3]), newZ = Double.parseDouble(parts[4]);
        int collisionIndex = Integer.parseInt(parts[5]);
        boolean gameOver = Boolean.parseBoolean(parts[6]);
        int newPieceIndex = Integer.parseInt(parts[7]);
        if(newPieceIndex != -1){
            chessBoard.chessPieces.changePiece(isWhite, newPieceIndex);
        }
        chessBoard.updateBoard(isWhite, pieceIndex, newX, newZ, collisionIndex, gameOver);
    }

    private void handleCheck(String line) {
        System.out.println("I'M HANDLING THE CHECK");
        String[] parts = line.split(" ");
        int id = Integer.parseInt(parts[1]);
        ArrayList<Piece> list = id==1 ? chessBoard.chessPieces.getBlackPieces() : chessBoard.chessPieces.getWhitePieces();
        Piece king = null;
        for (Piece p:list) {
            if (p.getName().equals("King")) {
                king = p;
                break;
            }
        }
        chessBoard.sounds.check();
        king.makePieceRed();
    }

    @Override
    public void run() {
        makeContact();

        while (true) {
            try {
                String line = in.readLine();
                System.out.println("Received: " + line);
                if (id == -1) {
                    id = Integer.parseInt(line);
                    continue;
                }

                if (line.startsWith("data")) {
                    parseData(line);
                    continue;
                }

                if (line.startsWith("check")) {
                    handleCheck(line);
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPlayerID() {
        return id;
    }

    private void makeContact() {
        try {
            sock = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Cannot contact the server");
            System.exit(0);
        }
    }

    synchronized public void sendMessage(String s) {
        out.println(s);
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void close() {
        try {
            sock.close();
        } catch (IOException e) {
            System.out.println("Couldn't close the socket");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
    }
}
