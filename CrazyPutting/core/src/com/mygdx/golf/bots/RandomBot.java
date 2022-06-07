package com.mygdx.golf.bots;

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

        float x = rand.nextFloat() * 4;
        float y = rand.nextFloat() * 4;

        return new Vector2(x,y);
    }
}
