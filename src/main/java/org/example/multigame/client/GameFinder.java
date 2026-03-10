package org.example.multigame.client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameFinder extends JFrame{
    private String serverIp;
    private ArrayList<String> lobbyList;
    private GameFinderGraphics graphics;


    public GameFinder(String serverIp) throws Exception {
        ArrayList<String> lobbyList = fetchLobbies(serverIp);

        JLabel errorField = new JLabel("");
        JLabel majorErrorField = new JLabel("");
        errorField.setForeground(Color.RED);
        majorErrorField.setForeground(Color.RED);

        if (serverIp == null) {
            serverIp = JOptionPane.showInputDialog("Auto-Discovery failed. Enter IP manually:");
        }

        System.out.println(serverIp);
        System.out.println(lobbyList);

        JList<Object> list = new JList<>(lobbyList.toArray());
        JTextField inputField = new JTextField();

        JButton createButton = new JButton("Create game");
        JButton joinButton = new JButton("Join game");
        JButton findButton = new JButton("Find game");

        String finalServerIp = serverIp;

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Fetch the updated list from the server
                ArrayList<String> updatedLobbies = fetchLobbies(finalServerIp);
                ArrayList<String> emptyList = new ArrayList<>();

                // 2. Update the JList with the new data
                if((updatedLobbies.isEmpty()) || (updatedLobbies == null)){
                    list.setListData(emptyList.toArray());
                }else {
                    list.setListData(updatedLobbies.toArray());
                }

            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selLobby = inputField.getText();
                ArrayList<String> tempList = fetchLobbies(finalServerIp);
                if(!tempList.contains(selLobby)){
                    System.out.println("ret is true");
                    if (selLobby != null && !selLobby.trim().isEmpty()) {
                        try {
                            new Game(selLobby);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        dispose();
                    }
                }
                else{
                    errorField.setText("Room '" + selLobby + "' already exists.");
                }
            }
        });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selLobby = inputField.getText();
                ArrayList<String> tempList = fetchLobbies(finalServerIp);


                if(tempList.contains(selLobby)){
                    System.out.println("ret is true");
                    if (selLobby != null && !selLobby.trim().isEmpty()) {
                        try {
                            new Game(selLobby);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        dispose();
                    }
                }else{
                    errorField.setText("Room '" + selLobby + "' doesn't exists.");
                }
            }
        });

        list.setBounds(1, 1, 199, 461);
        inputField.setBounds(200,1,300,20);

        createButton.setBounds(260,50,150,25);
        joinButton.setBounds(260,100,150,25);
        findButton.setBounds(260,150,150,25);

        if(inputField.getText().isEmpty()){
            joinButton.setEnabled(false);
            createButton.setEnabled(false);
        }
        else {
            joinButton.setEnabled(true);
            createButton.setEnabled(true);
        }

        inputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged(); // This method is mainly for text formatting changes.
            }

            public void textChanged() {
                if(inputField.getText().isEmpty()){
                    joinButton.setEnabled(false);
                    createButton.setEnabled(false);
                    findButton.setEnabled(false);
                }
                else {
                    joinButton.setEnabled(true);
                    createButton.setEnabled(true);
                    findButton.setEnabled(true);
                }
            }
        });

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    inputField.setText(list.getSelectedValue().toString());
                }
            }
        });

        GameFinderGraphics background = new GameFinderGraphics(serverIp);

        findButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                background.setFindImage(new ImageIcon(getClass().getResource("/" + "Find.gif")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                background.setFindImage(new ImageIcon(getClass().getResource("/" + "Back.gif")));
            }
        });
        createButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                background.setFindImage(new ImageIcon(getClass().getResource("/" + "CreateGame.gif")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                background.setFindImage(new ImageIcon(getClass().getResource("/" + "Back.gif")));
            }
        });
        joinButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                background.setFindImage(new ImageIcon(getClass().getResource("/" + "JoinGame.gif")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                background.setFindImage(new ImageIcon(getClass().getResource("/" + "Back.gif")));
            }
        });

        Border border =  BorderFactory.createLineBorder(new Color(0, 255, 235));

        createButton.setBackground(Color.white);
        createButton.setBorder(border);
        createButton.setForeground(Color.black);

        inputField.setBackground(Color.white);
        inputField.setSelectedTextColor(Color.white);
        inputField.setDisabledTextColor(Color.black);
        inputField.setSelectionColor(Color.black);
        inputField.setCaretColor(Color.black);
        inputField.setForeground(Color.black);
        inputField.setBorder(border);

        list.setBackground(Color.black);
        list.setBorder(border);
        list.setForeground(Color.white);
        list.setSelectionBackground(Color.white);
        list.setSelectionForeground(Color.black);


        background.setLayout(null);
        setContentPane(background);

        add(list);
        add(inputField);
        add(createButton);
        add(joinButton);
        add(findButton);
        add(majorErrorField);
        add(errorField);


        setTitle("Game finder");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }
    private static ArrayList<String> fetchLobbies(String ip) {
        try (Socket socket = new Socket(ip, 5555);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("GET_LOBBIES");
            out.flush();
            return (ArrayList<String>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>(); // Return empty if server is down
        }
    }
}
