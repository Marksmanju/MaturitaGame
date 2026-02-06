package org.example.otherstuff;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class GameGraphics extends JFrame {
    GameLogic logic;
    public final int scrWidth = 500;
    public final int scrHeight = 500;
    public Draw draw;

    public GameGraphics(GameLogic logic) throws HeadlessException {
        this.draw = new Draw();
        add(draw);
        this.logic = logic;

        setSize(scrWidth, scrHeight);
        setLocationRelativeTo(null); // sets location of the window. Null makes it in the middle of the screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setTitle("Game");
    }
    public class Draw extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponents(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.drawString("FUCK",20,20);

            g2.setColor(Color.red);
            g2.drawRect(logic.playerTwo.getX(),logic.playerTwo.getY(),logic.playerTwo.width,logic.playerTwo.height);
            g2.fillRect(logic.playerTwo.getX(),logic.playerTwo.getY(),logic.playerTwo.width,logic.playerTwo.height);
            g2.setColor(Color.blue);
            g2.drawRect(logic.player.getX(),logic.player.getY(),logic.player.width,logic.player.height);
            g2.fillRect(logic.player.getX(),logic.player.getY(),logic.player.width,logic.player.height);

            //g2.dispose();
        }
    }
    public void render(GameLogic logic) {
        this.logic = logic;
        repaint();
    }
}
