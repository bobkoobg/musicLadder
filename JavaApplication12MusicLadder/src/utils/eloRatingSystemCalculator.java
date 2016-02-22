package utils;

/**
 * General information - https://en.wikipedia.org/wiki/Elo_rating_system
 * Implementation -
 * https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/
 * Calculation - http://www.calculatorsoup.com/calculators/math/percentage.php
 * Singleton (Example 2) -
 * http://www.tutorialspoint.com/java/java_using_singleton.htm
 */
public class eloRatingSystemCalculator
{

    private static final float kFactor = 50;

    private static eloRatingSystemCalculator instance = null;

    private eloRatingSystemCalculator()
    {
        // Exists only to defeat instantiation.
    }

    public static eloRatingSystemCalculator getInstance()
    {
        if (instance == null)
        {
            instance = new eloRatingSystemCalculator();
        }
        return instance;
    }

    public float[] calculate(float song1Rating, float song1Points, float song2Rating,
            float song2Points)
    {
        float transformedRating1 = calculateTransformedRating(song1Rating);
        float transformedRating2 = calculateTransformedRating(song2Rating);

        float[] expectedScoreList = calculateExpectedScore(transformedRating1,
                                                           transformedRating2);
        float expectedScore1 = expectedScoreList[0];
        float expectedScore2 = expectedScoreList[1];

        float[] actualScores = calculateActualScore(song1Points, song2Points);
        float song1ScoreRating = actualScores[0];
        float song2ScoreRating = actualScores[1];

        float player1NewRating = calculateNewELORating(song1Rating, song1ScoreRating,
                                                       expectedScore1);
        float player2NewRating = calculateNewELORating(song2Rating, song2ScoreRating,
                                                       expectedScore2);

        float[] playersNewRatings =
        {
            player1NewRating, player2NewRating
        };

        return playersNewRatings;

    }

    /*
     * 1rd step
     */
    private float calculateTransformedRating(Float r)
    {
        float power = r / 400;
        float transformedRating = power;

        for (int i = 0; i < power; i++)
        {
            transformedRating = transformedRating * power;
        }

        return transformedRating;
    }

    /*
     * 2rd step
     */
    private float[] calculateExpectedScore(Float t1, Float t2)
    {
        float e1, e2;

        e1 = t1 / (t1 + t2);
        e2 = t2 / (t1 + t2);

        float[] expected =
        {
            e1, e2
        };

        return expected;
    }

    /*
     * 3rd step
     */
    private float[] calculateActualScore(float song1Points, float song2Points)
    {
        float actualScoreSong1, actualScoreSong2, roundValue;

        roundValue = (song1Points + song2Points) / 100;
        actualScoreSong1 = ((1 / roundValue) * song1Points) / 100;
        actualScoreSong2 = ((1 / roundValue) * song2Points) / 100;

        float[] actualScores =
        {
            actualScoreSong1, actualScoreSong2
        };

        return actualScores;
    }

    /*
     * 4th step
     */
    private float calculateNewELORating(float transformedRating, float actualScore,
            float expectedScore)
    {

        float newRating = transformedRating + kFactor * (actualScore - expectedScore);
        return newRating;

    }
}
