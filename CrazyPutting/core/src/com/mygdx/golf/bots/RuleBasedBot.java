package com.mygdx.golf.bots;


import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.State;
import com.mygdx.golf.engine.Engine;

public class RuleBasedBot {
    private Engine engine;

    public RuleBasedBot(Engine engine) {
        this.engine = engine;
    }


    public Vector2 findBestMove() {
        // State state = new State();
        double minDistance = Double.MAX_VALUE;
        Vector2 bestShot = new Vector2();
        for(float x = -5; x < 5; x += 1) {
            for (float y = -5; y < 5; y += 1) {
                State botState = new State();
                botState.setPosition(engine.state.getPosition());  
                Vector2 shootingVelocity = new Vector2(x,y); 
                botState.setVelocity(shootingVelocity);

                double distance = engine.simulateShot(botState);
                // System.out.println(distance);
                if(distance < minDistance) {
                    minDistance = distance;
                    System.out.println(distance);
                    bestShot = shootingVelocity;
                }

            }   
        }


        return bestShot;
    }
}
