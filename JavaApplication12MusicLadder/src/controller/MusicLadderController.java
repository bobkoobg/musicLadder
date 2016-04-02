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
    
    /*
    * Testing purposes
    * /
    
    public static void main(String[] args)
    {
        new MusicLadderController().helloWorld();
    }
    
    private void helloWorld() {
        System.out.println("Hello World!");   
        
        PerformanceLogger pl = new PerformanceLogger();
        Logger logger = pl.logMessage();
        Facade f = Facade.getInstance();
        
        f.initializeConnection(logger);
        f.wipeDuelDatabases(logger);
        f.wipeSongDatabases(logger);
        f.closeConnection(logger);
    }
    */
    
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
    
    public List<Song> loadSongs( Integer ladderId ) {
        List<Song> localSongs = facade.getSongs(logger, ladderId );
        model.setSongs(localSongs);
        return model.getSongs();
    }
    
    public List<Duel> loadNPlayedDuels( Integer amount ) {
        List<Duel> duels = facade.getNPlayedDuels(logger, amount);
        model.joinDuelsLists(duels);
        return duels;
    }
    
    public List<Duel> loadNDuelsToPlay( Integer amount ) {
        List<Duel> duels = facade.getNDuelsToPlay(logger, amount);
        model.joinDuelsLists(duels);
        return duels;
    }
    
    public List<Song> insertAndLoadSongs( String path) {
        File[] songFiles = sr.finder( path );
        for (int i = 0; i < songFiles.length; i++)
        {
            Integer songId = facade.insertSong(logger, songFiles[i].getName());
            if (songId != -1 ) {
                model.saveSong( new Song( songId, songFiles[i].getName() ) );
            } else {
                //Please cry!
                System.out.println("insertAndLoadSongs Please cry!");
            }
        }
        List<Song> songList = model.getSongs();
        return songList;
    }
    
    public List<Song> createAndLoadSongs( String jQueryObject ) {
        
        LocalSong jsonObject = gson.fromJson( jQueryObject , LocalSong.class );
        
        Integer songId = facade.insertSong(logger, jsonObject.getName() );
        if (songId != -1 ) {
            model.saveSong( new Song( songId, jsonObject.getName() ) );
        } else {
            //Please cry!
            System.out.println("createAndLoadSongs Please cry!");
        }
        List<Song> songList = model.getSongs();
        return songList;
    }
    
    private Integer getDuelsMatchMax() {
        return model.getDuelsMatchMax();
    }
    
    public List<Duel> generateDuels(Integer amount) {
        for (int i = 0; i < amount; i++)
        {
            Duel duel = dG.generator( model.getSongs(), getDuelsMatchMax() );
            duel = facade.insertDuel(logger, duel);
            if ( duel != null ) {
                model.addDuel( duel );
            } else {
                //Please cry!
                System.out.println("generateDuels Please cry!");
            }
        }
        //invent better solution
         List<Duel> duels = loadNDuelsToPlay( 100 );
        return duels;
    }
    
    public List<Song> generateResultsAndUpdateDuel(Duel duel, Integer song1Score, Integer song2Score) {
        
        duel.setSong1Score( song1Score );
        duel.setSong2Score( song2Score );
        
        float[] newSongRatings = eloRSC.calculate( duel );
        
        duel.setSong1AfterMatchRating( newSongRatings[0] );
        duel.setSong2AfterMatchRating( newSongRatings[1] );

        Song s1 = model.getSongByID( duel.getSong1ID() );
        Song s2 = model.getSongByID( duel.getSong2ID() );
        
        if( song1Score > song2Score ) {
            s1.incrementWins();
            s2.incrementLoses();
        } else if ( song1Score < song2Score ) {
            s1.incrementLoses();
            s2.incrementWins();
        } else  {
            s1.incremenetDraws();
            s2.incremenetDraws();
        }
        
        s1.setFormerRating( s1.getCurrentRating() );
        s1.setCurrentRating( newSongRatings[0] );
        s2.setFormerRating( s2.getCurrentRating() );
        s2.setCurrentRating( newSongRatings[1] );
        
        facade.updateSong(logger, s1);
        facade.updateSong(logger, s2);
        facade.updateDuel(logger, duel);
        
        //No inner error handling at any point
        model.updateSong(s1);
        model.updateSong(s2);
        
        return loadSongs(1);
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
    
    public List<Duel> getDuels( Integer amount ) {
        return model.getDuels( amount );
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
    
    //Cleanup before insert
    public void clearSystem() {
        model.clearDuels();
        model.clearSongs();
    }
    
    private class LocalSong {
        private String name;

        public LocalSong(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }
}
