package com.net;

import java.io.*;
import java.net.Socket;

public class Client extends Thread {
    private static final int PORT = 6969;
    private static final String HOST = "localhost";

    long startTime, endTime;
    int id;
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    public Client() throws IOException {
    }

    public void run() {
        makeContact();

        while (true) {
            try {
                if (in.ready()) {
                    String line = in.readLine();
                    System.out.println(line);

                }

            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
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

    public static void main(String[] args) throws IOException {
        Client c = new Client();
        c.makeContact();

        c.out.println("FUCK JAVA3D");
        String line = c.in.readLine();
        System.out.println(line);

        c.sock.close();
    }
}
