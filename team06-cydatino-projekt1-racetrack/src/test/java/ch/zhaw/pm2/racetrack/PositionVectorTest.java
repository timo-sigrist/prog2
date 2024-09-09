package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositionVectorTest {

    PositionVector a;
    PositionVector b;
    PositionVector c;

    /**
     * setup for tests
     */
    @BeforeEach
    void setUp() {
        a = new PositionVector(3, 5);
        b = new PositionVector(3, 5);
        c = new PositionVector(7, 8);
    }

    /**
     * Tests Equal Vectors
     */
    @Test
    void testEquals() {
        assertEquals(a, b);
    }

    /**
     * Tests Equals With HashMap
     */
    @Test
    void testEqualsWithHashMap() {
        Map<PositionVector, Integer> map = new HashMap<>();
        map.put(a, 1);
        assertTrue(map.containsKey(a), "Test with same object");
        assertTrue(map.containsKey(b), "Test with equal object");
    }

    @Test
    void testConvertPositionVectorToDirection() {
        assertEquals(PositionVector.Direction.DOWN_LEFT, PositionVector.Direction.convertStringToDirection("DOWN_LEFT"));
        assertEquals(PositionVector.Direction.DOWN, PositionVector.Direction.convertStringToDirection("DOWN"));
        assertEquals(PositionVector.Direction.DOWN_RIGHT, PositionVector.Direction.convertStringToDirection("DOWN_RIGHT"));
        assertEquals(PositionVector.Direction.LEFT, PositionVector.Direction.convertStringToDirection("LEFT"));
        assertEquals(PositionVector.Direction.NONE, PositionVector.Direction.convertStringToDirection("NONE"));
        assertEquals(PositionVector.Direction.RIGHT, PositionVector.Direction.convertStringToDirection("RIGHT"));
        assertEquals(PositionVector.Direction.UP_LEFT, PositionVector.Direction.convertStringToDirection("UP_LEFT"));
        assertEquals(PositionVector.Direction.UP, PositionVector.Direction.convertStringToDirection("UP"));
        assertEquals(PositionVector.Direction.UP_RIGHT, PositionVector.Direction.convertStringToDirection("UP_RIGHT"));
    }

    @Test
    void testConvertStringToDirection() {
        assertEquals(PositionVector.Direction.DOWN_LEFT, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(-1, 1)));
        assertEquals(PositionVector.Direction.DOWN, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(0, 1)));
        assertEquals(PositionVector.Direction.DOWN_RIGHT, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(1, 1)));
        assertEquals(PositionVector.Direction.LEFT, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(-1, 0)));
        assertEquals(PositionVector.Direction.NONE, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(0, 0)));
        assertEquals(PositionVector.Direction.RIGHT, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(1, 0)));
        assertEquals(PositionVector.Direction.UP_LEFT, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(-1, -1)));
        assertEquals(PositionVector.Direction.UP, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(0, -1)));
        assertEquals(PositionVector.Direction.UP_RIGHT, PositionVector.Direction.convertPositionVectorToDirection(new PositionVector(1, -1)));
    }

    @Test
    void testScalarProduct() {
        assertEquals(61, PositionVector.scalarProduct(a,c));
    }

    @Test
    void testDefaultConstructor() {
        PositionVector position = new PositionVector();
        assertEquals(0, position.getX());
        assertEquals(0, position.getY());
    }

    @Test
    void testCopyConstructor() {
        PositionVector position = new PositionVector(a);
        assertEquals(3, position.getX());
        assertEquals(5, position.getY());
    }

    @Test
    void testToString() {
        assertEquals("(X:7, Y:8)", c.toString());
    }

}
