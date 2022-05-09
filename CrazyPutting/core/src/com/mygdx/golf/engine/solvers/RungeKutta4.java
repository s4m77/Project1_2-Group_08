package com.mygdx.golf.engine.solvers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

public class RungeKutta4 implements Solver{

    private Engine e;

    public RungeKutta4(Engine e){
        this.e = e;
    }

    @Override
    public void setEngine(Engine engine) {
        this.e = engine;
    }

    @Override
    public Vector2 solveVel(Vector2 position, Vector2 velocity) {


        
        Vector2 acceleration = e.calcAcceleration(position,velocity);
        Vector2 partials = e.calcPartialDerivative(position);
        float epsilon = 0.1f;

        if(velocity.x < epsilon && velocity.x > -epsilon && velocity.y < epsilon && velocity.y > -epsilon  ) {
            //if ball stopped moving
            //we use epsilon because float is never perfectly equal to zero
            if(e.getGrassStatic() > Math.sqrt(partials.x * partials.x + partials.y *
            partials.y)) {
                //if the grass static friction is stronger than the force of gravity with the slope
                e.stopBall();
                return new Vector2(0,0);
            }else {
                acceleration = e.calcSlidingAcceleration(position, velocity);
            }
        }

        Vector2 k1 = new Vector2(e.calcAcceleration(position, velocity)).scl(e.getDt());

        Vector2 k2 = new Vector2(e.calcAcceleration(position.add(e.getDt()/2,e.getDt()/2),
                                velocity.add(k1.scl(.5f)))).scl(e.getDt());

        Vector2 k3 = new Vector2(e.calcAcceleration(position.add(e.getDt()/2, e.getDt()/2),
                                velocity.add(k2.scl(.5f)))).scl(e.getDt());

        Vector2 k4 = new Vector2(e.calcAcceleration(position.add(e.getDt(), e.getDt()),
                                velocity.add(k3))).scl(e.getDt());

        return velocity.add(k1).add(k2.scl(2)).add(k3.scl(2)).add(k4).scl(1/6);
    }

    @Override
    public Vector2 solvePos(Vector2 position, Vector2 velocity) {
        Vector2 newVel = solveVel(position, velocity);
        
        Vector2 k1 = new Vector2(newVel).scl(e.getDt());
        
        Vector2 pos2 = position.add(k1.scl(0.5f));
        Vector2 k2 = new Vector2(solveVel(pos2, newVel)).scl(e.getDt());

        Vector2 pos3 = position.add(k2.scl(0.5f));
        Vector2 k3 = solveVel(pos3, newVel).scl(e.getDt());

        Vector2 pos4 = position.add(k3);
        Vector2 k4 = new Vector2(solveVel(pos4, newVel)).scl(e.getDt());

        Vector2 sum = k1.add(k2.scl(2)).add(k3.scl(2)).add(k4);

        return position.add(sum.scl(1f/6f));
    }
}
