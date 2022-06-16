package com.mygdx.golf.maze_generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.mygdx.golf.A_star.Node;
import com.mygdx.golf.A_star.NodeGrid;

public class RandomMazeGenerator {

    public static int[][] createArrayGrid(int gridSizeX, int gridSizeY){
        int[][] randomArray= new int[gridSizeY][gridSizeX];
        for(int i=0; i<gridSizeX; i++){
            for(int j= 0; j<gridSizeY; j++){
                if(j%2 !=0 || i%2 !=0){
                    randomArray[j][i]= 1;
                }else{
                    randomArray[j][i]= 0;
                }
            }
        }
        return randomArray;
    }

    public static ArrayList<Node> returnToVisitList(Node[][] grid){
        ArrayList<Node> toVisit= new ArrayList<>();

        for(int i=0; i<grid.length; i++){
            for(int j= 0; j<grid[0].length; j++){
                if(grid[i][j].walkable== true){
                    toVisit.add(grid[i][j]);
                }
            }
        }
        return toVisit;
    }

    public static void addNeighbours(NodeGrid nodeGrid){
        for(Node[] nodeRow : nodeGrid.grid){
            for(Node node : nodeRow){
                node.neighbours= nodeGrid.NonWallNeighbours(node);
            }
        }
    }

    public static void avoidCycles(NodeGrid nodeGrid, Node currentNode){
        for(Node[] nodeRow : nodeGrid.grid){
            for(Node node : nodeRow){
                node.neighbours.remove(currentNode);
            }
        }
    }

    public static NodeGrid breakWall(Node currentNode, Node neighbourNode, NodeGrid nodeGrid){
        int x= (currentNode.gridX + neighbourNode.gridX)/2;
        int y= (currentNode.gridY + neighbourNode.gridY)/2;

        nodeGrid.grid[y][x].walkable= true;

        return nodeGrid;
    }

    public static void addRandomTargetandStart(int [][] randomArray, int gridSizeX, int gridSizeY){
        int xS= (int)(Math.random()*gridSizeX);
        int yS= (int)(Math.random()*gridSizeY);
        int xT=(int)(Math.random()*gridSizeX);
        int yT=(int)(Math.random()*gridSizeY);

        while((xS == xT && yS== yT) || (randomArray[yS][xS] == 1 || randomArray[yT][xT] ==1)){
            xS= (int)(Math.random()*gridSizeX);
            yS= (int)(Math.random()*gridSizeY);
            xT=(int)(Math.random()*gridSizeX);
            yT=(int)(Math.random()*gridSizeY);
        }

        // randomArray[xS][yS]= 2;
        // randomArray[xT][yT]=3;
        randomArray[yS][xS]= 2;
        randomArray[yT][xT]=3;
    }

    public static int[][] returnFinalRandomMaze(int gridSizeX, int gridSizeY, boolean moreHoles){
        int[][] arrayGrid= createArrayGrid(gridSizeX,gridSizeY);
        NodeGrid nodeGrid= new NodeGrid(arrayGrid);
        addNeighbours(nodeGrid);
        ArrayList<Node> toVisit= returnToVisitList(nodeGrid.grid);
        ArrayList<Node> visited= new ArrayList<>();
        
        Node currentNode;
        Node currentNeighbour;

        //get first currentNode and currentNeighbour
        do{
            currentNode= toVisit.get((int)(Math.random()*toVisit.size())); 
        }while(currentNode.walkable == false);

        currentNeighbour= currentNode.neighbours.get((int) (Math.random()*currentNode.neighbours.size()));

        //start backtracking
        while(!toVisit.isEmpty()){
            while(currentNode.hasNeighbours()){
                currentNeighbour= currentNode.neighbours.get((int) (Math.random()*currentNode.neighbours.size()));

                if(toVisit.contains(currentNode)){
                    toVisit.remove(currentNode);
                    visited.add(currentNode);
                }

                avoidCycles(nodeGrid, currentNode);
                breakWall(currentNode, currentNeighbour, nodeGrid);
                currentNode= currentNeighbour;
            }
            toVisit.remove(currentNode);
            avoidCycles(nodeGrid, currentNode);
            if(!visited.isEmpty()){
                currentNode= visited.get(visited.size()-1);
                visited.remove(visited.size()-1);
            }
        }

        //turn nodeGrid into randomMazeArray
        for(int i=0; i<nodeGrid.grid.length; i++){
            for(int j=0; j<nodeGrid.grid[i].length; j++){
                if(nodeGrid.grid[i][j].walkable== true){
                    arrayGrid[i][j]= 0;
                }else{
                    arrayGrid[i][j]=1;
                }
            }
        }

        //add random target and start
        addRandomTargetandStart(arrayGrid, gridSizeX, gridSizeY);
        if(moreHoles) {
            Random rn = new Random();
        for (int y = 0; y < arrayGrid.length; y++) {
            for (int x = 0; x < arrayGrid[0].length; x++) {
                int n = arrayGrid[y][x];
                if(n == 1 && rn.nextFloat()> 0.4f) {
                   
                    arrayGrid[y][x]=0;
                }
            }
        }
        }

        return arrayGrid;
    }
}
