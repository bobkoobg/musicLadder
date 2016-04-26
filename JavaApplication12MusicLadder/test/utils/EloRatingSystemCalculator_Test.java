package utils;

import utilities.EloRatingSystemCalculator;
import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith( Parameterized.class )
public class EloRatingSystemCalculator_Test {

    private EloRatingSystemCalculator eloRSC;
    private float song1OldRating, song2OldRating;
    private int song1Score, song2Score;
    private float[] expectedPlayersNewRatings;

    public EloRatingSystemCalculator_Test( float song1OldRating, float song2OldRating,
            int song1Score, int song2Score, float[] expectedPlayersNewRatings ) {
        this.song1OldRating = song1OldRating;
        this.song2OldRating = song2OldRating;
        this.song1Score = song1Score;
        this.song2Score = song2Score;
        this.expectedPlayersNewRatings = expectedPlayersNewRatings;
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
        eloRSC = new EloRatingSystemCalculator();
    }

    /**
     * Triggered after each test case
     */
    @After
    public void tearDown() {
        eloRSC = null;
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList( new Object[][]{
            //Basic algorithm test
            { 1000.0f, 1000.0f, 10, 0, new float[]{ 1025.0f, 975.0f } },
            { 1000.0f, 1000.0f, 5, 5, new float[]{ 1000.0f, 1000.0f } },
            { 1000.0f, 1000.0f, 0, 10, new float[]{ 975.0f, 1025.0f } },
            //Rating low borders (1)
            { 1.0f, 1.0f, 10, 0, new float[]{ 26.0f, 1.0f } },
            { 1.0f, 1.0f, 0, 10, new float[]{ 1.0f, 26.0f } },
            { 1.0f, 1.0f, 5, 5, new float[]{ 1.0f, 1.0f } },
            //Below low rating (1) borders (only 1 participant)
            { 0.5f, 1.0f, 10, 0, new float[]{ 26.0f, 1.0f } },
            { 0.5f, 1.0f, 0, 10, new float[]{ 1.0f, 26.0f } },
            { 0.5f, 1.0f, 5, 5, new float[]{ 1.0f, 1.0f } },
            //Below low rating (2) borders (2 participants)
            { 0.5f, -34.0f, 10, 0, new float[]{ 26.0f, 1.0f } },
            { 0.5f, -34.0f, 0, 10, new float[]{ 1.0f, 26.0f } },
            { 0.5f, -34.0f, 5, 5, new float[]{ 1.0f, 1.0f } },
            //Rating high borders (10,000)
            { 10000.0f, 10000.0f, 10, 0, new float[]{ 10000.0f, 9975.0f } },
            { 10000.0f, 10000.0f, 0, 10, new float[]{ 9975.0f, 10000.0f } },
            { 10000.0f, 10000.0f, 5, 5, new float[]{ 10000.0f, 10000.0f } },
            //Above high rating (10,000) borders (only 1 participant)
            { 10100.0f, 10000.0f, 10, 0, new float[]{ 10000.0f, 9975.0f } },
            { 10100.0f, 10000.0f, 0, 10, new float[]{ 9975.0f, 10000.0f } },
            { 10100.0f, 10000.0f, 5, 5, new float[]{ 10000.0f, 10000.0f } },
            //Above high rating (10,000) borders (2 participants)
            { 10100.0f, 12000.0f, 10, 0, new float[]{ 10000.0f, 9975.0f } },
            { 10100.0f, 12000.0f, 0, 10, new float[]{ 9975.0f, 10000.0f } },
            { 10100.0f, 12000.0f, 5, 5, new float[]{ 10000.0f, 10000.0f } },
            //Basic algoritmn test based on ratings
            { 1012.5f, 1000.0f, 10, 0, new float[]{ 1036.879f, 975.621f } },
            { 1025.0f, 1000.0f, 10, 0, new float[]{ 1048.7664f, 976.23364f } },
            { 1050.0f, 1000.0f, 10, 0, new float[]{ 1072.5682f, 977.4318f } },
            { 1100.0f, 1000.0f, 10, 0, new float[]{ 1120.2914f, 979.7086f } },
            { 1150.0f, 1000.0f, 10, 0, new float[]{ 1168.1884f, 981.8116f } },
            { 1200.0f, 1000.0f, 10, 0, new float[]{ 1216.2676f, 983.7324f } },
            { 1300.0f, 1000.0f, 10, 0, new float[]{ 1304.8627f, 995.13727f } },
            { 1400.0f, 1000.0f, 10, 0, new float[]{ 1403.4613f, 996.53876f } },
            { 1500.0f, 1000.0f, 10, 0, new float[]{ 1502.502f, 997.49805f } },
            { 1600.0f, 1000.0f, 10, 0, new float[]{ 1601.8373f, 998.1627f } },
            { 1700.0f, 1000.0f, 10, 0, new float[]{ 1700.3292f, 999.6708f } },
            //Score low borders (0)
            //Below low score (0) borders (only 1 participant)
            { 1000.0f, 1000.0f, -5, 0, new float[]{ 1000.0f, 1000.0f } },
            { 1000.0f, 1000.0f, -5, 5, new float[]{ 975.0f, 1025.0f } },
            { 1000.0f, 1000.0f, -5, 10, new float[]{ 975.0f, 1025.0f } },
            //Below low score (0) borders (2 participants)
            { 1000.0f, 1000.0f, -6, -10, new float[]{ 1000.0f, 1000.0f } },
            { 1000.0f, 1000.0f, -6, -5, new float[]{ 1000.0f, 1000.0f } },
            { 1000.0f, 1000.0f, -6, -1, new float[]{ 1000.0f, 1000.0f } },
            //Above high score (10) borders (only 1 participant)
            { 1000.0f, 1000.0f, 10000, 0, new float[]{ 1025.0f, 975.0f } },
            { 1000.0f, 1000.0f, 1000, 5, new float[]{ 1010.0f, 990.0f } }, //evaluate to 10:5 then to 7:3
            { 1000.0f, 1000.0f, 1000, 10, new float[]{ 1000.0f, 1000.0f } }, //evaluate to 10:10 then to 5:5
            //Above high score (10) borders (2 participants)
            { 1000.0f, 1000.0f, 30, 15, new float[]{ 1000.0f, 1000.0f } },
            { 1000.0f, 1000.0f, 30, 30, new float[]{ 1000.0f, 1000.0f } },
            { 1000.0f, 1000.0f, 30, 45, new float[]{ 1000.0f, 1000.0f } },
            //Basic algoritmn test based on scores
            { 1000.0f, 1000.0f, 10, 0, new float[]{ 1025.0f, 975.0f } },
            { 1000.0f, 1000.0f, 9, 1, new float[]{ 1020.0f, 980.0f } },
            { 1000.0f, 1000.0f, 8, 2, new float[]{ 1015.0f, 985.0f } },
            { 1000.0f, 1000.0f, 7, 3, new float[]{ 1010.0f, 990.0f } },
            { 1000.0f, 1000.0f, 6, 4, new float[]{ 1005.0f, 995.0f } },
            { 1000.0f, 1000.0f, 5, 5, new float[]{ 1000.0f, 1000.0f } },
            { 1000.0f, 1000.0f, 4, 6, new float[]{ 995.0f, 1005.0f } },
            { 1000.0f, 1000.0f, 3, 7, new float[]{ 990.0f, 1010.0f } },
            { 1000.0f, 1000.0f, 2, 8, new float[]{ 985.0f, 1015.0f } },
            { 1000.0f, 1000.0f, 1, 9, new float[]{ 980.0f, 1020.0f } },
            { 1000.0f, 1000.0f, 0, 10, new float[]{ 975.0f, 1025.0f } },
            //Random
            { 1037.2383f, 987.1233f, 3, 7, new float[]{ 1024.7703f, 999.5913f } } } );
    }

    @Test
    public void test_calculate() {
        float[] result = eloRSC.calculate( song1OldRating, song2OldRating, song1Score, song2Score );
        assertThat( result, is( expectedPlayersNewRatings ) );
    }

}
