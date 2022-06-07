package com.mygdx;

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

public class MapScreen extends ScreenAdapter implements InputProcessor {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);

    private int[][] grid = new int[][] {
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    };

    int size = 50;
    // coefficient from metres to pixels

    // Initializes everything
    public MapScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position
                .set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.shapeRenderer = new ShapeRenderer();

        this.batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
    }

    // method for whole grid
    public void drawGrid(int[][] grid) {

        int width = grid[0].length * size;
        int height = grid.length * size;
        int originX = Boot.INSTANCE.getScreenWidth() / 2 - width / 2;
        int originY = Boot.INSTANCE.getScreenHeight() / 2 - height / 2;
        int lineWidth = 2;

        for (int i = 0; i <= grid[0].length; i++) {
            shapeRenderer.rectLine(originX + i * size, originY, originX + i * size, originY + height, lineWidth);
        }
        for (int i = 0; i <= grid.length; i++) {
            shapeRenderer.rectLine(originX, originY + i * size, originX + width, originY + i * size, lineWidth);
        }
        shapeRenderer.setColor(0, 0, 0, 1);

        // shapeRenderer.rect(originX, originY, size -lineWidth , size-lineWidth );
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[grid.length - i - 1][j] == 1) {

                    shapeRenderer.rect(originX + j * size + 1, originY + i * size + 1, size - lineWidth,
                            size - lineWidth);
                }
            }
        }

    }

    // native gdx method that is called for each frame
    @Override
    public void render(float delta) {
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

        drawGrid(grid);
        shapeRenderer.end();

    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyTyped(char character) {

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

        return false;
    }

    //
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }
}
