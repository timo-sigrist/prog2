package ch.zhaw.pm2.racetrack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Path manager to manage paths on the racetrack
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class PathManager {

    /**
     * Method to change path point into list of directions
     *
     * @param positionVectors path points
     * @return list of directions
     */
    public static List<PositionVector.Direction> convertPathPointsToDirectionList(List<PositionVector> positionVectors) {
        PositionVector tempVelocity = PositionVector.Direction.NONE.vector;
        PositionVector tempLocation = positionVectors.get(0);
        List<PositionVector.Direction> directionList = new ArrayList<>();

        for (int i = 1; i < positionVectors.size(); i++) {
            PositionVector vectorChange =
                PositionVector.subtract(positionVectors.get(i), PositionVector.add(tempVelocity, tempLocation));

            if (BigDecimal.valueOf(vectorChange.getY()).abs().compareTo(BigDecimal.ONE) > 0 ||
                BigDecimal.valueOf(vectorChange.getX()).abs().compareTo(BigDecimal.ONE) > 0) {
                PositionVector stoppVector = new PositionVector(tempVelocity.getX() * -1, tempVelocity.getY() * -1);

                PositionVector secondVectorChange = new PositionVector();

                if (BigDecimal.valueOf(vectorChange.getY()).abs().compareTo(BigDecimal.ONE) > 0) {
                    secondVectorChange = new PositionVector(tempVelocity.getX(), vectorChange.getY() > 0 ? vectorChange.getY() - 1 : vectorChange.getY() + 1);
                }

                if (BigDecimal.valueOf(vectorChange.getX()).abs().compareTo(BigDecimal.ONE) > 0) {
                    secondVectorChange = new PositionVector(vectorChange.getX() > 0 ? vectorChange.getX() - 1 : vectorChange.getX() + 1, tempVelocity.getY());
                }

                directionList.add(PositionVector.Direction.convertPositionVectorToDirection(stoppVector));
                directionList.add(PositionVector.Direction.convertPositionVectorToDirection(secondVectorChange));
                tempVelocity = secondVectorChange;
            } else {
                directionList.add(PositionVector.Direction.convertPositionVectorToDirection(vectorChange));
                tempVelocity = PositionVector.add(tempVelocity, vectorChange);
            }


            tempLocation = positionVectors.get(i);
        }
        PositionVector stoppVector = new PositionVector(0, 0);

        if (tempVelocity.getX() != 0) {
            stoppVector = PositionVector.add(stoppVector, new PositionVector(tempVelocity.getX() * -1, 0));
        }
        if (tempVelocity.getY() != 0) {
            stoppVector = PositionVector.add(stoppVector, new PositionVector(0, tempVelocity.getY() * -1));
        }
        directionList.add(PositionVector.Direction.convertPositionVectorToDirection(stoppVector));
        return directionList;
    }

}
