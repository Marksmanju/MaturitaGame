package org.example.multigame.client;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameFinder{
    private String serverIp;

    public GameFinder(String serverIp) throws Exception {
        //this.serverIp = serverIp;


        ArrayList<String> lobbyList = fetchLobbies(serverIp);
        System.out.println(lobbyList);

        //JList<Object> list = new JList<>(lobbyList.toArray());


        JFrame frame = new JFrame();

        JList<Object> list = new JList<>(lobbyList.toArray());


        // Creating instance of JButton
        //JButton button = new JButton(" GFG WebSite Click");

        // x axis, y axis, width, height
        //button.setBounds(150, 200, 220, 50);

        list.setBounds(0, 0, 220, 200);

        frame.add(list);

        // adding button in JFrame
        //frame.add(button);

        // 400 width and 500 height
        frame.setSize(500, 600);

        // using no layout managers
        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);
        //JScrollPane scrollPane = new JScrollPane(list);
        //getContentPane().add(scrollPane);


       /* frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        JList list = new JList<>(lobbyList.toArray());
        //frame.getContentPane().add(label);
        frame.getContentPane().add(list);
        setSize(100,100);



        //Display the window.
        frame.pack();
        frame.setVisible(true);*/

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
