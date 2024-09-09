package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.DijkstraAlgorithm;
import ch.zhaw.pm2.racetrack.PathManager;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * The PathFollowerMoveStrategy class determines the next move based on a file containing points on a path.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class PathFollowerMoveStrategy extends PathFinderMoveStrategy {
    private final List<PositionVector> pathPositionVectors;
    private List<Direction> directionList = new ArrayList<>();

    /**
     * Constructor which requires the trackArray, config, mapName and the startingPosition
     *
     * @param trackArray       track array
     * @param config           config
     * @param mapName          map name
     * @param startingPosition starting position
     * @throws FileNotFoundException exception, if the move list file has not be found
     */
    public PathFollowerMoveStrategy(ConfigSpecification.SpaceType[][] trackArray, Config config, String mapName, PositionVector startingPosition) throws FileNotFoundException {
        super(trackArray);

        Path path = Paths.get(config.getFollowerDirectory() + File.separator + mapName + "_handout_points.txt");
        File fileWithPositions = path.toFile();

        Scanner fileReader = new Scanner(fileWithPositions, StandardCharsets.UTF_8.toString());
        pathPositionVectors = new ArrayList<>();
        pathPositionVectors.add(startingPosition);
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            PositionVector positionVector = convertLineToPositionVector(line);
            pathPositionVectors.add(positionVector);
        }
        fileReader.close();
    }

    /**
     * Method after constructor adds all nodes
     */
    public void postConstruct() {
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(nodeList, trackArray);
        Iterator<PositionVector> it = pathPositionVectors.iterator();
        PositionVector currentPosition = null;
        clearNodesForAlgorithm(nodeList, pathPositionVectors.get(0));
        while (it.hasNext()) {
            PositionVector nextPosition = it.next();
            if (currentPosition != null) {
                List<PositionVector> calculatedPositions =
                    dijkstraAlgorithm.calculateDijkstraAlgorithm(
                        findNodeToPosition(currentPosition, nodeList),
                        nodeList,
                        List.of(nextPosition)
                    );
                directionList.addAll(PathManager.convertPathPointsToDirectionList(calculatedPositions));
                clearNodesForAlgorithm(nodeList, nextPosition);
            }
            currentPosition = new PositionVector(nextPosition.getX(), nextPosition.getY());
        }

        //to finish we need to find the shortest path to the finish line
        clearNodesForAlgorithm(nodeList, pathPositionVectors.get(pathPositionVectors.size() - 1));
        List<PositionVector> positionsToFinishLine = dijkstraAlgorithm.calculateDijkstraAlgorithm(
            findNodeToPosition(pathPositionVectors.get(pathPositionVectors.size() - 1), nodeList),
            nodeList,
            getFinishLineOfNodes()
        );
        directionList.addAll(PathManager.convertPathPointsToDirectionList(positionsToFinishLine));
    }

    /**
     * Method to find and set initial start
     */
    private void clearNodesForAlgorithm(List<PathFinderMoveStrategy.Node> allNodes, PositionVector initialVector) {
        for (PathFinderMoveStrategy.Node node : allNodes) {
            if (node.getPositionVector().equals(initialVector)) {
                node.setDistance(0);
            } else {
                node.setDistance(MAX_TRACK_LENGTH);
            }
            node.setVisited(false);
            node.setPredecessor(null);
        }
    }

    /**
     * Returns direction to reach next waypoint
     *
     * @return direction to reach next waypoint
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
     * Converts a line to a position vector
     *
     * @param line single line like (X:63, Y:45)
     * @return position vector
     */
    private PositionVector convertLineToPositionVector(String line) {
        String formattedLine = line.replaceAll("[ ()XY:]", "");
        String[] coordinates = formattedLine.split(",");
        int xValue = Integer.parseInt(coordinates[0]);
        int yValue = Integer.parseInt(coordinates[1]);

        return new PositionVector(xValue, yValue);
    }

    /**
     * Finds the node to a position
     *
     * @param positionVector position vector
     * @param nodeList       node list
     * @return node
     */
    private Node findNodeToPosition(PositionVector positionVector, List<Node> nodeList) {
        for (Node node : nodeList) {
            if (node.getPositionVector().equals(positionVector)) {
                return node;
            }
        }
        return null;
    }
}
