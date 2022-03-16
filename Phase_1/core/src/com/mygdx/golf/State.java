package com.mygdx.golf;

import com.badlogic.gdx.math.Vector2;

public class State {
    private double xPos =1;
    private double yPos =1;
    private double xVel = 0;
    private double yVel = 0;

    static double xPosStatic;
    static double yPosStatic;
    static double xVelStatic;
    static double yVelStatic;

    static Vector2 position;
    static Vector2 velocity;

    public static Vector2 getPosition() {
        return position;
    }
    public static Vector2 getVelocity() {
        return velocity;
    }
    public static void setPosition(Vector2 position) {
        State.position = position;
    }
    public static void setVelocity(Vector2 velocity) {
        State.velocity = velocity;
    }

    public State(double xPos, double yPos, double xVel, double yVel){
        this.xPos = xPosStatic = xPos;
        this.yPos = yPosStatic = yPos;
        this.xVel = xVelStatic = xVel;
        this.yVel = yVelStatic = yVel;
    }
    //ACCESSORS
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
    //SETTERS
    public void setxPos(double xPos) {
        this.xPos = xPosStatic = xPos;
    }
    public void setyPos(double yPos) {
        this.yPos = yPosStatic = yPos;
    }
    public void setxVel(double xVel) {
        this.xVel = xVelStatic = xVel;
    }
    public void setyVel(double yVel) {
        this.yVel= yVelStatic = yVel;
    }
    //STATIC ACCESSORS
    public static double getxPosStatic(){
        return xPosStatic;
    }

    public static double getyPosStatic(){
        return yPosStatic;
    }

    public static double getxVelStatic(){
        return xVelStatic;
    }

    public static double getyVelStatic(){
        return yVelStatic;
    }

    public static void setxPosStatic(double xPosStatic) {
        State.xPosStatic = xPosStatic;
    }

    public static void setyPosStatic(double yPosStatic) {
        State.yPosStatic = yPosStatic;
    }



}

