package utils;

import entity.Duel;

/**
 * This class possesses the core functionality of the music ladder application.
 * It receives a duel from the controller. The duel contains ratings and scores
 * of songs. Those parameters will be evaluated and depending on that, the
 * calculate method will return new ratings, which should be assignment to the
 * songs, corresponding to the ID's in the Duel object.
 *
 * General information - https://en.wikipedia.org/wiki/Elo_rating_system
 * Implementation -
 * https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/
 * Calculation - http://www.calculatorsoup.com/calculators/math/percentage.php
 * Singleton (Example 2) -
 * http://www.tutorialspoint.com/java/java_using_singleton.htm
 */
public class EloRatingSystemCalculator {

    private static final float kFactor = 50;
    private static final int maxPoints = 10;

    private static EloRatingSystemCalculator instance = null;

    private EloRatingSystemCalculator() {
        // Exists only to defeat instantiation.
    }

    public static EloRatingSystemCalculator getInstance() {
        if ( instance == null ) {
            instance = new EloRatingSystemCalculator();
        }
        return instance;
    }

    public float[] calculate( Duel duel ) {

        float[] playersOldRatings = { duel.getSong1BeforeMatchRating(), duel.getSong2BeforeMatchRating() };
        int[] songsPoints = { duel.getSong1Score(), duel.getSong2Score() };

        //Rating check
        playersOldRatings = checkRatingBorders( playersOldRatings );
        //Score check
        songsPoints = checkSongPointsBorder( songsPoints );

        float transformedRating1 = calculateTransformedRating( playersOldRatings[ 0 ] );
        float transformedRating2 = calculateTransformedRating( playersOldRatings[ 1 ] );

        float[] expectedScoreList = calculateExpectedScore( transformedRating1,
                                                            transformedRating2 );
        float expectedScore1 = expectedScoreList[ 0 ];
        float expectedScore2 = expectedScoreList[ 1 ];

        float[] actualScores = calculateActualScore( songsPoints[ 0 ], songsPoints[ 1 ] );

        float song1ScoreRating = actualScores[ 0 ];
        float song2ScoreRating = actualScores[ 1 ];

        float player1NewRating = calculateNewELORating( playersOldRatings[ 0 ], song1ScoreRating,
                                                        expectedScore1 );
        float player2NewRating = calculateNewELORating( playersOldRatings[ 1 ], song2ScoreRating,
                                                        expectedScore2 );

        float[] playersNewRatings = { player1NewRating, player2NewRating };

        //Rating cap
        for ( int i = 0; i < playersNewRatings.length; i++ ) {
            if ( playersNewRatings[ i ] > 10000.0f ) {
                playersNewRatings[ i ] = 10000f;
            } else if ( playersNewRatings[ i ] < 1.0f ) {
                playersNewRatings[ i ] = 1f;
            }
        }

        return playersNewRatings;
    }

    /*
     * step 0 
     * Check of incorrect values of rating
     */
    private float[] checkRatingBorders( float[] playersOldRatings ) {
        for ( int i = 0; i < playersOldRatings.length; i++ ) {
            if ( playersOldRatings[ i ] > 10000.0f ) {
                playersOldRatings[ i ] = 10000.f;
            } else if ( playersOldRatings[ i ] < 1.0f ) {
                playersOldRatings[ i ] = 1.0f;
            }
        }
        return playersOldRatings;
    }

    /*
     * step 0 
     * Check of incorrect values of points
     */
    private int[] checkSongPointsBorder( int[] songsPoints ) {
        for ( int i = 0; i < songsPoints.length; i++ ) {
            if ( songsPoints[ i ] < 0 ) {
                songsPoints[ i ] = 0;
            } else if ( songsPoints[ i ] > 10 ) {
                songsPoints[ i ] = 10;
            }
        }
        int pointsSum = songsPoints[ 0 ] + songsPoints[ 1 ];
        if ( pointsSum != 10 && (songsPoints[ 0 ] != songsPoints[ 1 ]) ) {
            songsPoints[ 0 ] = Math.round( (( float ) songsPoints[ 0 ] / pointsSum) * maxPoints );
            songsPoints[ 1 ] = Math.round( (( float ) songsPoints[ 1 ] / pointsSum) * maxPoints );
        } else if ( pointsSum != 10 && (songsPoints[ 0 ] == songsPoints[ 1 ]) ) {
            songsPoints[ 0 ] = maxPoints / 2;
            songsPoints[ 1 ] = maxPoints / 2;
        }
        return songsPoints;
    }

    /*
     * 1st step 
     * Calculate Transformed rating based on the songs current rating
     */
    private float calculateTransformedRating( Float r ) {
        float power = r / 400;
        float transformedRating = power;

        for ( int i = 0; i < power; i++ ) {
            transformedRating = transformedRating * power;
        }

        return transformedRating;
    }

    /*
     * 2nd step
     * Calculate the expected scores for both songs based on the transformed rating
     */
    private float[] calculateExpectedScore( Float t1, Float t2 ) {
        float e1, e2;

        e1 = t1 / (t1 + t2);
        e2 = t2 / (t1 + t2);

        float[] expected
                = {
                    e1, e2
                };

        return expected;
    }

    /*
     * 3rd step
     * Calculate their actual score based on the point they scored in the match
     */
    private float[] calculateActualScore( float song1Points, float song2Points ) {
        float actualScoreSong1, actualScoreSong2, roundValue;

        roundValue = (song1Points + song2Points) / 100;
        actualScoreSong1 = ((1 / roundValue) * song1Points) / 100;
        actualScoreSong2 = ((1 / roundValue) * song2Points) / 100;

        float[] actualScores = { actualScoreSong1, actualScoreSong2 };

        return actualScores;
    }

    /*
     * 4th step
     * Calculate the new ELO Rating based on the transformed rating, the actial 
     *      calculated score of the songs and the expected calculated score
     */
    private float calculateNewELORating( float transformedRating, float actualScore,
            float expectedScore ) {

        float newRating = transformedRating + kFactor * (actualScore - expectedScore);
        return newRating;

    }
}
