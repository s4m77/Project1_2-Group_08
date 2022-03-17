package com.mygdx.golf.engine.solvers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

public class Euler2 implements Solver {
    Engine engine;

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Vector2 solvePos(Vector2 position, Vector2 velocity) {
        
        return new Vector2(position.x + engine.getDt() * velocity.x, position.y + engine.getDt() * velocity.y);
        
    }


    @Override
    public Vector2 solveVel(Vector2 position, Vector2 velocity) {
        Vector2 acceleration = engine.calcAcceleration(position,velocity);
        Vector2 partials = engine.calcPartialDerivative(position);
        
        float epsilon = 0.1f;
        if(velocity.x < epsilon && velocity.x > -epsilon && velocity.y < epsilon && velocity.y > -epsilon  ) {
            //if ball stopped moving
            if(engine.getGrassStatic() > Math.sqrt(partials.x * partials.x + partials.y *
            partials.y)) {

                engine.stopBall();
                return new Vector2(0,0);
            }
            
            acceleration = engine.calcSlidingAcceleration(position, velocity);

            
        }

        return new Vector2(velocity.x + engine.getDt()*acceleration.x, velocity.y +
        engine.getDt()*acceleration.y);

    }
}
