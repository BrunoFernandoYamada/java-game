package com.byamada.javagame;

import java.io.*;
import java.net.*;

public class Server {

    private ServerSocket serverSocket;
    private int numPlayers;
    private final int PORT = 51734;
    private ServerSideConnection player1;
    private ServerSideConnection player2;

    public Server() {
        System.out.println("----Game Server----");
        numPlayers = 0;
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
                dataOutputStream.flush();
            } catch (IOException ex) {
                System.out.println("IOException from SSC run() ");
            }
        }
    }

}
