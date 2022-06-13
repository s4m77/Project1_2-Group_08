package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class SettingsScreen implements Screen {
    private static final int GENERATE_MAP_BUTTON_WIDTH = 200;
    private static final int GENERATE_MAP_BUTTON_HEIGHT = 80;
    private static final int GENERATE_MAP_BUTTON_Y = 120;

    private static final int CLEAR_MAP_BUTTON_WIDTH = 200;
    private static final int CLEAR_MAP_BUTTON_HEIGHT = 80;
    private static final int CLEAR_MAP_BUTTON_Y = 120;
    private static final int BACK_BUTTON_WIDTH =200;
    private static final int BACK_BUTTON_Y = 120;
    private static final int BACK_BUTTON_HEIGHT = 80;

    Texture GENERATE_MAPBUTTON;
    Texture GENERATE_MAPButtonInactive;
    Texture ClearMapButton;
    Texture ClearMapButtonInactive;
    Texture backbutton;
    Texture backButtonInactive;
    
    final SpaceGame game;
    public SettingsScreen(SpaceGame game){
        this.game=game;
        GENERATE_MAPBUTTON = new Texture("GENERATE_MAP.png");
        GENERATE_MAPButtonInactive = new Texture("GENERATE_MAP_INACTIVE.png");
        ClearMapButton = new Texture("CLEAR_MAP.png");
        ClearMapButtonInactive = new Texture("CLEAR_MAP_INACTIVE.png");
        backbutton= new Texture("backon.png");
        backButtonInactive= new Texture("backof.png");
        final SettingsScreen sett= this;
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button){


               int  x = SpaceGame.WIDTH / 2 - BACK_BUTTON_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < x + BACK_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && SpaceGame.HEIGHT - game.cam.getInputInGameWorld().y < 420 + BACK_BUTTON_HEIGHT && SpaceGame.HEIGHT - game.cam.getInputInGameWorld().y > 380) {
                    sett.dispose();
                    game.setScreen(new MainMenuScreen(game));
                }
                return super.touchUp(screenX, screenY, pointer, button);

            }


        });
        
    }
    @Override
    public void show() {
        
        
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(192 / 255f, 192 / 255f, 192 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        int x = SpaceGame.WIDTH / 2 - GENERATE_MAP_BUTTON_WIDTH / 2;
    if (Gdx.input.getX() < x + GENERATE_MAP_BUTTON_WIDTH && Gdx.input.getY() > x
            && SpaceGame.HEIGHT - Gdx.input.getY() < GENERATE_MAP_BUTTON_Y + GENERATE_MAP_BUTTON_HEIGHT
            && SpaceGame.HEIGHT - Gdx.input.getY() > GENERATE_MAP_BUTTON_Y) {
        game.batch.draw(GENERATE_MAPBUTTON, 145, 620, GENERATE_MAP_BUTTON_WIDTH, GENERATE_MAP_BUTTON_HEIGHT);
    } else {
        game.batch.draw(GENERATE_MAPButtonInactive, 145, 620, GENERATE_MAP_BUTTON_WIDTH,
                GENERATE_MAP_BUTTON_HEIGHT);
    }

    x = SpaceGame.WIDTH / 2 - CLEAR_MAP_BUTTON_WIDTH / 2;
    if (Gdx.input.getX() < x + CLEAR_MAP_BUTTON_WIDTH && Gdx.input.getY() > x
            && SpaceGame.HEIGHT - Gdx.input.getY() < CLEAR_MAP_BUTTON_Y + CLEAR_MAP_BUTTON_HEIGHT
            && SpaceGame.HEIGHT - Gdx.input.getY() > CLEAR_MAP_BUTTON_Y) {
        game.batch.draw(ClearMapButton, 170, 490, CLEAR_MAP_BUTTON_WIDTH, CLEAR_MAP_BUTTON_HEIGHT);
    } else {
        game.batch.draw(ClearMapButtonInactive, 170, 490, CLEAR_MAP_BUTTON_WIDTH, CLEAR_MAP_BUTTON_HEIGHT);
    }

    x = SpaceGame.WIDTH / 2 - BACK_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + BACK_BUTTON_WIDTH && Gdx.input.getY() > x
                && SpaceGame.HEIGHT - Gdx.input.getY() < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT
                && SpaceGame.HEIGHT - Gdx.input.getY() > BACK_BUTTON_Y) {
            
            game.batch.draw(backbutton, 158, 380, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
             
        } else {
            game.batch.draw(backButtonInactive, 158, 380, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
           
        }
   
    
    

    game.batch.end();
    
    }

    @Override
    public void resize(int width, int height) {
        
        
    }

    @Override
    public void pause() {
        
        
    }

    @Override
    public void resume() {
        
        
    }

    @Override
    public void hide() {
        
        
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        
    }
    
}
