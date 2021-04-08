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

        System.out.println("Added player with id " + id);
        out.println(id);

        while (true) {
            try {
                String str = in.readLine();
                if (str == null) {
                    server.kill(id);
                    return;
                }
                System.out.println("Received message: \"" + str + "\"");
                server.tellOtherPlayer(id, str);

            } catch (IOException e) {
                e.printStackTrace();
                server.kill(id);
                return;
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
