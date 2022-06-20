package com.mygdx.golf.screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

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
import com.mygdx.golf.Boot;
import com.mygdx.golf.JsonFileManager;
import com.mygdx.golf.A_star.Node;
import com.mygdx.golf.A_star.NodeGrid;
import com.mygdx.golf.A_star.PathFinding;
import com.mygdx.golf.maze_generator.RandomMazeGenerator;

public class MapCreatorScreen extends ScreenAdapter implements InputProcessor {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);

    private int[][] intGrid;
    // for moving around the map
    private Vector2 dragDelta;
    private Vector2 dragStart;
    private Vector2 oldDragDelta;
    private boolean isDragging;

    private NodeGrid nodeGrid;
    PathFinding pathFinder;

    float gridWidth;
    float gridHeight;
    float gridOriginX;
    float gridOriginY;
    float gridTopY;
    float lineWidth = 2;
    float cellSizePixels = 20;

    String clickMode = "create wall";
    boolean isClicking = false;
    boolean showPath = false;
    // coefficient from metres to pixels

    // Initializes everything
    public MapCreatorScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position
                .set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.shapeRenderer = new ShapeRenderer();

        this.batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        generateIntGrid(20, 20);
        pathFinder = new PathFinding(intGrid);
        nodeGrid = pathFinder.grid;
        this.dragDelta = new Vector2(0, 0);

    }

    public void generateIntGrid(int xLength, int yLength) {
        intGrid = new int[yLength][xLength];
        for (int y = 0; y < intGrid.length; y++) {
            for (int x = 0; x < intGrid[0].length; x++) {
                intGrid[y][x] = 0;
            }
        }
    }

    // method for whole grid
    public void drawGrid() {
        Node[][] grid = nodeGrid.grid;
        shapeRenderer.setColor(1, 1, 1, 0.5f);
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

    public void updateGridValues() {

        gridWidth = nodeGrid.grid[0].length * cellSizePixels;
        gridHeight = nodeGrid.grid.length * cellSizePixels;
        gridOriginX = Boot.INSTANCE.getScreenWidth() / 2 - gridWidth / 2 - dragDelta.x;
        gridOriginY = Boot.INSTANCE.getScreenHeight() / 2 - gridHeight / 2 - dragDelta.y;
        gridTopY = Boot.INSTANCE.getScreenHeight() / 2 + gridHeight / 2 - dragDelta.y;
    }

    // native gdx method that is called for each frame
    @Override
    public void render(float delta) {
        updateGridValues();
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
        if(showPath) {
            shapeRenderer.setColor(0, 0, 1, 0.5f);
            drawPath(pathFinder.path);
        }
        shapeRenderer.setColor(0, 1, 1, 1);

        // drawPath(pathFinder.getStraightLinePath());

        shapeRenderer.end();
        pathFinder.findPath();

        batch.begin();
        String str1 = "Click mode : " + clickMode
                + "\nT : Target\nI : Starting node \nW : Creating walls \nD : Deleting Walls\n\n";
        String str2 = "G : Grid size \nS : Save Map \nO : Open Map \nN : Generate Random Maze\nP : Show/Hide A* Path";
        font.draw(batch, str1 + str2, 10, Boot.INSTANCE.getScreenHeight() - 20);

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

    public int[][] createRandomMaze(int gridSizeX, int gridSizeY) {
        int[][] randomMaze = new int[gridSizeX][gridSizeY];
        int target;
        int start;
        ArrayList<Node> toVisit = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();

        // initialize randomMaze
        for (int i = 0; i < gridSizeX; i++) {
            for (int j = 0; j < gridSizeY; j++) {
                randomMaze[i][j] = 1;
            }
        }

        // create nodeGrid from randomMaze
        NodeGrid nodeGrid = new NodeGrid(randomMaze);

        // add nodes to toVisit
        for (int i = 0; i < gridSizeX; i++) {
            for (int j = 0; j < gridSizeY; j++) {
                toVisit.add(nodeGrid.grid[i][j]);
            }
        }

        // start
        Node currentNode;
        Node currentNeighbour;
        NodeGrid newGrid;
        int visiting = visited.size();

        // initialize
        currentNode = toVisit.get((int) Math.random() * toVisit.size());
        currentNode.neighbours = nodeGrid.NonWallNeighbours(currentNode);
        currentNeighbour = currentNode.neighbours.get((int) Math.random() * currentNode.neighbours.size()); // same as 4
        toVisit.remove(currentNode);
        visited.add(currentNode);

        for (Node[] nodeRow : nodeGrid.grid) {
            for (Node node : nodeRow) {
                node.neighbours = nodeGrid.NonWallNeighbours(currentNode);
                node.neighbours.remove(currentNode);
            }
        }

        while (!toVisit.isEmpty()) {
            while (!currentNode.neighbours.isEmpty()) {

                currentNode = currentNeighbour;
                currentNode.neighbours = nodeGrid.NonWallNeighbours(currentNode);
                currentNeighbour = currentNode.neighbours.get((int) Math.random() * currentNode.neighbours.size());

                if (toVisit.contains(currentNode)) {
                    toVisit.remove(currentNode);
                }
                if (!visited.contains(currentNode)) {
                    visited.add(currentNode);
                }

                for (Node[] nodeRow : nodeGrid.grid) {
                    for (Node node : nodeRow) {
                        node.neighbours = nodeGrid.NonWallNeighbours(currentNode);
                        node.neighbours.remove(currentNode);
                    }
                }

                if (!currentNode.neighbours.isEmpty()) {
                    nodeGrid = breakWall(currentNode, currentNeighbour, nodeGrid);
                }
            }

            visiting = visiting - 1;
            currentNode = currentNeighbour = visited.get(visiting);
        }

        for (Node[] nodeRow : nodeGrid.grid) {
            for (Node node : nodeRow) {
                node.neighbours = nodeGrid.NonWallNeighbours(currentNode);
                node.neighbours.remove(currentNode);
            }
        }

        // turn nodeGrid into randomMaze
        for (int i = 0; i < nodeGrid.grid.length; i++) {
            for (int j = 0; j < nodeGrid.grid[i].length; j++) {
                if (nodeGrid.grid[i][j].walkable == true) {
                    randomMaze[i][j] = 0;
                } else {
                    randomMaze[i][j] = 1;
                }
            }
        }

        // initialize random target and start
        do {
            target = randomMaze[(int) Math.random() * gridSizeX][(int) Math.random() * gridSizeY];
            start = randomMaze[(int) Math.random() * gridSizeX][(int) Math.random() * gridSizeY];
        } while (target == start || !((target == 1 || start == 1)));

        return randomMaze;
    }

    public NodeGrid breakWall(Node currentNode, Node neighbourNode, NodeGrid nodeGrid) {
        int x = (currentNode.gridX + neighbourNode.gridX) / 2;
        int y = (currentNode.gridY + neighbourNode.gridY) / 2;

        nodeGrid.grid[currentNode.gridX][currentNode.gridY].walkable = false;
        nodeGrid.grid[neighbourNode.gridX][neighbourNode.gridY].walkable = false;
        nodeGrid.grid[y][x].walkable = false;

        return nodeGrid;
    }

    // gridSize has to be a string with two Integers seperated by the an x
    public void changeGridSize(String gridSize) {
        String[] strs = gridSize.split("x");
        if (strs.length != 2) {
            System.out.println("Wrong syntax");
            return;
        }

        try {
            int x = Integer.parseInt(strs[0]);
            int y = Integer.parseInt(strs[1]);
            generateIntGrid(x, y);
            pathFinder = new PathFinding(intGrid);
            nodeGrid = pathFinder.grid;
        } catch (NumberFormatException e) {
            System.out.println("Wrong syntax");
        }

    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println(character);
        if (character == '\n' || character == '\r') {
            Boot.INSTANCE.goToGameScreenWithMaze(nodeGrid.toIntGrid());
        }
        if (character == 't') {
            clickMode = "target";
        }
        if (character == 'w') {
            clickMode = "create wall";
        }
        if (character == 'd') {
            clickMode = "delete wall";
        }
        if (character == 'i') {
            clickMode = "start";
        }
       
        if (character == 'g') {
            String gridSize = JOptionPane.showInputDialog("What grid size ?", "20x20");
            changeGridSize(gridSize);
        }
        if (character == 's') {
            String mapName = JOptionPane.showInputDialog("Choose a map name");
            JsonFileManager.writeMapToJson(nodeGrid.toIntGrid(), mapName);
        }
        if (character == 'o') {
            String[] mapNames = JsonFileManager.getAllMapNames();

            String mapName = askUser("Open Saved Map", "Choose map name", mapNames);
            intGrid = JsonFileManager.readMapFromJson(mapName);
            pathFinder = new PathFinding(intGrid);
            nodeGrid = pathFinder.grid;

        }
        if (character == 'n') {
            intGrid = RandomMazeGenerator.returnFinalRandomMaze(nodeGrid.gridSizeX, nodeGrid.gridSizeY, false);
            ;
            pathFinder = new PathFinding(intGrid);
            nodeGrid = pathFinder.grid;

        }
        if(character == 'p') {
            showPath = !showPath;
        }

        return false;
    }

    String askUser(String title, String question, String[] choices) {
        String s = (String) JOptionPane.showInputDialog(
                null,
                question,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                choices[0]);
        return s;
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
        // changes the metreToPixelCoeff when scrolling, which creates a zoom effect
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
        if (button == 0) {
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
