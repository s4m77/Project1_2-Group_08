package com.mygdx.golf;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.golf.A_star.Node;
import com.mygdx.golf.A_star.NodeGrid;
import com.mygdx.golf.A_star.PathFinding;
import com.mygdx.golf.bots.Bot;
import com.mygdx.golf.bots.HillClimbBot;
import com.mygdx.golf.bots.MazeBot;
import com.mygdx.golf.bots.RandomBot;
import com.mygdx.golf.bots.RuleBasedBot;
import com.mygdx.golf.bots.StraightLineBot;
import com.mygdx.golf.bots.BruteForceBot;
import com.mygdx.golf.engine.Engine;

public class MapScreenMaze extends ScreenAdapter implements InputProcessor {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer, shootingLine;
    private BitmapFont font;

    // coefficient from metres to pixels
    private float metreToPixelCoeff = 0.01f;

    private Engine engine;

    // for moving around the map
    private Vector2 dragDelta;
    private Vector2 dragStart;
    private Vector2 oldDragDelta;
    private boolean isDragging;

    // for shooting
    private Vector2 mouseShootStart;
    private Vector2 mouseShootEnd;
    private boolean isShooting;

    private int[][] intGrid = new int[][] {
            { 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    };
    private NodeGrid nodeGrid;
    PathFinding pathFinder;

    float gridWidth;
    float gridHeight;
    float gridOriginX;
    float gridOriginY;
    float gridTopY;
    float lineWidth = 2;
    // float gridCellSizeMetres;

    // Initializes everything
    public MapScreenMaze(OrthographicCamera camera, Engine engine) {
        this.camera = camera;
        this.camera.position
                .set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.shapeRenderer = new ShapeRenderer();
        this.shootingLine = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.engine = engine;
        this.dragDelta = new Vector2(0, 0);
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        // generateIntGrid();

        pathFinder = engine.pathFinder;
        nodeGrid = engine.nodeGrid;
        updateGridValues();
        dragDelta.x = gridWidth / 2;
        dragDelta.y = gridHeight / 2;
    }

    // native gdx method that is called for each frame
    @Override
    public void render(float delta) {
        // Gdx stuff
        updateGridValues();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0.4f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // if game is finished than only show end screen
        if (engine.gameIsFinished) {
            renderEndScreen();
            return;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(255, 255, 255, 1);
        // shapeRenderer.rectLine(gridOriginX, gridOriginY, gridOriginX +500,
        // gridOriginY + gridHeight, lineWidth);
        drawGrid();
        drawPath(engine.pathFinder.path);
        // drawPath(engine.pathFinder.getStraightLinePath());
        // renderMap();

        // draws golf ball
        shapeRenderer.setColor(255, 255, 255, 1);
        shapeRenderer.circle(metresToPixels(engine.state.getPosition().x, true),
                metresToPixels(engine.state.getPosition().y, false),
                engine.BALL_RADIUS / metreToPixelCoeff);
        // draws the hole
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.circle(metresToPixels(engine.getTargetPosition().x, true),
                metresToPixels(engine.getTargetPosition().y, false),
                engine.getTargetRadius() / metreToPixelCoeff);

        shapeRenderer.end();

        // draws the text
        batch.begin();
        String str = "Number of shots : " + engine.getNumberOfShots();
        font.draw(batch, str, 10, 20);
        if (engine.getBallIsStopped()) {
            str = "Ball stopped at : x = " + engine.state.getPosition().x + ", y = " +
                    engine.state.getPosition().y;
            font.draw(batch, str, 10, Boot.INSTANCE.getScreenHeight() - 10);

        }

        batch.end();
        renderShootingLine(shootingLine);
        engine.update();
    }

    public void updateGridValues() {

        gridWidth = nodeGrid.grid[0].length * getCellSizeInPixels();
        gridHeight = nodeGrid.grid.length * getCellSizeInPixels();
        gridOriginX = Boot.INSTANCE.getScreenWidth() / 2 - dragDelta.x;
        gridOriginY = Boot.INSTANCE.getScreenHeight() / 2 - dragDelta.y;
        gridTopY = Boot.INSTANCE.getScreenHeight() / 2 + gridHeight - dragDelta.y;
        // gridOriginX = Boot.INSTANCE.getScreenWidth() / 2 - gridWidth / 2 -
        // dragDelta.x;
        // gridOriginY = Boot.INSTANCE.getScreenHeight() / 2 - gridHeight / 2 -
        // dragDelta.y;
        // gridTopY = Boot.INSTANCE.getScreenHeight() / 2 + gridHeight / 2 -
        // dragDelta.y;
    }

    public void generateIntGrid() {
        intGrid = new int[20][40];
        for (int y = 0; y < intGrid.length; y++) {
            for (int x = 0; x < intGrid[0].length; x++) {
                intGrid[y][x] = 0;
            }
        }
        intGrid[0][0] = 1;
    }

    public void drawGrid() {
        Node[][] grid = nodeGrid.grid;

        for (int i = 0; i <= grid[0].length; i++) {
            shapeRenderer.rectLine(gridOriginX + i * getCellSizeInPixels(), gridOriginY,
                    gridOriginX + i * getCellSizeInPixels(),
                    gridOriginY + gridHeight, lineWidth);
        }
        for (int i = 0; i <= grid.length; i++) {
            shapeRenderer.rectLine(gridOriginX, gridOriginY + i * getCellSizeInPixels(), gridOriginX + gridWidth,
                    gridOriginY + i * getCellSizeInPixels(), lineWidth);
        }
        shapeRenderer.setColor(0, 0, 0, 1);

        // shapeRenderer.rect(originX, originY, size -lineWidth , size-lineWidth );
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (!grid[y][x].walkable) {
                    shapeRenderer.setColor(0, 0, 0, 1);
                    shapeRenderer.rect(gridOriginX + x * getCellSizeInPixels() + 1,
                            gridTopY - (y + 1) * getCellSizeInPixels() + 1,
                            getCellSizeInPixels() - lineWidth,
                            getCellSizeInPixels() - lineWidth);
                }
                if (grid[y][x].isStart) {
                    shapeRenderer.setColor(0, 1, 0, 1);
                    shapeRenderer.rect(gridOriginX + x * getCellSizeInPixels() + 1,
                            gridTopY - (y + 1) * getCellSizeInPixels() + 1,
                            getCellSizeInPixels() - lineWidth,
                            getCellSizeInPixels() - lineWidth);
                }
                if (grid[y][x].isTarget) {
                    shapeRenderer.setColor(1, 0, 0, 1);
                    shapeRenderer.rect(gridOriginX + x * getCellSizeInPixels() + 1,
                            gridTopY - (y + 1) * getCellSizeInPixels() + 1,
                            getCellSizeInPixels() - lineWidth,
                            getCellSizeInPixels() - lineWidth);
                }
            }
        }

    }

    // renders the map
    // loops through (almost) all the pixels of the screen, and translate the x and
    // y in metres in order to calculate the height
    // with the height function. and thus choose a shade a color of green depending
    // on the height.
    public void renderMap() {
        int p = 10;
        for (float i = 0; i <= Boot.INSTANCE.getScreenWidth(); i += p) {
            for (float j = 0; j <= Boot.INSTANCE.getScreenHeight(); j += p) {

                float x = pixelsToMetres(i, true);
                float y = pixelsToMetres(j, false);

                float n = engine.calculateHeight(x, y);

                // I found n / 10 + 0.4f, was a good shade of green for any height function
                shapeRenderer.setColor(0, n / 10 + 0.4f, 0, 1);

                double[] sandPitCoords = engine.sandPitCoords;
                if (sandPitCoords[0] - sandPitCoords[1] != 0 && sandPitCoords[2] - sandPitCoords[3] != 0) {

                    if (x >= sandPitCoords[0] && x <= sandPitCoords[1] && y >= sandPitCoords[2]
                            && y <= sandPitCoords[3]) {
                        shapeRenderer.setColor(0.8f + n / 20, n / 10 + 0.45f, 0, 0.9f);
                    }
                }

                double[] lakeCoords = engine.lakeCoords;
                if (lakeCoords[0] - lakeCoords[1] != 0 && lakeCoords[2] - lakeCoords[3] != 0) {

                    if (x >= lakeCoords[0] && x <= lakeCoords[1] && y >= lakeCoords[2] && y <= lakeCoords[3]
                            || (engine.USE_NEGATIVE_LAKES && n < 0)) {
                        shapeRenderer.setColor(0, n / 10 + 0.5f, 1, 0.7f);
                    }
                }

                // if (n < 0) {
                // shapeRenderer.setColor(0, n / 10 + 0.4f, 255, 1);
                // }

                // shapeRenderer.setColor(0, 0, 255, 1);
                // shapeRenderer.setColor(0, n / 10 + 0.4f, 255, 1);

                shapeRenderer.rect(i, j, p, p);

            }
        }
    }

    public void drawPath(List<Node> path) {
        shapeRenderer.setColor(0.8f, 0.45f, 0, 1);

        for (Node n : path) {
            int x = n.gridX;
            int y = n.gridY;
            if (n.isTarget) {
                continue;
            }
            shapeRenderer.rect(gridOriginX + x * getCellSizeInPixels() + 1,
                    gridTopY - (y + 1) * getCellSizeInPixels() + 1,
                    getCellSizeInPixels() - lineWidth,
                    getCellSizeInPixels() - lineWidth);
        }
    }

    public void renderEndScreen() {
        batch.begin();
        font.getData().setScale(1.5f, 1.5f);
        String str = "You made it in " + engine.getNumberOfShots() + " shots";
        font.draw(batch, str, Boot.INSTANCE.getScreenWidth() / 2 - 94, Boot.INSTANCE.getScreenHeight() / 2 + 30);
        font.draw(batch, "Press enter to restart", Boot.INSTANCE.getScreenWidth() / 2 - 90,
                Boot.INSTANCE.getScreenHeight() / 2);
        batch.end();
    }

    private void renderShootingLine(ShapeRenderer renderer) {
        shootingLine.begin(ShapeRenderer.ShapeType.Line);
        if (isShooting) {
            Vector2 ballPosInMetres = engine.state.getPosition();
            Vector2 ballPosInPixels = new Vector2(metresToPixels(ballPosInMetres.x, true),
                    metresToPixels(ballPosInMetres.y, false));
            renderer.line(ballPosInPixels.x, ballPosInPixels.y, mouseShootEnd.x,
                    Boot.INSTANCE.getScreenHeight() - mouseShootEnd.y);

        }
        shootingLine.end();
    }

    // translates pixels to metres
    private float pixelsToMetres(float x, boolean forWidth) {
        if (forWidth) {
            return (x - Boot.INSTANCE.getScreenWidth() / 2 + dragDelta.x) * metreToPixelCoeff;
        } else {
            return (x - Boot.INSTANCE.getScreenHeight() / 2 + dragDelta.y) * metreToPixelCoeff;
        }

    }

    // translates metres to pixels
    private float metresToPixels(float x, boolean forWidth) {
        if (forWidth) {

            return x / metreToPixelCoeff + Boot.INSTANCE.getScreenWidth() / 2 - dragDelta.x;
        } else {
            return x / metreToPixelCoeff + Boot.INSTANCE.getScreenHeight() / 2 - dragDelta.y;
        }
    }

    private float getCellSizeInPixels() {
        return engine.gridCellSizeMetres / metreToPixelCoeff;
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }
    int counter = 0;
    @Override
    public boolean keyTyped(char character) {
        // restart game when press enter
        if (character == '\n' || character == '\r') {
            engine.initGame();
        }
        if (character == 'b') {
            MazeBot mazeBot = new MazeBot(engine);
            Vector2 bestMove = mazeBot.solveMaze();
            // Bot bot = new StraightLineBot(engine);
            // // Vector2 bestMove = bot.findBestMove(engine.targetPosition);
            // List<Node> path = engine.pathFinder.getStraightLinePath();

            // // for (int i = 0; i < path.size(); i++) {
            // Vector2 nextPos = engine.getCenterPositionFromGridCoords(path.get(counter));

            // Vector2 bestMove = bot.findBestMove(nextPos, false);
            engine.newShot(bestMove);
            counter++;
            // try {
            //     Thread.sleep(4000);
            // } catch (InterruptedException ex) {
            //     Thread.currentThread().interrupt();
            // }
            // System.out.println("Next move");
            // nextPos = engine.getCenterPositionFromGridCoords(path.get(1));

            // bestMove = bot.findBestMove(nextPos, false);
           
            // engine.newShot(bestMove);
            // }

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // changes the metreToPixelCoeff when scrolling, which creates a zoom effect
        if (amountY < 0) {
            metreToPixelCoeff = metreToPixelCoeff / 1.1f;
        } else {
            metreToPixelCoeff = metreToPixelCoeff * 1.1f;
        }
        return false;
    }

    //
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            // left click
            // start shooting mechanic
            if (engine.getBallIsStopped()) {
                mouseShootStart = new Vector2(screenX, screenY);
                mouseShootEnd = new Vector2(screenX, screenY);
                isShooting = true;
            }
        }
        if (button == 1) {
            // right click
            // start map drag mechanic
            isDragging = true;
            dragStart = new Vector2(screenX, screenY);
            oldDragDelta = new Vector2(dragDelta.x, dragDelta.y);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if (isShooting) {
            // if is shooting set the line position to mousepos
            mouseShootEnd.x = screenX;
            mouseShootEnd.y = screenY;
        }

        if (isDragging) {
            // math to drag the map
            dragDelta.x = oldDragDelta.x - (screenX - dragStart.x);
            dragDelta.y = oldDragDelta.y + (screenY - dragStart.y);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            // left click
            // shoots the ball
            if (isShooting) {
                isShooting = false;
                mouseShootEnd.x = screenX;
                mouseShootEnd.y = screenY;

                float velX = (mouseShootStart.x - mouseShootEnd.x) / 50;
                float velY = -(mouseShootStart.y - mouseShootEnd.y) / 50;

                // System.out.print(velX + " " + velY);
                engine.newShot(new Vector2(velX, velY));
            }
        }
        if (button == 1) {
            isDragging = false;
        }
        return false;
    }
}
