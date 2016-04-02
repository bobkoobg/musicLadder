package controller;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import entity.Duel;
import entity.Song;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import model.MusicLadderModel;
import utils.DuelGenerator;
import utils.EloRatingSystemCalculator;
import utils.PerformanceLogger;
import utils.SongReader;

public class MusicLadderController
{
    
    private static MusicLadderController instance = null;
    
    private PerformanceLogger pl = null;
    private Logger logger = null;
    private Facade facade = null;
    private MusicLadderModel model = null;  
    private EloRatingSystemCalculator eloRSC = null;
    private DuelGenerator dG = null;
    private SongReader sr = null;
    private Gson gson = null;

    private MusicLadderController()
    {
        // Exists only to defeat instantiation.
        
        //Logger functionality
        pl = new PerformanceLogger();
        logger = pl.logMessage();
        
        facade = Facade.getInstance();
        facade.initializeConnection(logger);
        
        model = new MusicLadderModel();
        eloRSC = EloRatingSystemCalculator.getInstance();
        dG = DuelGenerator.getInstance();
        sr = new SongReader();
        
        gson = new Gson();

    }

    public static MusicLadderController getInstance()
    {
        if (instance == null)
        {
            instance = new MusicLadderController();
        }
        return instance;
    }
    
    /*
    * Correct way of loading songs
    *   Missing : Error handling
    */
    public List<Song> loadSongs( Integer ladderId ) {
        return facade.getSongs(logger, ladderId );
    }
    
    /*
    * Correct way of loading songs
    *   Missing : Error handling
    */
    public List<Duel> loadNPlayedDuels( Integer amount ) {
        return facade.getNPlayedDuels(logger, amount);
    }
    
    /*
    * Correct way of loading songs
    *   1 ) Load from DB
    *     Missing : Error handling
    */
    public List<Duel> loadNDuelsToPlay( Integer amount ) {
        return facade.getNDuelsToPlay(logger, amount);
    }
    
    /*
    * Correct way of creating songs from file
    *   1 ) Find in local folder
    *   2 ) Loop through result
    *   3 ) Insert elem by elem in db
    *   4 ) Check if correct ***MISSING***
    *   5 ) Error handling ***MISSING***
    */
    public Boolean createSongs( String path ) {
        File[] songFiles = sr.finder( path );
        for (int i = 0; i < songFiles.length; i++)
        {
            Integer songId = facade.insertSong(logger, songFiles[i].getName());
            //Error handling
        }
        return true;
    }
    
    /*
    * Correct way of creating a song via API
    *   1 ) Create object from Json
    *   2 ) Insert it in the db
    *   3 ) Check if correct ***MISSING***
    *   4 ) Error handling ***MISSING***
    */
    public Boolean createSongAPI( String jQueryObject ) {
        Song jsonObject = gson.fromJson( jQueryObject , Song.class );
        
        Integer songId = facade.insertSong(logger, jsonObject.getName() );
        //Error handling
        return true;
    }
    
    private int getDuelsMatchMax( List<Song> songs ) {
        return model.getDuelsMatchMax( songs );
    }
    
    public boolean generateDuels(Integer ladderId, Integer amount) {
        List<Song> songs = facade.getSongs(logger, ladderId);
        for (int i = 0; i < amount; i++)
        {
            Duel duel = dG.generator( songs, getDuelsMatchMax( songs ) );
            if ( duel == null ) {
                System.out.println("Error : generateDuels no duel generated.");
                return false;
            } 
            
            if (  ! facade.insertDuel(logger, duel) ) {
                System.out.println("Error : duel not inserted in database");
                return false;
            }
        }
        return true;
    }
    
    public Duel getDuel( Integer duelID ) {
        return facade.getDuel( logger, duelID );
    }
    
    //Very same but with String jQueryObject
    public Boolean generateResultsAndUpdateDuel(Integer duelID, Integer song1Score, Integer song2Score) {
        //No inner error handling at any point
        Duel duel = getDuel( duelID );
        
        if ( duel == null ) {
            return false;
        }
        
        Song song1 = facade.getSong(logger, duel.getSong1ID() );
        Song song2 = facade.getSong(logger, duel.getSong2ID() );

        if ( song1 == null || song2 == null ) {
            return false;
        }

        duel.setSong1Score( song1Score );
        duel.setSong2Score( song2Score );

        float[] newSongRatings = eloRSC.calculate( duel );

        duel.setSong1AfterMatchRating( newSongRatings[0] );
        duel.setSong2AfterMatchRating( newSongRatings[1] );

        if( song1Score > song2Score ) {
            song1.incrementWins();
            song2.incrementLoses();
        } else if ( song1Score < song2Score ) {
            song1.incrementLoses();
            song2.incrementWins();
        } else  {
            song1.incremenetDraws();
            song2.incremenetDraws();
        }

        song1.setFormerRating( song1.getCurrentRating() );
        song1.setCurrentRating( newSongRatings[0] );
        song2.setFormerRating( song2.getCurrentRating() );
        song2.setCurrentRating( newSongRatings[1] );

        boolean isUpdatedDuel = facade.updateDuel(logger, duel);
        if ( isUpdatedDuel ) {
            boolean isUpdatedSong1 = facade.updateSong(logger, song1);
            boolean isUpdatedSong2 = facade.updateSong(logger, song2); 
            return ( isUpdatedSong1 && isUpdatedSong2 );
        } 
        return false;
    }
    
    public Song getSongByID( Integer songID ) {
        return facade.getSong(logger, songID);
    }
    
    public float[] predictDuelResults( Duel duel ) {
        return eloRSC.calculate(duel);
    }
    
    private List<Float> generateResults( Duel duel ) {
        List<Float> possibilities = new ArrayList();
        Integer[] song1Scores = {10, 5, 0};
        Integer[] song2Scores = {0, 5, 10};
        
        for (int i = 0; i < song1Scores.length; i++)
        {
            duel.setSong1Score( song1Scores[i] );
            duel.setSong2Score( song2Scores[i] );

            float[] calcResults = eloRSC.calculate(duel);

            possibilities.add( calcResults[0] );
            possibilities.add( calcResults[1] );
        }
        return possibilities;
    }
    
    public String predictDuelResults( String jQueryObject ) {
        //No inner error handling at any point
        Duel duel = null;
        try {
            duel = gson.fromJson( jQueryObject , Duel.class );
        }
        catch (JsonParseException e) {
            System.out.println("exception : " + e);
        }
        
        List<Float> possibilities = generateResults( duel );
        
        return gson.toJson( possibilities );
    }
    
    public void closeConnection() {
        facade.closeConnection(logger);
    }
}
