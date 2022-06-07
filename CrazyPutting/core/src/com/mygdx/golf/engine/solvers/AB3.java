package com.mygdx.golf.engine.solvers;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

public class AB3 implements Solver{

    // USING RK3 METHOD (SAME GLOBAL ERROR)
    Solver bootstrapper = new RungeKutta3();
    Engine e;

    private ArrayList<Vector2> positions = new ArrayList<Vector2>();
    private ArrayList<Vector2> velocities = new ArrayList<Vector2>();

    @Override
    public void setEngine(Engine engine) {
        this.e = engine;
        bootstrapper.setEngine(engine);
    }

    @Override
    public Vector2 solveVel(Vector2 position, Vector2 velocity) {
        
        if (velocities.size() < 3){
            velocities.add(velocity);
            positions.add(position);
            return bootstrapper.solveVel(position, velocity);
        }
        
        boolean sliding = false;
        Vector2 partials = e.calcPartialDerivative(position);
        
        float epsilon = 0.1f;
        if(velocity.x < epsilon && velocity.x > -epsilon && velocity.y < epsilon && velocity.y > -epsilon  ) {
            //if ball stopped moving
            //we use epsilon because float is never perfectly equal to zero
            if(e.getStatic(position) > Math.sqrt(partials.x * partials.x + partials.y *
            partials.y)) {
                //if the grass static friction is stronger than the force of gravity with the slope
                return new Vector2(0,0);
            }else {
                sliding = true;
            }
        }

        Vector2 fun1, fun2, fun3;
        fun1 = sliding ? e.calcSlidingAcceleration(positions.get(0), velocities.get(0)) : 
                         e.calcAcceleration(positions.get(0), velocities.get(0));
        fun2 = sliding ? e.calcSlidingAcceleration(positions.get(1), velocities.get(1)) : 
                         e.calcAcceleration(positions.get(1), velocities.get(1));
        fun3 = sliding ? e.calcSlidingAcceleration(positions.get(2), velocities.get(2)) : 
                         e.calcAcceleration(positions.get(2), velocities.get(2));

        return new Vector2(velocity.x + (e.getDt()/12)*(23*fun3.x - 16*fun2.x + 5*fun1.x), 
                           velocity.y + (e.getDt()/12)*(23*fun3.y - 16*fun2.y + 5*fun1.y));
    }


    @Override
    public Vector2 solvePos(Vector2 position, Vector2 velocity) {
        if(positions.size() < 3){
            return bootstrapper.solvePos(position, velocity);
        }

        Vector2 fun1, fun2, fun3;
        fun1 = solveVel(positions.get(0), velocities.get(0));
        fun2 = solveVel(positions.get(1), velocities.get(1));
        fun3 = solveVel(positions.get(2), velocities.get(2));

        return new Vector2(position.x + (e.getDt()/12)*(23*fun3.x - 16*fun2.x + 5*fun1.x),
                           position.y + (e.getDt()/12)*(23*fun3.y - 16*fun2.y + 5*fun1.y));
    }

   
    
}
