package com.mygdx.golf.engine;

import org.mariuszgromada.math.mxparser.Function;
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
    private final float dt = 0.2f;
    private Solver solver;
    private boolean ballIsStopped = true;
    public FileInputManager inputManager;
    private int numberOfShots = 0;
    private Vector2 targetPosition;
    private float targetRadius;

    public Engine(Solver solver, boolean useInitialVelocity) {
        solver.setEngine(this);
        this.solver = solver;
        this.inputManager = new FileInputManager();

        this.heightFunction = new Function("h(x,y) =" + inputManager.getHeightProfile());
        this.grassKinetic =  inputManager.grassKinetic();
        this.grassStatic = inputManager.grassStatic();
        this.targetPosition = inputManager.getTargetPos();
        this.targetRadius = inputManager.getRadius();
        State.setPosition(inputManager.getInitialPos());
        if (useInitialVelocity) {
            newShot(inputManager.getInitialVelocity());

        }

    }

    public void update() {
        if (!ballIsStopped) {
            State.setPosition(solver.solvePos(State.getPosition(), State.getVelocity()));
            State.setVelocity(solver.solveVel(State.getPosition(), State.getVelocity()));
        }
    }

    public boolean getBallIsStopped() {
        return ballIsStopped;
    }

    public void stopBall() {
        numberOfShots++;
        ballIsStopped = true;
    }

    public void newShot(Vector2 velocity) {
        if (ballIsStopped) {
            ballIsStopped = false;
            State.setVelocity(velocity);
        }
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

    public int getNumberOfShots() {
        return numberOfShots;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }
    public float getTargetRadius() {
        return targetRadius;
    }

}
