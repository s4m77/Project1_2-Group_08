package com.mygdx.golf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.golf.engine.Engine;

public class MapScreen extends ScreenAdapter implements InputProcessor {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private float metreToPixelCoeff = 0.01f;

    private final float BALLWIDTH = 0.1f;
    private Engine engine;

    // game objects
    public MapScreen(OrthographicCamera camera, Engine engine) {
        this.camera = camera;
        this.camera.position
                .set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.engine = engine;
        font = new BitmapFont();
        font.setColor(Color.BLACK);
    }

    public void update() {
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);

    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        int p = 10;
        for (float i = 0; i <= Boot.INSTANCE.getScreenWidth(); i += p) {
            for (float j = 0; j <= Boot.INSTANCE.getScreenHeight(); j += p) {

                float x = pixelsToMetres(i, true);
                float y = pixelsToMetres(j, false);

                float n = engine.calculateHeight(x, y);
                if (n > 10 || n < -10) {
                    continue;
                }

                shapeRenderer.setColor(0, n, 0, 1);

                shapeRenderer.rect(i, j, p, p);

            }
        }
        shapeRenderer.setColor(255, 255, 255, 1);

        shapeRenderer.circle(metresToPixels((float) State.getPosition().x, true),
                metresToPixels((float) State.getPosition().y, false),
                BALLWIDTH / metreToPixelCoeff);

        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.circle(metresToPixels((float) engine.inputManager.getTargetX(), true),
                metresToPixels((float) engine.inputManager.getTargetY(), false),
                (float) engine.inputManager.getRadius() / metreToPixelCoeff);

        shapeRenderer.end();

        batch.begin();
        if (engine.getBallIsStopped()) {
            String str = "Ball stopped at : x = " + State.getPosition().x + ", y = " + State.getPosition().y;
            font.draw(batch, str, 10, Boot.INSTANCE.getScreenHeight() - 10);
        }
        batch.end();
        engine.update();
    }

    private float pixelsToMetres(float x, boolean forWidth) {
        if (forWidth) {
            return (x - Boot.INSTANCE.getScreenWidth() / 2) * metreToPixelCoeff;
        } else {
            return (x - Boot.INSTANCE.getScreenHeight() / 2) * metreToPixelCoeff;
        }

    }

    private float metresToPixels(float x, boolean forWidth) {
        if (forWidth) {

            return x / metreToPixelCoeff + Boot.INSTANCE.getScreenWidth() / 2;
        } else {
            return x / metreToPixelCoeff + Boot.INSTANCE.getScreenHeight() / 2;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        System.out.println(character);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // if(metreToPixelCoeff < 0.00)
        if (amountY < 0) {
            metreToPixelCoeff = metreToPixelCoeff / 1.4f;
        } else {
            metreToPixelCoeff = metreToPixelCoeff * 1.4f;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

}
