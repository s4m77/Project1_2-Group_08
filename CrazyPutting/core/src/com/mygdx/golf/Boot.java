package com.mygdx.golf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.golf.engine.Engine;
import com.mygdx.golf.engine.solvers.*;
import com.mygdx.golf.screens.MapCreatorScreen;

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

        goToMazeCreatorScreen();

       
    }

    public void goToGameScreenWithMaze(int[][] intGrid) {
        //creates the engine with the selected solver 
        Engine engine = new Engine(new Euler(), false);
        engine.setIntGrid(intGrid);
        //creates the GUI with the camera and engine
        MapScreenMaze mapScreen = new MapScreenMaze(orthographicCamera, engine);
        setScreen(mapScreen);
                Gdx.input.setInputProcessor(mapScreen);

    }

    public void goToMazeCreatorScreen() {
        MapCreatorScreen mapCreatorScreen = new MapCreatorScreen(orthographicCamera);
        setScreen(mapCreatorScreen);
        Gdx.input.setInputProcessor(mapCreatorScreen);
    }

    //simple getter methods
    public int getScreenHeight() {
        return screenHeight;
    }
    public int getScreenWidth() {
        return screenWidth;
    }
}
