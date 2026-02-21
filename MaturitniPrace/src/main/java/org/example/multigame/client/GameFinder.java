package org.example.multigame.client;

import javax.swing.*;
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

public class GameFinder{
    private String serverIp;
    private ArrayList<String> lobbyList;


    public GameFinder(String serverIp) throws Exception {
        ArrayList<String> lobbyList = fetchLobbies(serverIp);

        JLabel errorField = new JLabel("");
        JLabel majorErrorField = new JLabel("");
        errorField.setForeground(Color.RED);
        majorErrorField.setForeground(Color.RED);
        if (serverIp == null) {
            majorErrorField.setText("Auto-Discovery failed. Enter IP manually:");
            serverIp = null;
        }

        System.out.println(serverIp);
        System.out.println(lobbyList);

        JFrame frame = new JFrame();

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

                // 2. Update the JList with the new data
                list.setListData(updatedLobbies.toArray());
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
                        frame.dispose();
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
                        frame.dispose();
                    }
                }else{
                    errorField.setText("Room '" + selLobby + "' doesn't exists.");
                }
            }
        });


        errorField.setBounds(400,100,350,25);
        majorErrorField.setBounds(200,400,350,25);

        list.setBounds(0, 0, 200, 600);
        inputField.setBounds(200,0,200,20);

        createButton.setBounds(200,50,150,25);
        joinButton.setBounds(200,100,150,25);
        findButton.setBounds(200,150,150,25);

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

        frame.add(list);
        frame.add(inputField);
        frame.add(createButton);
        frame.add(joinButton);
        frame.add(findButton);
        frame.add(majorErrorField);
        frame.add(errorField);

        frame.setSize(600, 600);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
