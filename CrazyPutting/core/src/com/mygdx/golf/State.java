package com.mygdx.golf;

import com.badlogic.gdx.math.Vector2;


//Stores the state 
public class State {

 Vector2 position;
 Vector2 velocity;

    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getVelocity() {
        return velocity;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    

}

