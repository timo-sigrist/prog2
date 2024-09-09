package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.given.TrackSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the racetrack board.
 *
 * <p>The racetrack board consists of a rectangular grid of 'width' columns and 'height' rows.
 * The zero point of he grid is at the top left. The x-axis points to the right and the y-axis points downwards.</p>
 * <p>Positions on the track grid are specified using {@link PositionVector} objects. These are vectors containing an
 * x/y coordinate pair, pointing from the zero-point (top-left) to the addressed space in the grid.</p>
 *
 * <p>Each position in the grid represents a space which can hold an enum object of type {@link ConfigSpecification.SpaceType}.<br>
 * Possible Space types are:
 * <ul>
 *  <li>WALL : road boundary or off track space</li>
 *  <li>TRACK: road or open track space</li>
 *  <li>FINISH_LEFT, FINISH_RIGHT, FINISH_UP, FINISH_DOWN :  finish line spaces which have to be crossed
 *      in the indicated direction to winn the race.</li>
 * </ul>
 * <p>Beside the board the track contains the list of cars, with their current state (position, velocity, crashed,...)</p>
 *
 * <p>At initialization the track grid data is read from the given track file. The track data must be a
 * rectangular block of text. Empty lines at the start are ignored. Processing stops at the first empty line
 * following a non-empty line, or at the end of the file.</p>
 * <p>Characters in the line represent SpaceTypes. The mapping of the Characters is as follows:</p>
 * <ul>
 *   <li>WALL : '#'</li>
 *   <li>TRACK: ' '</li>
 *   <li>FINISH_LEFT : '&lt;'</li>
 *   <li>FINISH_RIGHT: '&gt;'</li>
 *   <li>FINISH_UP   : '^;'</li>
 *   <li>FINISH_DOWN: 'v'</li>
 *   <li>Any other character indicates the starting position of a car.<br>
 *       The character acts as the id for the car and must be unique.<br>
 *       There are 1 to {@link Config#MAX_CARS} allowed. </li>
 * </ul>
 *
 * <p>All lines must have the same length, used to initialize the grid width).
 * Beginning empty lines are skipped.
 * The the tracks ends with the first empty line or the file end.<br>
 * An {@link InvalidTrackFormatException} is thrown, if
 * <ul>
 *   <li>not all track lines have the same length</li>
 *   <li>the file contains no track lines (grid height is 0)</li>
 *   <li>the file contains more than {@link Config#MAX_CARS} cars</li>
 * </ul>
 *
 * <p>The Track can return a String representing the current state of the race (including car positons)</p>
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class Track implements TrackSpecification {

    public static final char CRASH_INDICATOR = 'X';
    private ConfigSpecification.SpaceType[][] trackArray;
    private Car[] cars;
    private final String nameOfTrack;

    /**
     * Initialize a Track from the given track file.
     *
     * @param trackFile Reference to a file containing the track data
     * @throws FileNotFoundException       if the given track file could not be found
     * @throws InvalidTrackFormatException if the track file contains invalid data (no tracklines, ...)
     */
    public Track(File trackFile) throws FileNotFoundException, InvalidTrackFormatException {
        nameOfTrack = trackFile.getName().replaceFirst("[.][^.]+", "");
        List<String> lines = readLinesFromFile(trackFile);
        List<Car> carsFromFile = new ArrayList<>();

        //initialize track array
        int rowCount = lines.size();
        int colCount = lines.get(0).length();
        trackArray = new ConfigSpecification.SpaceType[rowCount][colCount];

        int carCount = 0;
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < colCount; x++) {
                switch (lines.get(y).charAt(x)) {
                    case '#' -> {
                        trackArray[y][x] = ConfigSpecification.SpaceType.WALL;
                    }
                    case ' ' -> {
                        trackArray[y][x] = ConfigSpecification.SpaceType.TRACK;
                    }
                    case '^' -> {
                        trackArray[y][x] = ConfigSpecification.SpaceType.FINISH_UP;
                    }
                    case 'v' -> {
                        trackArray[y][x] = ConfigSpecification.SpaceType.FINISH_DOWN;
                    }
                    case '<' -> {
                        trackArray[y][x] = ConfigSpecification.SpaceType.FINISH_LEFT;
                    }
                    case '>' -> {
                        trackArray[y][x] = ConfigSpecification.SpaceType.FINISH_RIGHT;
                    }
                    default -> {
                        if (carCount <= Config.MAX_CARS) {
                            carsFromFile.add(new Car(lines.get(y).charAt(x), new PositionVector(x, y)));
                            carCount++;
                        } else {
                            throw new InvalidTrackFormatException();
                        }
                        trackArray[y][x] = ConfigSpecification.SpaceType.TRACK;
                    }

                }
            }
        }

        writeCarsToArray(carsFromFile);
    }

    /**
     * Writes cars to an array
     *
     * @param carsFromFile list of the cars from the file
     */
    private void writeCarsToArray(List<Car> carsFromFile) {
        int i = 0;
        cars = new Car[carsFromFile.size()];
        for (Car car : carsFromFile) {
            cars[i++] = car;
        }
    }

    /**
     * This Methode Reads the trackfile and returns it as an Arraylist
     *
     * @param trackFile File of the track to import
     * @return List with lines (String) of the importet FIle
     * @throws FileNotFoundException shows if a file was not found
     */
    private List<String> readLinesFromFile(File trackFile) throws FileNotFoundException {
        Scanner fileReader = new Scanner(trackFile, StandardCharsets.UTF_8.toString());
        List<String> lines = new ArrayList<>();

        while (fileReader.hasNextLine()) {
            lines.add(fileReader.nextLine());
        }

        fileReader.close();
        return lines;
    }

    /**
     * Return the type of space at the given position.
     * If the location is outside the track bounds, it is considered a wall.
     *
     * @param position The coordinates of the position to examine
     * @return The type of track position at the given location
     */
    @Override
    public ConfigSpecification.SpaceType getSpaceType(PositionVector position) {
        return trackArray[position.getY()][position.getX()];
    }

    /**
     * Return the number of cars.
     *
     * @return Number of cars
     */
    @Override
    public int getCarCount() {
        return cars.length;
    }

    /**
     * Get instance of specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return The car instance at the given index
     */
    @Override
    public Car getCar(int carIndex) {
        return cars[carIndex];
    }

    /**
     * Get the id of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    @Override
    public char getCarId(int carIndex) {
        return cars[carIndex].getCarId();
    }


    /**
     * Returns an index for given Car object
     *
     * @param car Car objects
     * @return The index of the given Car object as Integer
     */
    int getCarIndex(Car car) {
        int index = -1;
        for (int i = 0; i < cars.length; i++) {
            if (cars[i].equals(car)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Get the position of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    @Override
    public PositionVector getCarPos(int carIndex) {
        return cars[carIndex].getPosition();
    }

    /**
     * Get the velocity of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    @Override
    public PositionVector getCarVelocity(int carIndex) {
        return cars[carIndex].getVelocity();
    }

    /**
     * Gets character at the given position.
     * If there is a crashed car at the position, {@link #CRASH_INDICATOR} is returned.
     *
     * @param y            position Y-value
     * @param x            position X-vlaue
     * @param currentSpace char to return if no car is at position (x,y)
     * @return character representing position (x,y) on the track
     */
    @Override
    public char getCharAtPosition(int y, int x, ConfigSpecification.SpaceType currentSpace) {
        char output = currentSpace.getValue();
        for (int i = 0; i < cars.length; i++) {
            if (cars[i].getPosition().equals(new PositionVector(x, y))) {
                if (!cars[i].isCrashed()) {
                    output = cars[i].getCarId();
                } else {
                    output = CRASH_INDICATOR;
                }
            }
        }
        return output;
    }

    /**
     * This Methode gets a car for a position.
     *
     * @param position PositionVector to check
     * @return The car at the given Position. If no car at the position, this function returns null
     */
    Car getCarAtPosition(PositionVector position) {
        //Check for if car crashes into another car
        Car carAtPosition = null;
        for (Car currentCar : cars) {
            if (currentCar.getPosition().equals(position)) {
                carAtPosition = currentCar;
            }
        }
        return carAtPosition;
    }

    /**
     * Return a String representation of the track, including the car locations.
     *
     * @return A String representation of the track
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < trackArray.length; y++) {
            for (int x = 0; x < trackArray[y].length; x++) {
                stringBuilder.append(getCharAtPosition(y, x, trackArray[y][x]));
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    /**
     * Methode returns the cars taking part in the game as a list.
     *
     * @return List with Car objects taking part in the game
     */
    Car[] getCars() {
        return cars;
    }

    /**
     * Methode gets the remaining (not yet crashed) cars of the game.
     *
     * @return List with Car objects not yet crashed
     */
    List<Car> getRemainingCarsAsList() {
        List<Car> remainingCars = new ArrayList<>();
        for (Car currentCar : cars) {
            if (!currentCar.isCrashed()) {
                remainingCars.add(currentCar);
            }
        }
        return remainingCars;
    }

    /**
     * Methode returns the name of the current track.
     *
     * @return Name of track as String
     */
    String getNameOfTrack() {
        return nameOfTrack;
    }

    /**
     * Methode returns the trackArray of current track
     *
     * @return Array of track
     */
    ConfigSpecification.SpaceType[][] getTrackArray() {
        return trackArray;
    }
}
