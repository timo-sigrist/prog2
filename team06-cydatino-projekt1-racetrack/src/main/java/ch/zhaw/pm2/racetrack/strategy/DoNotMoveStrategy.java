package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.ui.Frontend;

import static ch.zhaw.pm2.racetrack.PositionVector.Direction;

/**
 * This strategy serves the purpose of not accelerating in any direction.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class DoNotMoveStrategy implements MoveStrategy {
    Frontend frontend;

    /**
     * Constructor which requires frontend
     *
     * @param frontend frontend as input
     */
    public DoNotMoveStrategy(Frontend frontend) {
        this.frontend = frontend;
    }

    /**
     * Get next move to accelerate
     *
     * @return Direction to accelerate - DoNotMove always returns NONE
     */
    @Override
    public Direction nextMove() {
        frontend.showDoNotMoveStrategyTurnText();
        return Direction.NONE;
    }
}
