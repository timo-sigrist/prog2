package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the config, which is needed for the game set-up.
 * The config contains all tracks, winning spacetypes and crash-spacetypes.
 * It also loads needed files for some strategies.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class Config implements ConfigSpecification {

    // Directory containing the track files
    private File trackDirectory = new File("tracks");

    // Directory containing the track files
    private File moveDirectory = new File("moves");

    // Directory containing the follower files
    private File followerDirectory = new File("follower");

    /**
     * Returns directory with files for movelist-strategy.
     *
     * @return directory with movelist-strategy
     */
    public File getMoveDirectory() {
        return moveDirectory;
    }

    /**
     * Sets path for directory with files for movelist-strategy.
     *
     * @param moveDirectory path for directory
     */
    public void setMoveDirectory(File moveDirectory) {
        Objects.requireNonNull(moveDirectory);
        this.moveDirectory = moveDirectory;
    }

    /**
     * Returns directory with files with waypoints for follower-strategy.
     *
     * @return directory with follower-strategy files
     */
    public File getFollowerDirectory() {
        return followerDirectory;
    }

    /**
     * Sets path for directory with files for follower-strategy.
     *
     * @param followerDirectory path for directory
     */
    public void setFollowerDirectory(File followerDirectory) {
        Objects.requireNonNull(followerDirectory);
        this.followerDirectory = followerDirectory;
    }

    /**
     * Returns directory with all racetracks.
     *
     * @return directory with racetracks
     */
    public File getTrackDirectory() {
        return trackDirectory;
    }

    /**
     * Sets path for directory with all racetracks.
     *
     * @param trackDirectory path for directory
     */
    public void setTrackDirectory(File trackDirectory) {
        Objects.requireNonNull(trackDirectory);
        this.trackDirectory = trackDirectory;
    }

    /**
     * Returns a list with all possible finish-line-spacetypes.
     *
     * @return all finish-line-spacetypes
     */
    public static List<SpaceType> getWinTrackSpaceTypes() {
        List<SpaceType> winTrackSpaceTypes = new ArrayList<SpaceType>();
        winTrackSpaceTypes.add(SpaceType.FINISH_DOWN);
        winTrackSpaceTypes.add(SpaceType.FINISH_UP);
        winTrackSpaceTypes.add(SpaceType.FINISH_LEFT);
        winTrackSpaceTypes.add(SpaceType.FINISH_RIGHT);

        return winTrackSpaceTypes;
    }

    /**
     * Returns a list with all spacetypes which can be driven on without causing the car to crash.
     * This includes the track and all finish-line-types.
     *
     * @return list of all drivable spacetypes
     */
    public static List<SpaceType> getNoCrashTrackSpaceTypes() {
        List<SpaceType> noCrashTrackSpaceTypes = getWinTrackSpaceTypes();
        noCrashTrackSpaceTypes.add(SpaceType.TRACK);
        return noCrashTrackSpaceTypes;
    }

}
