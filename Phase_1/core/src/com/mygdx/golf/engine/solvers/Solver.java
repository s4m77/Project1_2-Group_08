package com.mygdx.golf.engine.solvers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

//Interface for all future solvers
public interface Solver {
    void setEngine(Engine engine);
    Vector2 solveVel(Vector2 position, Vector2 velocity);
    Vector2 solvePos(Vector2 position, Vector2 velocity);
    
}
