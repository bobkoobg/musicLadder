package utilities;

import java.io.File;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utilities.PerformanceLogger;

/*
 * PerformanceLogger jUnit Test class
 * Testing : 
 * 1) Init logger without existing log file
 * 2) Init logger with existing log file
*/

public class PerformanceLogger_Test {

    private PerformanceLogger performanceLogger;
    private String loggerName = "testChillMaster";
    private String loggerPath = "/MyTestLogFile.log";

    /**
     * Triggered once on class initialization
     */
    @BeforeClass
    public static void beforeClassBeforeAllTests() {
    }

    /**
     * Triggered once after the end of the tests
     */
    @AfterClass
    public static void afterClassAfterAllTests() {
    }

    /**
     * Triggered before each test case
     */
    @Before
    public void beforeEachTest() {
        performanceLogger = new PerformanceLogger();
    }

    /**
     * Triggered after each test case
     */
    @After
    public void afterEachTess() {
        performanceLogger = null;
    }

    @Test
    public void test_initLogger_WithoutLogFile() {
        File file = new File( System.getProperty( "user.dir" ) + loggerPath );
        file.delete();
        Logger logger = performanceLogger.initLogger( loggerName, loggerPath );
        assertThat( logger.getName(), is( loggerName ) );
    }

    @Test
    public void test_initLogger_WithLogFile() {
        Logger logger = performanceLogger.initLogger( loggerName, loggerPath );
        assertThat( logger.getName(), is( loggerName ) );
    }

}
