package com.mygdx.golf.engine;

import org.mariuszgromada.math.mxparser.Function;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.Derivation;
import com.mygdx.golf.FileInputManager;
import com.mygdx.golf.MapScreen;
import com.mygdx.golf.State;
import com.mygdx.golf.engine.solvers.Solver;

public class Engine {


    //physics variables
    private Function heightFunction;
    private final float GRAVITY = 9.81f;
    private float grassKinetic;
    private float grassStatic;
    private Vector2 targetPosition;
    private float targetRadius;
    public final float BALL_RADIUS = 0.1f;
    private final float dt = 0.2f;

    public FileInputManager inputManager;

    private Solver solver;

    //stores the state of the ball
    public State state;

    //game logic variables
    private int numberOfShots = 0;
    public boolean gameIsFinished;
    private boolean ballIsStopped = true;

    public boolean inWater;


    //Constructor for engine class, sets all the variables and starts the game
    public Engine(Solver solver, boolean useInitialVelocity) {

        solver.setEngine(this);
        this.solver = solver;

        this.inputManager = new FileInputManager();
        this.heightFunction = new Function("h(x,y) =" + inputManager.getHeightProfile());
        this.grassKinetic =  inputManager.grassKinetic();
        this.grassStatic = inputManager.grassStatic();
        this.targetPosition = inputManager.getTargetPos();
        this.targetRadius = inputManager.getRadius();

        state = new State();

        
        initGame();

        if (useInitialVelocity) {
            //shoots the ball automatically if useInitialVelocity is true
            newShot(inputManager.getInitialVelocity());

        }

    }

    //resets the game 
    public void initGame() {
        state.setPosition(inputManager.getInitialPos());
        numberOfShots = 0;
        gameIsFinished = false;
        
    }


    //method that gets called every frame, it updates the ball position and also checks if user scored using the scored method
    public void update() {
        if (!ballIsStopped) {
            state.setPosition(solver.solvePos(state.getPosition(), state.getVelocity()));
            state.setVelocity(solver.solveVel(state.getPosition(), state.getVelocity()));
            if(scored()) {
                stopBall();
                gameIsFinished = true;
            }
        }
        if(this.calculateHeight(state.getPosition().x, state.getPosition().y)<0){
            stopBall();
            inWater= true;
        }
    }

    public boolean getBallIsStopped() {
        return ballIsStopped;
    }

    //checks if ball has collision with hole
    public boolean scored(){
        Vector2 ballPos = state.getPosition();

        double xDiff = ballPos.x - targetPosition.x;
        double yDiff = ballPos.y - targetPosition.y;
    
        double distance = Math.sqrt((Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));
    
        return distance < (BALL_RADIUS + targetRadius);
    }

    //stops the ball, and increments the number of shots
    public void stopBall() {
        numberOfShots++;
        ballIsStopped = true;
    }

    //Shoots a new shot, only if the ball is in a stopped position
    public void newShot(Vector2 velocity) {
        if (ballIsStopped) {
            ballIsStopped = false;
            state.setVelocity(velocity);
        }
    }

    //methods that calculates the height of the map for a given x and y
    public float calculateHeight(float x, float y) {
        return (float) heightFunction.calculate(x, y);
    }

    //Calculates the partial derivative for the map at a given position
    public Vector2 calcPartialDerivative(Vector2 position) {
        Vector2 partials = new Vector2();
        partials.x = (float) Derivation.derivativeX(heightFunction, (double) position.x, (double) position.y); 
        partials.y = (float) Derivation.derivativeY(heightFunction, (double) position.x, (double) position.y); 

        return partials;
    }
    // Calculates acceleration with the formula from the project manual
    public Vector2 calcAcceleration(Vector2 position, Vector2 velocity) {
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


    //read the first paragraph of the project manual page 9
    public Vector2 calcSlidingAcceleration(Vector2 position, Vector2 velocity) {
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
