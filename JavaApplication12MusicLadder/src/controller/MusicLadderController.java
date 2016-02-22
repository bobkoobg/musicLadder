package controller;

import entity.Duel;
import entity.Song;
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
        System.out.println("song1 startup rating : 950");
        System.out.println("song2 startup rating : 1150");
        
        Song s1 = new Song(0001,"Carly Rae Jepsen - Call Me Maybe", 950, 1000);
        Song s2 = new Song(0002, "KOLLEGAH - John Gotti (prod. von Alexis Troy)", 1150, 1120);
        
        Duel d1 = new Duel(000001, s1.getId(), s2.getId(), s1.getCurrentRating(), s2.getCurrentRating() );
        
        d1.setSong1Score( 16 );
        d1.setSong2Score( 1 );
        
        eloRSC = eloRatingSystemCalculator.getInstance();
        
        float[] newSongRatings = eloRSC.calculate( d1 );
        System.out.println("*****************");
        System.out.println("song1 new rating : "+ newSongRatings[0]);
        System.out.println("song2 new rating : "+ newSongRatings[1]);
        
        //after match
        s1.setFormerRating( s1.getCurrentRating() );
        s1.setCurrentRating( newSongRatings[0] );
        s2.setFormerRating( s2.getCurrentRating() );
        s2.setCurrentRating( newSongRatings[1] );
        
        //end
        Duel d2 = new Duel(000002, s1.getId(), s2.getId(), s1.getCurrentRating(), s2.getCurrentRating() );
        
        d2.setSong1Score( 16 );
        d2.setSong2Score( 7 );
        
        newSongRatings = eloRSC.calculate( d2 );
        System.out.println("*****************");
        System.out.println("song1 new rating : "+ newSongRatings[0]);
        System.out.println("song2 new rating : "+ newSongRatings[1]);
        
        s1.setFormerRating( s1.getCurrentRating() );
        s1.setCurrentRating( newSongRatings[0] );
        s2.setFormerRating( s2.getCurrentRating() );
        s2.setCurrentRating( newSongRatings[1] );
    }
}
