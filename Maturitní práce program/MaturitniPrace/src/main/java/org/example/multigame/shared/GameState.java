package org.example.multigame.shared;

import org.example.multigame.server.ServerGameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState implements Serializable{
    public int localPlayerId;
    public ArrayList<Boolean> roomPlayerOnlineList;
    public int gameState;
    public int gameTimer;
    public int preGameTimer;
    public int postGameTimer;
    public int stateOfGame; // This is what state the game is in, ie. Waiting for the game to start, game started

    public ArrayList<ProjectileState> projectileStates = new ArrayList<>();
    public PointState pointState;

    public BombState bombState1;
    public BombState bombState2;
    public BombState bombState3;

    public PointState MpointState1;
    public PointState MpointState2;
    public Events events;

    public Map<String, ServerGameLogic> activeLobbies;


    public String test;

    public PlayerState player1;
    public PlayerState player2;
    public PlayerState player3;
    public PlayerState player4;

    public PlayerState winner;


    public GameState copy() {
        //THIS IS FOR STUFF ALL PLAYERS SHOULD SEE THE SAME
        GameState gs = new GameState();

        gs.projectileStates = new ArrayList<>();
        for (ProjectileState p : projectileStates) {
            gs.projectileStates.add(p.copy());
        }
        gs.bombState1 = bombState1.copy();
        gs.bombState2 = bombState2.copy();
        gs.bombState3 = bombState3.copy();
        gs.roomPlayerOnlineList = roomPlayerOnlineList;
        gs.stateOfGame = stateOfGame;
        gs.events = events;
        gs.gameTimer = gameTimer;
        gs.preGameTimer = preGameTimer;
        gs.postGameTimer = postGameTimer;
        gs.test = "TEST";
        gs.player1 = player1.copy();
        gs.player2 = player2.copy();
        gs.player3 = player3.copy();
        gs.player4 = player4.copy();
        gs.pointState = pointState.copy();
        gs.MpointState1 = MpointState1.copy();
        gs.MpointState2 = MpointState2.copy();
        gs.winner = winner.copy();
        return gs;
    }
}
