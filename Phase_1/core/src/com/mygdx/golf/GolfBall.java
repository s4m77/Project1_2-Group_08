package com.mygdx.golf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.Buffer;

public class GolfBall {
    
    double x, y, radius, speed, momentum;


    GolfBall(int x, int y){
        this. x = x; this.y = y;
        //radius = 2.13;
        speed = 0;


    }
    //set speed aof ball
    public void setSpeed(double speed){
        this.speed = speed;
    }
    //move a ball to a given position
    public void moveBall(double x, double y){
        this.x = x; this.y = y;
    }





    
}
