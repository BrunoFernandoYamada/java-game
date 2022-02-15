package com.byamada.javagame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private int[] values;
    private int maxTurns;
    private int turnsMade;
    private int myPoints;
    private int opponentPoints;
    private boolean buttonsEnabled;

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
        values = new int[4];
        turnsMade = 0;
        myPoints = 0;
        opponentPoints = 0;

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
            buttonsEnabled = true;
        } else {
            message.setText("You are player #2. Wait for your turn");
            opponentID = 1;
            buttonsEnabled = false;
        }
        //toggleButtons();

        this.setVisible(true);
    }


    public void connectionToServer() {
        clientSideConnection = new ClientSideConnection();
    }

    public void setUpButtons() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                int bNum = Integer.parseInt(btn.getText());

                message.setText("You clicked button #" + bNum + ". Now wait for player #" + opponentID);
                turnsMade++;
                System.out.println("Turns made  " + turnsMade);

                buttonsEnabled = false;
                //toggleButtons();

                myPoints += values[bNum - 1];
                System.out.println("My points " + myPoints);
                clientSideConnection.sendButtonNum(bNum);
            }
        };

        b1.addActionListener(al);
        b2.addActionListener(al);
        b3.addActionListener(al);
        b4.addActionListener(al);

    }

    public void toggleButtons() {
        b1.setEnabled(buttonsEnabled);
        b2.setEnabled(buttonsEnabled);
        b3.setEnabled(buttonsEnabled);
        b4.setEnabled(buttonsEnabled);
    }

    public void startReceivingButtonsNums() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    clientSideConnection.receiveButtonNum();
                }
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        Player p = new Player(500, 100);
        p.connectionToServer();
        p.setUpGUI();
        p.setUpButtons();
        p.startReceivingButtonsNums();
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
                maxTurns = dataInputStream.readInt() / 2;
                values[0] = dataInputStream.readInt();
                values[1] = dataInputStream.readInt();
                values[2] = dataInputStream.readInt();
                values[3] = dataInputStream.readInt();
                System.out.println("maxTurns: " + maxTurns);
                System.out.println("Value # 1 is " + values[0]);
                System.out.println("Value # 2 is " + values[1]);
                System.out.println("Value # 3 is " + values[2]);
                System.out.println("Value # 4 is " + values[3]);
            } catch (IOException ex) {
                System.out.println("IOException from CSC constructor");
            }
        }

        public void sendButtonNum(int n) {
            try {
                dataOutputStream.writeInt(n);
                dataOutputStream.flush();
            } catch (IOException ex) {
                System.out.println("IOException from sendButtonNum() CSC");
            }
        }

        public int receiveButtonNum() {
            int n = -1;
            try {
                n = dataInputStream.readInt();
                System.out.println("Player #" + opponentID + " clicked button #" + n);
            }catch (IOException ex){
                System.out.println("IOException from receiveButtonNum() CSC");
            }
            return n;
        }
    }
}
