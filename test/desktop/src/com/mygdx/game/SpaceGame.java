package com.mygdx.game;

import java.util.Set;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;

public class SpaceGame extends Game{

    public SpriteBatch batch;
  
    public static final int WIDTH=480;
    public static final int HEIGHT=720;


    @Override
    public void create() {
        // TODO Auto-generated method stub
        batch=new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
    }

    public void reneder(){
        super.render();
    }
    

    

}
