package ch.zhaw.pm2.racetrack;

/**
 * The ConsoleApp starts the total racetrack game
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class ConsoleApp {
    /**
     * This is the entrypoint to the racetrack application
     *
     * @param args input arguments (are not used)
     */
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.setUp();
        gameManager.run();
    }
}
