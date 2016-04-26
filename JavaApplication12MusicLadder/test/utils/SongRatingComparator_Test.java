package utils;

import utilities.SongRatingComparator;
import entity.Song;
import java.util.Arrays;
import java.util.Collection;
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
public class SongRatingComparator_Test {

    private SongRatingComparator songRC;
    private Song song1, song2;
    private int expectedResult;

    public SongRatingComparator_Test( Song song1, Song song2, int expectedResult ) {
        this.song1 = song1;
        this.song2 = song2;
        this.expectedResult = expectedResult;
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
        songRC = new SongRatingComparator();
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        songRC = null;
    }

    @Parameterized.Parameters
    public static Collection data() {

        return Arrays.asList( new Object[][]{
            //new Song( 1, 5, 2, 7, 965.50f ) == id, wins, draws, losses, rating
            //Basic rating check
            { new Song( 1, 0, 0, 0, 1000.1f ), new Song( 2, 0, 0, 0, 1000.2f ), 1 },
            { new Song( 1, 0, 0, 0, 1000.2f ), new Song( 2, 0, 0, 0, 1000.1f ), -1 },
            { new Song( 1, 0, 0, 0, 1000.0f ), new Song( 2, 0, 0, 0, 1000.0f ), 0 },
            //Equal rating, different match sum
            { new Song( 1, 1, 0, 0, 1000.0f ), new Song( 2, 0, 0, 0, 1000.0f ), 1 },
            { new Song( 1, 0, 0, 0, 1000.0f ), new Song( 2, 1, 0, 0, 1000.0f ), -1 },
            { new Song( 1, 1, 0, 0, 1000.0f ), new Song( 2, 1, 0, 0, 1000.0f ), 0 }
        } );
    }

    @Test
    public void test_compare() {
        int result = songRC.compare( song1, song2 );
        assertThat( result, is( expectedResult ) );
    }

}
