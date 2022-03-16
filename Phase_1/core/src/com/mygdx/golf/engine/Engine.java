package com.mygdx.golf.engine;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.Derivation;
import com.mygdx.golf.FileInputManager;
import com.mygdx.golf.State;
import com.mygdx.golf.engine.solvers.Solver;

public class Engine {

    private Function heightFunction;
    private final float GRAVITY = 9.81f;
    private float grassKinetic;
    private float grassStatic;
    private final float dt = 0.1f;
    private Solver solver;

    public Engine(Solver solver, FileInputManager inputManager) {
        solver.setEngine(this);
        this.solver = solver;
        System.out.println("h(x,y) =" + inputManager.getHeightProfile());
        this.heightFunction = new Function("h(x,y) =" + inputManager.getHeightProfile());
        this.grassKinetic = (float) inputManager.grassKinetic();
        this.grassStatic = (float) inputManager.grassStatic();
        System.out.println("Grass static " + grassStatic);
    }

    public void update() {
        State.setPosition(solver.solvePos(State.getPosition(), State.getVelocity()));
        State.setVelocity(solver.solveVel(State.getPosition(), State.getVelocity()));
    }

    public float calculateHeight(float x, float y) {

        return (float) heightFunction.calculate(x, y);
    }

    public Vector2 calcPartialDerivative(Vector2 position) {
        // implement here calculation of partial derivative according to courseprofile
        // so derivativecalculator will return the partial derivative of a function
        // calcPartialDerivative needs to be called again every time there is a new x or
        // y position
        Vector2 partials = new Vector2();

        partials.x = (float) Derivation.derivativeX(heightFunction, (double) position.x, (double) position.y); // example
        partials.y = (float) Derivation.derivativeY(heightFunction, (double) position.x, (double) position.y); // example
        return partials;
    }

    public Vector2 calcAcceleration(Vector2 position, Vector2 velocity) {
        // using previous speeds and the constant Mk and g and the partial derivative,
        // calculate the new acceleration, so currentAx and currentAy
        Vector2 partials = calcPartialDerivative(position);
        Vector2 acceleration = new Vector2();

        acceleration.x = (-1 * GRAVITY * partials.x)
                - grassKinetic * GRAVITY
                        * (velocity.x / ((float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y)));
        acceleration.y = (-1 * GRAVITY * partials.y)
                - grassKinetic * GRAVITY
                        * (velocity.y / ((float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y)));

        return acceleration;
    }

    public Vector2 calcSlidingAcceleration(Vector2 position, Vector2 velocity) {
        // using previous speeds and the constant Mk and g and the partial derivative,
        // calculate the new acceleration, so currentAx and currentAy
        // should the second term be negative or positive?
        Vector2 partials = calcPartialDerivative(position);
        Vector2 acceleration = new Vector2();
        acceleration.x = (-1 * GRAVITY * partials.x)
                + grassKinetic * GRAVITY
                        * (partials.x / ((float) Math.sqrt(partials.x * partials.x + partials.y * partials.y)));
        acceleration.y = (-1 * GRAVITY * partials.y)
                + grassKinetic * GRAVITY
                        * (partials.y / ((float) Math.sqrt(partials.x * partials.x + partials.y * partials.y)));
        return acceleration;
    }

    public float getDt() {
        return dt;
    }

    public float getGrassStatic() {
        return grassStatic;
    }

}
