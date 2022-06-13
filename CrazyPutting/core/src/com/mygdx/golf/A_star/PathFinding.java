package com.mygdx.golf.A_star;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PathFinding {
    public NodeGrid grid;
    public List<Node> path;

    public PathFinding(int[][] intGrid) {
        grid = new NodeGrid(intGrid);
        path = new ArrayList<Node>();
        findPath();
    }

    public void findPath() {
        path = new ArrayList<Node>();
        Node startNode = grid.startNode;
        Node targetNode = grid.targetNode;
        if (startNode == null || targetNode == null) {
            return;
        }
        List<Node> openSet = new ArrayList<Node>();
        HashSet<Node> closedSet = new HashSet<Node>();

        openSet.add(startNode);

        while (openSet.size() > 0) {
            Node node = openSet.get(0);
            for (int i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).getFCost() < node.getFCost() || openSet.get(i).getFCost() == node.getFCost()) {
                    if (openSet.get(i).hCost < node.hCost)
                        node = openSet.get(i);
                }
            }

            openSet.remove(node);
            closedSet.add(node);

            if (node == targetNode) {
                // found
                path = retracePath(startNode, targetNode);
                return;
            }

            for (Node neighbour : grid.GetNeighbours(node)) {
                if (!neighbour.walkable || closedSet.contains(neighbour)) {
                    continue;
                }

                int newCostToNeighbour = node.gCost + GetDistance(node, neighbour);
                if (newCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
                    neighbour.gCost = newCostToNeighbour;
                    neighbour.hCost = GetDistance(neighbour, targetNode);
                    neighbour.parent = node;

                    if (!openSet.contains(neighbour))
                        openSet.add(neighbour);
                }

            }

        }
    }

    public int GetDistance(Node nodeA, Node nodeB) {
        int dstX = Math.abs(nodeA.gridX - nodeB.gridX);
        int dstY = Math.abs(nodeA.gridY - nodeB.gridY);

        if (dstX > dstY)
            return 14 * dstY + 10 * (dstX - dstY);
        return 14 * dstX + 10 * (dstY - dstX);
    }

    public int GetDistanceWithDiags(Node nodeA, Node nodeB) {
        int dstX = Math.abs(nodeA.gridX - nodeB.gridX);
        int dstY = Math.abs(nodeA.gridY - nodeB.gridY);

        return dstX + dstY;
    }

    List<Node> retracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<Node>();
        Node currentNode = endNode;

        while (currentNode != startNode) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }

        Collections.reverse(path);

        return path;

    }

    public List<Node> getStraightLinePath() {
        if (path.size() == 0) {
            return path;
        }
        List<Node> straightLinePath = new ArrayList<Node>();
        Node startNode = grid.startNode;
        boolean isHorizontal = true;

        for (int i = 0; i < path.size(); i++) {
            Node nextNode = path.get(i);
            if (i == 0) {
                isHorizontal = startNode.gridY == nextNode.gridY;
                continue;
            }
            Node prevNode =path.get(i-1);

            boolean isH = prevNode.gridY == nextNode.gridY;

            if (isH != isHorizontal) {
                straightLinePath.add(prevNode);
                startNode = prevNode;
                isHorizontal = isH;
            }
            if (nextNode == grid.targetNode) {
                straightLinePath.add(grid.targetNode);
            }
        }

        return straightLinePath;
    }
}
