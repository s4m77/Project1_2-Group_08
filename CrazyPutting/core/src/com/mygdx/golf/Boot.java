package com.mygdx.golf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.golf.engine.Engine;
import com.mygdx.golf.engine.solvers.*;

public class Boot extends Game {

    public static Boot INSTANCE;
    public int screenWidth, screenHeight;
    private OrthographicCamera orthographicCamera;

    public Boot() {
        //Makes Boot statically accessible in non static methods
        //Not very important to understand, it just works
        
        INSTANCE = this;
    }

    public void create() {
        //sets the camera settings
        this.screenHeight = Gdx.graphics.getHeight();
        this.screenWidth = Gdx.graphics.getWidth();
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenWidth,screenHeight);

        //creates the engine with the selected solver 
        Engine engine = new Engine(new AB3(), true);
        //creates the GUI with the camera and engine
        MapScreen mapScreen = new MapScreen(orthographicCamera, engine);
        Gdx.input.setInputProcessor(mapScreen);

        setScreen(mapScreen);
    }

    //simple getter methods
    public int getScreenHeight() {
        return screenHeight;
    }
    public int getScreenWidth() {
        return screenWidth;
    }
}
