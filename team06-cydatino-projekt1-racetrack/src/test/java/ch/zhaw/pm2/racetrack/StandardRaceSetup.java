package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.UserMoveStrategy;
import ch.zhaw.pm2.racetrack.ui.TerminalUI;

import java.io.File;
import java.io.FileNotFoundException;

public class StandardRaceSetup {

    private static GameManager gameManager;
    private static Game game;
    private static final PositionVector winningPosition = new PositionVector(7, 7);
    private static final PositionVector crashingPosition = new PositionVector(23, 2);
    private static final PositionVector.Direction winningAcceleration = PositionVector.Direction.UP;
    private static final PositionVector.Direction crashingAcceleration = PositionVector.Direction.UP;
    public static final char specialCarId = '$';

    /**
     * Static method to return a GameManager after an initial setup
     */
    public static GameManager getGameManagerAfterStandardSetup() {
        try {
            setUp();
            return gameManager;
        } catch (InvalidTrackFormatException | FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Setup for standard game
     */
    private static void setUp() throws InvalidTrackFormatException, FileNotFoundException {
        File trackFile = new File("tracks/challenge.txt");
        Track track = new Track(trackFile);
        Config config = new Config();
        game = new Game(track);

        gameManager = new GameManager(config, game);
        gameManager.getFrontend().updateTrack(game.getRaceTrackAsString());
        setCarStrategies(gameManager);
    }

    /**
     * set car strategies for standard game
     */
    private static void setCarStrategies(GameManager gameManager) {
        for (Car car : gameManager.getGame().getRaceTrack().getCars()) {
            car.setMoveStrategy(new UserMoveStrategy(new TerminalUI(new Config())));
            gameManager.getFrontend().updateCarStrategy(car.getMoveStrategy(), car.getCarId());
        }
        gameManager.getFrontend().showStrategyTypesSelected();
    }

    /**
     * print for manual test
     */
    public static void printRace() {
        System.out.println(game.getRaceTrackAsString());
    }

    public static PositionVector.Direction crashCarIntoEachOther() {
        game.getCar(0).setPosition(new PositionVector(8,5));
        game.getCar(1).setPosition(new PositionVector(9,5));
        return PositionVector.Direction.RIGHT;
    }

    public static PositionVector.Direction crashCarIntoWall() {
        game.getCar(0).setPosition(new PositionVector(6,5));
        return PositionVector.Direction.LEFT;
    }
}
