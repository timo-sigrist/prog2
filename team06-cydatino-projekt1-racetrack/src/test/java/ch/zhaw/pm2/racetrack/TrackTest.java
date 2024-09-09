package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class TrackTest {

    Track challenge;
    Track ovalAnticlockRight;
    Track ovalClockUp;
    Track quarterMile;

    /**
     * setup for tests
     */
    @BeforeEach
    void setUp() {
        try {
            challenge = new Track(new File("tracks/challenge.txt"));
            ovalAnticlockRight = new Track(new File("tracks/oval-anticlock-right.txt"));
            ovalClockUp = new Track(new File("tracks/oval-clock-up.txt"));
            quarterMile = new Track(new File("tracks/quarter-mile.txt"));

        } catch (FileNotFoundException | InvalidTrackFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * tests Challange Track
     */
    @Test
    void testSetupChallengeTrack() {
        // Tests spacetypes
        assertEquals(ConfigSpecification.SpaceType.TRACK, challenge.getSpaceType(new PositionVector(26,23)));
        assertEquals(ConfigSpecification.SpaceType.WALL, challenge.getSpaceType(new PositionVector(6,8)));
        assertEquals(ConfigSpecification.SpaceType.FINISH_RIGHT, challenge.getSpaceType(new PositionVector(22,23)));
        // Test Carposition
        assertEquals(new PositionVector(24,22), challenge.getCarPos(0));
        assertEquals(new PositionVector(24,24), challenge.getCarPos(1));
        // Test Car-Velocity
        assertEquals(new PositionVector(0,0), challenge.getCarVelocity(0));
        assertEquals(new PositionVector(0,0), challenge.getCarVelocity(1));
        // Test Car-Count
        assertEquals(2, challenge.getCarCount());
    }

    /**
     * tests Oval Anticlock Right Track
     */
    @Test
    void testSetupOvalAnticlockRightTrack() {
        // Tests spacetypes
        assertEquals(ConfigSpecification.SpaceType.TRACK, ovalAnticlockRight.getSpaceType(new PositionVector(13,5)));
        assertEquals(ConfigSpecification.SpaceType.WALL, ovalAnticlockRight.getSpaceType(new PositionVector(45,3)));
        assertEquals(ConfigSpecification.SpaceType.FINISH_RIGHT, ovalAnticlockRight.getSpaceType(new PositionVector(20,10)));
        // Test Carposition
        assertEquals(new PositionVector(22,9), ovalAnticlockRight.getCarPos(0));
        assertEquals(new PositionVector(22,11), ovalAnticlockRight.getCarPos(1));
        // Test Car-Velocity
        assertEquals(new PositionVector(0,0), ovalAnticlockRight.getCarVelocity(0));
        assertEquals(new PositionVector(0,0), ovalAnticlockRight.getCarVelocity(1));
        // Test Car-Count
        assertEquals(2, ovalAnticlockRight.getCarCount());

    }

    /**
     * tests Oval Clock Up Track
     */
    @Test
    void testSetupOvalClockUpTrack() {
        // Tests spacetypes
        assertEquals(ConfigSpecification.SpaceType.TRACK, ovalClockUp.getSpaceType(new PositionVector(28,3)));
        assertEquals(ConfigSpecification.SpaceType.WALL, ovalClockUp.getSpaceType(new PositionVector(7,10)));
        assertEquals(ConfigSpecification.SpaceType.FINISH_UP, ovalClockUp.getSpaceType(new PositionVector(9, 6)));
        // Test Carposition
        assertEquals(new PositionVector(8,5), ovalClockUp.getCarPos(0));
        assertEquals(new PositionVector(12,5), ovalClockUp.getCarPos(1));
        // Test Car-Velocity
        assertEquals(new PositionVector(0,0), ovalClockUp.getCarVelocity(0));
        assertEquals(new PositionVector(0,0), ovalClockUp.getCarVelocity(1));
        // Test Car-Count
        assertEquals(2, ovalClockUp.getCarCount());
    }

    /**
     * tests Quartermile Track
     */
    @Test
    void testSetupQuartermileTrack() {
        // Tests spacetypes
        assertEquals(ConfigSpecification.SpaceType.TRACK, quarterMile.getSpaceType(new PositionVector(34,5)));
        assertEquals(ConfigSpecification.SpaceType.WALL, quarterMile.getSpaceType(new PositionVector(26,1)));
        assertEquals(ConfigSpecification.SpaceType.FINISH_LEFT, quarterMile.getSpaceType(new PositionVector(10, 4)));
        // Test Carposition with special chars
        assertEquals(new PositionVector(56,3), quarterMile.getCarPos(0));
        assertEquals(new PositionVector(56,5), quarterMile.getCarPos(1));

        // Test Car-Velocity
        assertEquals(new PositionVector(0,0), quarterMile.getCarVelocity(0));
        assertEquals(new PositionVector(0,0), quarterMile.getCarVelocity(1));
        // Test Car-Count
        assertEquals(2, quarterMile.getCarCount());
    }

    /**
     * tests Car Index To CarId
     */
    @Test
    void testCarIndexToCarId() {
        assertEquals('a',challenge.getCarId(0));
        assertEquals('b',challenge.getCarId(1));
    }

    @Test
    void testGetNameOfTrack() {
        assertEquals("challenge", challenge.getNameOfTrack());
        assertEquals("oval-anticlock-right", ovalAnticlockRight.getNameOfTrack());
        assertEquals("oval-clock-up", ovalClockUp.getNameOfTrack());
        assertEquals("quarter-mile", quarterMile.getNameOfTrack());
    }

    @Test
    void testGetTrackArray() {
        assertEquals(ConfigSpecification.SpaceType.WALL, challenge.getTrackArray()[0][0]);
        assertEquals(ConfigSpecification.SpaceType.WALL, challenge.getTrackArray()[8][8]);
        assertEquals(ConfigSpecification.SpaceType.FINISH_RIGHT, challenge.getTrackArray()[24][22]);
    }

}
