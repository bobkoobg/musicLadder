package controller;

import java.util.List;
import utils.eloRatingSystemCalculator;

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
    
    private eloRatingSystemCalculator eloRSC = null;
    
    public static void main(String[] args)
    {
        new MusicLadderController().helloWorld();
    }
    
    private void helloWorld() {
        System.out.println("Hello World!");
        
        float song1Rating = 1000;
        float song2Rating = 1000;
        Integer song1Score = 16;
        Integer song2Score = 1;
        
        eloRSC = eloRatingSystemCalculator.getInstance();
        float[] newSongRatings = eloRSC.calculate(song1Rating, song1Score, song2Rating, song2Score);
        System.out.println("*****************");
        System.out.println("song1 new rating : "+ newSongRatings[0]);
        System.out.println("song2 new rating : "+ newSongRatings[1]);
    }
}
