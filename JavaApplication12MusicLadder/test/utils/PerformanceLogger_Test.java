package utils;

import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utilities.PerformanceLogger;

public class PerformanceLogger_Test {

    private PerformanceLogger performanceLogger;
    private String loggerName = "chillMaster";

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
        performanceLogger = new PerformanceLogger();
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        performanceLogger = null;
    }

    @Test
    public void test_checkSession_1User() {
        Logger logger = performanceLogger.logMessage();
        assertThat( logger.getName(), is( loggerName ) );
    }

}
