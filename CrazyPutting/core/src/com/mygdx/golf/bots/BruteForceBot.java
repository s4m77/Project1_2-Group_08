package com.mygdx.golf.bots;


import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.State;
import com.mygdx.golf.engine.Engine;

public class BruteForceBot implements Bot{
    private Engine engine;

    public BruteForceBot(Engine engine) {
        this.engine = engine;
    }


    public Vector2 findBestMove(Vector2 target, boolean zeroWhenScored) {
        // State state = new State();
        double minDistance = Double.MAX_VALUE;
        float range = 7;
        Vector2 bestShot = new Vector2();
        for(float x = -range; x < range; x += 1) {
            for (float y = -range; y < range; y += 1) {
                State botState = new State();
                botState.setPosition(engine.state.getPosition());  
                Vector2 shootingVelocity = new Vector2(x,y); 
                botState.setVelocity(shootingVelocity);

                double distance = engine.simulateShotDistanceToPoint(botState, target, zeroWhenScored);
                if(distance == 0) {
                    return shootingVelocity;
                }
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
