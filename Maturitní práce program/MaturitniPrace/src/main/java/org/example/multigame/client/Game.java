package org.example.multigame.client;

/*import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;*/

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
    ImageIcon icon = new ImageIcon(getClass().getResource("/" + "Colorless.png"));

    public Game(String lobbyName) throws Exception {
        try {
            client = new GameClient(discoverServerIP(), lobbyName);
        } catch (IOException e) {
            if (e.getMessage().equals("LOBBY_FULL")) {
                JOptionPane.showMessageDialog(null,
                        "This lobby is full or the game has started. Please try another name.",
                        "Cannot join Lobby",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            throw e; // Rethrow other connection errors
        }


        graphics = new GameGraphics(); // CREATES NEW GAMEGRAPHICS OBJECT



        add(graphics); //ADDS GRAPHICS AS A COMPONENT
        setTitle("Lobby: " + lobbyName);
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setIconImage(icon.getImage());
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
                if (state.postGameTimer == 0){
                    System.exit(0);
                }
                Thread.sleep(16);
            } catch (Exception e) {
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
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