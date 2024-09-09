package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.UserMoveStrategy;
import ch.zhaw.pm2.racetrack.ui.TerminalUI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarTest {

    private PositionVector startingPosition = new PositionVector(1, 1);
    private PositionVector positionAfterMoveRight = new PositionVector(2, 1);
    private Car carA;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        // info: carA has no initial acceleration.
        carA = new Car('a', startingPosition);
    }

    /**
     * Tests Set Position
     */
    @Test
    void testSetPosition() {
        carA.setPosition(new PositionVector(2, 2));
        Assertions.assertEquals(carA.getPosition(), new PositionVector(2, 2));
    }

    /**
     * Tests Next Position
     */
    @Test
    void testNextPosition() {
        carA.accelerate(PositionVector.Direction.RIGHT);
        PositionVector nextPosition = carA.nextPosition();
        Assertions.assertEquals(nextPosition, positionAfterMoveRight);
    }

    /**
     * Tests Accelerate
     */
    @Test
    void testAccelerate() {
        carA.accelerate(PositionVector.Direction.RIGHT);
        Assertions.assertEquals(carA.getVelocity(), new PositionVector(1, 0));
    }

    /**
     * Tests Next Move
     */
    @Test
    void testMove() {
        carA.accelerate(PositionVector.Direction.RIGHT);
        carA.move();
        Assertions.assertEquals(carA.getPosition(), positionAfterMoveRight);
    }

    /**
     * Tests Crashed
     */
    @Test
    void testCrashed() {
        carA.crash();
        Boolean carAhasCrashed = carA.isCrashed();
        Assertions.assertTrue(carAhasCrashed);
    }

    /**
     * Tests Move Strategy
     */
    @Test
    void testMoveStrategy() {
        MoveStrategy moveStrategy = new UserMoveStrategy(new TerminalUI(new Config()));
        carA.setMoveStrategy(moveStrategy);
        MoveStrategy carAMoveStrategy = carA.getMoveStrategy();
        Assertions.assertEquals(carAMoveStrategy, moveStrategy);
    }

}
