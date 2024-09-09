package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Car;
import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveListStrategyTest {

    /**
     * tests input to direction
     */
    @Test
    void testMove() throws FileNotFoundException {
        assertDoesNotThrow(() -> new MoveListStrategy(new Config(), new Car('a',new PositionVector(8,8)),"challenge"));
        assertEquals(PositionVector.Direction.RIGHT, new MoveListStrategy(new Config(), new Car('a',new PositionVector(8,8)),"challenge").nextMove());
    }
}


