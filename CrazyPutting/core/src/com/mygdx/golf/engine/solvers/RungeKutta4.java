package com.mygdx.golf.engine.solvers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

public class RungeKutta4 implements Solver{

    private Engine e;

    public RungeKutta4(Engine e){
        this.e = e;
    }

    @Override
    public void setEngine(Engine engine) {
        this.e = engine;
    }

    @Override
    public Vector2 solveVel(Vector2 position, Vector2 velocity) {
        Vector2 k1 = new Vector2(e.calcAcceleration(position, velocity)).scl(e.getDt());
        Vector2 k2 = new Vector2(e.calcAcceleration(position.add(e.getDt()/2,e.getDt()/2),
                                velocity.add(k1.scl(.5f)))).scl(e.getDt());
        Vector2 k3 = new Vector2(e.calcAcceleration(position.add(e.getDt()/2, e.getDt()/2),
                                velocity.add(k2.scl(.5f)))).scl(e.getDt());
        Vector2 k4 = new Vector2(e.calcAcceleration(position.add(e.getDt(), e.getDt()),
                                velocity.add(k3))).scl(e.getDt());
        return velocity.add(k1).add(k2.scl(2)).add(k3.scl(2)).add(k4).scl(1/6);
    }

    @Override
    public Vector2 solvePos(Vector2 position, Vector2 velocity) {
        return null;
    }
}
