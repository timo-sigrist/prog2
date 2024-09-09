package ch.zhaw.pm2.racetrack;

/**
 * Holds a position (vector to x,y-position of the car on the track grid)
 * or a velocity vector (x,y-components of the velocity vector of a car).
 * <p>
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public final class PositionVector {
    private int x; // horizontal component (position / velocity)
    private int y; // vertical component (position / velocity)

    /**
     * Enum representing a direction on the track grid.
     * Also representing the possible acceleration values.
     */
    public enum Direction {
        DOWN_LEFT(new PositionVector(-1, 1)),
        DOWN(new PositionVector(0, 1)),
        DOWN_RIGHT(new PositionVector(1, 1)),
        LEFT(new PositionVector(-1, 0)),
        NONE(new PositionVector(0, 0)),
        RIGHT(new PositionVector(1, 0)),
        UP_LEFT(new PositionVector(-1, -1)),
        UP(new PositionVector(0, -1)),
        UP_RIGHT(new PositionVector(1, -1));

        public final PositionVector vector;

        Direction(final PositionVector v) {
            vector = v;
        }


        /**
         * This methode converts a String to an Enum.
         *
         * @param directionAsString String containing the direction
         * @return Direction
         */
        public static Direction convertStringToDirection(String directionAsString) {
            Direction output = Direction.NONE;
            for (Direction currentDirection : Direction.values()) {
                if (currentDirection.toString().equals(directionAsString)) {
                    output = currentDirection;
                }
            }
            return output;
        }

        /**
         * This methode converts a PositionVector to an Enum
         *
         * @param positionVector PositionVector containing the direction
         * @return Direction
         */
        public static Direction convertPositionVectorToDirection(PositionVector positionVector) {
            if (new PositionVector(-1, 1).equals(positionVector)) {
                return Direction.DOWN_LEFT;
            } else if (new PositionVector(0, 1).equals(positionVector)) {
                return Direction.DOWN;
            } else if (new PositionVector(1, 1).equals(positionVector)) {
                return Direction.DOWN_RIGHT;
            } else if (new PositionVector(-1, 0).equals(positionVector)) {
                return Direction.LEFT;
            } else if (new PositionVector(0, 0).equals(positionVector)) {
                return Direction.NONE;
            } else if (new PositionVector(1, 0).equals(positionVector)) {
                return Direction.RIGHT;
            } else if (new PositionVector(-1, -1).equals(positionVector)) {
                return Direction.UP_LEFT;
            } else if (new PositionVector(0, -1).equals(positionVector)) {
                return Direction.UP;
            } else if (new PositionVector(1, -1).equals(positionVector)) {
                return Direction.UP_RIGHT;
            }
            throw new IllegalArgumentException("Illegal String for Direction --> " + positionVector);

        }
    }

    /**
     * Adds two PositionVectors (e.g. car position and velocity vector or two velocity vectors).
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return A new PositionVector holding the result of the addition. If both
     * arguments are positions (not velocity), the result is mathematically
     * correct but meaningless.
     */
    public static PositionVector add(final PositionVector vectorA, final PositionVector vectorB) {
        return new PositionVector(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY());
    }

    /**
     * Subtracts two PositionVectors (e.g. car position and velocity vector or two velocity vectors).
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return A new PositionVector holding the result of the addition. If both
     * arguments are positions (not velocity), the result is mathematically
     * correct but meaningless.
     */
    public static PositionVector subtract(final PositionVector vectorA, final PositionVector vectorB) {
        return new PositionVector(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY());
    }

    /**
     * Calculates the scalar product (Skalarprodukt) of two 2D vectors. The scalar product
     * multiplies the lengths of the parallel components of the vectors.
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return The scalar product (vectorA * vectorB). Since vectorA and
     * vectorB are PositionVectors, which hold only integer coordinates,
     * the resulting scalar product is an integer.
     */
    public static int scalarProduct(final PositionVector vectorA, final PositionVector vectorB) {
        return (vectorA.getY() * vectorB.getY()) + (vectorA.getX() * vectorB.getX());
    }

    /**
     * Constructor with x and y coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public PositionVector(final int x, final int y) {
        this.y = y;
        this.x = x;
    }

    /**
     * Constructor with PositionVector.
     *
     * @param other input-positionvector
     */
    public PositionVector(final PositionVector other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    /**
     * Constructor when no data provided.
     */
    public PositionVector() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * get x-coordinate from positionvector
     *
     * @return x-coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * get y-coordinate from positionvector
     *
     * @return y-coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Equal methode for Positionvector comparison.
     * This equals compares the x- and y-coordiantes.
     *
     * @param other positionvector to compare
     * @return if x- and y-coordinates are equals with given positionvector
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof PositionVector)) throw new ClassCastException();
        final PositionVector otherPositionVector = (PositionVector) other;
        return this.y == otherPositionVector.getY() && this.x == otherPositionVector.getX();
    }

    /**
     * Create haashcode from positionvector
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return this.x ^ this.y;
    }

    /**
     * ToString methode.
     *
     * @return string of positionvector
     */
    @Override
    public String toString() {
        return "(X:" + this.x + ", Y:" + this.y + ")";
    }

}
