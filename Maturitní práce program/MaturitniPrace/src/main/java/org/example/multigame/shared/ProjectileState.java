package org.example.multigame.shared;

import java.io.Serializable;

public class ProjectileState implements Serializable {
    public int x,y,speed, timeToLive;
    public double direction;

    public ProjectileState(int x, int y, int speed, int timeToLive, double direction) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.timeToLive = timeToLive;
        this.direction = direction;
    }

    public ProjectileState copy(){
        return new ProjectileState(x, y, speed,timeToLive,direction);
    }
}
