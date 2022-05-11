package com.mygdx.golf.bot;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.engine.Engine;

public class RandomBot implements Bot{
    private Engine engine;

    public RandomBot(Engine engine) {
        this.engine = engine;
    }
    @Override
    public Vector2 findBestMove() {
        Random rand = new Random();

        float x = rand.nextFloat() * 10;
        float y = rand.nextFloat() * 10;

        return new Vector2(x,y);
    }
}