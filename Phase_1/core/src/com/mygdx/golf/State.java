package com.mygdx.golf;

public class State {
    private static double xPos =1;
    private static double yPos =1;
    private static double xVel = 0;
    private static double yVel = 0;

   

    public static double getxPos() {
        return xPos;
    }
    public static double getyPos() {
        return yPos;
    }
    public static double getxVel() {
        return xVel;
    }
    public static double getyVel() {
        return yVel;
    }

    public static void setxPos(double xPos) {
        State.xPos = xPos;
    }
    public static void setyPos(double yPos) {
        State.yPos = yPos;
    }
    public static void setxVel(double xVel) {
        State.xVel = xVel;
    }
    public static void setyVel(double yVel) {
        State.yVel = yVel;
    }
}

