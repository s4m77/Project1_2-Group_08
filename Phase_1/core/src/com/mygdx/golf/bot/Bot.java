package com.mygdx.golf.bot;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

public interface Bot {
    
    public Vector2 findBestMove();
}