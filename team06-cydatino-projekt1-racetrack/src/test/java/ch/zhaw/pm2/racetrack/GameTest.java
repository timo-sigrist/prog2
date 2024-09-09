package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class GameTest {

    private Game game;

    /**
     * Setup for tests
     */
    @BeforeEach
    void setup() {
        game = StandardRaceSetup.getGameManagerAfterStandardSetup().getGame();
        StandardRaceSetup.printRace();
    }

    /**
     * Tests Current Car Index
     */
    @Test
    void testCurrentCarIndex() {
        Assertions.assertEquals(0, game.getCurrentCarIndex());
    }

    /**
     * Tests Car Id
     */
    @Test
    void testCarId() {
        Assertions.assertEquals('a', game.getCarId(0));
    }

    /**
     * Tests Car Position
     */
    @Test
    void testCarPosition() {
        Assertions.assertEquals(new PositionVector(24, 22), game.getCarPosition(0));
    }

    /**
     * Tests Car Velocity
     */
    @Test
    void testCarVelocity() {
        Assertions.assertEquals(new PositionVector(0, 0), game.getCarVelocity(0));
    }

    /**
     * Tests Winner
     */
    @Test
    void testWinner() {
        Assertions.assertEquals(Game.NO_WINNER, game.getWinner());
        game.getCar(0).setPosition(new PositionVector(22,22));
        game.doCarTurn(PositionVector.Direction.UP);
        Assertions.assertEquals(game.getCurrentCarIndex(), game.getWinner());
    }

    /**
     * Tests Winner
     */
    @Test
    void testWinnerIfAllOtherCarsCrashed() {
        game.doCarTurn(StandardRaceSetup.crashCarIntoWall());
        game.switchToNextActiveCar();
        Assertions.assertEquals(game.getCurrentCarIndex(), game.getWinner());
    }

    /**
     * Tests car that crashes into a Wall
     */
    @Test
    void testCarCrashIntoWall() {
        game.doCarTurn(StandardRaceSetup.crashCarIntoWall());
        Assertions.assertTrue(game.getCar(game.getCurrentCarIndex()).isCrashed());
    }

    /**
     * Tests Do Car Turn
     */
    @Test
    void testDoCarTurn() {
        game.doCarTurn(PositionVector.Direction.UP);
        // todo: implement test
    }

    /**
     * Tests car that crashes into another Car
     */
    @Test
    void testCarCrashIntoOtherCar() {
        game.doCarTurn(StandardRaceSetup.crashCarIntoEachOther());
        Assertions.assertTrue(game.getCar(game.getCurrentCarIndex()).isCrashed());
        game.switchToNextActiveCar();
        Assertions.assertTrue(game.getCar(game.getCurrentCarIndex()).isCrashed());
    }

    /**
     * Tests for draw
     */
    @Test
    void testDraw() {
        game.doCarTurn(StandardRaceSetup.crashCarIntoEachOther());
        Assertions.assertEquals(game.getWinner(), -2);
    }

    /**
     * Tests Drive Backwards Through Finish
     */
    @Test
    void carDriveBackwardsThroughFinishTest() {
        PositionVector carDriveUpVelocity = new PositionVector(0,-1);
        PositionVector carDriveDownVelocity = new PositionVector(0,1);
        PositionVector carDriveLeftVelocity = new PositionVector(-1,0);
        PositionVector carDriveRightVelocity = new PositionVector(1,0);

        //Finish_up
        Assertions.assertFalse(
            game.carDriveBackwardsThroughFinish(carDriveUpVelocity,ConfigSpecification.SpaceType.FINISH_UP.value));
        Assertions.assertTrue(
            game.carDriveBackwardsThroughFinish(carDriveDownVelocity,ConfigSpecification.SpaceType.FINISH_UP.value));

        //Finish_down
        Assertions.assertFalse(
            game.carDriveBackwardsThroughFinish(carDriveDownVelocity,ConfigSpecification.SpaceType.FINISH_DOWN.value));
        Assertions.assertTrue(
            game.carDriveBackwardsThroughFinish(carDriveUpVelocity,ConfigSpecification.SpaceType.FINISH_DOWN.value));

        //Finish_left
        Assertions.assertFalse(
            game.carDriveBackwardsThroughFinish(carDriveLeftVelocity,ConfigSpecification.SpaceType.FINISH_LEFT.value));
        Assertions.assertTrue(
            game.carDriveBackwardsThroughFinish(carDriveRightVelocity,ConfigSpecification.SpaceType.FINISH_LEFT.value));

        //Finish_right
        Assertions.assertFalse(
            game.carDriveBackwardsThroughFinish(carDriveRightVelocity,ConfigSpecification.SpaceType.FINISH_RIGHT.value));
        Assertions.assertTrue(
            game.carDriveBackwardsThroughFinish(carDriveLeftVelocity,ConfigSpecification.SpaceType.FINISH_RIGHT.value));
    }

    /**
     * Tests the calculate path algorithm
     */
    @Test
    void testCalculatePath() {
        List<PositionVector> expectedPositions = new ArrayList<>();
        List<PositionVector> positions = game.calculatePath(new PositionVector(1,1), new PositionVector(3,3));

        expectedPositions.add(new PositionVector(1,1));
        expectedPositions.add(new PositionVector(2,2));
        expectedPositions.add(new PositionVector(3,3));

        int index = 0;
        for(PositionVector position : positions) {
            Assertions.assertEquals(expectedPositions.get(index), position);
            index++;
        }
    }
}
