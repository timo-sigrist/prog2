package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.strategy.PathFinderMoveStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DijkstraAlgorithmTest {

    private final List<PathFinderMoveStrategy.Node> nodeList = new ArrayList<>();
    private final ConfigSpecification.SpaceType[][] trackArray = new ConfigSpecification.SpaceType[3][3];;

    /**
     * Setup for tests
     */
    @BeforeEach
    void setup() {
        PathFinderMoveStrategy pathFinderMoveStrategy = new PathFinderMoveStrategy(trackArray);
        boolean firstNode = true;
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<3; j++) {
                trackArray[i][j] = ConfigSpecification.SpaceType.TRACK;
                nodeList.add(pathFinderMoveStrategy.createNode(new PositionVector(i, j), ConfigSpecification.SpaceType.TRACK, firstNode));
                firstNode = false;
            }
        }
    }

    /**
     * Tests calculating Dijkstra Algorithm
     */
    @Test
    void testCalculateDijkstraAlgorithm() {
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(nodeList, trackArray);
        List<PositionVector> result = dijkstraAlgorithm.calculateDijkstraAlgorithm(
            nodeList.get(0),
            nodeList,
            List.of(nodeList.get(6).getPositionVector())
        );
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(new PositionVector(0, 0), result.get(0));
        Assertions.assertEquals(new PositionVector(1, 0), result.get(1));
        Assertions.assertEquals(new PositionVector(2, 0), result.get(2));
    }

}
