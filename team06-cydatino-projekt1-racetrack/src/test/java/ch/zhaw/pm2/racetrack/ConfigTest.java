package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ConfigTest {

    private Config config;
    private File testFile;

    /**
     * Setup for tests
     */
    @BeforeEach
    void setup() {
        config = new Config();
        testFile = new File("test");
    }

    /**
     * Tests Move Directory
     */
    @Test
    void testMoveDirectory() {
        config.setMoveDirectory(testFile);
        File file = config.getMoveDirectory();
        Assertions.assertEquals(file, testFile);
    }

    /**
     * Tests Follower Directory
     */
    @Test
    void testFollowerDirectory() {
        config.setFollowerDirectory(testFile);
        File file = config.getFollowerDirectory();
        Assertions.assertEquals(file, testFile);
    }

    /**
     * Tests Track Directory
     */
    @Test
    void testTrackDirectory() {
        config.setTrackDirectory(testFile);
        File file = config.getTrackDirectory();
        Assertions.assertEquals(file, testFile);
    }

    /**
     * Tests Win Track Space Types
     */
    @Test
    void testWinTrackSpaceTypes() {
        List<ConfigSpecification.SpaceType> winTrackSpaceTypes = new ArrayList<>();
        winTrackSpaceTypes.add(ConfigSpecification.SpaceType.FINISH_DOWN);
        winTrackSpaceTypes.add(ConfigSpecification.SpaceType.FINISH_UP);
        winTrackSpaceTypes.add(ConfigSpecification.SpaceType.FINISH_LEFT);
        winTrackSpaceTypes.add(ConfigSpecification.SpaceType.FINISH_RIGHT);

        List<ConfigSpecification.SpaceType> winTrackSpaceTypesReal = Config.getWinTrackSpaceTypes();
        for (ConfigSpecification.SpaceType spaceType : winTrackSpaceTypesReal) {
            Assertions.assertTrue(winTrackSpaceTypes.contains(spaceType));
        }
    }

    /**
     * Tests No Crash Track Space Types
     */
    @Test
    void testNoCrashTrackSpaceTypes() {
        List<ConfigSpecification.SpaceType> noCrashTrackSpaceTypes = new ArrayList<>();
        noCrashTrackSpaceTypes.add(ConfigSpecification.SpaceType.FINISH_DOWN);
        noCrashTrackSpaceTypes.add(ConfigSpecification.SpaceType.FINISH_UP);
        noCrashTrackSpaceTypes.add(ConfigSpecification.SpaceType.FINISH_LEFT);
        noCrashTrackSpaceTypes.add(ConfigSpecification.SpaceType.FINISH_RIGHT);
        noCrashTrackSpaceTypes.add(ConfigSpecification.SpaceType.TRACK);

        List<ConfigSpecification.SpaceType> noCrashTrackSpaceTypesReal = Config.getNoCrashTrackSpaceTypes();
        for (ConfigSpecification.SpaceType spaceType : noCrashTrackSpaceTypesReal) {
            Assertions.assertTrue(noCrashTrackSpaceTypes.contains(spaceType));
        }
    }

}
