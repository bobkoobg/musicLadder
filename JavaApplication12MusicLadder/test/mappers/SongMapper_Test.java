package mappers;

import java.sql.Connection;
import java.util.logging.Logger;
import mapper.SongMapper;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.DatabaseConnector;
import utilities.PerformanceLogger;

public class SongMapper_Test {

    private SongMapper songMapper;

    private PerformanceLogger performanceLogger;
    private Logger logger;

    private DatabaseConnector dbConnection;
    private Connection connection;

    private static String databaseHost, databaseUsername, databasePassword;

    private String loggerName = "testChillMaster";
    private String loggerPath = "/MyTestLogFile.log";

    private static String ERROR_NOELEM = "Error - not element found";

    /**
     * Triggered once on class initialization
     */
    @BeforeClass
    public static void setUpClass() {
        databaseHost = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
        databaseUsername = "testingboy";
        databasePassword = "qwerty12345";
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
        songMapper = new SongMapper();

        performanceLogger = new PerformanceLogger();
        logger = performanceLogger.initLogger( loggerName, loggerPath );

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, null );
        connection = dbConnection.getConnection( logger );
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        songMapper.wipeDatabase( connection, logger );

        songMapper = null;

        dbConnection.closeConnection( connection, logger );

        performanceLogger = null;
        logger = null;
    }

    @Test
    public void test_getSong_NoElemWithID_Error() {
        int songId = 1;
        assertThat( songMapper.getSong( logger, connection, songId ), is( ERROR_NOELEM ) );
    }

    @Test
    public void test_isertSong_Success() {
        String name = "Vesela - 300 Noshti, 300 Dni";
        int result = songMapper.insertSong( logger, connection, name );
        int expectedSongId = songMapper.getSongID();
        assertThat( result, is( expectedSongId ) );
    }
}
