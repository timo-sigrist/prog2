package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathManagerTest {

    private List<PositionVector.Direction> resultlist1 = new ArrayList<>();
    private List<PositionVector.Direction> resultlist2 = new ArrayList<>();
    private List<PositionVector.Direction> resultlist3 = new ArrayList<>();

    @BeforeEach
    void setup() {
        resultlist1.add(PositionVector.Direction.NONE);
        resultlist2.add(PositionVector.Direction.DOWN_RIGHT);
        resultlist2.add(PositionVector.Direction.UP_LEFT);
        resultlist3.add(PositionVector.Direction.UP_LEFT);
        resultlist3.add(PositionVector.Direction.DOWN_RIGHT);
    }

    @Test
    void testConvertPathPointsToDirectionList() {
        List<PositionVector> positionVectors1 = new ArrayList<>();
        List<PositionVector> positionVectors2 = new ArrayList<>();

        positionVectors1.add(new PositionVector(0,0));
        assertEquals(resultlist1, PathManager.convertPathPointsToDirectionList(positionVectors1));

        positionVectors1.add(new PositionVector(1,1));
        assertEquals(resultlist2, PathManager.convertPathPointsToDirectionList(positionVectors1));

        positionVectors2.add(new PositionVector(0,0));
        positionVectors2.add(new PositionVector(-1,-1));
        assertEquals(resultlist3, PathManager.convertPathPointsToDirectionList(positionVectors2));
    }
}
