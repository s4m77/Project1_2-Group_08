package com.mygdx.golf;

import com.badlogic.gdx.math.Vector2;

public class State {

    static Vector2 position;
    static Vector2 velocity;

    public static Vector2 getPosition() {
        return position;
    }
    public static Vector2 getVelocity() {
        return velocity;
    }
    public static void setPosition(Vector2 position) {
        State.position = position;
    }
    public static void setVelocity(Vector2 velocity) {
        State.velocity = velocity;
    }

    

}

