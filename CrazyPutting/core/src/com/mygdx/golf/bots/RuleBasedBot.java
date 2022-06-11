package com.mygdx.golf.bots;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

public class RuleBasedBot implements Bot {
    private Engine engine;

    public RuleBasedBot(Engine engine) {
        this.engine = engine;
    }
    @Override
    public Vector2 findBestMove(Vector2 target, boolean zeroWhenScored) {
        Vector2 position = engine.state.getPosition();
        float distance = (float) engine.calcDistance(position,target);
        float x = target.x - position.x  ;
        float y = target.y - position.y  ;

        if(distance < 4) {
            x = 2*x;
            y = 2*y;
        }
        return new Vector2(x,y);
    }
}
