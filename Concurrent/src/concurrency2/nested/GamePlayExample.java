package concurrency2.nested;

import java.util.Random;


public class GamePlayExample {
    private static final float ACCELERATION = 1.3f;

    private final Strategy strategy;  // member record type
    private final Playfield field;    // member class type
    private MoveStrategy moveStrategy;// abstract member class type (local / anonymous implementation)

    /**
     * Constructor to start game play
     * @param strategy strategy to use to calculate next turn
     */
    public GamePlayExample(Strategy strategy) {
        this.strategy = strategy;
        // initialize Playfield
        this.field = new Playfield(new Coordinates(0,0));
        // initialize move strategy
        selectMoveStrategy();
    }

    // helper method to initialize the chosen move strategy.
    private void selectMoveStrategy() {
        float[] directions = {-1.0f, 1.0f};
        Random random = new Random();

        // local inner class definition. Has access to outer class.
        // - Only accessible within the method.
        // - Has access to local variables (random, directions) and outer class members.
        class FallbackStrategy implements MoveStrategy {
            private final float maxDistance;
            public FallbackStrategy(float maxDistance) {
                this.maxDistance = maxDistance;
                System.out.printf("Using maximal step distance: %f%n", maxDistance);
            }
            public float nextStep() {
                float distance = random.nextFloat(maxDistance);
                float direction = directions[random.nextInt(directions.length)];
                return distance * direction;
            }
        }

        // create move strategy instance based on chosen strategy
        moveStrategy = switch (this.strategy) {
            // using the local class (multiple instances or different configs are possible)
            case DEFAULT -> new FallbackStrategy(1.5f);
            case FALLBACK -> new FallbackStrategy(1.0f);

            // using anonymous classes (only single instance)
            case STEADY -> new MoveStrategy() {
                public float nextStep() {
                    return 1.0f;
                }
            };

            // another anonymous class (only single instance possible).
            // - Has access to outer class (directions, ACCELERATE, ...)
            case ACCELERATE -> new MoveStrategy() {
                float distance = 1;
                public float nextStep() {
                    distance *= ACCELERATION;
                    float direction = directions[random.nextInt(directions.length)];
                    return distance * direction;
                }
            };
        };
    }

    public void nextTurn() {
        float distanceLongitude = moveStrategy.nextStep();
        float distanceLatitude = moveStrategy.nextStep();
        field.move(distanceLongitude, distanceLatitude);
        System.out.printf("Moved to position: %f / %f%n", field.position.longitude, field.position.latitude);
    }

    // Start with argument:
    //  STEADY for linear movement
    //  ACCELERATE for accelerating movement
    //  DEFAULT or empty for default (random max distance 1.5)
    //  FALLBACK or anything else for fallback (random max distance 1.0)

    public static void main(String[] args) {
        Strategy selectedStrategy = null;
        try {
            if (args.length > 0) {
                System.out.printf("Argument: %s%n", args[0]);
                selectedStrategy = Strategy.valueOf(args[0]);
                System.out.printf("Using %s strategy.%n", selectedStrategy);
            } else {
                System.out.println("No strategy selected. Using default.");
                selectedStrategy = Strategy.DEFAULT;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown strategy. Using fallback.");
            selectedStrategy = Strategy.FALLBACK;
        }
        GamePlayExample gamePlay = new GamePlayExample(selectedStrategy);
        for (int step = 1; step <= 10; step++) {
            gamePlay.nextTurn();
        }
    }

    /*
     * examples of static nested and inner member class definitions
     */
    // static nested class: GamePlayExample.Strategy
    public static enum Strategy { STEADY, ACCELERATE, DEFAULT, FALLBACK }

    // private member abstract class / interface used for anonymous classes (above)
    // - even interfaces can be declared private (only accessible within the outer class)
    private interface MoveStrategy {
        float nextStep();
    }

    // private member (inner) record
    // - only visible by members of outer class
    private record Coordinates (float longitude, float latitude) {}

    // private member (inner) class
    // - only visible by members of outer class
    private class Playfield {

        // current position of the player (using private member record)
        private Coordinates position;

        /**
         * Initialize the playfield with a specific start position
         * @param startPosition for player
         */
        public Playfield (Coordinates startPosition) {
            this.position = startPosition;
        }

        /**
         * Move the position by a distance in longitude and latitude
         * @param deltaLongitude distance to move in longitude
         * @param deltaLatitude distance to move in latitude
         */
        public void move(float deltaLongitude, float deltaLatitude) {
            // can access private types of outer class
            this.position = new Coordinates(
                position.longitude + deltaLongitude,
                position.latitude + deltaLatitude);
        }
        /**
         * What is the current position
         * @return Coordinates of the current position
         */
        public Coordinates getPosition() {
            return this.position;
        }

    }

}
