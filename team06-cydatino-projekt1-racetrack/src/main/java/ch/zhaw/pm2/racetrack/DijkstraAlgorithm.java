package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.strategy.PathFinderMoveStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dijkstra algorithm, calculates the shortest path
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class DijkstraAlgorithm {

    private final List<PathFinderMoveStrategy.Node> nodeList;
    private final ConfigSpecification.SpaceType[][] trackArray;

    /**
     * constructor of class
     */
    public DijkstraAlgorithm(List<PathFinderMoveStrategy.Node> nodeList, ConfigSpecification.SpaceType[][] trackArray) {
        this.nodeList = nodeList;
        this.trackArray = trackArray;
    }

    /**
     * The main dijkstra method. It calculates the distance between possible neighbours.
     *
     * @param currentNode         actual position of car
     * @param nodeList            all nodes which are not a wall (possible paths)
     * @param destinationNodeList destination/finish line
     * @return list of all positions the car needs to follow --> PathFollower
     */
    public List<PositionVector> calculateDijkstraAlgorithm(PathFinderMoveStrategy.Node currentNode, List<PathFinderMoveStrategy.Node> nodeList, List<PositionVector> destinationNodeList) {
        if (currentNode == null) {
            currentNode = getNodeWithMinDist(nodeList);
        }

        for (PathFinderMoveStrategy.Node neighbourNode : getAllNeighboursFromNode(currentNode)) {
            if (neighbourNode != null) {
                //setting current node as predecessor on our neighbour to show where the car has already 'driven through'
                if (neighbourNode.getPredecessor() == null) {
                    neighbourNode.setPredecessor(currentNode);
                }
                //with our predecessor we can check now if we crossed the finish line backwards
                if (crossFinishLineWrongDirection(neighbourNode)) {
                    neighbourNode.setPredecessor(currentNode);
                    neighbourNode.setDistance(Integer.MAX_VALUE);
                    neighbourNode.setVisited(true);
                } else if (destinationNodeList.contains(neighbourNode.getPositionVector())) {
                    neighbourNode.setDistance(currentNode.getDistance() + 1);
                    List<PositionVector> tmpList = fillList(neighbourNode, new ArrayList<>());
                    Collections.reverse(tmpList);
                    return tmpList;
                } else {
                    if (neighbourNode.getDistance() > currentNode.getDistance() + 1) {
                        neighbourNode.setDistance(currentNode.getDistance() + 1);
                        neighbourNode.setPredecessor(currentNode);
                    }
                }
            }
        }

        // All neigbhours are updated, so current node is done -> visited
        currentNode.setVisited(true);
        while (true) {
            List<PositionVector> retList = calculateDijkstraAlgorithm(null, nodeList, destinationNodeList);
            if (retList != null) {
                return retList;
            }
        }
    }

    /**
     * If we dont have a starting node we get one with the minimum distance
     *
     * @param nodeList all nodes which are not a wall
     * @return closest node
     */
    private PathFinderMoveStrategy.Node getNodeWithMinDist(List<PathFinderMoveStrategy.Node> nodeList) {
        PathFinderMoveStrategy.Node nodeWithMinDist = null;
        for (PathFinderMoveStrategy.Node node : nodeList) {
            if (!node.getVisited() && (nodeWithMinDist == null || node.getDistance() < nodeWithMinDist.getDistance())) {
                nodeWithMinDist = node;
            }
        }
        return nodeWithMinDist;
    }

    /**
     * Method to return all neighbours from a node
     *
     * @param centerNode center node
     * @return closest node / neighbours
     */
    private List<PathFinderMoveStrategy.Node> getAllNeighboursFromNode(PathFinderMoveStrategy.Node centerNode) {
        List<PathFinderMoveStrategy.Node> neibhbors = new ArrayList<>();
        for (PositionVector.Direction direction : PositionVector.Direction.values()) {
            // Direction.NONE would be our centerNode
            if (direction != PositionVector.Direction.NONE) {
                neibhbors.add(getNodeByPosition(PositionVector.add(centerNode.getPositionVector(),
                    direction.vector)));
            }
        }
        return neibhbors;
    }

    /**
     * Method to return node by a position vector
     *
     * @param positionVector position to search node
     * @return node from given position
     */
    private PathFinderMoveStrategy.Node getNodeByPosition(PositionVector positionVector) {
        // looping through all nodes to find the node equivalent to our searched position
        for (PathFinderMoveStrategy.Node node : nodeList) {
            if (positionVector.equals(node.getPositionVector())) {
                return node;
            }
        }
        return null;
    }

    /**
     * Method to fill list with positions
     *
     * @param endNode         Last node
     * @param positionVectors List of positions to add
     * @return list with positions to endNode
     */
    private List<PositionVector> fillList(PathFinderMoveStrategy.Node endNode, List<PositionVector> positionVectors) {
        positionVectors.add(endNode.getPositionVector());
        if (endNode.getDistance() != 0) {
            fillList(endNode.getPredecessor(), positionVectors);
        }
        return positionVectors;
    }

    /**
     * Method check if we finished over starting line
     *
     * @param currentNode current node with predecessor
     * @return boolean if we finished over starting line
     */
    private boolean crossFinishLineWrongDirection(PathFinderMoveStrategy.Node currentNode) {
        PositionVector direction = PositionVector.subtract(currentNode.getPositionVector(),
            currentNode.getPredecessor().getPositionVector());
        int ySpeed = direction.getY();
        int xSpeed = direction.getX();
        return switch (currentNode.getSpaceType()) {
            case FINISH_DOWN -> ySpeed < 0;
            case FINISH_UP -> ySpeed > 0;
            case FINISH_RIGHT -> xSpeed < 0;
            case FINISH_LEFT -> xSpeed > 0;
            default -> false;
        };
    }
}
