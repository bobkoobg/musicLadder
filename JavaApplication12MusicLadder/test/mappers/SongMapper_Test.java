package mappers;

import static com.sun.jmx.snmp.ThreadContext.contains;
import entity.Song;
import java.sql.Connection;
import java.util.List;
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

public class SongMapper_Test {

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
    private static String ERROR_QUERYEXEC = "Error - during query execution";

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
        duelMapper = new DuelMapper();

        performanceLogger = new PerformanceLogger();
        logger = performanceLogger.initLogger( loggerName, loggerPath );

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, null );
        connection = dbConnection.getConnection( logger );

        duelMapper.wipeDatabase( connection, logger );
        songMapper.wipeDatabase( connection, logger );
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {

        songMapper = null;

        dbConnection.closeConnection( connection, logger );

        performanceLogger = null;
        logger = null;
    }

    @Test
    public void test_getSong_Failure() {
        int songId = 1;
        assertThat( songMapper.getSong( logger, connection, songId ), is( ERROR_NOELEM ) );
    }

    @Test
    public void test_getSong_ConnectionFailure() {
        connection = null;
        int songId = 1;
        assertThat( songMapper.getSong( logger, connection, songId ), is( ERROR_QUERYEXEC ) );
    }

    @Test
    public void test_insertSong_Success() {
        String name = "Vesela - 300 Noshti, 300 Dni";
        int result = songMapper.insertSong( logger, connection, name );
        int expectedSongId = songMapper.getSongID();
        assertThat( result, is( expectedSongId ) );
    }

    @Test
    public void test_insertSong_Failure() {
        connection = null;
        String name = "Vesela - 300 Noshti, 300 Dni";
        boolean result = songMapper.insertSong( logger, connection, name );
        assertThat( result, is( false ) );
    }

    @Test
    public void test_insertSong_GetSongById_Success() {
        String name = "Vesela - 300 Noshti, 300 Dni";
        int result = songMapper.insertSong( logger, connection, name );
        int expectedSongId = songMapper.getSongID();

        Song song = songMapper.getSong( logger, connection, result );

        assertThat( song.getId(), is( expectedSongId ) );
        assertThat( song.getName(), is( name ) );
    }

    @Test
    public void test_insertSong_GetSongById_Failure() {
        String name = "Vesela - 300 Noshti, 300 Dni";
        songMapper.insertSong( logger, connection, name );
        int songId = songMapper.getSongID();

        connection = null;

        String result = songMapper.getSong( logger, connection, songId );

        assertThat( result, is( ERROR_QUERYEXEC ) );
    }

    @Test
    public void test_insertSong_UpdateSong_CheckUpdate_Success() {
        String name = "Vesela - 300 Noshti, 300 Dni";
        songMapper.insertSong( logger, connection, name );
        int expectedSongId = songMapper.getSongID();

        String songToBeUpdatedName = "D2 - Sto godini";
        Song songToBeUpdated = new Song( expectedSongId, songToBeUpdatedName );

        songMapper.updateSong( logger, connection, songToBeUpdated );

        Song song = songMapper.getSong( logger, connection, expectedSongId );

        assertThat( song.getId(), is( expectedSongId ) );
        assertThat( song.getName(), is( songToBeUpdatedName ) );
    }

    @Test( expected = NullPointerException.class )
    public void test_insertSong_UpdateSong_CheckUpdate_Failure() {
        String name = "Vesela - 300 Noshti, 300 Dni";
        songMapper.insertSong( logger, connection, name );
        int expectedSongId = songMapper.getSongID();

        String songToBeUpdatedName = "D2 - Sto godini";
        Song songToBeUpdated = new Song( expectedSongId, songToBeUpdatedName );

        connection = null;

        songMapper.updateSong( logger, connection, songToBeUpdated );
    }

    @Test
    public void test_insertSongs_getSongs_Success() {
        int[] songIds = new int[ 2 ];
        String[] songNames = { "Vesela - 300 Noshti, 300 Dni", "D2 - Sto godini" };
        songMapper.insertSong( logger, connection, songNames[ 0 ] );
        songIds[ 0 ] = songMapper.getSongID();

        songMapper.insertSong( logger, connection, songNames[ 1 ] );
        songIds[ 1 ] = songMapper.getSongID();

        List<Song> songs = songMapper.getSongs( logger, connection, 1 );

        int[] actualIds = new int[ 2 ];
        String[] actualSongNames = new String[ 2 ];
        for ( int i = 0; i < songs.size(); i++ ) {
            actualIds[ i ] = songs.get( i ).getId();
            actualSongNames[ i ] = songs.get( i ).getName();
        }

        //assertThat( actualSongNames, contains( songNames[ 0 ] ) );
    }
}
