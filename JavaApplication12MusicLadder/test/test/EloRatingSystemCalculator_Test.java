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
            { new Duel( 1025.0f, 975.0f, 1, 9 ), new float[]{ 1002.5078f, 997.4922f } },
            { new Duel( 10000.0f, 9750.0f, 10, 0 ), new float[]{ 10000.0f, 9732.943f } },
            { new Duel( 10000.0f, 9750.0f, 0, 10 ), new float[]{ 9967.057f, 9782.943f } },
            { new Duel( 1f, 1.2f, 1, 9 ), new float[]{ 1.0f, 21.691803f } },
            { new Duel( 0.9f, 1.2f, 10, 0 ), new float[]{ 1.0f, 21.691803f } },
            { new Duel( 0.9f, 1.2f, 10, 0 ), new float[]{ 32.900005f, 1.0f } },
            { new Duel( 1025.0f, 975.0f, 1, 9 ), new float[]{ 1002.5078f, 997.4922f } },
            { new Duel( 1000.0f, 1000.0f, -1, 0 ), new float[]{ 1000.0f, 1000.0f } },
            { new Duel( 1000.0f, 1000.0f, 33, 74 ), new float[]{ 1002.5078f, 997.4922f } },
            { new Duel( 1000.0f, 1000.0f, 1, 9 ), new float[]{ 1002.5078f, 997.4922f } }
        } );
    }

    @Test
    public void test_calculations() {
        EloRatingSystemCalculator controller = EloRatingSystemCalculator.getInstance();
        float[] result = controller.calculate( duel );
        assertThat( result, is( expectedPlayersNewRatings ) );
    }

}
