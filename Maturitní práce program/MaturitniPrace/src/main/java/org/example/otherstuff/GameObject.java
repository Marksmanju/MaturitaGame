package org.example.otherstuff;

public class GameObject {
    protected Coordinates coord;
    protected int height, width;

    public GameObject(int x, int y){
        this.coord = new Coordinates(x,y);

        this.width = 32;
        this.height = 32;
    }

    public int getX() {
        return coord.x;
    }

    public void setX(int x) {
        this.coord.x = x;
    }

    public int getY() {
        return coord.y;
    }

    public void setY(int y) {
        this.coord.y = y;
    }
}
