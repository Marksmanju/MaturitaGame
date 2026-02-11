package org.example.multigame.client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameFinder{
    private String serverIp;

    public GameFinder(String serverIp) throws Exception {

        ArrayList<String> lobbyList = fetchLobbies(serverIp);
        System.out.println(lobbyList);

        JFrame frame = new JFrame();

        JList<Object> list = new JList<>(lobbyList.toArray());
        JTextField inputField = new JTextField();

        JButton createButton = new JButton("Create game");
        JButton joinButton = new JButton("Join game");
        JButton findButton = new JButton("Find game");

        // Creating instance of JButton
        //JButton button = new JButton(" GFG WebSite Click");

        // x axis, y axis, width, height
        //button.setBounds(150, 200, 220, 50);

        list.setBounds(0, 0, 200, 600);
        inputField.setBounds(200,0,200,20);

        createButton.setBounds(200,50,150,25);
        joinButton.setBounds(200,100,150,25);
        findButton.setBounds(200,150,150,25);

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


        // adding button in JFrame
        //frame.add(button);

        // 400 width and 500 height
        frame.setSize(600, 600);

        // using no layout managers
        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //JScrollPane scrollPane = new JScrollPane(list);
        //getContentPane().add(scrollPane);

       /*


        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        JList list = new JList<>(lobbyList.toArray());
        //frame.getContentPane().add(label);
        frame.getContentPane().add(list);
        setSize(100,100);

        //Display the window.
        frame.pack();
        frame.setVisible(true);*/
//------------------------------------------------------------------------------------------------------------------------------------------
        System.out.println(serverIp);

        if (serverIp == null) {
            serverIp = JOptionPane.showInputDialog("Auto-Discovery failed. Enter IP manually:");
        }

        if (serverIp != null) {
            List<String> lobbies = fetchLobbies(serverIp);
            // ... show lobby browser as we did before
        }

        List<String> lobbies = fetchLobbies(serverIp);

        String selectedLobby;
        if (lobbies.isEmpty()) {
            selectedLobby = JOptionPane.showInputDialog("No active lobbies. Enter name to create one:");
        } else {
            // Show a dropdown list of active lobbies + an option to create new
            lobbies.add("Create New...");
            selectedLobby = (String) JOptionPane.showInputDialog(null,
                    "Select a Lobby:", "Lobby Browser",
                    JOptionPane.QUESTION_MESSAGE, null,
                    lobbies.toArray(), lobbies.get(0));

            if ("Create New...".equals(selectedLobby)) {
                selectedLobby = JOptionPane.showInputDialog("Enter new lobby name:");
            }
        }

        if (selectedLobby != null && !selectedLobby.trim().isEmpty()) {
            new Game(selectedLobby);
            frame.dispose();

        }
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
//    private static String discoverServerIP() {
//        try (DatagramSocket socket = new DatagramSocket()) {
//            socket.setBroadcast(true);
//            socket.setSoTimeout(2000); // Wait 2 seconds max
//
//            byte[] sendData = "DISCOVER_GAME_SERVER".getBytes();
//            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
//                    InetAddress.getByName("255.255.255.255"), 8888);
//            socket.send(sendPacket);
//
//            byte[] recvBuf = new byte[15000];
//            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
//            socket.receive(receivePacket);
//
//            String response = new String(receivePacket.getData()).trim();
//            if (response.equals("I_AM_THE_SERVER")) {
//                return receivePacket.getAddress().getHostAddress();
//            }
//        } catch (Exception e) {
//            System.out.println("Server not found automatically.");
//        }
//        return null;
//    }
}
