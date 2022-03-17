package com.mygdx.golf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.golf.engine.Engine;
import com.mygdx.golf.engine.solvers.Euler2;

public class Boot extends Game {
	
    public static Boot INSTANCE;
    public int screenWidth, screenHeight;
    private OrthographicCamera orthographicCamera;
    private FileInputManager fileInputManager;
    public Boot() {
        INSTANCE = this;
    }

    public void create() {
        this.screenHeight = Gdx.graphics.getHeight();
        this.screenWidth = Gdx.graphics.getWidth();
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenWidth,screenHeight);

        FileInputManager fileInputManager = new FileInputManager();
        State.setPosition(new Vector2((float) fileInputManager.getInitialX(),(float) fileInputManager.getInitialY()));
        State.setVelocity(new Vector2((float) fileInputManager.getV0x(),(float) fileInputManager.getV0y()));

        Engine engine = new Engine(new Euler2(), fileInputManager);
        MapScreen mapScreen = new MapScreen(orthographicCamera, engine);
        Gdx.input.setInputProcessor(mapScreen);

        setScreen(mapScreen);
    }

    public int getScreenHeight() {
        return screenHeight;
    }
    public int getScreenWidth() {
        return screenWidth;
    }
}
