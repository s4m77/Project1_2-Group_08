package com.mygdx.golf.engine.solvers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;


public class RungeKutta2 implements Solver {
    Engine engine;

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Vector2 solvePos(Vector2 position, Vector2 velocity) {

        Vector2 ki1= new Vector2(engine.getDt()*velocity.x, engine.getDt()*velocity.y);

        //calculate Velocity2
        Vector2 updatedPosition= new Vector2(position.x + 2.f/3.f * engine.getDt(), position.y + 2.f/3.f * engine.getDt());
        Vector2 updatedVelocity= new Vector2(velocity.x + 2.f/3.f * ki1.x, velocity.y + 2.f/3.f * ki1.y);
        Vector2 Velocity2= solveVel(updatedPosition, updatedVelocity);

        Vector2 ki2= new Vector2(engine.getDt()*Velocity2.x, engine.getDt()*Velocity2.y);

        return new Vector2(position.x + 1.f/4.f * ki1.x + 3.f/4.f * ki2.x, position.y + 1.f/4.f * ki1.y + 3.f/4.f * ki2.y); 
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
                //engine.stopBall();
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