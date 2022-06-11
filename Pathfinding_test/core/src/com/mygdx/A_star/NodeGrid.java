package com.mygdx.A_star;

import java.util.ArrayList;
import java.util.List;

public class NodeGrid {
    public Node[][] grid;
	int gridSizeX, gridSizeY;
	public List<Node> path;
	public NodeGrid(int[][] intGrid) {
		 gridSizeX = intGrid[0].length;
		 gridSizeY = intGrid.length;
		grid = createGrid(intGrid);

	}
	Node startNode;
	Node targetNode;

    public void setNodeToTarget(int x, int y) {
		Node n = grid[y][x];
		if(n.isStart || n.isTarget){
			return;
		}
		if(targetNode != null) {

			targetNode.isTarget =false;
		}
		n.isTarget = true;
		targetNode = n;
	}
    public void setNodeToStart(int x, int y) {
		Node n = grid[y][x];
		if(n.isStart || n.isTarget){
			return;
		}
		if(startNode != null) {

			startNode.isStart =false;
		}
		
		n.isStart = true;
		startNode = n;
	}
    public void setNodeToWalkable(int x, int y) {
		Node n = grid[y][x];
		if(n.isStart || n.isTarget){
			return;
		}
		n.walkable = true;
		
		
	}
    public void setNodeToNotWalkable(int x, int y) {
		Node n = grid[y][x];
		if(n.isStart || n.isTarget){
			return;
		}
		n.walkable = false;
		
		
	}
    Node[][] createGrid(int[][] intGrid) {
		Node[][] grid = new Node[gridSizeY][gridSizeX];

		for (int x = 0; x < gridSizeX; x ++) {
			for (int y = 0; y < gridSizeY; y ++) {
				boolean walkable = intGrid[y][x] != 1;
				boolean isTarget =intGrid[y][x] == 3;
				boolean isStart =intGrid[y][x] == 2;
				grid[y][x] = new Node(walkable,isStart,isTarget, x, y);
				if(isTarget) {
					targetNode = grid[y][x];
				}
				if(isStart) {
					startNode = grid[y][x];
				}
			}
		}
		return grid;
	}

	public List<Node> GetNeighbours(Node node) {
		List<Node> neighbours = new ArrayList<Node>();

		for (int x = -1; x <= 1; x++) {
			if (x == 0)
				continue;

			int checkX = node.gridX + x;
			if (checkX >= 0 && checkX < gridSizeX ) {
				neighbours.add(grid[node.gridY][checkX]);
			}
		}
		for (int y = -1; y <= 1; y++) {
			if (y == 0)
				continue;

			int checkY = node.gridY + y;
			if (checkY >= 0 && checkY < gridSizeY ) {
				neighbours.add(grid[checkY][node.gridX]);
			}
		}


		return neighbours;
	}
	public List<Node> GetNeighboursWithDiags(Node node) {
		List<Node> neighbours = new ArrayList<Node>();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 && y == 0)
					continue;

				int checkX = node.gridX + x;
				int checkY = node.gridY + y;

				if (checkX >= 0 && checkX < gridSizeX && checkY >= 0 && checkY < gridSizeY) {
					neighbours.add(grid[checkY][checkX]);
				}
			}
		}

		return neighbours;
	}

	public ArrayList<Node> NonWallNeighbours(Node node) {
		ArrayList<Node> neighbours = new ArrayList<Node>();

		for (int x = -2; x <= 2; x= x+2) {
			for (int y = -2; y <= 2; y= y+2) {
				if (x == 0 && y == 0 || !(x==0 || y==0))
					continue;

				int checkX = node.gridX + x;
				int checkY = node.gridY + y;

				if (checkX >= 0 && checkX < gridSizeX && checkY >= 0 && checkY < gridSizeY) {
					neighbours.add(grid[checkY][checkX]);
				}
			}
		}

		return neighbours;
	}

}
