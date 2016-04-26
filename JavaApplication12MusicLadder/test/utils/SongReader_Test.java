package utils;

import java.io.File;
import utilities.SongReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static Map<String, List<String>> userPaths;
    private static String userBobkooUbuntu = "bobkooUbuntu";

    private static String errorMessage = "Incorrect path (directory).";

    public SongReader_Test( String path, Object expectedListSize ) {
        this.path = path;
        if ( expectedListSize instanceof Integer ) {
            this.expectedListSize = ( int ) expectedListSize;
        }
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
            { userPaths.get( userBobkooUbuntu ).get( 0 ), 100 },
            { userPaths.get( userBobkooUbuntu ).get( 1 ), 119 },
            { userPaths.get( userBobkooUbuntu ).get( 2 ), 183 },
            { userPaths.get( userBobkooUbuntu ).get( 3 ), 50 },
            { userPaths.get( userBobkooUbuntu ).get( 4 ), 108 },
            { userPaths.get( userBobkooUbuntu ).get( 5 ), 10 },
            { userPaths.get( userBobkooUbuntu ).get( 6 ), 91 },
            { userPaths.get( userBobkooUbuntu ).get( 7 ), 47 },
            //Errors
            { userPaths.get( userBobkooUbuntu ).get( 8 ), 0 },
            { userPaths.get( userBobkooUbuntu ).get( 9 ), errorMessage },
            { userPaths.get( userBobkooUbuntu ).get( 10 ), errorMessage }
        } );
    }

    @Test
    public void test_finder() {
        String stringResult;
        int fileArrayResultSize;
        Object actualResult = songReader.finder( path, null );

        if ( actualResult instanceof File[] ) {
            fileArrayResultSize = Arrays.asList( songReader.finder( path, null ) ).size();
            assertThat( fileArrayResultSize, is( expectedListSize ) );

        } else if ( actualResult instanceof String ) {
            stringResult = ( String ) songReader.finder( path, null );
            assertThat( stringResult, is( errorMessage ) );

        }
    }
}
