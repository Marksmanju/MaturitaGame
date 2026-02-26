package org.example.multigame.shared;

import org.example.multigame.server.ServerGameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState implements Serializable{
    public int localPlayerId;
    public int gameState;
    public int gameTimer;

    public ArrayList<ProjectileState> projectileStates = new ArrayList<>();
    public PointState pointState;

    public Map<String, ServerGameLogic> activeLobbies;


    public String test;

    public PlayerState player1;
    public PlayerState player2;
    public PlayerState player3;
    public PlayerState player4;


    public GameState copy() {
        //THIS IS FOR STUFF ALL PLAYERS SHOULD SEE THE SAME
        GameState gs = new GameState();

        gs.projectileStates = new ArrayList<>();
        for (ProjectileState p : projectileStates) {
            gs.projectileStates.add(p.copy());
        }

        gs.gameTimer = gameTimer;
        gs.test = "TEST";
        gs.player1 = player1.copy();
        gs.player2 = player2.copy();
        gs.player3 = player3.copy();
        gs.player4 = player4.copy();
        gs.pointState = pointState.copy();
        return gs;
    }
}
