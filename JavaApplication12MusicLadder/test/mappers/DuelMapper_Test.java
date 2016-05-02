package mappers;

import entity.Duel;
import java.sql.Connection;
import java.util.logging.Logger;
import mapper.DuelMapper;
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

public class DuelMapper_Test {

    private DuelMapper duelMapper;
    private SongMapper songMapper;

    private PerformanceLogger performanceLogger;
    private Logger logger;

    private DatabaseConnector dbConnection;
    private Connection connection;

    private static String databaseHost, databaseUsername, databasePassword;

    private String loggerName = "testChillMaster";
    private String loggerPath = "/MyTestLogFile.log";

    private static String ERROR_NOELEM = "Error - not element found";

    private int song1ID, song2ID;

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
        duelMapper = new DuelMapper();
        songMapper = new SongMapper();

        performanceLogger = new PerformanceLogger();
        logger = performanceLogger.initLogger( loggerName, loggerPath );

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, null );
        connection = dbConnection.getConnection( logger );

        duelMapper.wipeDatabase( connection, logger );
        songMapper.wipeDatabase( connection, logger );

        song1ID = songMapper.insertSong( logger, connection, "Krisko - Dqlkam bqlo" );
        song2ID = songMapper.insertSong( logger, connection, "Kilata - 100 kila gaidi" );
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {

        duelMapper = null;
        songMapper = null;

        dbConnection.closeConnection( connection, logger );

        performanceLogger = null;
        logger = null;

    }

    @Test
    public void test_getDuel_Failure() {
        assertThat( duelMapper.getDuel( logger, connection, 1 ), is( false ) );
    }

    @Test
    public void test_getDuel_ConnectionFailure() {
        connection = null;
        assertThat( duelMapper.getDuel( logger, connection, 1 ), is( false ) );
    }

    @Test
    public void test_insertDuel_Success() {
        int duelId = duelMapper.insertDuel( logger, connection, new Duel( song1ID, song2ID, 1000.0f, 1050.0f ) );
        int expectedDuelID = duelMapper.getDuelID();
        assertThat( duelId, is( expectedDuelID ) );
    }

    @Test
    public void test_insertDuel_Failure() {
        int duelId = duelMapper.insertDuel( logger, connection, new Duel( song1ID, song2ID, 1000.0f, 1050.0f ) );
        connection = null;
        boolean result = duelMapper.getDuel( logger, connection, duelId );

        assertThat( result, is( false ) );
    }

    @Test
    public void test_insertDuel_GetDuelById_Success() {
        int duelId = duelMapper.insertDuel( logger, connection, new Duel( song1ID, song2ID, 1000.0f, 1050.0f ) );
        Duel duel = duelMapper.getDuel( logger, connection, duelId );

        assertThat( duel.getDuelID(), is( duelId ) );
        assertThat( duel.getDuelID(), is( duelId ) );
    }

    @Test
    public void test_insertDuel_GetDuelById_Failure() {
        int duelId = duelMapper.insertDuel( logger, connection, new Duel( song1ID, song2ID, 1000.0f, 1050.0f ) );
        connection = null;
        boolean result = duelMapper.getDuel( logger, connection, duelId );

        assertThat( result, is( false ) );
    }

    @Test
    public void test_insertDuel_UpdateDuel_CheckUpdate_Success() {
        int duelId = duelMapper.insertDuel( logger, connection, new Duel( song1ID, song2ID, 1000.0f, 1050.0f ) );

        Duel duel = duelMapper.getDuel( logger, connection, duelId );
        duel.setSong1Score( 10 );
        duel.setSong2Score( 0 );
        duel.setSong1AfterMatchRating( 1025.0f );
        duel.setSong2AfterMatchRating( 1025.0f );

        duelMapper.updateDuel( logger, connection, duel );

        Duel updatedDuel = duelMapper.getDuel( logger, connection, duelId );

        assertThat( duelId, is( updatedDuel.getDuelID() ) );
        assertThat( updatedDuel.getSong1ID(), is( song1ID ) );
        assertThat( updatedDuel.getSong2ID(), is( song2ID ) );
        assertThat( updatedDuel.getSong1Score(), is( 10 ) );
        assertThat( updatedDuel.getSong2Score(), is( 0 ) );
        assertThat( updatedDuel.getSong1AfterMatchRating(), is( 1025.0f ) );
        assertThat( updatedDuel.getSong2AfterMatchRating(), is( 1025.0f ) );
    }

    @Test
    public void test_insertDuel_UpdateDuel_CheckUpdate_Failure() {
        int duelId = duelMapper.insertDuel( logger, connection, new Duel( song1ID, song2ID, 1000.0f, 1050.0f ) );

        Duel duel = duelMapper.getDuel( logger, connection, duelId );
        duel.setSong1Score( 10 );
        duel.setSong2Score( 0 );
        duel.setSong1AfterMatchRating( 1025.0f );
        duel.setSong2AfterMatchRating( 1025.0f );

        duelMapper.updateDuel( logger, connection, duel );

        connection = null;

        boolean result = duelMapper.getDuel( logger, connection, duelId );
        assertThat( result, is( false ) );
    }
}
