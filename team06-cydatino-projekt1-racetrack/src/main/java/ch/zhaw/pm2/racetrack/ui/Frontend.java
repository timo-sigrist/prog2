package ch.zhaw.pm2.racetrack.ui;

import ch.zhaw.pm2.racetrack.Car;
import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

/**
 * This abstract class represents the front-end.
 * All front-end must extend this class.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public abstract class Frontend {
    private final Config config;

    /**
     * Constructor for class AbstractUI.
     *
     * @param config config for UI
     */
    public Frontend(Config config) {
        this.config = config;
    }

    /**
     * Return the current config of UI.
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Returns trackname.
     *
     * @return trackname
     */
    public abstract String getTrackName();

    /**
     * Refrehses map.
     *
     * @param text track as text
     */
    public abstract void updateTrack(String text);

    /**
     * Displays welcome-text.
     */
    public abstract void showWelcomeStrategyType();

    /**
     * Displays confirmation-text strategy for all cars selected.
     */
    public abstract void showStrategyTypesSelected();

    /**
     * Displays all strategies and returns user-input for chosen track.
     *
     * @param carId carId for display
     * @return selected StrategyType from user
     */
    public abstract ConfigSpecification.StrategyType getStrategyTypeForCar(char carId);

    /**
     * Displays confirmation-text for chosen strategy for car.
     *
     * @param moveStrategy choosen strategy for display
     * @param carId        carId
     */
    public abstract void updateCarStrategy(MoveStrategy moveStrategy, char carId);

    /**
     * Displays Help
     */
    public abstract void showHelp();

    /**
     * Displays current player.
     *
     * @param playerndex playerindex of current player
     * @param carId      carId from phasing player for display
     */
    public abstract void showCurrentPlayerText(int playerndex, char carId);

    /**
     * Displays turn-text for DoNotMoveStrategy.
     */
    public abstract void showDoNotMoveStrategyTurnText();

    /**
     * Displays turn-text for UserMoveStrategy and returns Userinput.
     *
     * @return Userinput
     */
    public abstract char getUserMoveStrategyAcceleration();

    /**
     * Displays the current velocity for the given car.
     *
     * @param car given car
     */
    public abstract void showCurrentVelocity(Car car);

    /**
     * Displays message that all cars have choosen the DO_NOT_MOVE strategy.
     */
    public abstract void showAllCarsDoNotMoveAlert();

    /**
     * Displays quit-message.
     */
    public abstract void showQuitMessage();

    /**
     * Displays victory-message.
     *
     * @param carId winnig carId
     */
    public abstract void showVictoryMessage(char carId);

    /**
     * Displays loose-message.
     */
    public abstract void showDrawMessage();

    /**
     * Shows error message.
     *
     * @param message error message
     */
    public abstract void showErrorMessage(String message);

    /**
     * Quits the game.
     */
    public abstract void quit();
}
