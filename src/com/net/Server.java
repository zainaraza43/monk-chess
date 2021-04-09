package com.net;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

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

    synchronized public int addPlayer(PlayerServerHandler h) {
        boolean full = true;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (handlers[i] == null) {
                full = false;
                break;
            }
        }

        if (full) {
            System.out.println("WE HAVE REACHED THE MAX AMOUNT OF PLAYERS");
            return -1;
        }
        Random r = new Random();
        int i = -1;
        do {
            i = r.nextInt(MAX_PLAYERS);
            System.out.println("trying to assign id: " + i);
        } while (handlers[i] != null);
        handlers[i] = h;
        numPlayers++;
        return i + 1;
    }

    synchronized public void tellOtherPlayer(int playerID, String msg) {
        int otherID = ((playerID == PLAYER1) ? PLAYER2 : PLAYER1);
        if (handlers[otherID - 1] != null) // index is ID-1
            handlers[otherID - 1].sendMessage(msg);
    }

    synchronized public void kill(int id) {
        this.handlers[id - 1] = null;
        numPlayers--;
    }
}
