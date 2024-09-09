package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.given.GameSpecification;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.pm2.racetrack.PositionVector.Direction;

/**
 * This class represents the game controller, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if there is a winner.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class Game implements GameSpecification {
    public static final int NO_WINNER = -1;
    public static final int NO_WINNER_DRAW = -2;
    private final Track raceTrack;
    private int winner;
    private int currentCarIndex;

    /**
     * Constructor for game class
     *
     * @param raceTrack instance of racetrack class
     */
    public Game(Track raceTrack) {
        this.raceTrack = raceTrack;
        this.currentCarIndex = 0;
        this.winner = NO_WINNER;
    }

    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     *
     * @return The zero-based number of the current car
     */
    @Override
    public int getCurrentCarIndex() {
        return currentCarIndex;
    }

    /**
     * Get the id of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    @Override
    public char getCarId(int carIndex) {
        return raceTrack.getCarId(currentCarIndex);
    }

    /**
     * Get the position of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    @Override
    public PositionVector getCarPosition(int carIndex) {
        return raceTrack.getCarPos(currentCarIndex);
    }

    /**
     * Get the velocity of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    @Override
    public PositionVector getCarVelocity(int carIndex) {
        return raceTrack.getCarVelocity(currentCarIndex);
    }

    /**
     * Return the winner of the game. If the game is still in progress, returns NO_WINNER.
     *
     * @return The winning car's index (zero-based, see getCurrentCar()), or NO_WINNER if the game is still in progress
     */
    @Override
    public int getWinner() {
        return winner;
    }

    /**
     * Execute the next turn for the current active car.
     * <p>This method changes the current car's velocity and checks on the path to the next position,
     * if it crashes (car state to crashed) or passes the finish line in the right direction (set winner state).</p>
     * <p>The steps are as follows</p>
     * <ol>
     *   <li>Accelerate the current car</li>
     *   <li>Calculate the path from current (start) to next (end) position
     *       (see {@link Game#calculatePath(PositionVector, PositionVector)})</li>
     *   <li>Verify for each step what space type it hits:
     *      <ul>
     *          <li>TRACK: check for collision with other car (crashed &amp; don't continue), otherwise do nothing</li>
     *          <li>WALL: car did collide with the wall - crashed &amp; don't continue</li>
     *          <li>FINISH_*: car hits the finish line - wins only if it crosses the line in the correct direction</li>
     *      </ul>
     *   </li>
     *   <li>If the car crashed or wins, set its position to the crash/win coordinates</li>
     *   <li>If the car crashed, also detect if there is only one car remaining, remaining car is the winner</li>
     *   <li>Otherwise move the car to the end position</li>
     * </ol>
     * <p>The calling method must check the winner state and decide how to go on. If the winner is different
     * than {@link Game#NO_WINNER}, or the current car is already marked as crashed the method returns immediately.</p>
     *
     * @param acceleration A Direction containing the current cars acceleration vector (-1,0,1) in x and y direction
     *                     for this turn
     */
    @Override
    public void doCarTurn(Direction acceleration) {
        Car currentCar = raceTrack.getCar(currentCarIndex);

        //Accelerate the current car
        currentCar.accelerate(acceleration);

        //Check if car hits a wall or a winning line in between
        boolean carCrashedOrWon = false;
        for (PositionVector currentPosition : calculatePath(currentCar.getPosition(), currentCar.nextPosition())) {
            if (!carCrashedOrWon) {
                if (willCarCrash(currentCarIndex, currentPosition)) {
                    crashAndSetWinner(currentPosition, currentCar);
                    carCrashedOrWon = true;
                }
                if (willCarWin(currentCarIndex, currentPosition)) {
                    setWinner(currentPosition, currentCar);
                    carCrashedOrWon = true;
                }
            }
        }

        //Move the car
        if (!carCrashedOrWon) {
            currentCar.move();
        }

    }

    /**
     * Method handles a crash of a car and set a winner if there is only one car left.
     *
     * @param currentPosition position of the car crash
     * @param currentCar      Car that is responsible for the crash
     */
    private void crashAndSetWinner(PositionVector currentPosition, Car currentCar) {
        //Car crashes
        currentCar.crash();

        //if there is another car, it also crashes
        if (raceTrack.getCarAtPosition(currentPosition) != null)
            raceTrack.getCarAtPosition(currentPosition).crash();

        //Set new Position of the current car
        currentCar.setPosition(currentPosition);

        //Check if only one car left to declare a winner
        if (raceTrack.getRemainingCarsAsList().isEmpty()) winner = NO_WINNER_DRAW;

        //Check if only one car left to declare a winner
        if (raceTrack.getRemainingCarsAsList().size() == 1)
            winner = raceTrack.getCarIndex(raceTrack.getRemainingCarsAsList().get(0));
    }

    /**
     * Methode sets a winner and places the car at the position provided
     *
     * @param currentPosition Position at whitch the car ends
     * @param currentCar      Car that won the game
     */
    private void setWinner(PositionVector currentPosition, Car currentCar) {
        currentCar.setPosition(currentPosition);
        winner = currentCarIndex;
    }

    /**
     * Switches to the next car, which is still in the game. Skips crashed cars.
     */
    @Override
    public void switchToNextActiveCar() {
        currentCarIndex = (currentCarIndex + 1) % raceTrack.getCarCount();
    }

    /**
     * Returns all of the grid positions in the path between two positions, for use in determining line of sight.
     * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
     * (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
     * Basic steps are
     * - Detect which axis of the distance vector is longer (faster movement)
     * - for each pixel on the 'faster' axis calculate the position on the 'slower' axis.
     * Direction of the movement has to correctly considered
     *
     * @param startPosition Starting position as a PositionVector
     * @param endPosition   Ending position as a PositionVector
     * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
     */
    @Override
    public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
        PositionVector delta = PositionVector.subtract(endPosition, startPosition);

        int dx = delta.getX();
        int dy = delta.getY();

        int absDx = Math.abs(dx);
        int absDy = Math.abs(dy);
        int sigDx = (int) Math.signum(dx);
        int sigDy = (int) Math.signum(dy);

        int parallelDx;
        int parallelDy;
        int dxOfSlowDirection;
        int dxOfFastDirection;

        if (absDx > absDy) {
            parallelDx = sigDx;
            parallelDy = 0;
            dxOfSlowDirection = absDy;
            dxOfFastDirection = absDx;
        } else {
            parallelDx = 0;
            parallelDy = sigDy;
            dxOfSlowDirection = absDx;
            dxOfFastDirection = absDy;
        }

        int currentX = startPosition.getX();
        int currentY = startPosition.getY();
        int failure = dxOfFastDirection / 2;

        //Add start Postion
        List<PositionVector> pathPositions = new ArrayList<>();
        pathPositions.add(new PositionVector(currentX, currentY));

        //check for every position
        for (int i = 0; i < dxOfFastDirection; ++i) {

            //Update failure
            failure -= dxOfSlowDirection;

            if (failure < 0) {
                failure += dxOfFastDirection;
                currentX += sigDx;
                currentY += sigDy;
            } else {
                currentX += parallelDx;
                currentY += parallelDy;
            }

            //Add position
            pathPositions.add(new PositionVector(currentX, currentY));
        }

        return pathPositions;
    }

    /**
     * Does indicate if a car would have a crash with a WALL space or another car at the given position.
     *
     * @param carIndex The zero-based carIndex number
     * @param position A PositionVector of the possible crash position
     * @return A boolean indicator if the car would crash with a WALL or another car.
     */
    @Override
    public boolean willCarCrash(int carIndex, PositionVector position) {
        int x = position.getX();
        int y = position.getY();
        char charAtPosition = raceTrack.getCharAtPosition(y, x, raceTrack.getSpaceType(position));

        //Check for if car lands on an empty space a winning line and backwards through finish
        for (ConfigSpecification.SpaceType noCrashSpaceType : Config.getNoCrashTrackSpaceTypes()) {
            if ((charAtPosition == noCrashSpaceType.getValue() || charAtPosition == getCarId(carIndex)) && !carDriveBackwardsThroughFinish(getCarVelocity(carIndex), charAtPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the car drives backwards through the finish-line (for shortcut use probably).
     *
     * @param carVelocity    velocity of the current car
     * @param charAtPosition spacetype value to check
     * @return car drive backwards through finishline
     */
    boolean carDriveBackwardsThroughFinish(PositionVector carVelocity, char charAtPosition) {
        for (ConfigSpecification.SpaceType winType : Config.getWinTrackSpaceTypes()) {
            if (winType.value == charAtPosition) {
                return switch (winType) {
                    case FINISH_UP -> carVelocity.getY() > 0;
                    case FINISH_DOWN -> carVelocity.getY() < 0;
                    case FINISH_LEFT -> carVelocity.getX() > 0;
                    case FINISH_RIGHT -> carVelocity.getX() < 0;
                    default -> false;
                };
            }
        }
        return false;
    }

    /**
     * This methode checks if the car crosses the finish line at the given position.
     *
     * @param position A PositionVector of the possible crash position
     * @return A boolean indication if the car crosses the finish line
     */
    private boolean willCarWin(int carIndex, PositionVector position) {
        ConfigSpecification.SpaceType spaceTypeAtPosition = raceTrack.getSpaceType(position);
        //Check for if car lands on a winning line
        for (ConfigSpecification.SpaceType winSpaceType : Config.getWinTrackSpaceTypes()) {
            if (spaceTypeAtPosition.equals(winSpaceType) && !carDriveBackwardsThroughFinish(getCarVelocity(carIndex), spaceTypeAtPosition.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Methode returns a car object for a given index.
     *
     * @param carIndex index of a car as integer
     * @return Car object for the index provided
     */
    Car getCar(int carIndex) {
        return raceTrack.getCar(carIndex);
    }


    /**
     * Methode returns the racetrack as a string.
     *
     * @return String containing the racetrack
     */
    String getRaceTrackAsString() {
        return raceTrack.toString();
    }

    /**
     * Methode returns the track object.
     *
     * @return Current track object
     */
    Track getRaceTrack() {
        return raceTrack;
    }

}
