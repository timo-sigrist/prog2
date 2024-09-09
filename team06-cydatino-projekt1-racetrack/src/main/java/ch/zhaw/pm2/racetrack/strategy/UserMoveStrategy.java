package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.GameManager;
import ch.zhaw.pm2.racetrack.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.ui.Frontend;

/**
 * This strategy gets a user-input and converts it to a direction.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class UserMoveStrategy implements MoveStrategy {

    private Frontend frontend;

    /**
     * Constructor which requires the frontend
     *
     * @param frontend frontend
     */
    public UserMoveStrategy(Frontend frontend) {
        this.frontend = frontend;
    }

    /**
     * Gets user-input and converts it to a direction. Also handles help, show track and quit.
     *
     * @return Direction to accelerate based on user-input.
     */
    @Override
    public Direction nextMove() {
        char userinput = frontend.getUserMoveStrategyAcceleration();
        if (Character.toString(userinput).matches("[1-9]")) {
            return inputToDirection(userinput);
        } else if (userinput == 'h') {
            GameManager.setShowHelp();
        } else if (userinput == 't') {
            GameManager.setShowTrack();
        } else if (userinput == 'q') {
            GameManager.setQuitGame();
        }
        return Direction.NONE;
    }

    /**
     * Converts char-input to direction.
     *
     * @param userinput user-input
     * @return direction based of user-input
     */
    Direction inputToDirection(char userinput) {
        return switch (userinput) {
            case '1' -> Direction.DOWN_LEFT;
            case '2' -> Direction.DOWN;
            case '3' -> Direction.DOWN_RIGHT;
            case '4' -> Direction.LEFT;
            case '5' -> Direction.NONE;
            case '6' -> Direction.RIGHT;
            case '7' -> Direction.UP_LEFT;
            case '8' -> Direction.UP;
            case '9' -> Direction.UP_RIGHT;
            default -> Direction.NONE;
        };
    }
}
