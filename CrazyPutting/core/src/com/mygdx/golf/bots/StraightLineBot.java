package com.mygdx.golf.bots;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.State;
import com.mygdx.golf.engine.Engine;

public class StraightLineBot implements Bot {
    private Engine engine;

    public StraightLineBot(Engine engine) {
        this.engine = engine;
    }
// public static void main(String[] args) {
//     Vector2 rest = new Vector2(1,1);
//     rest.scl(2);
//     System.out.println(rest);
// }

    public Vector2 findBestMove(Vector2 target, boolean zeroWhenScored) {
        Vector2 position = engine.state.getPosition();
        // double distance = Double.MAX_VALUE;
        
        float x = target.x - position.x  ;
        float y = target.y - position.y  ;
        Vector2 initialShot = new Vector2(x,y);
        Vector2 bestShot = new Vector2(x,y);
        float scalarA = 2;
        float scalarB = 0.2f;
        double distA = calcDistForScalar(scalarA, initialShot, target);
        System.out.println(distA);
        
        double distB= calcDistForScalar(scalarB, initialShot, target);
        if(distA == Double.MIN_VALUE|| distB== Double.MIN_VALUE ) {
            System.out.println("Return null!");
            return null;
        }
        double minDistance;
        if(Math.abs(distA) < Math.abs(distB)) {
            minDistance = Math.abs(distA);
            bestShot.x = initialShot.x*scalarA;
            bestShot.y = initialShot.y*scalarA;
        }else {
            minDistance = Math.abs(distB);
            bestShot.x = initialShot.x*scalarB;
            bestShot.y = initialShot.y*scalarB;
        }
        // System.out.println("dist A" + distA);
        // System.out.println("dist B" + distB);

        while(minDistance > 0.25) {
            float scalarMiddle = (scalarA + scalarB)/2.0f;
            // System.out.println("scalarA" + scalarA);
            // System.out.println("scalarB" + scalarB);
            // System.out.println("scalarMiddle" + scalarMiddle);

            double distMiddle = calcDistForScalar(scalarMiddle, initialShot, target);
            if(distMiddle== Double.MIN_VALUE) {
                System.out.println("Return null!");
                return null;
            }
            if(Math.abs(distMiddle) < minDistance) {
                minDistance = Math.abs(distMiddle);
                bestShot.x = initialShot.x*scalarMiddle;
                bestShot.y = initialShot.y*scalarMiddle;
            }
            if(distA * distMiddle > 0) {
                //they are the same sign
                scalarA = scalarMiddle;
                distA = distMiddle;
            }else {
                scalarB = scalarMiddle;
                distB = distMiddle;
            }
            // System.out.println(minDistance);
          
        }
        return bestShot;
    }

    public double calcDistForScalar(float scalar,Vector2 initialShot, Vector2 target) {
        State botState = new State();
        float testX = initialShot.x*scalar;
        float testY = initialShot.y*scalar;
    
            // Vector2 testShot = new Vector2(initialShot.x * scalar, initialShot.y * scalar);
            Vector2 testShot = new Vector2(testX, testY);
            // System.out.println("testShot " + testShot);
            botState.setPosition(engine.state.getPosition());  
            botState.setVelocity(testShot);
            return engine.simulatePreciseShotToPoint(botState, target);

    }


    public Vector2 findBestMove1(Vector2 target, boolean zeroWhenScored) {
        Vector2 position = engine.state.getPosition();
        // double distance = Double.MAX_VALUE;
        double minDistance = Double.MAX_VALUE;

        float x = target.x - position.x  ;
        float y = target.y - position.y  ;
        Vector2 bestShot = new Vector2(x,y);
        float scalar = 1;

        while(minDistance > 0.5) {

            boolean localMaximum = false;
            while(!localMaximum) {
                localMaximum = true;
                for (int i = -2; i <= 2; i++) {
                    float testX = bestShot.x * i *scalar;
                    float testY = bestShot.y * i *scalar;
                    if(i == 0) {
                        // continue;
                         testX = bestShot.x;
                         testY = bestShot.y;
                    }
                    Vector2 testShot = new Vector2(testX,testY);
                    State botState = new State();
                    botState.setPosition(engine.state.getPosition());  
                    botState.setVelocity(testShot);
                    double distance = engine.simulateShotDistanceToPoint(botState, target, false);
                    
                    if(distance < minDistance) {
                        
                        minDistance = distance;
                        System.out.println("New min distance " + minDistance);
                        bestShot = testShot;
                        localMaximum = false;
                        break;
                    }
                }

            }

               scalar = scalar -0.08f;
        }

        return bestShot;
    }
}
