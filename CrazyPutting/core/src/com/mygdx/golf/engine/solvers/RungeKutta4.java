package com.mygdx.golf.engine.solvers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;
/**
 * This implementation of the Runge-Kutta 4 method uses the 
 * fourth-order Runge-Kutta method with final expression:
 * w' = w + h/8(k1 + 3k2 + 3k3 + k4)
 * Local error = O(h^4)
 * Global error = O(h^5)
 */
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
                return new Vector2(0,0);
            }else {
                sliding = true;
            }
        }
        
        //k1
        Vector2 acc1 = sliding ? new Vector2(e.calcSlidingAcceleration(position, velocity)) : new Vector2(e.calcAcceleration(position, velocity));
        Vector2 k1 = new Vector2(acc1.x*e.getDt(), acc1.y*e.getDt());
        
        //k2
        Vector2 pos2 = new Vector2(position.x + e.getDt()*(1.f/3.f), position.y + e.getDt()*(1.f/3.f));
        Vector2 vel2 = new Vector2(velocity.x + k1.x*(1.f/3.f), velocity.y + k1.y*(1.f/3.f));
        Vector2 acc2 = sliding ? new Vector2(e.calcSlidingAcceleration(pos2, vel2)) : new Vector2(e.calcAcceleration(pos2, vel2));
        Vector2 k2 = new Vector2(acc2.x*e.getDt(), acc2.y*e.getDt());
        
        //k3
        Vector2 pos3 = new Vector2(position.x + e.getDt()*(2.f/3.f), position.y + e.getDt()*(2.f/3.f));
        Vector2 vel3 = new Vector2(velocity.x - k1.x*(1.f/3.f) + k2.x,velocity.y - k1.y*(1.f/3.f) + k2.y);
        Vector2 acc3 = sliding ? new Vector2(e.calcSlidingAcceleration(pos3, vel3)) : new Vector2(e.calcAcceleration(pos3, vel3));
        Vector2 k3 = new Vector2(acc3.x*e.getDt(), acc3.y*e.getDt());
        
        //k4
        Vector2 pos4 = new Vector2(position.x + e.getDt(), position.y + e.getDt());
        Vector2 vel4 = new Vector2(velocity.x + k1.x - k2.x + k3.x, velocity.y + k1.y - k2.y + k3.y);
        Vector2 acc4 = sliding ? new Vector2(e.calcSlidingAcceleration(pos4, vel4)) : new Vector2(e.calcAcceleration(pos4, vel4));
        Vector2 k4 = new Vector2(acc4.x*e.getDt(), acc4.y*e.getDt());

        return new Vector2(velocity.x + (1.f/8.f)*(k1.x + (3*k2.x) + (3*k3.x) + k4.x),
                            velocity.y + (1.f/8.f)*(k1.y + (3*k2.y) + (3*k3.y) + k4.y));
    }

    @Override
    public Vector2 solvePos(Vector2 position, Vector2 velocity) {
        //k1
        Vector2 fun1 = solveVel(position, velocity);
        Vector2 k1 = new Vector2(fun1.x*e.getDt(), fun1.y*e.getDt());
        
        //k2
        Vector2 pos2 = new Vector2(position.x + e.getDt()*(1.f/3.f), position.y + e.getDt()*(1.f/3.f));
        Vector2 vel2 = new Vector2(velocity.x + k1.x*(1.f/3.f), velocity.y + k1.y*(1.f/3.f));
        Vector2 fun2 = solveVel(pos2, vel2);
        Vector2 k2 = new Vector2(fun2.x*e.getDt(), fun2.y*e.getDt());
        
        //k3
        Vector2 pos3 = new Vector2(position.x + e.getDt()*(2.f/3.f), position.y + e.getDt()*(2.f/3.f));
        Vector2 vel3 = new Vector2(velocity.x - k1.x*(1.f/3.f)+k2.x, velocity.y - k1.y*(1.f/3.f)+k2.y);
        Vector2 fun3 = solveVel(pos3, vel3);
        Vector2 k3 = new Vector2(fun3.x*e.getDt(), fun3.y*e.getDt());
        
        //k4
        Vector2 pos4 = new Vector2(position.x + e.getDt(), position.y + e.getDt());
        Vector2 vel4 = new Vector2(velocity.x + k1.x - k2.x + k3.x, velocity.y + k1.y - k2.y + k3.y);
        Vector2 fun4 = solveVel(pos4, vel4);
        Vector2 k4 = new Vector2(fun4.x*e.getDt(), fun4.y*e.getDt());

        return new Vector2(position.x + 1f/8f*(k1.x + (3*k2.x) + (3*k3.x) + k4.x), 
                            position.y + 1f/8f*(k1.y + (3*k2.y) + (3*k3.y) + k4.y));
    }
}
