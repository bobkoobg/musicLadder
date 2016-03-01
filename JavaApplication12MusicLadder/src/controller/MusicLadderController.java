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
    
    public Integer getDuelsSum() {
        return model.getDuelsSum();
    }
    
    public Integer getDuelsMatchMax() {
        return model.getDuelsMatchMax();
    }
    
    private void helloWorld() {
        System.out.println("Hello World!");
        
        List<Duel> nextThreeDuels = dG.getThreeDuels( getDuelsMatchMax() );
        
        //Testing
        
        for (int i = 0; i < nextThreeDuels.size() ; i++)
        {
            System.out.println("### DUEL " + i + " ### : " + nextThreeDuels.get(i).toString() );
            
            Duel duel = nextThreeDuels.get(i);

            duel.setSong1Score( 16 );
            duel.setSong2Score( 1 );

            float[] newSongRatings = eloRSC.calculate( duel );

            //after match
            Song s1 = model.getSongByID(duel.getSong1ID() );
            Song s2 = model.getSongByID(duel.getSong2ID() );
            
            s1.incrementWins();
            s2.incrementLoses();

            s1.setFormerRating( s1.getCurrentRating() );
            s1.setCurrentRating( newSongRatings[0] );
            s2.setFormerRating( s2.getCurrentRating() );
            s2.setCurrentRating( newSongRatings[1] );

            System.out.println("*****************");
            System.out.println( "song1 new rating : " + s1.toString() );
            System.out.println( "song2 new rating : " + s2.toString() );
        }
    }
}
