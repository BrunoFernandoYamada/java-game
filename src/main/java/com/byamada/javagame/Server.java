package com.byamada.javagame;

import java.io.*;
import java.net.*;

public class Server {

    private ServerSocket serverSocket;
    private int numPlayers;
    private final int PORT = 51734;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int turnsMade;
    private int maxTurns;
    private int[] values;

    public Server() {
        System.out.println("----Game Server----");
        numPlayers = 0;
        turnsMade = 0;
        maxTurns = 0;
        values = new int[4];

        for (int i = 0; i < values.length; i++) {
            values[i] = (int) Math.ceil(Math.random() * 100);
            System.out.println("Value #" + (i + 1) + " is " + values[i]);
        }

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex){
            ex.printStackTrace();
            System.out.println("IOException from Server Constructor");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Wating for connections...");
            while (numPlayers < 2) {
                Socket socket = serverSocket.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected");
                ServerSideConnection ssc = new ServerSideConnection(socket, numPlayers);
                if(numPlayers == 1) {
                    player1 = ssc;
                } else {
                    player2 = ssc;
                }
                Thread t = new Thread(ssc);
                t.start();
            }
            System.out.println("We now have 2 players. No longer accepting connections");

        } catch (IOException ex) {
            System.out.println("IOException from accetiongConnections()");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.acceptConnections();
    }

    private class ServerSideConnection implements Runnable {

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;

        public ServerSideConnection(Socket s, int id) {
            socket = s;
            playerID = id;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                System.out.println("IOException from SSC constructor");
            }
        }

        @Override
        public void run() {
            try {
                dataOutputStream.writeInt(playerID);
                dataOutputStream.writeInt(maxTurns);
                dataOutputStream.writeInt(values[0]);
                dataOutputStream.writeInt(values[1]);
                dataOutputStream.writeInt(values[2]);
                dataOutputStream.writeInt(values[3]);
                dataOutputStream.flush();

                while (true) {

                }
            } catch (IOException ex) {
                System.out.println("IOException from SSC run() ");
            }
        }
    }

}
