package com.mygdx.game;

import javax.swing.text.View;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {
  

    private static final int PLAY_GAME_BUTTON_WIDTH = 200;
    private static final int PLAY_GAME_BUTTON_HEIGHT = 150;
    private static final int PLAY_GAME_BUTTON_Y = 150;

    private static final int EXIT_BUTTON_WIDTH = 200;
    private static final int EXIT_BUTTON_HEIGHT = 150;
   
    private static final int SETTINGS_WIDTH = 200;
    private static final int SETTINGS_HEIGHT = 150;
    private static final int SETTINGS_Y = 120;
    private static final int EXIT_BUTTON_Y = 110;
    

    final SpaceGame game;
    Texture PLAY_GAMEButton;
    Texture PLAY_GAMEButtonInactive;
    Texture exitButton;
    Texture exitButtonInactive;
    Texture SETTINGS;
    Texture SETTINGSInactive;
   
    

    public MainMenuScreen(SpaceGame game) {
        this.game = game;
        PLAY_GAMEButton = new Texture("PLAY_GAME_BUTTON.png");
        PLAY_GAMEButtonInactive = new Texture("PLAY_GAME_BUTTON_INACTIVE.png");
        exitButton=new Texture("exiton.png");
        exitButtonInactive=new Texture("exitof.png");
        SETTINGS = new Texture("SETTING_BUTTON.png");
        SETTINGSInactive = new Texture("SETTING_BUTTON_INACTIVE.png");
        final MainMenuScreen menu=this;
         Gdx.input.setInputProcessor(new InputAdapter(){  
             @Override
         public boolean touchDown(int screenX, int screenY, int pointer, int button){
        //     int x = SpaceGame.WIDTH / 2 - PLAY_GAME_BUTTON_WIDTH / 2;
        //     if (game.cam.getInputInGameWorld().x<x + PLAY_GAME_BUTTON_WIDTH && game.cam.getInputInGameWorld().x>x  && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y<PLAY_GAME_BUTTON_Y+ PLAY_GAME_BUTTON_HEIGHT && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y>PLAY_GAME_BUTTON_Y){
        //     menu.dispose();
        //     game.setScreen( );  // here you do the same thing when the game is connected
        // }

                int x = SpaceGame.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
				if (game.cam.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && SpaceGame.HEIGHT - game.cam.getInputInGameWorld().y < 200 + EXIT_BUTTON_HEIGHT && SpaceGame.HEIGHT - game.cam.getInputInGameWorld().y > 200) {
					menu.dispose();
					Gdx.app.exit();
				}
                x = SpaceGame.WIDTH / 2 - SETTINGS_WIDTH / 2;
				if (game.cam.getInputInGameWorld().x < x + SETTINGS_WIDTH && game.cam.getInputInGameWorld().x > x && SpaceGame.HEIGHT - game.cam.getInputInGameWorld().y < 420 + SETTINGS_HEIGHT && SpaceGame.HEIGHT - game.cam.getInputInGameWorld().y > 420) {
					menu.dispose();
					game.setScreen(new SettingsScreen(game));
				}
            return super.touchUp(screenX, screenY, pointer, button);

             }});
    
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        Gdx.gl.glClearColor(192 / 255f, 192 / 255f, 192 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        
        // creating PLAY_GAME BUTON
        int x = SpaceGame.WIDTH / 2 - PLAY_GAME_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + PLAY_GAME_BUTTON_WIDTH && Gdx.input.getY() > x
                && SpaceGame.HEIGHT - Gdx.input.getY() < PLAY_GAME_BUTTON_Y + PLAY_GAME_BUTTON_HEIGHT
                && SpaceGame.HEIGHT - Gdx.input.getY() > PLAY_GAME_BUTTON_Y) {
            game.batch.draw(PLAY_GAMEButton, 155, 550, PLAY_GAME_BUTTON_WIDTH, PLAY_GAME_BUTTON_HEIGHT);

            
        } else {
            game.batch.draw(PLAY_GAMEButtonInactive, 155, 550, PLAY_GAME_BUTTON_WIDTH, PLAY_GAME_BUTTON_HEIGHT);
        }
        

        // create settings button
        x = SpaceGame.WIDTH / 2 - SETTINGS_WIDTH / 2;
        if (Gdx.input.getX() < x + SETTINGS_WIDTH && Gdx.input.getY() > x
                && SpaceGame.HEIGHT - Gdx.input.getY() < SETTINGS_Y + SETTINGS_HEIGHT
                && SpaceGame.HEIGHT - Gdx.input.getY() > SETTINGS_Y) {
            game.batch.draw(SETTINGS, 158, 420, SETTINGS_WIDTH, SETTINGS_HEIGHT);
            


              
    
        } else {
            game.batch.draw(SETTINGSInactive, 158, 420, SETTINGS_WIDTH, SETTINGS_HEIGHT);

    }

        x = SpaceGame.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getY() > x
                && SpaceGame.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT
                && SpaceGame.HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitButton, 158, 200, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

        } else {
            game.batch.draw(exitButtonInactive, 158, 200, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

        }
       

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);

    }

}
