package com.byamada.javagame;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player extends JFrame {
    private int width;
    private int height;
    private Container contentPane;
    private JTextArea message;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private int playerID;
    private int opponentID;

    private ClientSideConnection clientSideConnection;

    public Player(int w, int h) {
        width = w;
        height = h;
        contentPane = this.getContentPane();
        message = new JTextArea();
        b1 = new JButton("1");
        b2 = new JButton("2");
        b3 = new JButton("3");
        b4 = new JButton("4");
    }

    public void setUpGUI() {
        this.setSize(width, height);
        this.setTitle("Player #"+ playerID);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridLayout(1, 5));
        contentPane.add(message);
        message.setText("Creating a simple turn-based game in java");
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        contentPane.add(b1);
        contentPane.add(b2);
        contentPane.add(b3);
        contentPane.add(b4);

        if (playerID == 1){
            message.setText("You are player #1. You go first");
            opponentID = 2;
        } else {
            message.setText("You are player #2. Wait for your turn");
            opponentID = 1;
        }

        this.setVisible(true);
    }

    public void connectionToServer() {
        clientSideConnection = new ClientSideConnection();
    }

    public static void main(String[] args) {
        Player p = new Player(500, 100);
        p.connectionToServer();
        p.setUpGUI();
    }

    //Client Connection inner class
    private class ClientSideConnection {

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        private final static String HOSTNAME = "localhost";
        private final static int PORT = 51734;

        public ClientSideConnection() {
            System.out.println("----Client----");
            try {
                socket = new Socket(HOSTNAME, PORT);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                playerID = dataInputStream.readInt();
                System.out.println("Connected to server as Player #" + playerID + ".");
            } catch (IOException ex) {
                System.out.println("IOException from CSC constructor");
            }
        }



    }


}
