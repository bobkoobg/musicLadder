package controller;

import entity.Duel;
import entity.Song;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.MusicLadderModel;
import utils.duelGenerator;
import utils.eloRatingSystemCalculator;
import utils.songReader;

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
    }
    */
    
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
        
        songReader sr = new songReader();
        File[] songFiles = sr.finder("/media/bobkoo/SWAG/Music/Flashback OldSchool Mix/");
        for (int i = 0; i < songFiles.length; i++)
        {
            model.saveSong( new Song( model.getSongsCount(), songFiles[i].getName() ) );
        }
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
    
    public List<Duel> getDuels( Integer amount ) {
        return model.getDuels( amount );
    }
    
    public List<Duel> generateDuels(Integer amount) {
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
        
        float[] newSongRatings = eloRSC.calculate( duel );
        
        duel.setSong1AfterMatchRating( newSongRatings[0] );
        duel.setSong2AfterMatchRating( newSongRatings[1] );

        Song s1 = model.getSongByID(duel.getSong1ID() );
        Song s2 = model.getSongByID(duel.getSong2ID() );
        
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

        return getSongs();
    }
    
    public float[] predictDuelResults( Duel duel ) {
        return eloRSC.calculate(duel);
    }
}
