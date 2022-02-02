package com.byamada.javagame;

import java.io.*;
import java.net.*;

public class Server {

    private ServerSocket serverSocket;
    private int numPlayers;
    private final int PORT = 51734;

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
                System.out.println("Player #" + numPlayers + "has connected");
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

}
