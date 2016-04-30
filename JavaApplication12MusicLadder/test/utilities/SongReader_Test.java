package utilities;

import java.io.File;
import utilities.SongReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith( Parameterized.class )
public class SongReader_Test {

    private SongReader songReader;

    private String path;
    private int expectedListSize;
    private boolean toUseLogger;

    private static Map<String, List<String>> userPaths;
    private static String userBobkooUbuntu = "bobkooUbuntu";

    private static String errorMessage = "Incorrect path (directory).";

    private String loggerName = "testChillMaster";
    private String loggerPath = "/MyTestLogFile.log";

    public SongReader_Test( String path, Object expectedListSize, boolean toUseLogger ) {
        this.path = path;
        if ( expectedListSize instanceof Integer ) {
            this.expectedListSize = ( int ) expectedListSize;
        }
        this.toUseLogger = toUseLogger;
    }

    /**
     * Triggered once on class initialization
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Triggered once after the end of the tests
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Triggered before each test case
     */
    @Before
    public void setUp() {
        songReader = new SongReader();
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        songReader = null;
    }

    @Parameterized.Parameters
    public static Collection data() {
        userPaths = new HashMap<String, List<String>>();
        userPaths.put( userBobkooUbuntu, Arrays.asList(
                       "/media/bobkoo/SWAG/Music/Evening Chillout Dreaming Mix/",
                       "/media/bobkoo/SWAG/Music/Morning Uplift/",
                       "/media/bobkoo/SWAG/Music/Flashback OldSchool Mix/",
                       "/media/bobkoo/SWAG/Music/BG Rap/",
                       "/media/bobkoo/SWAG/Music/Chalga/",
                       "/media/bobkoo/SWAG/Music/Newborn/", //does not accept folder Music Stack (ok)
                       "/media/bobkoo/SWAG/Music/Newborn/Music Stack",
                       "/media/bobkoo/SWAG/Music/Trash Collection/",
                       "/media/bobkoo/SWAG/Music/",
                       "/media/bobkoo/SWAG/Music/asdasdf",
                       "",
                       "C:\\Users\\S\\Downloads\\cool stuff"
               ) );

        return Arrays.asList( new Object[][]{
            //Basic check of all my local folders (for mp3 and MP3 files)
            { userPaths.get( userBobkooUbuntu ).get( 0 ), 100, false },
            { userPaths.get( userBobkooUbuntu ).get( 1 ), 119, false },
            { userPaths.get( userBobkooUbuntu ).get( 2 ), 183, false },
            { userPaths.get( userBobkooUbuntu ).get( 3 ), 50, false },
            { userPaths.get( userBobkooUbuntu ).get( 4 ), 108, false },
            { userPaths.get( userBobkooUbuntu ).get( 5 ), 10, false },
            { userPaths.get( userBobkooUbuntu ).get( 6 ), 91, false },
            { userPaths.get( userBobkooUbuntu ).get( 7 ), 47, false },
            //Errors
            { userPaths.get( userBobkooUbuntu ).get( 8 ), 0, false },
            { userPaths.get( userBobkooUbuntu ).get( 9 ), errorMessage, false },
            { userPaths.get( userBobkooUbuntu ).get( 10 ), errorMessage, false },
            { userPaths.get( userBobkooUbuntu ).get( 8 ), 0, true },
            { userPaths.get( userBobkooUbuntu ).get( 8 ), 0, true }
        } );
    }

    @Test
    public void test_finder() {
        Logger logger = null;
        if ( toUseLogger ) {

            PerformanceLogger performanceLogger = new PerformanceLogger();
            logger = performanceLogger.initLogger( loggerName, loggerPath );
        }

        String stringResult;
        int fileArrayResultSize;
        Object actualResult = songReader.finder( path, logger );

        if ( actualResult instanceof File[] ) {
            fileArrayResultSize = Arrays.asList( songReader.finder( path, logger ) ).size();
            assertThat( fileArrayResultSize, is( expectedListSize ) );

        } else if ( actualResult instanceof String ) {
            stringResult = ( String ) songReader.finder( path, logger );
            assertThat( stringResult, is( errorMessage ) );

        }
    }
}
