package org.example.otherstuff;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Game extends Thread{
    Thread gameThread;
    private GameLogic logic;
    public GameGraphics graphics;
    int FPS = 60;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public Game(){
        logic = new GameLogic(this);
        graphics = new GameGraphics(logic);
        logic.initialize();

        graphics.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                //int steps = 10;
                if((code == KeyEvent.VK_W) || (code == KeyEvent.VK_UP)){
                    upPressed = true;
                }
                if((code == KeyEvent.VK_S) || (code == KeyEvent.VK_DOWN)){
                    downPressed = true;
                }
                if((code == KeyEvent.VK_A) || (code == KeyEvent.VK_LEFT)){
                    leftPressed = true;
                }
                if((code == KeyEvent.VK_D) || (code == KeyEvent.VK_RIGHT)){
                    rightPressed = true;
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if((code == KeyEvent.VK_W) || (code == KeyEvent.VK_UP)){
                    upPressed = false;
                }
                if((code == KeyEvent.VK_S) || (code == KeyEvent.VK_DOWN)){
                    downPressed = false;
                }
                if((code == KeyEvent.VK_A) || (code == KeyEvent.VK_LEFT)){
                    leftPressed = false;
                }
                if((code == KeyEvent.VK_D) || (code == KeyEvent.VK_RIGHT)){
                    rightPressed = false;
                }



            }
        });
        graphics.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {



            }

            @Override
            public void mousePressed(MouseEvent e) {


            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    //This starts the game thread.
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void run(){
        //startGameThread();
        double drawInterval = 1000000000/FPS;// returns the current value of the running java virtual machines high resolution time source in nanoseconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;


        while(gameThread != null) { // this is for setting the FPS and such its kinda complicated so dont play with this too much

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >=1) {
                logic.update();
                graphics.render(logic);
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                System.out.println("game loop is funcntional"); // debug message to check if game loop works
                System.out.println("FPS: " + drawCount);
                System.out.println(logic.player.getX() + " " + logic.player.getY());
                drawCount = 0;
                timer = 0;

            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game(); // starts the game Thread ie. starts the game loop
            game.startGameThread();
        });
    }
}