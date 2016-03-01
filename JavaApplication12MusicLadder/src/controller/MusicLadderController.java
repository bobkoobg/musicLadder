package controller;

import entity.Duel;
import entity.Song;
import java.util.ArrayList;
import java.util.List;
import model.MusicLadderModel;
import sun.util.calendar.CalendarUtils;
import utils.duelGenerator;
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
    public static void main(String[] args)
    {
        new MusicLadderController().helloWorld();
    }
    
    private static MusicLadderController instance = null;
    
    private MusicLadderModel model = null;  
    private eloRatingSystemCalculator eloRSC = null;
    private duelGenerator dG = null;

    private MusicLadderController()
    {
        // Exists only to defeat instantiation.
        model = new MusicLadderModel();
        eloRSC = eloRatingSystemCalculator.getInstance();
        dG = duelGenerator.getInstance();
        dG.setSongs( model.getSongs() );
    }

    public static MusicLadderController getInstance()
    {
        if (instance == null)
        {
            instance = new MusicLadderController();
        }
        return instance;
    }
    
    public List<Song> getSongs() {
        return model.getSongs();
    }
    
    public Song getSongByID( Integer songID ) {
        return model.getSongByID( songID );
    }
    
    public Integer getDuelsSum() {
        return model.getDuelsSum();
    }
    
    public Integer getDuelsMatchMax() {
        return model.getDuelsMatchMax();
    }
    
    public Integer getAmountOfDuels() {
        return model.getAmountOfDuels();
    }
    
    public List<Duel> getDuels(Integer amount) {
        List<Duel> duels = new ArrayList();
        for (int i = 0; i < amount; i++)
        {
            Duel duel = dG.generator( getAmountOfDuels(), getDuelsMatchMax() );
            model.addDuel( duel );
            duels.add( duel );
        }
        return duels;
    }
    
    public List<Song> saveDuel(Duel duel, Integer song1Score, Integer song2Score) {
        
        duel.setSong1Score( song1Score );
        duel.setSong2Score( song2Score );
        
        System.out.println("duel ? : " + duel.toString() );
        
        float[] newSongRatings = eloRSC.calculate( duel );
        
        //after match
        Song s1 = model.getSongByID(duel.getSong1ID() );
        Song s2 = model.getSongByID(duel.getSong2ID() );
        
        System.out.println("song1Socre : " + song1Score);
        System.out.println("song2Socre : " + song2Score);
        
        if( song1Score > song2Score ) {
            s1.incrementWins();
            s2.incrementLoses();
            System.out.println("1 wins");
        } else if ( song1Score < song2Score ) {
            s1.incrementLoses();
            s2.incrementWins();
            System.out.println("2 wins");
        } else  {
            s1.incremenetDraws();
            s2.incremenetDraws();
            System.out.println("draw");
        }
        System.out.println( "New song rating 1 : " + newSongRatings[0] );
        System.out.println( "New song rating 2 : " + newSongRatings[1] );
        s1.setFormerRating( s1.getCurrentRating() );
        s1.setCurrentRating( newSongRatings[0] );
        s2.setFormerRating( s2.getCurrentRating() );
        s2.setCurrentRating( newSongRatings[1] );

        return getSongs();
    }
    
    private void helloWorld() {
        System.out.println("Hello World!");
        
        Float song1Rating = 2.5f;
        Float song2Rating = 3.5f;
        
        String song1 = "A";
        String song2 = "B";
        
        System.out.println("1>>>" + Float.compare(song1Rating, song2Rating) );
        System.out.println("2>>>" + song1.compareTo(song2) );
        
//        List<Duel> nextThreeDuels = getDuels( 3 );
//        
//        //Testing
//        
//        for (int i = 0; i < nextThreeDuels.size() ; i++)
//        {
//            System.out.println("### DUEL " + i + " ### : " + nextThreeDuels.get(i).toString() );
//            
//            Duel duel = nextThreeDuels.get(i);
//
//            duel.setSong1Score( 16 );
//            duel.setSong2Score( 1 );
//
//            float[] newSongRatings = eloRSC.calculate( duel );
//
//            //after match
//            Song s1 = model.getSongByID(duel.getSong1ID() );
//            Song s2 = model.getSongByID(duel.getSong2ID() );
//            
//            s1.incrementWins();
//            s2.incrementLoses();
//
//            s1.setFormerRating( s1.getCurrentRating() );
//            s1.setCurrentRating( newSongRatings[0] );
//            s2.setFormerRating( s2.getCurrentRating() );
//            s2.setCurrentRating( newSongRatings[1] );
//
//            System.out.println("*****************");
//            System.out.println( "song1 new rating : " + s1.toString() );
//            System.out.println( "song2 new rating : " + s2.toString() );
//        }
    }
}
