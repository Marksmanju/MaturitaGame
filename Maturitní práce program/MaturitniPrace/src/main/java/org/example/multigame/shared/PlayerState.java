package org.example.multigame.shared;

import java.io.Serializable;

public class PlayerState implements Serializable {
    public int x, y, id, score;
    public boolean online;
    public int targetMouseX, targetMouseY;

    public PlayerState(int x, int y,boolean online) {
        this.x = x;
        this.y = y;
        this.id = 0;
        this.score = 0;
        this.online = online;
    }
    public PlayerState copy() {
        return new PlayerState(x, y, online);
    }
}
