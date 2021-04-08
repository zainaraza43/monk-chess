package com.net;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 6969;

    private static final int MAX_PLAYERS = 2;
    private final static int PLAYER1 = 1;
    private final static int PLAYER2 = 2;

    private final PlayerServerHandler[] handlers;        // handlers for players
    private int numPlayers;

    public Server() {                    // Concurrently process players
        handlers = new PlayerServerHandler[MAX_PLAYERS];
        handlers[0] = handlers[1] = null;
        numPlayers = 0;

        ServerSocket serverSock;
        try {
            serverSock = new ServerSocket(PORT);
            Socket clientSock;
            while (true) {
                System.out.println("Waiting for a client...");
                clientSock = serverSock.accept();
                new PlayerServerHandler(clientSock, this).start();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    synchronized public boolean enoughPlayers() {
        return (numPlayers == MAX_PLAYERS);
    }

    synchronized public int addPlayer(PlayerServerHandler h) {
        for (int i = 0; i < MAX_PLAYERS; i++)
            if (handlers[i] == null) {
                handlers[i] = h;
                numPlayers++;
                if (enoughPlayers()) {
                    System.out.println("WE HAVE REACHED THE MAX AMOUNT OF PLAYERS");
                }
                return i + 1; // playerID is 1 or 2 (array index + 1)
            }


        return -1; // means we have enough players already
    }

    synchronized public void tellOtherPlayer(int playerID, String msg) {
        int otherID = ((playerID == PLAYER1) ? PLAYER2 : PLAYER1);
        if (handlers[otherID - 1] != null) // index is ID-1
            handlers[otherID - 1].sendMessage(msg);
    }

    synchronized public void kill(int id) {
        this.handlers[id-1] = null;
        numPlayers--;
        System.out.println("ID " + id + " IS DEAD LAMO!");
    }
}
