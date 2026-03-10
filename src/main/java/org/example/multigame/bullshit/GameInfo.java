package org.example.multigame.bullshit;

import java.io.Serializable;

public class GameInfo implements Serializable {
    public int gameId;
    public int playerCount;

    public GameInfo(int gameId, int playerCount) {
        this.gameId = gameId;
        this.playerCount = playerCount;
    }

    @Override
    public String toString() {
        return "Game " + gameId + " (" + playerCount + "/4)";
    }
}