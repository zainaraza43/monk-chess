package com.net;

import com.Main.ChessBoard;

import java.io.*;
import java.net.Socket;

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
        boolean isWhite = Boolean.parseBoolean(parts[0]);
        int pieceIndex = Integer.parseInt(parts[1]);
        double newX = Double.parseDouble(parts[2]), newZ = Double.parseDouble(parts[3]);
        int collisionIndex = Integer.parseInt(parts[4]);
        chessBoard.updateBoard(isWhite, pieceIndex, newX, newZ, collisionIndex);
    }

    @Override
    public void run() {
        makeContact();

        while (true) {
            try {
                String line = in.readLine();
                System.out.println("Received data: " + line);
                if (id == -1) {
                    id = Integer.parseInt(line);
                    continue;
                }
                parseData(line);
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
        System.out.println("SENDING: " + s);
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
