package utils;

import java.sql.Connection;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseConnector_Test {

    private DatabaseConnector dbConnection;
    private String databaseHost, databaseUsername, databasePassword;

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
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        dbConnection = null;
    }

    @Test
    public void test_getConnection_success() {
        databaseHost = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
        databaseUsername = "testingboy";
        databasePassword = "qwerty12345";

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, null );
        Connection connection = dbConnection.getConnection( null );

        assertThat( connection != null, is( true ) );
        dbConnection.closeConnection( connection, null );
    }

    @Test
    public void test_getConnection_failure() {
        databaseHost = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
        databaseUsername = "testingboy";
        databasePassword = "qwerty123456";

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, null );
        Connection connection = dbConnection.getConnection( null );

        assertThat( connection == null, is( true ) );
    }

    @Test
    public void test_getConnection_otherJDBCDriver_success() {
        databaseHost = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
        databaseUsername = "testingboy";
        databasePassword = "qwerty12345";

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, "oracle.jdbc.driver.OracleDriver" );
        Connection connection = dbConnection.getConnection( null );

        assertThat( connection != null, is( true ) );
        dbConnection.closeConnection( connection, null );
    }

    @Test
    public void test_getConnection_otherJDBCDriver_failure() {
        databaseHost = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
        databaseUsername = "testingboy";
        databasePassword = "qwerty12345";

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, "oracle.jdbc.driver.OracleDriver1" );
        Connection connection = dbConnection.getConnection( null );

        assertThat( connection == null, is( true ) );
    }

    @Test
    public void test_getConnection_otherJDBCDriver_failure2() {
        databaseHost = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
        databaseUsername = "testingboy";
        databasePassword = "qwerty12345";

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, "" );
        Connection connection = dbConnection.getConnection( null );

        assertThat( connection == null, is( true ) );
    }

    @Test
    public void test_closeConnection_failure() {
        databaseHost = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
        databaseUsername = "testingboy";
        databasePassword = "qwerty12345";

        dbConnection = new DatabaseConnector( databaseHost, databaseUsername, databasePassword, "" );
        Connection connection = dbConnection.getConnection( null );
        boolean isConnectionClosed = dbConnection.closeConnection( connection, null );

        assertThat( isConnectionClosed, is( false ) );
    }

}
