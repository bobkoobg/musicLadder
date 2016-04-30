package utilities;

import utilities.DuelGenerator;
import entity.Duel;
import entity.Song;
import java.util.ArrayList;
import java.util.Arrays;
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

public class DuelGenerator_Test {

    private DuelGenerator duelGen;
    private static List<Song> songs;
    private static List<Integer> expectedSongIds;

    private static String errorMessage = "The duel generator needs at least 3 songs.";

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
        duelGen = DuelGenerator.getInstance();
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        duelGen = null;
    }

    @Test
    public void test_finder_BasicTest() {
        songs = new ArrayList( Arrays.asList(
                //new Song( 1, 5, 2, 7, 965.50f ) == id, wins, draws, losses, rating
                new Song( 1, 0, 0, 0, 1010.0f ),
                new Song( 2, 0, 0, 0, 1020.0f ),
                new Song( 3, 0, 0, 0, 990.0f )
        ) );
        expectedSongIds = new ArrayList<Integer>( Arrays.asList( 1, 2, 3 ) );

        Duel duel = duelGen.generator( songs );

        assertThat( expectedSongIds.contains( duel.getSong1ID() ), is( true ) );
        expectedSongIds.remove( ( Integer ) duel.getSong1ID() );
        assertThat( expectedSongIds.contains( duel.getSong2ID() ), is( true ) );
    }

    @Test
    public void test_finder2_1000ValuesTest() {
        songs = new ArrayList( Arrays.asList(
                //new Song( 1, 5, 2, 7, 965.50f ) == id, wins, draws, losses, rating
                new Song( 1, 100, 0, 0, 1000.0f ),
                //new Song( 2, 90, 0, 0, 1000.0f ),
                new Song( 3, 80, 0, 0, 1000.0f ),
                new Song( 4, 70, 0, 0, 1000.0f ),
                new Song( 5, 60, 0, 0, 1000.0f ),
                new Song( 6, 50, 0, 0, 1000.0f ),
                new Song( 7, 40, 0, 0, 1000.0f ),
                new Song( 8, 30, 0, 0, 1000.0f ),
                new Song( 9, 20, 0, 0, 1000.0f ),
                new Song( 10, 10, 0, 0, 1000.0f ),
                //new Song( 11, 1, 0, 0, 1000.0f ),
                new Song( 12, 0, 0, 0, 1000.0f )
        ) );
        expectedSongIds = new ArrayList<Integer>( Arrays.asList( 1, 3, 4, 5, 6, 7, 8, 9, 10, 12 ) );

        Map<Integer, Integer> repetitions = new HashMap();

        List<Integer> currentChosenElems;
        for ( int i = 0; i < 10000; i++ ) {

            Duel duel = duelGen.generator( songs );

            assertThat( expectedSongIds.contains( duel.getSong1ID() ), is( true ) );
            expectedSongIds.remove( ( Integer ) duel.getSong1ID() );
            assertThat( expectedSongIds.contains( duel.getSong2ID() ), is( true ) );
            expectedSongIds.add( ( Integer ) duel.getSong1ID() );

            currentChosenElems = new ArrayList<Integer>( Arrays.asList( duel.getSong1ID(), duel.getSong2ID() ) );

            for ( int j = 0; j < currentChosenElems.size() - 1; j++ ) {
                if ( repetitions.containsKey( currentChosenElems.get( j ) ) ) {
                    repetitions.put( currentChosenElems.get( j ), (repetitions.get( currentChosenElems.get( j ) ) + 1) );
                } else {
                    repetitions.put( currentChosenElems.get( j ), 1 );
                }
            }
        }

        for ( Map.Entry entry : repetitions.entrySet() ) {
            System.out.println( entry.getKey() + ", " + entry.getValue() );
        }

        assertThat( (repetitions.get( 1 ) < repetitions.get( 3 )), is( true ) );
        assertThat( (repetitions.get( 1 ) >= repetitions.get( 3 )), is( false ) );
        assertThat( (repetitions.get( 3 ) < repetitions.get( 4 )), is( true ) );
        assertThat( (repetitions.get( 3 ) >= repetitions.get( 4 )), is( false ) );
        assertThat( (repetitions.get( 4 ) < repetitions.get( 5 )), is( true ) );
        assertThat( (repetitions.get( 4 ) >= repetitions.get( 5 )), is( false ) );
        assertThat( (repetitions.get( 5 ) < repetitions.get( 6 )), is( true ) );
        assertThat( (repetitions.get( 5 ) >= repetitions.get( 6 )), is( false ) );
        assertThat( (repetitions.get( 6 ) < repetitions.get( 7 )), is( true ) );
        assertThat( (repetitions.get( 6 ) >= repetitions.get( 7 )), is( false ) );
        assertThat( (repetitions.get( 7 ) < repetitions.get( 8 )), is( true ) );
        assertThat( (repetitions.get( 7 ) >= repetitions.get( 8 )), is( false ) );
        assertThat( (repetitions.get( 8 ) < repetitions.get( 9 )), is( true ) );
        assertThat( (repetitions.get( 8 ) >= repetitions.get( 9 )), is( false ) );
        assertThat( (repetitions.get( 9 ) < repetitions.get( 10 )), is( true ) );
        assertThat( (repetitions.get( 9 ) >= repetitions.get( 10 )), is( false ) );
        assertThat( (repetitions.get( 10 ) < repetitions.get( 12 )), is( true ) );
        assertThat( (repetitions.get( 10 ) >= repetitions.get( 12 )), is( false ) );
    }

    @Test
    public void test_finder_NotElems() {
        songs = new ArrayList( Arrays.asList(
                new Song( 1, 100, 0, 0, 1000.0f ),
                new Song( 3, 80, 0, 0, 1000.0f )
        ) );

        String response = duelGen.generator( songs );

        assertThat( response, is( errorMessage ) );
    }

    @Test
    public void test_finder_NotEnoughElems() {
        songs = new ArrayList( Arrays.asList() );

        String response = duelGen.generator( songs );

        assertThat( response, is( errorMessage ) );
    }

    @Test
    public void test_finder_Null() {
        String response = duelGen.generator( null );

        assertThat( response, is( errorMessage ) );
    }
}
