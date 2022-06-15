package com.mygdx.golf.engine.solvers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

/**
 * This implementation of the Runge-Kutta 2 method uses the 
 * classical second-order Runge-Kutta method.
 * Therefore, it uses as final expression for solving:
 * w' = w + h/4(k1 + 3k2)
 * Local error = O(h^3)
 * Global error = O(h^4)
 */
public class RungeKutta2 implements Solver {
    Engine engine;

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Vector2 solvePos(Vector2 position, Vector2 velocity) {
        Vector2 fun1 = solveVel(position, velocity);
        Vector2 k1 = new Vector2(fun1.x*engine.getDt(), fun1.y*engine.getDt());
        
        Vector2 pos2 = new Vector2(position.x + (2.f/3.f)*engine.getDt(), position.y + (2.f/3.f)*engine.getDt());
        Vector2 vel2 = new Vector2(velocity.x + (2.f/3.f)*k1.x, velocity.y + (2.f/3.f)*k1.y);
        Vector2 fun2 = solveVel(pos2, vel2);
        Vector2 k2 = new Vector2(fun2.x*engine.getDt(), fun2.y*engine.getDt());

        return new Vector2(position.x + (1.f/4.f)*(k1.x + 3*k2.x), position.y + (1.f/4.f)*(k1.y + 3*k2.y));
    }


    @Override
    public Vector2 solveVel(Vector2 position, Vector2 velocity) {
        boolean sliding = false;
        Vector2 partials = engine.calcPartialDerivative(position);
        
        float epsilon = 0.1f;
        if(velocity.x < epsilon && velocity.x > -epsilon && velocity.y < epsilon && velocity.y > -epsilon  ) {
            //if ball stopped moving
            //we use epsilon because float is never perfectly equal to zero
            if(engine.getStatic(position) > Math.sqrt(partials.x * partials.x + partials.y *
            partials.y)) {
                //if the grass static friction is stronger than the force of gravity with the slope
                return new Vector2(0,0);
            }else {
                sliding = true;
            }

            
        }

        Vector2 acceleration = sliding ? engine.calcSlidingAcceleration(position, velocity) :
                                         engine.calcAcceleration(position,velocity);

        Vector2 ki1= new Vector2(engine.getDt()*acceleration.x, engine.getDt()*acceleration.y);
        //calculate Acceleration2
        Vector2 updatedPosition = new Vector2(position.x + 2.f/3.f * engine.getDt(), position.y + 2.f/3.f * engine.getDt());
        Vector2 updatedVelocity = new Vector2(velocity.x + 2.f/3.f * ki1.x, velocity.y + 2.f/3.f * ki1.y);
        Vector2 Acceleration2 = sliding ? engine.calcSlidingAcceleration(updatedPosition, updatedVelocity) :
                                         engine.calcAcceleration(updatedPosition, updatedVelocity);
        Vector2 ki2= new Vector2(engine.getDt()*Acceleration2.x, engine.getDt()*Acceleration2.y);

        return new Vector2(velocity.x + 1.f/4.f * ki1.x + 3.f/4.f * ki2.x, velocity.y + 1.f/4.f * ki1.y + 3.f/4.f * ki2.y); 
    }
}