package utils;

import entity.Duel;

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

    public float[] calculate( Duel duel )
    {
             
        float song1Rating = duel.getSong1BeforeMatchRating();
        float song1Points = duel.getSong1Score();
        float song2Rating = duel.getSong2BeforeMatchRating();
        float song2Points = duel.getSong2Score();
        
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
     * 1st step 
     * Calculate Transformed rating based on the songs current rating
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
     * 2nd step
     * Calculate the expected scores for both songs based on the transformed rating
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
     * Calculate their actual score based on the point they scored in the match
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
     * Calculate the new ELO Rating based on the transformed rating, the actial 
     *      calculated score of the songs and the expected calculated score
     */
    private float calculateNewELORating(float transformedRating, float actualScore,
            float expectedScore)
    {

        float newRating = transformedRating + kFactor * (actualScore - expectedScore);
        return newRating;

    }
}
