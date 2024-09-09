package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Car;
import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.PositionVector.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * This strategy loads a list with waypoints from a text-file, navigates the car through these waypoints.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class MoveListStrategy implements MoveStrategy {
    private final Queue<Direction> directionList;

    /**
     * Class constructor which requires config, car and map name
     *
     * @param config  config
     * @param car     car
     * @param mapName name of the map
     * @throws FileNotFoundException exception, if the move list file has not be found
     */
    public MoveListStrategy(Config config, Car car, String mapName) throws FileNotFoundException {
        Path path = Paths.get(config.getMoveDirectory() + File.separator + mapName + "-car-" + car.getCarId() + ".txt");
        File fileWithDirections = path.toFile();

        directionList = new LinkedList<>();

        Scanner fileReader = new Scanner(fileWithDirections, StandardCharsets.UTF_8.toString());
        while (fileReader.hasNextLine()) {
            directionList.add(Direction.convertStringToDirection(fileReader.nextLine()));
        }
        fileReader.close();
    }

    /**
     * Return the next waypoint as a direction
     *
     * @return next waypoint as direction
     */
    @Override
    public Direction nextMove() {
        return directionList.poll();
    }
}
