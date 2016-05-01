package mappers;

import entity.Duel;
import java.sql.Connection;
import java.util.logging.Logger;
import mapper.DuelMapper;
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
        duelMapper = new DuelMapper();

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
        duelMapper = null;

        performanceLogger = null;
        logger = null;

        dbConnection.closeConnection( connection, logger );
    }

    @Test
    public void test_getDuel_Error() {
        assertThat( duelMapper.getDuel( logger, connection, 1 ), is( ERROR_NOELEM ) );
    }

    @Test
    public void test_insertDuel() {
        //duelMapper.insertDuel( logger, connection, new Duel );
    }
}
