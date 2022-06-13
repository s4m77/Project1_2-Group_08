package com.mygdx.golf.bots;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.golf.A_star.Node;
import com.mygdx.golf.engine.Engine;

public class MazeBot {
    Engine engine;
    public MazeBot(Engine engine) {
        this.engine = engine;
    }
    public Vector2 solveMaze() {
        Bot bot = new StraightLineBot(engine);
        
        // Vector2 bestMove = bot.findBestMove(engine.targetPosition);
        List<Node> path = engine.pathFinder.path;

        for (int i = path.size()-1; i >= 0; i--) {
            Vector2 nextPos = engine.getCenterPositionFromGridCoords(path.get(i));
            Vector2 bestMove = bot.findBestMove(nextPos, false);
            if(bestMove != null) {
                System.out.println("Node " + i + "is reachable");
                return bestMove;
            }
        }
        return null;
        
    }
}
