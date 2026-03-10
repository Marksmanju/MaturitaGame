package org.example.multigame.client;

import org.example.multigame.shared.Events;
import org.example.multigame.shared.GameState;
import org.example.multigame.shared.ProjectileState;

import javax.swing.*;
import java.awt.*;

public class GameGraphics extends JPanel {
    private GameState state;

    ImageIcon player1Image = new ImageIcon(getClass().getResource("/" + "Blue.png"));
    ImageIcon player2Image = new ImageIcon(getClass().getResource("/" + "Red.png"));
    ImageIcon player3Image = new ImageIcon(getClass().getResource("/" + "Yellow.png"));
    ImageIcon player4Image = new ImageIcon(getClass().getResource("/" + "Green.png"));
    ImageIcon pointImage = new ImageIcon(getClass().getResource("/" + "Point.gif"));
    //ImageIcon backImage = new ImageIcon(getClass().getResource("/" + "Back.png"));
    ImageIcon guiImage = new ImageIcon(getClass().getResource("/" + "GuiGame.png"));
    ImageIcon guiBlue = new ImageIcon(getClass().getResource("/" + "GuiBlue.png"));
    ImageIcon guiRed = new ImageIcon(getClass().getResource("/" + "GuiRed.png"));
    ImageIcon guiYellow = new ImageIcon(getClass().getResource("/" + "GuiYellow.png"));
    ImageIcon guiGreen = new ImageIcon(getClass().getResource("/" + "GuiGreen.png"));
    ImageIcon colorlessImage = new ImageIcon(getClass().getResource("/" + "Colorless.png"));

    Font scoreFont = new Font("DS-Digital",Font.PLAIN,40);
    Font timerFont = new Font("DS-Digital",Font.PLAIN,100);

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (state == null) return;

        g.setColor(new Color(255, 252, 78, 126));
        g.fillRect(0,0,1000,1000);

        g.setColor(Color.black);
        switch (state.localPlayerId){
            case 1:
                g.drawImage(guiBlue.getImage(),0, 0, 786, 800,null);
                break;
            case 2:
                g.drawImage(guiRed.getImage(),0, 0, 786, 800,null);
                break;
            case 3:
                g.drawImage(guiYellow.getImage(),0, 0, 786, 800,null);
                break;
            case 4:
                g.drawImage(guiGreen.getImage(),0, 0, 786, 800,null);
                break;
            default:
                g.drawImage(guiImage.getImage(),0, 0, 786, 800,null);
        }

        //g.drawImage(backImage.getImage(),0, 0, 800, 800,null);


        if(state.player1.online == true) {
            g.setColor(Color.RED);
            //g.fillRect(state.player1.x, state.player1.y, 20, 20);
            if (state.events != Events.SAMECOLOR){
                g.drawImage(player1Image.getImage(), state.player1.x, state.player1.y, 40, 40, null);
            }else {
                g.drawImage(colorlessImage.getImage(), state.player1.x, state.player1.y, 40, 40, null);
            }
        }
        if(state.player2.online == true) {
            g.setColor(Color.CYAN);
            //g.fillRect(state.player2.x, state.player2.y, 20, 20);
            if (state.events != Events.SAMECOLOR){
                g.drawImage(player2Image.getImage(), state.player2.x, state.player2.y, 40, 40, null);
            }else {
                g.drawImage(colorlessImage.getImage(), state.player2.x, state.player2.y, 40, 40, null);
            }
        }
        if(state.player3.online == true) {
            g.setColor(Color.yellow);
            g.fillRect(state.player3.x, state.player3.y, 20, 20);
            if (state.events != Events.SAMECOLOR){
                g.drawImage(player3Image.getImage(), state.player3.x, state.player3.y, 40, 40, null);
            }else {
                g.drawImage(colorlessImage.getImage(), state.player3.x, state.player3.y, 40, 40, null);
            }
        }
        if(state.player4.online == true) {
            g.setColor(Color.GREEN);
            //g.fillRect(state.player4.x, state.player4.y, 20, 20);
            if (state.events != Events.SAMECOLOR){
                g.drawImage(player4Image.getImage(), state.player4.x, state.player4.y, 40, 40, null);
            }else {
                g.drawImage(colorlessImage.getImage(), state.player4.x, state.player4.y, 40, 40, null);
            }
        }

        g.setColor(Color.WHITE);
        for (ProjectileState p : state.projectileStates) {
            g.fillOval(p.x, p.y, 6, 6);
        }

        g.setColor(Color.WHITE);
        //g.fillOval(state.pointState.x,state.pointState.y,10,10);
        g.drawImage(pointImage.getImage(),state.pointState.x,state.pointState.y,20,20,this);
        g.drawImage(pointImage.getImage(),state.MpointState1.x,state.MpointState1.y,20,20,this);
        g.drawImage(pointImage.getImage(),state.MpointState2.x,state.MpointState2.y,20,20,this);
        
        g.setColor(Color.white);
        g.setFont(scoreFont);
        //g.drawString("Player: " + state.localPlayerId,35,80);
        g.setColor(Color.cyan);
        g.drawString("" + state.player1.score,290,50);
        g.setColor(Color.red);
        g.drawString("" + state.player2.score,480,50);
        g.setColor(Color.yellow);
        g.drawString("" + state.player3.score,290,100);
        g.setColor(Color.green);
        g.drawString("" + state.player4.score,480,100);
        g.setColor(Color.white);
        g.setFont(timerFont);
        g.drawString(getFormattedTime(),35,90);

        g.setFont(scoreFont);
        g.drawString(state.events.name(),500,90);
    }
    public void updateState(GameState state) {
        this.state = state;
        repaint();
    }
    public String getFormattedTime() {
        int minutes;
        int seconds;
        if(state.stateOfGame == 1){
            minutes = state.gameTimer / 60;
            seconds = state.gameTimer % 60;
        }
        else{
            minutes = state.preGameTimer / 60;
            seconds = state.preGameTimer % 60;
        }
        return String.format("%d:%02d", minutes, seconds);
    }
}