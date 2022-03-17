package com.mygdx.golf;

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
import com.mygdx.golf.engine.Engine;

public class MapScreen extends ScreenAdapter implements InputProcessor {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer, shootingLine;
    private BitmapFont font;

    private float metreToPixelCoeff = 0.01f;
    // for moving around the map
    private Vector2 dragDelta;
    private Vector2 dragStart;
    private Vector2 oldDragDelta;
    private boolean isDragging;

    private final float BALLWIDTH = 0.1f;
    private Engine engine;

    // for shooting
    private Vector2 mouseShootStart;
    private Vector2 mouseShootEnd;
    private boolean isShooting;

    // game objects
    public MapScreen(OrthographicCamera camera, Engine engine) {
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
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

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

                shapeRenderer.setColor(0, n / 10 + 0.4f, 0, 1);

                shapeRenderer.rect(i, j, p, p);

            }
        }
        shapeRenderer.setColor(255, 255, 255, 1);

        shapeRenderer.circle(metresToPixels(State.getPosition().x, true),
                metresToPixels(State.getPosition().y, false),
                BALLWIDTH / metreToPixelCoeff);

        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.circle(metresToPixels(engine.getTargetPosition().x, true),
                metresToPixels(engine.getTargetPosition().y, false),
                engine.getTargetRadius() / metreToPixelCoeff);

        shapeRenderer.end();

        batch.begin();
        String str = "Number of shots : " + engine.getNumberOfShots();
        font.draw(batch, str, 10, 20);
        if (engine.getBallIsStopped()) {
            str = "Ball stopped at : x = " + State.getPosition().x + ", y = " + State.getPosition().y;
            font.draw(batch, str, 10, Boot.INSTANCE.getScreenHeight() - 10);

        }

        batch.end();

        renderShootingLine(shootingLine);
        engine.update();
    }

    public float ln(float n) {
        return (float) (-Math.log(1 - n)) / n;
    }

    private void renderShootingLine(ShapeRenderer renderer) {
        shootingLine.begin(ShapeRenderer.ShapeType.Line);
        if (isShooting) {
            Vector2 ballPosInMetres = State.getPosition();
            Vector2 ballPosInPixels = new Vector2(metresToPixels( ballPosInMetres.x, true),
                    metresToPixels(ballPosInMetres.y, false));
            renderer.line(ballPosInPixels.x, ballPosInPixels.y, mouseShootEnd.x,
                    Boot.INSTANCE.getScreenHeight() - mouseShootEnd.y);

        }
        shootingLine.end();
    }

    private float pixelsToMetres(float x, boolean forWidth) {
        if (forWidth) {
            return (x - Boot.INSTANCE.getScreenWidth() / 2 + dragDelta.x) * metreToPixelCoeff;
        } else {
            return (x - Boot.INSTANCE.getScreenHeight() / 2 + dragDelta.y) * metreToPixelCoeff;
        }

    }

    private float metresToPixels(float x, boolean forWidth) {
        if (forWidth) {

            return x / metreToPixelCoeff + Boot.INSTANCE.getScreenWidth() / 2 - dragDelta.x;
        } else {
            return x / metreToPixelCoeff + Boot.INSTANCE.getScreenHeight() / 2 - dragDelta.y;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
    
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
    
        System.out.println(character);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
    
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
    
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        
        if (amountY < 0) {
            metreToPixelCoeff = metreToPixelCoeff / 1.4f;
        } else {
            metreToPixelCoeff = metreToPixelCoeff * 1.4f;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            // left click
            if (engine.getBallIsStopped()) {
                mouseShootStart = new Vector2(screenX, screenY);
                mouseShootEnd = new Vector2(screenX, screenY);
                isShooting = true;
            }
        }
        if (button == 1) {
            isDragging = true;
            dragStart = new Vector2(screenX, screenY);
            oldDragDelta = new Vector2(dragDelta.x, dragDelta.y);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
    
        if (isShooting) {
            mouseShootEnd.x = screenX;
            mouseShootEnd.y = screenY;
        }
        if (isDragging) {

            dragDelta.x = oldDragDelta.x - (screenX - dragStart.x);
            dragDelta.y = oldDragDelta.y + (screenY - dragStart.y);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            // left click
            if (isShooting) {
                isShooting = false;
                mouseShootEnd.x = screenX;
                mouseShootEnd.y = screenY;

                float velX = (mouseShootStart.x - mouseShootEnd.x) / 50;
                float velY = -(mouseShootStart.y - mouseShootEnd.y) / 50;
                engine.newShot(new Vector2(velX, velY));
            }
        }
        if (button == 1) {
            isDragging = false;
        }
        return false;
    }

}
