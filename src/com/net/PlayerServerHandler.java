package com.net;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class PlayerServerHandler extends Thread {

    private Socket clientSocket;
    private Server server;
    private int id;

    private PrintWriter out;
    private BufferedReader in;

    public PlayerServerHandler(Socket clientSock, Server server) {
        this.server = server;
        this.clientSocket = clientSock;

        System.out.println("Player is trying to connect");
        try {
            in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            out = new PrintWriter(clientSock.getOutputStream(), true);     // autoflush
        } catch (Exception e) {
            System.out.println("Couldn't connect");
            e.printStackTrace();
        }

    }

    public void run() {
        id = server.addPlayer(this);

        out.println(id);
        System.out.println(id);

        while (true) {
            try {
                if (in.ready()) {
                    String str = in.readLine();
                    System.out.println("Received message: \"" + str + "\"");
                }
                else {
                    System.out.println("DISCONNECTED?????????");
                    server.kill(id);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public void sendMessage(String msg) {
        try {
            out.println(msg);
        } catch (Exception e) {
            System.out.println("Handler for player " + id + "\n" + e);
        }
    }

}