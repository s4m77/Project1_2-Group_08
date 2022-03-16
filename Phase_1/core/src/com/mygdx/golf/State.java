package com.mygdx.golf;

public class State {
    private double xPos =1;
    private double yPos =1;
    private double xVel = 0;
    private double yVel = 0;

    public State(double xPos, double yPos, double xVel, double yVel){
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = xVel;
        this.yVel = yVel;

    }

    public double getxPos() {
        return xPos;
    }
    public double getyPos() {
        return yPos;
    }
    public double getxVel() {
        return xVel;
    }
    public double getyVel() {
        return yVel;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }
    public void setyPos(double yPos) {
        this.yPos = yPos;
    }
    public void setxVel(double xVel) {
        this.xVel = xVel;
    }
    public void setyVel(double yVel) {
        this.yVel = yVel;
    }
}

