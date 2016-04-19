package test;

import entity.Duel;
import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.EloRatingSystemCalculator;

@RunWith( Parameterized.class )
public class EloRatingSystemCalculator_Test {

    private Duel duel;
    private float[] expectedPlayersNewRatings;

    public EloRatingSystemCalculator_Test( Duel duel, float[] expectedPlayersNewRatings ) {
        this.duel = duel;
        this.expectedPlayersNewRatings = expectedPlayersNewRatings;
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList( new Object[][]{
            //Basic algorithm test
            { new Duel( 1000.0f, 1000.0f, 10, 0 ), new float[]{ 1025.0f, 975.0f } },
            { new Duel( 1000.0f, 1000.0f, 5, 5 ), new float[]{ 1000.0f, 1000.0f } },
            { new Duel( 1000.0f, 1000.0f, 0, 10 ), new float[]{ 975.0f, 1025.0f } },
            //Rating low borders (1)
            { new Duel( 1.0f, 1.0f, 10, 0 ), new float[]{ 26.0f, 1.0f } },
            { new Duel( 1.0f, 1.0f, 0, 10 ), new float[]{ 1.0f, 26.0f } },
            { new Duel( 1.0f, 1.0f, 5, 5 ), new float[]{ 1.0f, 1.0f } },
            //Below low rating (1) borders (only 1 participant)
            { new Duel( 0.5f, 1.0f, 10, 0 ), new float[]{ 26.0f, 1.0f } },
            { new Duel( 0.5f, 1.0f, 0, 10 ), new float[]{ 1.0f, 26.0f } },
            { new Duel( 0.5f, 1.0f, 5, 5 ), new float[]{ 1.0f, 1.0f } },
            //Below low rating (2) borders (2 participants)
            { new Duel( 0.5f, -34.0f, 10, 0 ), new float[]{ 26.0f, 1.0f } },
            { new Duel( 0.5f, -34.0f, 0, 10 ), new float[]{ 1.0f, 26.0f } },
            { new Duel( 0.5f, -34.0f, 5, 5 ), new float[]{ 1.0f, 1.0f } },
            //Rating high borders (10,000)
            { new Duel( 10000.0f, 10000.0f, 10, 0 ), new float[]{ 10000.0f, 9975.0f } },
            { new Duel( 10000.0f, 10000.0f, 0, 10 ), new float[]{ 9975.0f, 10000.0f } },
            { new Duel( 10000.0f, 10000.0f, 5, 5 ), new float[]{ 10000.0f, 10000.0f } },
            //Above high rating (10,000) borders (only 1 participant)
            { new Duel( 10100.0f, 10000.0f, 10, 0 ), new float[]{ 10000.0f, 9975.0f } },
            { new Duel( 10100.0f, 10000.0f, 0, 10 ), new float[]{ 9975.0f, 10000.0f } },
            { new Duel( 10100.0f, 10000.0f, 5, 5 ), new float[]{ 10000.0f, 10000.0f } },
            //Above high rating (10,000) borders (2 participants)
            { new Duel( 10100.0f, 12000.0f, 10, 0 ), new float[]{ 10000.0f, 9975.0f } },
            { new Duel( 10100.0f, 12000.0f, 0, 10 ), new float[]{ 9975.0f, 10000.0f } },
            { new Duel( 10100.0f, 12000.0f, 5, 5 ), new float[]{ 10000.0f, 10000.0f } },
            //Basic algoritmn test based on ratings
            { new Duel( 1012.5f, 1000.0f, 10, 0 ), new float[]{ 1036.879f, 975.621f } },
            { new Duel( 1025.0f, 1000.0f, 10, 0 ), new float[]{ 1048.7664f, 976.23364f } },
            { new Duel( 1050.0f, 1000.0f, 10, 0 ), new float[]{ 1072.5682f, 977.4318f } },
            { new Duel( 1100.0f, 1000.0f, 10, 0 ), new float[]{ 1120.2914f, 979.7086f } },
            { new Duel( 1150.0f, 1000.0f, 10, 0 ), new float[]{ 1168.1884f, 981.8116f } },
            { new Duel( 1200.0f, 1000.0f, 10, 0 ), new float[]{ 1216.2676f, 983.7324f } },
            { new Duel( 1300.0f, 1000.0f, 10, 0 ), new float[]{ 1304.8627f, 995.13727f } },
            { new Duel( 1400.0f, 1000.0f, 10, 0 ), new float[]{ 1403.4613f, 996.53876f } },
            { new Duel( 1500.0f, 1000.0f, 10, 0 ), new float[]{ 1502.502f, 997.49805f } },
            { new Duel( 1600.0f, 1000.0f, 10, 0 ), new float[]{ 1601.8373f, 998.1627f } },
            { new Duel( 1700.0f, 1000.0f, 10, 0 ), new float[]{ 1700.3292f, 999.6708f } },
            //Score low borders (0)
            //Below low score (0) borders (only 1 participant)
            { new Duel( 1000.0f, 1000.0f, -5, 0 ), new float[]{ 1000.0f, 1000.0f } },
            { new Duel( 1000.0f, 1000.0f, -5, 5 ), new float[]{ 975.0f, 1025.0f } },
            { new Duel( 1000.0f, 1000.0f, -5, 10 ), new float[]{ 975.0f, 1025.0f } },
            //Below low score (0) borders (2 participants)
            { new Duel( 1000.0f, 1000.0f, -6, -10 ), new float[]{ 1000.0f, 1000.0f } },
            { new Duel( 1000.0f, 1000.0f, -6, -5 ), new float[]{ 1000.0f, 1000.0f } },
            { new Duel( 1000.0f, 1000.0f, -6, -1 ), new float[]{ 1000.0f, 1000.0f } },
            //Above high score (10) borders (only 1 participant)
            { new Duel( 1000.0f, 1000.0f, 10000, 0 ), new float[]{ 1025.0f, 975.0f } },
            { new Duel( 1000.0f, 1000.0f, 1000, 5 ), new float[]{ 1010.0f, 990.0f } }, //evaluate to 10:5 then to 7:3
            { new Duel( 1000.0f, 1000.0f, 1000, 10 ), new float[]{ 1000.0f, 1000.0f } }, //evaluate to 10:10 then to 5:5
            //Above high score (10) borders (2 participants)
            { new Duel( 1000.0f, 1000.0f, 30, 15 ), new float[]{ 1000.0f, 1000.0f } },
            { new Duel( 1000.0f, 1000.0f, 30, 30 ), new float[]{ 1000.0f, 1000.0f } },
            { new Duel( 1000.0f, 1000.0f, 30, 45 ), new float[]{ 1000.0f, 1000.0f } },
            //Basic algoritmn test based on scores
            { new Duel( 1000.0f, 1000.0f, 10, 0 ), new float[]{ 1025.0f, 975.0f } },
            { new Duel( 1000.0f, 1000.0f, 9, 1 ), new float[]{ 1020.0f, 980.0f } },
            { new Duel( 1000.0f, 1000.0f, 8, 2 ), new float[]{ 1015.0f, 985.0f } },
            { new Duel( 1000.0f, 1000.0f, 7, 3 ), new float[]{ 1010.0f, 990.0f } },
            { new Duel( 1000.0f, 1000.0f, 6, 4 ), new float[]{ 1005.0f, 995.0f } },
            { new Duel( 1000.0f, 1000.0f, 5, 5 ), new float[]{ 1000.0f, 1000.0f } },
            { new Duel( 1000.0f, 1000.0f, 4, 6 ), new float[]{ 995.0f, 1005.0f } },
            { new Duel( 1000.0f, 1000.0f, 3, 7 ), new float[]{ 990.0f, 1010.0f } },
            { new Duel( 1000.0f, 1000.0f, 2, 8 ), new float[]{ 985.0f, 1015.0f } },
            { new Duel( 1000.0f, 1000.0f, 1, 9 ), new float[]{ 980.0f, 1020.0f } },
            { new Duel( 1000.0f, 1000.0f, 0, 10 ), new float[]{ 975.0f, 1025.0f } },
            //Random
            { new Duel( 1037.2383f, 987.1233f, 3, 7 ), new float[]{ 1024.7703f, 999.5913f } }
        } );
    }

    @Test
    public void test_calculations() {
        EloRatingSystemCalculator controller = EloRatingSystemCalculator.getInstance();
        float[] result = controller.calculate( duel );
        assertThat( result, is( expectedPlayersNewRatings ) );
    }

}
