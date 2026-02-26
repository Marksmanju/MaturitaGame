package org.example.multigame.client;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.example.multigame.lobby.LobbyList;
import org.example.multigame.shared.GameState;
import org.example.multigame.shared.PlayerInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Game extends JFrame implements Runnable {
    private GameClient client;
    private GameGraphics graphics;
    private PlayerInput bimput = new PlayerInput();
    private volatile boolean up, down, left, right;
    private volatile int mouseX,mouseY;

    public Game(String lobbyName) throws Exception {
        try {
            client = new GameClient(discoverServerIP(), lobbyName);
        } catch (IOException e) {
            if (e.getMessage().equals("LOBBY_FULL")) {
                JOptionPane.showMessageDialog(null,
                        "This lobby is full (4/4 players). Please try another name.",
                        "Lobby Full",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            throw e; // Rethrow other connection errors
        }
        graphics = new GameGraphics(); // CREATES NEW GAMEGRAPHICS OBJECT

        add(graphics); //ADDS GRAPHICS AS A COMPONENT
        setTitle("Lobby: " + lobbyName);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                setKey(e.getKeyCode(), true);
                //System.out.println("Keypressed");
            }

            public void keyReleased(KeyEvent e) {
                setKey(e.getKeyCode(), false);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                bimput.mouseX = e.getX();
                bimput.mouseY = e.getY();
            }
        });

        new Thread(this).start();
    }

    private void setKey(int key, boolean pressed) {
        if (key == KeyEvent.VK_W) up = pressed;
        if (key == KeyEvent.VK_S) down = pressed;
        if (key == KeyEvent.VK_A) left = pressed;
        if (key == KeyEvent.VK_D) right = pressed;
    }

    public void run() {
        while (true) {
            try {
                PlayerInput input = new PlayerInput();
                input.up = up;
                input.down = down;
                input.left = left;
                input.right = right;
                input.mouseX = bimput.mouseX;
                input.mouseY = bimput.mouseY;


                client.sendInput(input);

                GameState state = client.receiveState();
                graphics.updateState(state);



                Thread.sleep(16);
            } catch (Exception e) {
                break;
            }
        }
    }
//    private static List<String> fetchLobbies(String ip) {
//        try (Socket socket = new Socket(ip, 5555);
//             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
//
//            out.writeObject("GET_LOBBIES");
//            out.flush();
//            return (List<String>) in.readObject();
//        } catch (Exception e) {
//            return new ArrayList<>(); // Return empty if server is down
//        }
//    }

    public static void main(String[] args) throws Exception {
//        String serverIp = discoverServerIP();
//        System.out.println(serverIp);
//
//        if (serverIp == null) {
//            serverIp = JOptionPane.showInputDialog("Auto-Discovery failed. Enter IP manually:");
//        }
//
//        if (serverIp != null) {
//            List<String> lobbies = fetchLobbies(serverIp);
//            // ... show lobby browser as we did before
//        }
//
//        List<String> lobbies = fetchLobbies(serverIp);
//
//        String selectedLobby;
//        if (lobbies.isEmpty()) {
//            selectedLobby = JOptionPane.showInputDialog("No active lobbies. Enter name to create one:");
//        } else {
//            // Show a dropdown list of active lobbies + an option to create new
//            lobbies.add("Create New...");
//            selectedLobby = (String) JOptionPane.showInputDialog(null,
//                    "Select a Lobby:", "Lobby Browser",
//                    JOptionPane.QUESTION_MESSAGE, null,
//                    lobbies.toArray(), lobbies.get(0));
//
//            if ("Create New...".equals(selectedLobby)) {
//                selectedLobby = JOptionPane.showInputDialog("Enter new lobby name:");
//            }
//        }
//
//        if (selectedLobby != null && !selectedLobby.trim().isEmpty()) {
//            new Game(selectedLobby);
//        }
        try {
            UIManager.setLookAndFeel( new FlatDarculaLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        GameFinder gameFinder = new GameFinder(discoverServerIP());
    }
    private static String discoverServerIP() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            socket.setSoTimeout(2000); // Wait 2 seconds max

            byte[] sendData = "DISCOVER_GAME_SERVER".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName("255.255.255.255"), 8888);
            socket.send(sendPacket);

            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData()).trim();
            if (response.equals("I_AM_THE_SERVER")) {
                return receivePacket.getAddress().getHostAddress();
            }
        } catch (Exception e) {
            System.out.println("Server not found automatically.");
        }
        return null;
    }
}