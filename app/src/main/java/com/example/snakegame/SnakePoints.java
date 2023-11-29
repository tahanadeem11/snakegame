package com.example.snakegame;

public class SnakePoints {

    private int positionX, PositionY;

    public SnakePoints(int positionX, int positionY) {
        this.positionX = positionX;
        this.PositionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX){
        this.positionX = positionX;
    }

    public int getPositionY() {
        return PositionY;
    }

    public void setPositionY(int positionY) {
        this.PositionY = positionY;
    }
}
