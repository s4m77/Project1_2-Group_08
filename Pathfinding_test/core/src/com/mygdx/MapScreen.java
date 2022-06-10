package com.mygdx;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.A_star.Node;
import com.mygdx.A_star.NodeGrid;
import com.mygdx.A_star.PathFinding;

public class MapScreen extends ScreenAdapter implements InputProcessor {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);

    // for moving around the map
    private Vector2 dragDelta;
    private Vector2 dragStart;
    private Vector2 oldDragDelta;
    private boolean isDragging;

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
    float cellSizePixels = 40;

    String clickMode = "create wall";
    boolean isClicking = false;

    // coefficient from metres to pixels

    // Initializes everything
    public MapScreen(OrthographicCamera camera) {
        generateIntGrid();
        this.camera = camera;
        this.camera.position
                .set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.shapeRenderer = new ShapeRenderer();

        this.batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        pathFinder = new PathFinding(intGrid);
        nodeGrid = pathFinder.grid;
        pathFinder.findPath();

        gridWidth = nodeGrid.grid[0].length * cellSizePixels;
        gridHeight = nodeGrid.grid.length * cellSizePixels;

        this.dragDelta = new Vector2(0, 0);

        gridOriginX = Boot.INSTANCE.getScreenWidth() / 2 - gridWidth / 2;
        gridOriginY = Boot.INSTANCE.getScreenHeight() / 2 - gridHeight / 2;
        gridTopY = Boot.INSTANCE.getScreenHeight() / 2 + gridHeight / 2;

    }

    public void generateIntGrid() {
        intGrid = new int[20][20];
        for (int y = 0; y < intGrid.length; y++) {
            for (int x = 0; x < intGrid[0].length; x++) {
                intGrid[y][x] = 0;
            }
        }
    }

    // method for whole grid
    public void drawGrid() {
        Node[][] grid = nodeGrid.grid;

        for (int i = 0; i <= grid[0].length; i++) {
            shapeRenderer.rectLine(gridOriginX + i * cellSizePixels, gridOriginY, gridOriginX + i * cellSizePixels,
                    gridOriginY + gridHeight, lineWidth);
        }
        for (int i = 0; i <= grid.length; i++) {
            shapeRenderer.rectLine(gridOriginX, gridOriginY + i * cellSizePixels, gridOriginX + gridWidth,
                    gridOriginY + i * cellSizePixels, lineWidth);
        }
        shapeRenderer.setColor(0, 0, 0, 1);

        // shapeRenderer.rect(originX, originY, size -lineWidth , size-lineWidth );
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (!grid[y][x].walkable) {
                    shapeRenderer.setColor(0, 0, 0, 1);
                    shapeRenderer.rect(gridOriginX + x * cellSizePixels + 1, gridTopY - (y + 1) * cellSizePixels + 1,
                            cellSizePixels - lineWidth,
                            cellSizePixels - lineWidth);
                }
                if (grid[y][x].isStart) {
                    shapeRenderer.setColor(1, 1, 1, 1);
                    shapeRenderer.rect(gridOriginX + x * cellSizePixels + 1, gridTopY - (y + 1) * cellSizePixels + 1,
                            cellSizePixels - lineWidth,
                            cellSizePixels - lineWidth);
                }
                if (grid[y][x].isTarget) {
                    shapeRenderer.setColor(1, 0, 0, 1);
                    shapeRenderer.rect(gridOriginX + x * cellSizePixels + 1, gridTopY - (y + 1) * cellSizePixels + 1,
                            cellSizePixels - lineWidth,
                            cellSizePixels - lineWidth);
                }
            }
        }

    }

    public void drawPath(List<Node> path) {
        for (Node n : path) {
            int x = n.gridX;
            int y = n.gridY;
            if (n.isTarget) {
                continue;
            }
            shapeRenderer.rect(gridOriginX + x * cellSizePixels + 1, gridTopY - (y + 1) * cellSizePixels + 1,
                    cellSizePixels - lineWidth,
                    cellSizePixels - lineWidth);
        }
    }

    // native gdx method that is called for each frame
    @Override
    public void render(float delta) {
        gridOriginX = Boot.INSTANCE.getScreenWidth() / 2 - gridWidth / 2 - dragDelta.x;
        gridOriginY = Boot.INSTANCE.getScreenHeight() / 2 - gridHeight / 2 - dragDelta.y;
        gridTopY = Boot.INSTANCE.getScreenHeight() / 2 + gridHeight / 2 - dragDelta.y;
        // Gdx stuff
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0.4f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // if game is finished than only show end screen

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.setColor(255, 255, 255, 1);
        // shapeRenderer.circle(40,
        // 40, 10);

        drawGrid();
        shapeRenderer.setColor(0, 0, 1, 1);

        drawPath(pathFinder.path);
        shapeRenderer.setColor(0, 1, 1, 1);

        drawPath(pathFinder.getStraightLinePath());
        
        shapeRenderer.end();
        pathFinder.findPath();

        batch.begin();
        String str = "Click mode : " + clickMode + ". press T for target, W for creating walls, D for deleting Walls, and S for start";
        font.draw(batch, str, 10, 20);

        batch.end();
    }

    public int[] getGridCoordsFromMousePos(int screenX, int screenY) {

        int x = (int) (screenX - gridOriginX) / (int) cellSizePixels;
        int y = (int) (screenY - (Boot.INSTANCE.getScreenHeight() - gridTopY)) / (int) cellSizePixels;

        if (x < 0 || x >= nodeGrid.grid[0].length || y < 0 || y >= nodeGrid.grid.length) {

            return null;
        }

        int[] coords = new int[] { x, y };
        return coords;
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == 't') {
            clickMode = "target";
        }
        if (character == 'w') {
            clickMode = "create wall";
        }
        if (character == 'd') {
            clickMode = "delete wall";
        }
        if (character == 's') {
            clickMode = "start";
        }
        if (character == 'p') {
            pathFinder.getStraightLinePath();
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
  //changes the metreToPixelCoeff when scrolling, which creates a zoom effect
  if (amountY < 0) {
    cellSizePixels = cellSizePixels / 1.4f;
} else {
    cellSizePixels = cellSizePixels * 1.4f;
}
        return false;
    }

    //
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 1) {
            // right click
            // start map drag mechanic
            isDragging = true;
            dragStart = new Vector2(screenX, screenY);
            oldDragDelta = new Vector2(dragDelta.x, dragDelta.y);
        }
        if(button == 0) {
            spawnItem(screenX, screenY);
            isClicking = true;
        }

        return false;
    }
    public void spawnItem(int screenX, int screenY) {
        int[] coords = getGridCoordsFromMousePos(screenX, screenY);
            if (coords != null) {
                switch (clickMode) {
                    case "create wall":
                        nodeGrid.setNodeToNotWalkable(coords[0], coords[1]);
                        break;
                    case "delete wall":
                        nodeGrid.setNodeToWalkable(coords[0], coords[1]);
                        break;
                    case "target":
                        nodeGrid.setNodeToTarget(coords[0], coords[1]);
                        break;
                    case "start":
                        nodeGrid.setNodeToStart(coords[0], coords[1]);
                        break;
                    default:
                        break;
                }
            }
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging) {
            // math to drag the map
            dragDelta.x = oldDragDelta.x - (screenX - dragStart.x);
            dragDelta.y = oldDragDelta.y + (screenY - dragStart.y);
        }
        if (isClicking) {
          spawnItem(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 1) {
            isDragging = false;
        }
        if (button == 0) {
            isClicking = false;
        }
        
        return false;
    }
}
