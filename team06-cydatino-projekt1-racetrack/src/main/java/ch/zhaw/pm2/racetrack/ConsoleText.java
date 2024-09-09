package ch.zhaw.pm2.racetrack;

/**
 * This class contains all texts for the console frontend.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public enum ConsoleText {
    TITLE("Welcome to 'Racetrack'"),
    SELECTTRACKFILE("Select track file"),
    WELCOMEMOVESTRATEGY("Please select your move strategy for your car"),
    SELECTMOVESTRATEGY("Move strategy for car ({0}):"),
    SELECTEDMOVESTRATEGY("{0} for car ({1}) was selected."),
    SELECTEDMOVESTRATEGIES("Move strategies for cars were selected."),
    HELP("Directions are based on the number pad:" + System.lineSeparator() +
        "7 8 9\t7=up-left\t8=up\t\t\t9=up-right" + System.lineSeparator() +
        "4 5 6\t4=left\t\t5=no acceleration\t6=right" + System.lineSeparator() +
        "1 2 3\t1=down-left\t2=down\t\t\t3=down-right" + System.lineSeparator() +
        System.lineSeparator() +
        "h for help" + System.lineSeparator() +
        "t to show track" + System.lineSeparator() +
        "q to quit game" + System.lineSeparator()),
    QUIT_MESSAGE("The game has been quitted"),
    VICTORY_MESSAGE("Car ({0}) has won the game"),
    DRAW_MESSAGE("No car left. Nobody won the game"),
    CURRENT_PLAYER_TEXT("Playing car {0}: {1}"),
    CURRENT_CAR_VELOCITY("Car {0} has a velocity of: {1}"),
    DO_NOT_MOVE_TURNTEXT("For this car was the DO_NOT_MOVE strategy selected. "),
    USER_TURNTEXT("Acceleration direction (h for help) (1, 2, 3, 4, 5, 6, 7, 8, 9, h, t ,q):"),
    USER_INPUTERROR("{0} is an invalid input!"),
    ALL_CARS_DONOTMOVESTRATEGY("All cars use DO_NOT_MOVE strategy. Game was quit."),
    ENTER_TO_QUIT("Hit 'ENTER' to quit"),
    FILENOTFOUND_EXC("File not found"),
    INVALIDFILE_EXC("The file is invalid"),
    INVALIDTRACKFORMAT_EXC("Track has an invalid format");

    private final String text;

    /**
     * Enum constructor which requires a text.
     *
     * @param text input text
     */
    ConsoleText(String text) {
        this.text = text;
    }

    /**
     * ToString methods - behavior by converting to string.
     *
     * @return returns the enum as string
     */
    @Override
    public String toString() {
        return text;
    }
}
