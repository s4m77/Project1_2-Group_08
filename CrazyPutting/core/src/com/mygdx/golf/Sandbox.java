package com.mygdx.golf;


import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;
import com.mygdx.golf.engine.solvers.*;

public class Sandbox {
    public static void main(String[] args) {
        double tstart;
        double tend = 1.5;
        Solver solver = new RungeKutta4();
        Vector2 position = new Vector2(-1f, -0.5f);
        Vector2 velocity = new Vector2(3f, 0.5f);
        Engine e = new Engine(solver, false);
        for (tstart = 0; tstart <= tend; tstart+=e.getDt()){
            position = solver.solvePos(position, velocity);
            velocity = solver.solveVel(position, velocity);
            
        }
        System.out.println("EULER: " + position.x );

    }
}
