package utils;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.SessionIDsGenerator;

public class SessionIDsGenerator_Test {

    private SessionIDsGenerator sessionIDsGen;
    private String ipAddress, username;

    private static String errorMessage = "Empty essential parameters";

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
        sessionIDsGen = new SessionIDsGenerator();
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        sessionIDsGen = null;
    }

    @Test
    public void test_checkSession_1User() {
        ipAddress = "123.123.123.123";
        username = "Hear me roar";
        String key = sessionIDsGen.registerSession( ipAddress, username );
        assertThat( sessionIDsGen.checkSession( key, ipAddress ), is( true ) );
    }

    @Test
    public void test_checkSession_3Users() {
        ipAddress = "123.123.123.123";
        username = "Hear me roar";
        String key = sessionIDsGen.registerSession( ipAddress, username );

        ipAddress = "234.234.234.234";
        username = "Mlada bulka";
        sessionIDsGen.registerSession( ipAddress, username );

        ipAddress = "345.345.345.345";
        username = "Krisko vliza";
        sessionIDsGen.registerSession( ipAddress, username );

        ipAddress = "123.123.123.123";
        assertThat( sessionIDsGen.checkSession( key, ipAddress ), is( true ) );
    }

    @Test
    public void test_checkSession_1User_2Registrations() {
        ipAddress = "123.123.123.123";
        username = "Hear me roar";
        String key = sessionIDsGen.registerSession( ipAddress, username );
        String newKey = sessionIDsGen.registerSession( ipAddress, username );
        assertThat( newKey, is( key ) );
    }

    @Test
    public void test_checkSession_NonExistingKey() {
        ipAddress = "123.123.123.123";
        username = "Hear me roar";
        sessionIDsGen.registerSession( ipAddress, username );
        String key = "123123asdf123123";
        assertThat( sessionIDsGen.checkSession( key, ipAddress ), is( false ) );
    }

    @Test
    public void test_checkSession_ExpiredSession() {
        ipAddress = "123.123.123.123";
        username = "Hear me roar";
        String key = sessionIDsGen.registerSession( ipAddress, username );

        sessionIDsGen.setMAX_DURATION( MILLISECONDS.convert( 1, SECONDS ) );

        try {
            Thread.sleep( 1200 );//1000 milliseconds is one second.
        } catch ( InterruptedException ex ) {
            Thread.currentThread().interrupt();
        }

        String newKey = sessionIDsGen.registerSession( ipAddress, username );

        sessionIDsGen.setMAX_DURATION( MILLISECONDS.convert( 30, MINUTES ) );

        assertThat( sessionIDsGen.checkSession( key, ipAddress ), is( false ) );
        assertThat( sessionIDsGen.checkSession( newKey, ipAddress ), is( true ) );
    }

    @Test
    public void test_checkSession_ExpiredSession2() {
        ipAddress = "123.123.123.123";
        username = "Hear me roar";
        String key = sessionIDsGen.registerSession( ipAddress, username );

        sessionIDsGen.setMAX_DURATION( MILLISECONDS.convert( 1, SECONDS ) );

        try {
            Thread.sleep( 1200 );//1000 milliseconds is one second.
        } catch ( InterruptedException ex ) {
            Thread.currentThread().interrupt();
        }

        assertThat( sessionIDsGen.checkSession( key, ipAddress ), is( false ) );
    }
    
    @Test
    public void test_registerSession_NullValues() {
        String key = sessionIDsGen.registerSession( ipAddress, username );
        assertThat( sessionIDsGen.registerSession( key, ipAddress ), is( errorMessage ) );
    }

    @Test
    public void test_checkSession_NullValues() {
        String key = sessionIDsGen.registerSession( ipAddress, username );
        assertThat( sessionIDsGen.checkSession( key, ipAddress ), is( false ) );
    }
}
