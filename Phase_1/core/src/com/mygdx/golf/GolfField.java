package com.mygdx.golf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GolfField {

    //size of field
    final static double X = 20, Y = 20;
    //constants of field - some to be determined
    final static double GRASS_KINETIC = 0.75, GRASS_STATIC = 0.15, SAND_KINETIC = 0, SAND_STATIC = 0;
    //physical constants
    final static double GRAVITY = 9.81, MAX_SPEED = 5, RADIUS_FINAL = 0.15;

    /**
    * array storing the input.txt file values
    * stores, in order: [0]:x0, [1]:y0, [2]:xt, [3]:yt, [4]:r, [5]:muk, [6]:mus
    * [7]:heightProfile, [8]:sandPitX, [9]:sandPitY, [10]:muks, [11]:muss
    */
    double[] input;

    GolfBall ball;

    GolfField(GolfBall ball){
        this.ball = ball;
        input = new double[12];

    }

    //method responsible for reading the input.txt file when the ball is created
    public void readFile() throws IOException {
        BufferedReader read = new BufferedReader(new FileReader("input.txt"));
        for (int i = 0; i < input.length; i++) {
            String s = read.readLine();
            double d = Double.parseDouble(s);
            input[i] = d;
        }
        read.close();
    }
    
}
