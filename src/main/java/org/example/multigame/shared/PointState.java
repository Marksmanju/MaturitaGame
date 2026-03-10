package org.example.multigame.shared;

import java.io.Serializable;

public class PointState implements Serializable {
    public int x, y;

    public PointState(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PointState copy() {
        return new PointState(x, y);
    }
}
