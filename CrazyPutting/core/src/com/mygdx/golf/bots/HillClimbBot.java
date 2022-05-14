package com.mygdx.golf.bots;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.State;
import com.mygdx.golf.engine.Engine;

public class HillClimbBot implements Bot {
    private Engine engine;

    public HillClimbBot(Engine engine) {
        this.engine = engine;
    }
    @Override
    public Vector2 findBestMove() {
        Vector2 bestShot = new Vector2(0,0);
        State botState = new State(engine.state.getPosition(), bestShot);
        double minDistance = engine.simulateShot(botState);
        
        float[] ranges = new float[] {10, 4, 1};

        for (int i = 0; i < ranges.length; i++) {
            float range= ranges[i];
            float increment = range/2;
            
            boolean localMaximum = false;
            while(!localMaximum) {

                System.out.println("loop");
                localMaximum = true;

                outer : for(float x = -range; x <= range; x += increment) {
                    for (float y = -range; y <= range; y += increment) {
                         botState = new State();
                        botState.setPosition(engine.state.getPosition()); 
                        Vector2 shootingVelocity = new Vector2(bestShot.x + x, bestShot.y + y); 
                        botState.setVelocity(shootingVelocity);
                        double distance = engine.simulateShot(botState);
                        // System.out.println(distance);
                        if(distance < minDistance) {
                            // System.out.println("min distance: " +minDistance);
                            // System.out.println("distance: " +distance);
                            // System.out.println("best shot: " +shootingVelocity);
    
                            System.out.println(distance < minDistance);
                            minDistance = distance;
                            bestShot = shootingVelocity;
                            localMaximum = false;
                            break outer;
                        }
        
                    }   
                }
            }
        }

        
      
        System.out.println("best returned shot: " +bestShot);
        System.out.println("best returned distance: " +minDistance);

        return bestShot;
    }
}
