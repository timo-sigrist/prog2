package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.zhaw.pm2.racetrack.ui.TerminalUI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DoNotMoveStrategyTest {

    /**
     * tests input to direction
     */
    @Test
    void testMove() {
        Assertions.assertEquals(PositionVector.Direction.NONE, (new DoNotMoveStrategy(new TerminalUI(new Config()))).nextMove());
    }
}
