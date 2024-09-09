package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.strategy.*;
import ch.zhaw.pm2.racetrack.ui.Frontend;
import ch.zhaw.pm2.racetrack.ui.TerminalUI;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * This class represents the game-manager which manages the whole game including the game setup.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class GameManager {
    private static boolean showHelp = false;
    private static boolean showTrack = false;
    private static boolean quitGame = false;
    private final Config config;
    private final Frontend frontend;
    private static final Properties frontendProperties = new Properties();
    private final Game game;
    private boolean setupDone;
    private boolean allCarsHaveDoNotMoveStrategy;

    /**
     * Set static bool showHelp to true
     */
    public static void setShowHelp() {
        showHelp = true;
    }

    /**
     * Set static bool showTrack to true
     */
    public static void setShowTrack() {
        showTrack = true;
    }

    /**
     * Set static bool quitGame to true
     */
    public static void setQuitGame() {
        quitGame = true;
    }

    /**
     * Default constructor initiates the GameManager
     */
    public GameManager() {
        config = new Config();
        frontend = getFrontend(this.config);
        game = new Game(getTrack());
    }

    /**
     * Default constructor initiates the GameManager with parameters config and game.
     */
    public GameManager(Config config, Game game) {
        this.config = config;
        this.frontend = getFrontend(this.config);
        this.game = game;
    }

    /**
     * Runs the setup phase of the racetrack game.
     */
    void setUp() {
        allCarsHaveDoNotMoveStrategy = true;
        setCarStrategies();
        this.setupDone = true;
    }

    /**
     * Runs the main flow.
     */
    void run() {
        if (setupDone) {
            int winnerIndex = Game.NO_WINNER;

            while (!quitGame && winnerIndex == Game.NO_WINNER && !allCarsHaveDoNotMoveStrategy) {
                showTrack();
                int currentCarIndex = game.getCurrentCarIndex();
                Car currentCar = game.getCar(currentCarIndex);

                frontend.showCurrentPlayerText(currentCarIndex, currentCar.getCarId());
                frontend.showCurrentVelocity(currentCar);
                runCar(currentCar);

                winnerIndex = game.getWinner();
                game.switchToNextActiveCar();
            }

            if (allCarsHaveDoNotMoveStrategy) frontend.showAllCarsDoNotMoveAlert();
            if (quitGame) frontend.showQuitMessage();

            if (winnerIndex == Game.NO_WINNER_DRAW) {
                showTrack();
                frontend.showDrawMessage();
            } else {
                if (winnerIndex != Game.NO_WINNER) {
                    showTrack();
                    frontend.showVictoryMessage(game.getCar(winnerIndex).getCarId());
                }
            }

            frontend.quit();
        }
    }

    /**
     * This methode returns based off the properties a frontend instance.
     * If the properties are empty, a Terminalfrontend will be retured
     *
     * @param config config of current game
     * @return instance of frontend
     */
    Frontend getFrontend(Config config) {
        String frontendFromProperties = frontendProperties.getProperty("frontend.frontendtype", "terminal");
        return switch (frontendFromProperties) {
            case "terminal" -> new TerminalUI(config);
            default -> null;
        };
    }

    /**
     * Runs the main flow for a single car
     *
     * @param car actual car
     */
    private void runCar(Car car) {
        MoveStrategy strategy = car.getMoveStrategy();
        Direction nextMove;
        boolean moveIsMade = false;

        do {
            nextMove = strategy.nextMove();
            if (showHelp || showTrack) {
                handleAdditionalActions();
            } else {
                moveIsMade = true;
            }
        } while (!moveIsMade);
        game.doCarTurn(nextMove);
    }

    /**
     * Creates the track instance from a selected file in the UI
     *
     * @return created track instance
     */
    private Track getTrack() {
        Track raceTrack = null;
        boolean invalidFile = true;
        String pathDelimeter = "/";
        while (invalidFile) {
            try {
                Path path = Paths.get(config.getTrackDirectory() + pathDelimeter + frontend.getTrackName());
                raceTrack = new Track(path.toFile());
                invalidFile = false;
            } catch (InvalidTrackFormatException | FileNotFoundException e) {
                frontend.showErrorMessage(e.toString());
            }
        }
        return raceTrack;
    }

    /**
     * Gets the strategies from the user and sets them to the car
     */
    private void setCarStrategies() {
        frontend.showWelcomeStrategyType();
        for (int i = 0; i < game.getRaceTrack().getCarCount(); i++) {
            Car car = game.getRaceTrack().getCar(i);

            boolean invalidFile = true;
            MoveStrategy moveStrategy = null;
            while (invalidFile) {
                try {
                    ConfigSpecification.StrategyType strategyType = frontend.getStrategyTypeForCar(car.getCarId());
                    moveStrategy = handleStrategyTypeToMoveStrategy(strategyType, car);
                    invalidFile = false;
                } catch (FileNotFoundException e) {
                    frontend.showErrorMessage(ConsoleText.FILENOTFOUND_EXC.toString());
                }
            }

            if (!(moveStrategy instanceof DoNotMoveStrategy)) {
                allCarsHaveDoNotMoveStrategy = false;
            }
            car.setMoveStrategy(moveStrategy);
            frontend.updateCarStrategy(car.getMoveStrategy(), car.getCarId());
        }
    }

    /**
     * Handle strategy type to move strategy
     *
     * @param strategyType strategy type
     * @param car          actual car
     * @return move strategy
     * @throws FileNotFoundException
     */
    MoveStrategy handleStrategyTypeToMoveStrategy(ConfigSpecification.StrategyType strategyType, Car car) throws FileNotFoundException {
        Track raceTrack = game.getRaceTrack();
        MoveStrategy moveStrategy = null;
        switch (strategyType) {
            case DO_NOT_MOVE:
                moveStrategy = new DoNotMoveStrategy(frontend);
                break;
            case USER:
                moveStrategy = new UserMoveStrategy(frontend);
                break;
            case MOVE_LIST:
                moveStrategy = new MoveListStrategy(config, car, raceTrack.getNameOfTrack());
                break;
            case PATH_FOLLOWER:
                moveStrategy = new PathFollowerMoveStrategy(raceTrack.getTrackArray(), config, raceTrack.getNameOfTrack(), car.getPosition());
                ((PathFollowerMoveStrategy) moveStrategy).postConstruct();
                break;
            case PATH_FINDER:
                moveStrategy = new PathFinderMoveStrategy(raceTrack.getTrackArray());
                ((PathFinderMoveStrategy) moveStrategy).postConstruct(car.getPosition());
                break;
        }
        return moveStrategy;
    }

    /**
     * Handles the additional acitions in the user move strategy
     */
    private void handleAdditionalActions() {
        if (showHelp) showHelp();
        if (showTrack) showTrack();
        setShowHelpAndTrackFalse();
    }

    /**
     * Sets variables shopHelp and showTrack to false
     */
    private static void setShowHelpAndTrackFalse() {
        showHelp = false;
        showTrack = false;
    }

    /**
     * Shows the help on the frontend
     */
    private void showHelp() {
        frontend.showHelp();
    }

    /**
     * Shows the track
     */
    private void showTrack() {
        frontend.updateTrack(game.getRaceTrackAsString());
    }

    /**
     * Return the game
     *
     * @return game
     */
    Game getGame() {
        return game;
    }

    /**
     * Returns the frontend
     *
     * @return frontend
     */
    Frontend getFrontend() {
        return frontend;
    }
}
