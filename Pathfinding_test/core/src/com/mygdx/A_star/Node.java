package com.mygdx.A_star;

import com.badlogic.gdx.math.Vector2;

public class Node {
    public boolean walkable;
    public boolean isTarget;
    public boolean isStart;
    public Vector2 pos;

    public int gCost;
    public int hCost;

    public int gridX;
	public int gridY;

    public Node parent;

    public Node(boolean _walkable,boolean _isStart, boolean _isTarget,  int _gridX, int _gridY) {
        walkable = _walkable;
        // pos = _pos;
        isStart = _isStart;
        isTarget = _isTarget;

        gridX = _gridX;
		gridY = _gridY;
    }

    public int getFCost() {
        return gCost + hCost;
    }
}
