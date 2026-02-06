package org.example.multigame.client;

import org.example.multigame.shared.GameState;
import org.example.multigame.shared.PlayerInput;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Game extends JFrame implements Runnable {
    private GameClient client;
    private GameGraphics graphics;
    private PlayerInput bimput = new PlayerInput();
    private volatile boolean up, down, left, right;
    private volatile int mouseX,mouseY;

    public Game() throws Exception {
        client = new GameClient("192.168.1.222"); // CREATES AND CONNECTTS CLIENT TO CURRENT IP
        graphics = new GameGraphics(); // CREATES NEW GAMEGRAPHICS OBJECT

        add(graphics); //ADDS GRAPHICS AS A COMPONENT
        setTitle("LAN Multiplayer Game");
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

    public static void main(String[] args) throws Exception {
        new Game();
    }
}