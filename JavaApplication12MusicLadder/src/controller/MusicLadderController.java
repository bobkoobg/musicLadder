package controller;

import entity.Duel;
import entity.Song;
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
    
//    public List<Duel> getNextDuels( Integer ammount ) {
//        if ( ammount == 3 ) {
//            dG.getThreeDuels();
//        } else if ( ammount == 1 ) {
//            dG.getSigularDuel();
//        } else {
//            return null;
//        }
//        
//        return null;  
//    }
    
    private void helloWorld() {
        System.out.println("Hello World!");
        
        List<Duel> nextThreeDuels = dG.getThreeDuels();
        
        //Testing
        
        for (int i = 0; i < nextThreeDuels.size() ; i++)
        {
            System.out.println("### DUEL " + i + " ### : " + nextThreeDuels.get(i).toString() );
        }
        
        
        
//        Song s1 = new Song(0001,"Carly Rae Jepsen - Call Me Maybe", 0, 2, 5, 950, 1000);
//        Song s2 = new Song(0002, "KOLLEGAH - John Gotti (prod. von Alexis Troy)", 3, 1, 0, 1150, 1120);
//        
//        System.out.println( "song1 startup : " + s1.toString() );
//        System.out.println( "song2 startup : " + s2.toString() );
//        
//        Duel d1 = new Duel(000001, s1.getId(), s2.getId(), s1.getCurrentRating(), s2.getCurrentRating() );
//        
//        d1.setSong1Score( 16 );
//        d1.setSong2Score( 1 );
//
//        float[] newSongRatings = eloRSC.calculate( d1 );
//         
//        //after match
//        s1.incrementWins();
//        s2.incrementLoses();
//        
//        s1.setFormerRating( s1.getCurrentRating() );
//        s1.setCurrentRating( newSongRatings[0] );
//        s2.setFormerRating( s2.getCurrentRating() );
//        s2.setCurrentRating( newSongRatings[1] );
//        
//        System.out.println("*****************");
//        System.out.println( "song1 new rating : " + s1.toString() );
//        System.out.println( "song2 new rating : " + s2.toString() );
//        
//        //end
//        
//        Duel d2 = new Duel(000002, s1.getId(), s2.getId(), s1.getCurrentRating(), s2.getCurrentRating() );
//        
//        d2.setSong1Score( 16 );
//        d2.setSong2Score( 7 );
//        
//        newSongRatings = eloRSC.calculate( d2 );
//        
//        //after match
//        s1.incrementWins();
//        s2.incrementLoses();
//        
//        s1.setFormerRating( s1.getCurrentRating() );
//        s1.setCurrentRating( newSongRatings[0] );
//        s2.setFormerRating( s2.getCurrentRating() );
//        s2.setCurrentRating( newSongRatings[1] );
//        
//        System.out.println("*****************");
//        System.out.println( "song1 new rating : " + s1.toString() );
//        System.out.println( "song2 new rating : " + s2.toString() );
//        
//        //end
    }
}
