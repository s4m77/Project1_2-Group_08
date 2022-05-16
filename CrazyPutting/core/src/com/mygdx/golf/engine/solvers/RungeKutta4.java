package com.mygdx.golf.engine.solvers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

public class RungeKutta4 implements Solver{

    private Engine e;


    @Override
    public void setEngine(Engine engine) {
        this.e = engine;
    }

    @Override
    public Vector2 solveVel(Vector2 position, Vector2 velocity) {
        boolean sliding = false;
        Vector2 partials = e.calcPartialDerivative(position);
        
        float epsilon = 0.1f;
        if(velocity.x < epsilon && velocity.x > -epsilon && velocity.y < epsilon && velocity.y > -epsilon  ) {
            //if ball stopped moving
            //we use epsilon because float is never perfectly equal to zero
            if(e.getStatic(position) > Math.sqrt(partials.x * partials.x + partials.y *
            partials.y)) {
                //if the grass static friction is stronger than the force of gravity with the slope
                //e.stopBall();
                return new Vector2(0,0);
            }else {
                sliding = true;
            }
        }

        Vector2 a1 = sliding ? new Vector2(e.calcAcceleration(position, velocity)) : new Vector2(e.calcSlidingAcceleration(position, velocity));
        Vector2 k1 = a1.scl(e.getDt());

        Vector2 p2 = new Vector2(position).add(e.getDt()/2f, e.getDt()/2f);
        Vector2 v2 = new Vector2(velocity).add(k1.scl(0.5f));
        Vector2 a2 = sliding ? new Vector2(e.calcAcceleration(p2, v2)) : new Vector2(e.calcSlidingAcceleration(p2, v2));
        Vector2 k2 = a2.scl(e.getDt());

        //p3 == p2
        Vector2 v3 = new Vector2(velocity).add(k2.scl(0.5f));
        Vector2 a3 = sliding ? new Vector2(e.calcAcceleration(p2, v3)) : new Vector2(e.calcSlidingAcceleration(p2, v3));
        Vector2 k3 = a3.scl(e.getDt());

        Vector2 p4 = new Vector2(position).add(e.getDt(), e.getDt());
        Vector2 v4 = new Vector2(velocity).add(k3);
        Vector2 a4 = sliding ? new Vector2(e.calcAcceleration(p4, v4)) : new Vector2(e.calcSlidingAcceleration(p4, v4));
        Vector2 k4 = a4.scl(e.getDt());

        k2.scl(2f); k3.scl(2f);
        Vector2 sum = k1.add(k2).add(k3).add(k4);
        return new Vector2(velocity.add(sum.scl(1f/6f)));
    }

    @Override
    public Vector2 solvePos(Vector2 position, Vector2 velocity) {
        Vector2 newVel = solveVel(position, velocity);
        
        Vector2 k1 = new Vector2(newVel).scl(e.getDt());
        
        Vector2 pos2 = new Vector2(position).add(k1.scl(0.5f));
        Vector2 k2 = new Vector2(solveVel(pos2, newVel)).scl(e.getDt());

        Vector2 pos3 = new Vector2(position).add(k2.scl(0.5f));
        Vector2 k3 = new Vector2(solveVel(pos3, newVel)).scl(e.getDt());

        Vector2 pos4 = new Vector2(position).add(k3);
        Vector2 k4 = new Vector2(solveVel(pos4, newVel)).scl(e.getDt());

        k2.scl(2f); k3.scl(2f);
        Vector2 sum = k1.add(k2).add(k3).add(k4);
        return new Vector2(position.add(sum.scl(1f/6f)));
    }
}
