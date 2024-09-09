package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.strategy.*;
import ch.zhaw.pm2.racetrack.ui.TerminalUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class GameManagerTest {
  private GameManager gameManager;

    /**
     * Setup for tests
     */
    @BeforeEach
    void setUp() {
        gameManager = StandardRaceSetup.getGameManagerAfterStandardSetup();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests HandleStrategyTypeToMoveStrategy
     */
    @Test
    void testHandleStrategyTypeToMoveStrategy() throws FileNotFoundException {
        Car car = gameManager.getGame().getCar(0);
        for (ConfigSpecification.StrategyType strategyType : ConfigSpecification.StrategyType.values()) {
            switch (strategyType) {
                case DO_NOT_MOVE -> {
                    assertInstanceOf(DoNotMoveStrategy.class, gameManager.handleStrategyTypeToMoveStrategy(strategyType, car));
                }
                case USER -> {
                    assertInstanceOf(UserMoveStrategy.class, gameManager.handleStrategyTypeToMoveStrategy(strategyType, car));
                }
                case MOVE_LIST -> {
                    assertInstanceOf(MoveListStrategy.class, gameManager.handleStrategyTypeToMoveStrategy(strategyType, car));
                }
                case PATH_FOLLOWER -> {
                    assertInstanceOf(PathFollowerMoveStrategy.class, gameManager.handleStrategyTypeToMoveStrategy(strategyType, car));
                }
                case PATH_FINDER -> {
                    assertInstanceOf(PathFinderMoveStrategy.class, gameManager.handleStrategyTypeToMoveStrategy(strategyType, car));
                }
            }
        }
    }

    @Test
    void testGetFrontend()  {
        assertInstanceOf(TerminalUI.class, gameManager.getFrontend(new Config()));
    }

}
