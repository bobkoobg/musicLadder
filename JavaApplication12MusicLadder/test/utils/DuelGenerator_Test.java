package utils;

import entity.Duel;
import entity.Song;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
public class DuelGenerator_Test {
    
    private DuelGenerator duelGen;
    private static List<Song> songs;
    private static List<Integer> expectedSongIds;
    
    public DuelGenerator_Test( List<Song> songs ) {
        this.songs = songs;
        
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
        duelGen = DuelGenerator.getInstance();
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        duelGen = null;
    }
    
    @Parameterized.Parameters
    public static Collection data() {
        songs = new ArrayList( Arrays.asList(
                //new Song( 1, 5, 2, 7, 965.50f ) == id, wins, draws, losses, rating
                new Song( 1, 0, 0, 0, 1010.0f ),
                new Song( 2, 0, 0, 0, 1020.0f ),
                new Song( 3, 0, 0, 0, 990.0f )
        ) );
        expectedSongIds = Arrays.asList( 1, 2, 3 );
        
        return Arrays.asList( new Object[][]{
            //Basic rating check
            { songs } 
        } );
    }
    
    @Test
    public void test_finder() {
        Duel duel = duelGen.generator( songs );
        System.out.println( "duel : " + duel.toString() );
        assertThat( expectedSongIds.contains( duel.getSong1ID() ), is( true ) );
        assertThat( expectedSongIds.contains( duel.getSong2ID() ), is( true ) );
    }
    
}
