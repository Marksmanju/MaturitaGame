package org.example.multigame.client;

import org.example.multigame.shared.GameState;
import org.example.multigame.shared.ProjectileState;

import javax.swing.*;
import java.awt.*;

public class GameGraphics extends JPanel {
    private GameState state;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (state == null) return;

        g.setColor(Color.black);
        g.fillRect(0,0,1000,1000);
        if(state.player1.online == true) {
            g.setColor(Color.RED);
            g.fillRect(state.player1.x, state.player1.y, 20, 20);
        }
        if(state.player2.online == true) {
            g.setColor(Color.CYAN);
            g.fillRect(state.player2.x, state.player2.y, 20, 20);
        }
        if(state.player3.online == true) {
            g.setColor(Color.yellow);
            g.fillRect(state.player3.x, state.player3.y, 20, 20);
        }
        if(state.player4.online == true) {
            g.setColor(Color.GREEN);
            g.fillRect(state.player4.x, state.player4.y, 20, 20);
        }

        g.setColor(Color.WHITE);
        for (ProjectileState p : state.projectileStates) {
            g.fillOval(p.x, p.y, 6, 6);
        }

        g.setColor(Color.WHITE);
        g.fillOval(state.pointState.x,state.pointState.y,10,10);
        
        g.setColor(Color.white);
        g.drawString("SUP" + state.localPlayerId,50,50);
        g.drawString("Timer" + state.gameTimer,50,150);

    }
    public void updateState(GameState state) {
        this.state = state;
        repaint();
    }
}