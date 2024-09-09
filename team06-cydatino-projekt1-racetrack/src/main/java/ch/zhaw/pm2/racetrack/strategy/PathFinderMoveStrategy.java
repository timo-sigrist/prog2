package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.DijkstraAlgorithm;
import ch.zhaw.pm2.racetrack.PathManager;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * This strategy finds a way through the whole game
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class PathFinderMoveStrategy implements MoveStrategy {
    private List<PositionVector.Direction> directionList = new ArrayList<>();
    protected List<Node> nodeList = new ArrayList<>();
    protected ConfigSpecification.SpaceType[][] trackArray;
    protected final int MAX_TRACK_LENGTH = Integer.MAX_VALUE;

    /**
     * Constructor of PathFinderMoveStrategy
     *
     * @param trackArray array with spaceTypes of whole track
     */
    public PathFinderMoveStrategy(ConfigSpecification.SpaceType[][] trackArray) {
        this.trackArray = trackArray;

        // looping through x and y axis of trackArray
        for (int y = 0; y < trackArray.length; y++) {
            for (int x = 0; x < trackArray[y].length; x++) {
                for (ConfigSpecification.SpaceType allowedSpaceType : Config.getNoCrashTrackSpaceTypes()) {
                    if (trackArray[y][x] == allowedSpaceType) {
                        // adding node if it is an allowed track space --> we then have only track and finish line in the list of nodes
                        nodeList.add(new Node(new PositionVector(x, y), false, trackArray[y][x],
                            MAX_TRACK_LENGTH, null));
                    }
                }
            }
        }
    }

    /**
     * Method after constructor adds all nodes
     *
     * @param startingPosition starting position of car
     */
    public void postConstruct(PositionVector startingPosition) {
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(nodeList, trackArray);
        List<PositionVector> positionVectors = dijkstraAlgorithm.calculateDijkstraAlgorithm(getInitialNode(startingPosition, nodeList), nodeList, getFinishLineOfNodes());
        directionList = PathManager.convertPathPointsToDirectionList(positionVectors);
    }

    /**
     * Gets Initial Node to start calculating with dijkstra algorithm
     *
     * @return initial node
     */
    private Node getInitialNode(PositionVector initialVector, List<Node> nodeList) {
        for (Node node : nodeList) {
            if (node.positionVector.equals(initialVector)) {
                node.distance = 0;
                return node;
            }
        }
        return null;
    }

    /**
     * Creates List with all Nodes which represent the finish line.
     *
     * @return list of finish line nodes
     */
    protected List<PositionVector> getFinishLineOfNodes() {
        List<PositionVector> finalLine = new ArrayList<>();
        for (Node node : nodeList) {
            for (ConfigSpecification.SpaceType winSpaceType : Config.getWinTrackSpaceTypes()) {
                if (node.spaceType == winSpaceType) {
                    finalLine.add(node.getPositionVector());
                }
            }
        }
        return finalLine;
    }

    /**
     * Interface nextMove method
     *
     * @return Direction of next move
     */
    @Override
    public PositionVector.Direction nextMove() {
        for (PositionVector.Direction moveString : directionList) {
            directionList.remove(moveString);
            return moveString;
        }
        return null;
    }

    /**
     * Method to create node in tests
     *
     * @param positionVector
     * @param spaceType
     * @param firstNode
     * @return Node
     */
    public Node createNode(PositionVector positionVector, ConfigSpecification.SpaceType spaceType, boolean firstNode) {
        return new Node(positionVector, false, spaceType, firstNode? 0 : 500, null);
    }

    /**
     * Node Class for List
     */
    public class Node {
        private PositionVector positionVector;
        private boolean visited;
        private ConfigSpecification.SpaceType spaceType;
        private Integer distance;
        private Node predecessor;

        /**
         * Constructor of Node
         */
        public Node(PositionVector positionVector, boolean visited, ConfigSpecification.SpaceType spaceType, Integer distance, Node predecessor) {
            this.positionVector = positionVector;
            this.visited = visited;
            this.spaceType = spaceType;
            this.distance = distance;
            this.predecessor = predecessor;
        }

        /**
         * setter for PositionVector of node
         *
         * @param positionVector position of node
         */
        public void setPositionVector(PositionVector positionVector) {
            this.positionVector = positionVector;
        }

        /**
         * getter of PositionVector of node
         *
         * @return positionVector position of node
         */
        public PositionVector getPositionVector() {
            return positionVector;
        }

        /**
         * setter for spaceType of node
         *
         * @param spaceType spaceType of node
         */
        public void setSpaceType(ConfigSpecification.SpaceType spaceType) {
            this.spaceType = spaceType;
        }

        /**
         * getter of spaceType of node
         *
         * @return spaceType of node
         */
        public ConfigSpecification.SpaceType getSpaceType() {
            return spaceType;
        }

        /**
         * setter for distance of node
         *
         * @param distance distance of node
         */
        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        /**
         * getter of distance of node
         *
         * @return distance of node
         */
        public Integer getDistance() {
            return distance;
        }

        /**
         * setter for visited of node
         *
         * @param visited visited of node
         */
        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        /**
         * getter of visited of node
         *
         * @return visited of node
         */
        public boolean getVisited() {
            return visited;
        }

        /**
         * setter for predecessor of node
         *
         * @param predecessor predecessor of node
         */
        public void setPredecessor(Node predecessor) {
            this.predecessor = predecessor;
        }

        /**
         * getter of predecessor of node
         *
         * @return predecessor of node
         */
        public Node getPredecessor() {
            return predecessor;
        }
    }
}
