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
    private ShapeRenderer shapeRenderer, shootingLine;
    private BitmapFont font;
    private float metreToPixelCoeff = 0.01f;

    private final float BALLWIDTH = 0.1f;
    private Engine engine;

    //for shooting
    private Vector2 mouseStartPos;
    private Vector2 mouseDragPos;
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

        renderShootingLine(shootingLine);
        engine.update();
    }

    private void renderShootingLine(ShapeRenderer renderer) {
        shootingLine.begin(ShapeRenderer.ShapeType.Line);
        if (isShooting) {
            Vector2 ballPosInMetres = State.getPosition();
            Vector2 ballPosInPixels = new Vector2(metresToPixels((float) ballPosInMetres.x, true),
            metresToPixels((float) ballPosInMetres.y,false));
			renderer.line(ballPosInPixels.x,ballPosInPixels.y, mouseDragPos.x,Boot.INSTANCE.getScreenHeight() - mouseDragPos.y);

            
		}
        shootingLine.end();
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
        System.out.println("Clicked");
        System.out.println(screenX + " " + screenY);
        if(engine.getBallIsStopped()) {
            mouseStartPos = new Vector2(screenX, screenY);
            mouseDragPos = new Vector2(screenX, screenY);
            isShooting = true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        if(isShooting) {
            mouseDragPos.x = screenX;
            mouseDragPos.y = screenY;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        if(isShooting) {
            isShooting = false;
            mouseDragPos.x = screenX;
            mouseDragPos.y = screenY;

            float velX = (mouseStartPos.x - mouseDragPos.x) /100;
            float velY = -(mouseStartPos.y - mouseDragPos.y) /100;
            System.out.println("vel x :" + velX);
            System.out.println("vel y :" + velY);
            engine.newShot(new Vector2(velX, velY));
        }
        return false;
    }

}
