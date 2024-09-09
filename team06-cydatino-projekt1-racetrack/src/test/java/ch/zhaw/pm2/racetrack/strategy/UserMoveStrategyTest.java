package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.ui.TerminalUI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ch.zhaw.pm2.racetrack.strategy.UserMoveStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserMoveStrategyTest {

    UserMoveStrategy userMoveStrategy;

    /**
     * setup for tests
     */
    @BeforeEach
    void setUp() {
        userMoveStrategy = new UserMoveStrategy(new TerminalUI(new Config()));
    }

    /**
     * tests input to direction
     */
    @Test
    void testInputToDirection() {
        Assertions.assertEquals(PositionVector.Direction.DOWN_LEFT, userMoveStrategy.inputToDirection('1'));
        assertEquals(PositionVector.Direction.DOWN, userMoveStrategy.inputToDirection('2'));
        assertEquals(PositionVector.Direction.DOWN_RIGHT, userMoveStrategy.inputToDirection('3'));
        assertEquals(PositionVector.Direction.LEFT, userMoveStrategy.inputToDirection('4'));
        assertEquals(PositionVector.Direction.NONE, userMoveStrategy.inputToDirection('5'));
        assertEquals(PositionVector.Direction.RIGHT, userMoveStrategy.inputToDirection('6'));
        assertEquals(PositionVector.Direction.UP_LEFT, userMoveStrategy.inputToDirection('7'));
        assertEquals(PositionVector.Direction.UP, userMoveStrategy.inputToDirection('8'));
        assertEquals(PositionVector.Direction.UP_RIGHT, userMoveStrategy.inputToDirection('9'));
    }
}
