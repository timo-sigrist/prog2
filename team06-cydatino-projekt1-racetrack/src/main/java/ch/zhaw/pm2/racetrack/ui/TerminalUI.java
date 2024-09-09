package ch.zhaw.pm2.racetrack.ui;

import ch.zhaw.pm2.racetrack.Car;
import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.ConsoleText;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import org.beryx.textio.TerminalProperties;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.text.MessageFormat;

/**
 * Terminal UI implementation for the RaceTrack game.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class TerminalUI extends Frontend {
    private final TextIO textIO = TextIoFactory.getTextIO();
    private final TextTerminal<?> textTerminal = textIO.getTextTerminal();
    private final TerminalProperties<?> properties = textTerminal.getProperties();

    /**
     * Color enum which contains all colors in the terminal UI
     */
    private enum Color {
        WHITE("white"),
        RED("red");

        public final String value;

        Color(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Constructor with needs a config instance.
     *
     * @param config actual config
     */
    public TerminalUI(Config config) {
        super(config);
        properties.setPromptColor(Color.WHITE.toString());
        properties.setInputColor(Color.WHITE.toString());
        textTerminal.println(ConsoleText.TITLE.toString());
    }

    /**
     * Return the name of the track file.
     *
     * @return track file name
     */
    @Override
    public String getTrackName() {
        return textIO.newStringInputReader()
            .withNumberedPossibleValues(getConfig().getTrackDirectory().list())
            .read(ConsoleText.SELECTTRACKFILE.toString());
    }

    /**
     * Prints the updated track version.
     *
     * @param text track as text
     */
    @Override
    public void updateTrack(String text) {
        textTerminal.println(text);
    }

    /**
     * Shows a welcome message to select the strategy type.
     */
    @Override
    public void showWelcomeStrategyType() {
        textTerminal.println(ConsoleText.WELCOMEMOVESTRATEGY + System.lineSeparator());
    }

    /**
     * Shows a confirmation message to confirm that the strategies were selected.
     */
    @Override
    public void showStrategyTypesSelected() {
        textTerminal.println(ConsoleText.SELECTEDMOVESTRATEGIES + System.lineSeparator());
    }

    /**
     * Gets a car strategy for a car id.
     *
     * @param carId carId for display
     * @return strategy type
     */
    @Override
    public ConfigSpecification.StrategyType getStrategyTypeForCar(char carId) {
        textTerminal.println(MessageFormat.format(ConsoleText.SELECTMOVESTRATEGY.toString(), carId));
        return textIO.newEnumInputReader(ConfigSpecification.StrategyType.class).read();
    }

    /**
     * Shows a confirmation message which strategy got selected by which car.
     *
     * @param moveStrategy choosen strategy for display
     * @param carId        car id
     */
    @Override
    public void updateCarStrategy(MoveStrategy moveStrategy, char carId) {
        textTerminal.println(
            System.lineSeparator()
                + MessageFormat.format(ConsoleText.SELECTEDMOVESTRATEGY.toString(), moveStrategy.getClass().getSimpleName(), carId)
                + System.lineSeparator()
        );
    }

    /**
     * Shows help.
     */
    @Override
    public void showHelp() {
        textTerminal.println(ConsoleText.HELP.toString());
    }

    /**
     * Shows which player's turn it is.
     *
     * @param playerIndex player index of current player
     * @param carId       carId from phasing player for display
     */
    @Override
    public void showCurrentPlayerText(int playerIndex, char carId) {
        textTerminal.println(MessageFormat.format(ConsoleText.CURRENT_PLAYER_TEXT.toString(), playerIndex, carId));
    }

    /**
     * Show do not move strategy turn text.
     */
    @Override
    public void showDoNotMoveStrategyTurnText() {
        textTerminal.println(
            System.lineSeparator()
                + ConsoleText.DO_NOT_MOVE_TURNTEXT
                + System.lineSeparator()
        );
    }

    /**
     * Get terminal-based user-input.
     *
     * @return user-input
     */
    @Override
    public char getUserMoveStrategyAcceleration() {
        char res = 0;
        boolean isValidInput = false;
        while (!isValidInput) {
            textTerminal.print(ConsoleText.USER_TURNTEXT.toString());
            res = textIO.newCharInputReader().read();
            if (Character.toString(res).matches("[1-9htq]")) {
                isValidInput = true;
            } else {
                textTerminal.println(MessageFormat.format(ConsoleText.USER_INPUTERROR.toString(), res));
            }
        }
        return res;
    }

    /**
     * Displays velocity of given car in terminal.
     *
     * @param car given car
     */
    @Override
    public void showCurrentVelocity(Car car) {
        textTerminal.println(
            System.lineSeparator()
                + MessageFormat.format(ConsoleText.CURRENT_CAR_VELOCITY.toString(), car.getCarId(), car.getVelocity())
                + System.lineSeparator()
        );
    }

    /**
     * Displays message that all cars have chosen DoNotMoveStrategy in terminal.
     */
    @Override
    public void showAllCarsDoNotMoveAlert() {
        textTerminal.println(
            System.lineSeparator()
                + ConsoleText.ALL_CARS_DONOTMOVESTRATEGY
                + System.lineSeparator()
        );
    }

    /**
     * Displays quit-message in terminal.
     */
    @Override
    public void showQuitMessage() {
        textTerminal.println(ConsoleText.QUIT_MESSAGE.toString());
    }

    /**
     * Displays win-message with winning car in terminal.
     *
     * @param carId winnig carId
     */
    @Override
    public void showVictoryMessage(char carId) {
        textTerminal.println(
            MessageFormat.format(ConsoleText.VICTORY_MESSAGE.toString(), carId)
        );
    }

    /**
     * Shows draw message.
     */
    @Override
    public void showDrawMessage() {
        textTerminal.println(ConsoleText.DRAW_MESSAGE.toString());
    }

    /**
     * Shows error message.
     */
    @Override
    public void showErrorMessage(String message) {
        properties.setPromptColor(Color.RED.toString());
        textTerminal.println(message);
        properties.setPromptColor(Color.WHITE.toString());
    }

    /**
     * Hit 'ENTER' to quit message with terminal dispose.
     */
    @Override
    public void quit() {
        textTerminal.print(ConsoleText.ENTER_TO_QUIT.toString());
        textIO.getTextTerminal().read(true);
        textIO.dispose();
    }
}
