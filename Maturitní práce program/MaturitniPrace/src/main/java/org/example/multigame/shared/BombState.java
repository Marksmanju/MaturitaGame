package org.example.multigame.shared;

import java.io.Serializable;

public class BombState implements Serializable {
    public int x, y;

    public BombState(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public BombState copy() {
        return new BombState(x, y);
    }
}
