package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Boot extends Game {

    public static Boot INSTANCE;
    public int screenWidth, screenHeight;
    private OrthographicCamera orthographicCamera;
    public static Stage stage;

    public Boot() {
        //Makes Boot statically accessible in non static methods
        //Not very important to understand, it just works
        
        INSTANCE = this;
    }

    public void create() {
        //make a stage for your button to go on
        /*stage = new Stage();

        //load a skin(a collection of styles for objects)
        // skin is from gdx-skins (https://github.com/czyzby/gdx-skins)
        Skin skin = new Skin(Gdx.files.internal("neon-ui.json"));

        //create your button
        TextButton button= new TextButton("Button1", skin);

        //add it to your stage
        stage.addActor(button);

        // add a listener to your buttons so it does something when clicked
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("I was clicked");
            }
        });*/

        // set the sgae as the input processor so it will respond to clicks etc
        //Gdx.input.setInputProcessor(stage);
        //sets the camera settings
        this.screenHeight = Gdx.graphics.getHeight();
        this.screenWidth = Gdx.graphics.getWidth();
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenWidth,screenHeight);

        
        //creates the GUI with the camera and engine
        MapScreen mapScreen = new MapScreen(orthographicCamera);
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
