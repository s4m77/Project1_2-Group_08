package com.mygdx.golf.engine;

import org.mariuszgromada.math.mxparser.Function;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.Derivation;
import com.mygdx.golf.FileInputManager;
import com.mygdx.golf.MapScreen;
import com.mygdx.golf.State;
import com.mygdx.golf.A_star.Node;
import com.mygdx.golf.A_star.NodeGrid;
import com.mygdx.golf.A_star.PathFinding;
import com.mygdx.golf.engine.solvers.AB3;
import com.mygdx.golf.engine.solvers.Euler;
import com.mygdx.golf.engine.solvers.RungeKutta2;
import com.mygdx.golf.engine.solvers.Solver;
import com.mygdx.golf.maze_generator.RandomMazeGenerator;

public class Engine {
    public final boolean USE_NEGATIVE_LAKES = false;
    // physics variables
    private Function heightFunction;
    private final float GRAVITY = 9.81f;
    private float grassKinetic;
    private float grassStatic;
    public Vector2 targetPosition;
    private float targetRadius;
    public final float BALL_RADIUS = 0.1f;

    private final float dt = 0.0001f;

    public double[] sandPitCoords;
    public double[] lakeCoords;

    public final boolean USING_MAZE = false;
    public float gridCellSizeMetres = 0.8f;
    private int[][] intGrid;
    public NodeGrid nodeGrid;
    public PathFinding pathFinder;

    public FileInputManager inputManager;

    private Solver solver;

    // stores the state of the ball
    public State state;

    // game logic variables
    private int numberOfShots = 0;
    public boolean gameIsFinished;
    public boolean ballIsStopped = true;
    private Vector2 savedPos;
    public boolean inWater;

    // Constructor for engine class, sets all the variables and starts the game
    public Engine(Solver solver, boolean useInitialVelocity) {

        solver.setEngine(this);
        this.solver = solver;

        this.inputManager = new FileInputManager();
        this.heightFunction = new Function("h(x,y) =" + inputManager.getHeightProfile());
        this.grassKinetic = inputManager.grassKinetic();
        this.grassStatic = inputManager.grassStatic();
        this.targetRadius = inputManager.getRadius();
        this.sandPitCoords = inputManager.getSandPitCoords();
        this.lakeCoords = inputManager.getLakeCoords();
        state = new State();
        initGame();

        if (useInitialVelocity) {
            // shoots the ball automatically if useInitialVelocity is true
            newShot(inputManager.getInitialVelocity());

        }

    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Euler(), false);
        State botState = new State();

        // Vector2 testShot = new Vector2(initialShot.x * scalar, initialShot.y *
        // scalar);
        Vector2 testShot = new Vector2(0, 0.9f);
        System.out.println("testShot " + testShot);
        botState.setPosition(engine.state.getPosition());
        botState.setVelocity(testShot);
        double distA = engine.simulatePreciseShotToPoint(botState, new Vector2(0, 0));
        System.out.println(distA);

        botState = new State();
        testShot = new Vector2(0, 0.2f);
        System.out.println("testShot " + testShot);
        botState.setPosition(engine.state.getPosition());
        botState.setVelocity(testShot);
        double distB = engine.simulatePreciseShotToPoint(botState, new Vector2(0, 0));
        System.out.println(distB);

    }

    // resets the game
    public void initGame() {
        if (USING_MAZE) {
            generateIntGrid();
        pathFinder = new PathFinding(intGrid);
        nodeGrid = pathFinder.grid;
            state.setPosition(getCenterPositionFromGridCoords(nodeGrid.startNode));
            this.targetPosition = getCenterPositionFromGridCoords(nodeGrid.targetNode);
           
        } else {
            this.targetPosition = inputManager.getTargetPos();

            state.setPosition(inputManager.getInitialPos());
        }
        numberOfShots = 0;
        gameIsFinished = false;
        inWater = false;

    }

    // method that gets called evesquarePos.y frame, it updates the ball position
    // and also
    // checks if user scored using the scored method
    public void update() {
        if (!ballIsStopped) {
            state.setPosition(solver.solvePos(state.getPosition(), state.getVelocity()));
            Vector2 newVelocity = solver.solveVel(state.getPosition(), state.getVelocity());
            if (newVelocity.x == 0 && newVelocity.y == 0) {
                stopBall();
                // System.out.println("distance from target when stopped : " +
                // calcDistanceToTarget(state.getPosition()));
            }
            state.setVelocity(newVelocity);
            if (scored(state)) {
                stopBall();
                gameIsFinished = true;
            }

            if (inWater(state) || (USING_MAZE && isCollidingWithWalls(state))) {
                System.out.println("fail");
                stopBall();
                state.setPosition(savedPos); // put ball back to its previous position
                inWater = true;
            }

        } else {
            savedPos = state.getPosition();
        }
    }

    public double simulateShotDistanceToPoint(State botState, Vector2 target, boolean zeroWhenScored) {
        while (botState.getVelocity().x != 0 || botState.getVelocity().y != 0) {

            botState.setPosition(solver.solvePos(botState.getPosition(), botState.getVelocity()));
            Vector2 newVelocity = solver.solveVel(botState.getPosition(), botState.getVelocity());
            System.out.println("newVelocity " + newVelocity);
            botState.setVelocity(newVelocity);
            if (zeroWhenScored && scored(botState)) {
                return 0;
            }
            if (inWater(botState) || (USING_MAZE && isCollidingWithWalls(botState))) {
                return Double.MAX_VALUE;
            }

        }
        double distance = calcDistance(botState.getPosition(), target);

        return distance;
    }

    public double simulatePreciseShotToPoint(State botState, Vector2 target) {
        Vector2 initialPos = botState.getPosition();
        double distance;
        boolean shouldBreak = false;
        while ((botState.getVelocity().x != 0 || botState.getVelocity().y != 0) && !shouldBreak) {

            botState.setPosition(solver.solvePos(botState.getPosition(), botState.getVelocity()));
            Vector2 newVelocity = solver.solveVel(botState.getPosition(), botState.getVelocity());
            botState.setVelocity(newVelocity);

            if (inWater(botState) || (USING_MAZE && isCollidingWithWalls(botState))) {
                float distXInitialToTarget = Math.abs(initialPos.x - target.x);
                float distXInitialToCur = Math.abs(initialPos.x - botState.getPosition().x);

                float distYInitialToTarget = Math.abs(initialPos.y - target.y);
                float distYInitialToCur = Math.abs(initialPos.y - botState.getPosition().y);
                System.out.println("Collided with wall");

                if (distXInitialToCur < distXInitialToTarget || distYInitialToCur < distYInitialToTarget) {
                    return Double.MIN_VALUE;
                } else {
                    return Double.MAX_VALUE;
                }

            }

        }
        // System.out.println("Out of loop");
        distance = calcDistance(botState.getPosition(), target);

        float distXInitialToTarget = Math.abs(initialPos.x - target.x);
        float distXInitialToCur = Math.abs(initialPos.x - botState.getPosition().x);

        float distYInitialToTarget = Math.abs(initialPos.y - target.y);
        float distYInitialToCur = Math.abs(initialPos.y - botState.getPosition().y);
        if (distXInitialToCur < distXInitialToTarget || distYInitialToCur < distYInitialToTarget) {
            distance = -distance;

        }

        return distance;
    }

    public boolean getBallIsStopped() {
        return ballIsStopped;
    }

    public double calcDistance(Vector2 ballPos, Vector2 targetPoint) {
        double xDiff = ballPos.x - targetPoint.x;
        double yDiff = ballPos.y - targetPoint.y;

        double distance = Math.sqrt((Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));

        return distance;
    }

    // checks if ball has collision with hole
    public boolean scored(State state) {

        double distance = calcDistance(state.getPosition(), targetPosition);

        return distance < (BALL_RADIUS + targetRadius);
    }

    public boolean inWater(State state) {

        float x = state.getPosition().x;
        float y = state.getPosition().y;

        if (USE_NEGATIVE_LAKES && calculateHeight(x, y) < 0) {
            return true;
        }

        if (x > lakeCoords[0] && x < lakeCoords[1] && y > lakeCoords[2] && y < lakeCoords[3]) {
            return true;
        }
        return false;
    }

    // public boolean collidingWithWalls(State state) {
    // float x = state.getPosition().x;
    // float y = state.getPosition().y;

    // for (int i = 0; i < intGrid.length; i++) {

    // }
    // }

    // stops the ball, and increments the number of shots
    public void stopBall() {
        numberOfShots++;
        ballIsStopped = true;
    }

    // Shoots a new shot, only if the ball is in a stopped position
    public void newShot(Vector2 velocity) {
        if (ballIsStopped) {
            ballIsStopped = false;
            state.setVelocity(velocity);
        }
    }

    public Vector2 getCenterPositionFromGridCoords(Node node) {
        float x = (node.gridX + 0.5f) * gridCellSizeMetres;
        float y = (nodeGrid.grid.length - node.gridY - 0.5f) * gridCellSizeMetres;
        // x = gridCellSizeMetres;
        return new Vector2(x, y);
    }

    public Vector2 getPositionFromGridCoords(Node node) {
        float x = (node.gridX) * gridCellSizeMetres;
        float y = (nodeGrid.grid.length - node.gridY - 1) * gridCellSizeMetres;
        // x = gridCellSizeMetres;
        return new Vector2(x, y);
    }

    public boolean isCollidingWithWalls(State state) {
        for (int x = 0; x < nodeGrid.grid[0].length; x++) {
            for (int y = 0; y < nodeGrid.grid.length; y++) {
                Node n = nodeGrid.grid[y][x];
                if (!n.walkable) {
                    Vector2 squarePos = getPositionFromGridCoords(n);
                    boolean isColliding = circleSquareCollising(state.getPosition(), BALL_RADIUS, squarePos,
                            gridCellSizeMetres);
                    if (isColliding) {
                        // System.out.println(n);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean circleSquareCollising(Vector2 circlePos, float circleRadius, Vector2 squarePos,
            float squareLength) {

        float testX = circlePos.x;
        float testY = circlePos.y;

        if (circlePos.x < squarePos.x) {
            testX = squarePos.x; // test left edge
        } else if (circlePos.x > squarePos.x + squareLength) {
            testX = squarePos.x + squareLength;
        } // right edge}
        if (circlePos.y < squarePos.y) {
            testY = squarePos.y; // top edge
        } else if (circlePos.y > squarePos.y + squareLength) {
            testY = squarePos.y + squareLength;
        } // bottom edge

        // get distance from closest edges
        float distX = circlePos.x - testX;
        float distY = circlePos.y - testY;
        float distance = (float) Math.sqrt((double) (distX * distX) + (distY * distY));

        // if the distance is less than the radius, collision!
        if (distance <= circleRadius) {
            return true;
        }
        return false;
    }

    // methods that calculates the height of the map for a given x and y
    public float calculateHeight(float x, float y) {
        return (float) heightFunction.calculate(x, y);
    }

    // Calculates the partial derivative for the map at a given position
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
                - getKinetic(position) * GRAVITY
                        * (velocity.x / ((float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y)));
        acceleration.y = (-1 * GRAVITY * partials.y)
                - getKinetic(position) * GRAVITY
                        * (velocity.y / ((float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y)));

        return acceleration;
    }

    // Overloaded this method for efficiencirclePos.y, if partials was already
    // calculated
    // might as well pass it as an argument,
    // since it takes a bit of time to calculate partial derivatives
    public Vector2 calcAcceleration(Vector2 position, Vector2 velocity, Vector2 partials) {
        Vector2 acceleration = new Vector2();

        acceleration.x = (-1 * GRAVITY * partials.x)
                - getKinetic(position) * GRAVITY
                        * (velocity.x / ((float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y)));
        acceleration.y = (-1 * GRAVITY * partials.y)
                - getKinetic(position) * GRAVITY
                        * (velocity.y / ((float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y)));

        return acceleration;
    }

    public Vector2 calcImprovedAcceleration(Vector2 position, Vector2 velocity, Vector2 partials) {
        Vector2 acceleration = new Vector2();

        acceleration.x = ((-1 * GRAVITY * partials.x) / (1 + partials.x * partials.x + partials.y * partials.y))
                - ((getKinetic(position) * GRAVITY * velocity.x) /
                        ((float) Math.sqrt((1 + partials.x * partials.x + partials.y * partials.y) *
                                (velocity.x * velocity.x + velocity.y * velocity.y
                                        + (partials.x * velocity.x + partials.y * velocity.y) *
                                                (partials.x * velocity.x + partials.y * velocity.y)))));

        acceleration.y = ((-1 * GRAVITY * partials.y) / (1 + partials.x * partials.x + partials.y * partials.y))
                - ((getKinetic(position) * GRAVITY * velocity.y) /
                        ((float) Math.sqrt((1 + partials.x * partials.x + partials.y * partials.y) *
                                (velocity.x * velocity.x + velocity.y * velocity.y
                                        + (partials.x * velocity.x + partials.y * velocity.y) *
                                                (partials.x * velocity.x + partials.y * velocity.y)))));

        return acceleration;
    }

    // read the first paragraph of the project manual page 9
    public Vector2 calcSlidingAcceleration(Vector2 position, Vector2 velocity) {
        Vector2 partials = calcPartialDerivative(position);
        Vector2 acceleration = new Vector2();
        acceleration.x = (-1 * GRAVITY * partials.x)
                + getKinetic(position) * GRAVITY
                        * (partials.x / ((float) Math.sqrt(partials.x * partials.x + partials.y * partials.y)));
        acceleration.y = (-1 * GRAVITY * partials.y)
                + getKinetic(position) * GRAVITY
                        * (partials.y / ((float) Math.sqrt(partials.x * partials.x + partials.y * partials.y)));
        return acceleration;
    }

    public Vector2 calcSlidingAcceleration(Vector2 position, Vector2 velocity, Vector2 partials) {
        Vector2 acceleration = new Vector2();
        acceleration.x = (-1 * GRAVITY * partials.x)
                + getKinetic(position) * GRAVITY
                        * (partials.x / ((float) Math.sqrt(partials.x * partials.x + partials.y * partials.y)));
        acceleration.y = (-1 * GRAVITY * partials.y)
                + getKinetic(position) * GRAVITY
                        * (partials.y / ((float) Math.sqrt(partials.x * partials.x + partials.y * partials.y)));
        return acceleration;
    }

    public Vector2 calcImprovedSlidingAcceleration(Vector2 position, Vector2 velocity, Vector2 partials) {
        Vector2 acceleration = new Vector2();

        acceleration.x = ((-1 * GRAVITY * partials.x) / (1 + partials.x * partials.x + partials.y * partials.y))
                + ((getKinetic(position) * GRAVITY * partials.x) /
                        ((float) Math.sqrt((1 + partials.x * partials.x + partials.y * partials.y) *
                                (partials.x * partials.x + partials.y * partials.y
                                        + (partials.x * partials.x + partials.y * partials.y) *
                                                (partials.x * partials.x + partials.y * partials.y)))));

        acceleration.y = ((-1 * GRAVITY * partials.y) / (1 + partials.x * partials.x + partials.y * partials.y))
                + ((getKinetic(position) * GRAVITY * partials.y) /
                        ((float) Math.sqrt((1 + partials.x * partials.x + partials.y * partials.y) *
                                (partials.x * partials.x + partials.y * partials.y
                                        + (partials.x * partials.x + partials.y * partials.y) *
                                                (partials.x * partials.x + partials.y * partials.y)))));

        return acceleration;
    }

    public float getDt() {
        return dt;
    }

    public float getStatic(Vector2 position) {
        double x = position.x;
        double y = position.y;
        if (x > sandPitCoords[0] && x < sandPitCoords[1] && y > sandPitCoords[2] && y < sandPitCoords[3]) {
            return inputManager.sandStatic();
        }
        return grassStatic;
    }

    public float getKinetic(Vector2 position) {
        double x = position.x;
        double y = position.y;
        if (x > sandPitCoords[0] && x < sandPitCoords[1] && y > sandPitCoords[2] && y < sandPitCoords[3]) {
            return inputManager.sandKinetic();
        }
        return grassKinetic;
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

    public void generateIntGrid() {

        intGrid = RandomMazeGenerator.returnFinalRandomMaze(30, 30, true);

    }
}
