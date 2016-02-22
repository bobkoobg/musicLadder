package controller;

import java.util.List;

/**
 *
 * @author root
 */
public class MusicLadderController
{

    /**
     * @param args the command line arguments
     * Testing purposes!
     */
    public static void main(String[] args)
    {
        new MusicLadderController().helloWorld();
    }
    
    private void helloWorld() {
        System.out.println("Hello World!");
        
        float r1 = 1000;
        float r2 = 1000;
        Integer kFactor = 50;
        
        System.out.println("Player 1 Rating : " + r1 );
        System.out.println("Player 2 Rating : " + r2 );
        System.out.println("K Factor : " + kFactor );
              
        float transformedRating1 = getTransformedRating(r1);
        float transformedRating2 = getTransformedRating(r2);
        
        System.out.println("Transformed rating 1 is : " + transformedRating1);
        System.out.println("Transformed rating 2 is : " + transformedRating2);
        
        float[] expectedScoreList = calculateExpectedScore(transformedRating1, transformedRating2);
        float expectedScore1 = expectedScoreList[0];
        float expectedScore2 = expectedScoreList[1];
        
        System.out.println("Expected score 1 : " + expectedScore1 );
        System.out.println("Expected score 2 : " + expectedScore2 );
        
        Integer player1Score = 10;
        Integer player2Score = 7;
        
        float actualScore1, actualScore2;
        
        if ( player1Score > player2Score ) {
            actualScore1 = 1;
            actualScore2 = 0;
        } else if ( player2Score > player1Score ) {
            actualScore2 = 1;
            actualScore1 = 0;
        } else {
            actualScore1 = 0.5f;
            actualScore2 = 0.5f;
        }
        
        System.out.println("Actual score 1 : " + actualScore1 );
        System.out.println("Actual score 2 : " + actualScore2 );
        
        float player1NewRating = calculateNewRating(r1, kFactor, actualScore1, expectedScore1);
        float player2NewRating = calculateNewRating(r2, kFactor, actualScore2, expectedScore2);
        
        System.out.println("Player 1 New Rating :  " + player1NewRating );
        System.out.println("Player 2 New Rating :  " + player2NewRating );
    }
    
    private float getTransformedRating(Float r) {
        float power = r/400;
        float transformedRating = power;
        System.out.println("Power is : " + power);
        
        for (int i = 0; i < power; i++)
        {
            transformedRating = transformedRating * power;
        }
        
        return transformedRating;
    }
    
    private float[] calculateExpectedScore(Float t1, Float t2) {
        float e1, e2;
        
        e1 = t1 / ( t1 + t2 );
        e2 = t2 / ( t1 + t2 );
        
        float[] expected = { e1, e2 };
        
        return expected;
    }
    
    private float calculateNewRating( float transformedRating, float kFactor, 
                float actualScore, float expectedScore ) {
        System.out.println(transformedRating + " +  " + kFactor + " * ( " + actualScore + " - " + expectedScore + " )");
        float newRating = transformedRating + kFactor * ( actualScore - expectedScore );
        return newRating;
        
    }
    
}
