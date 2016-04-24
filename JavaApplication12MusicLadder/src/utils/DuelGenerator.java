package utils;

import entity.Duel;
import entity.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DuelGenerator {

    private static DuelGenerator instance = null;
    private static Integer probabilityRateLevels = 10;

    private DuelGenerator() {
        // Exists only to defeat instantiation.
    }

    public static DuelGenerator getInstance() {
        if ( instance == null ) {
            instance = new DuelGenerator();
        }
        return instance;
    }

    /*
     * Probability generator (self created and untestable since it's based on 
     * probabilites
     */
    public Duel generator( List<Song> songs ) {
        //Get the maximum ammount of duel by a specific song (in a specific ladder)
        int duelsSum = getDuelsMatchMax( songs );
        List<Integer> probabilitySongMap = new ArrayList();
        int probabilityRange = duelsSum / probabilityRateLevels;
        int probabilityRate, ammountOfMatches;

        for ( int i = 0; i < songs.size(); i++ ) {
            ammountOfMatches = songs.get( i ).getAmmountOfMatches();
            if ( probabilityRange > 0 ) {
                probabilityRate = (10 - Math.round( (ammountOfMatches / probabilityRange) ));
            } else {
                probabilityRate = 10;
            }
            //Rank up songs with most songs to ProbabilityRate 1.
            probabilityRate = (probabilityRate == 0) ? probabilityRate += 1 : probabilityRate;

            for ( int j = 0; j < probabilityRate; j++ ) {
                probabilitySongMap.add( songs.get( i ).getId() );
            }
        }

        Random rn = new Random();
        int i = rn.nextInt( probabilitySongMap.size() );
        Integer participant1 = probabilitySongMap.get( i );

        probabilitySongMap.remove( i );

        int j = rn.nextInt( probabilitySongMap.size() );
        Integer participant2 = probabilitySongMap.get( j );

        Integer participant1ID = null;
        Float participant1Rating = null;
        Integer participant2ID = null;
        Float participant2Rating = null;

        for ( int k = 0; k < songs.size(); k++ ) {
            if ( songs.get( k ).getId() == participant1 ) {
                participant1ID = participant1;
                participant1Rating = songs.get( k ).getCurrentRating();
            } else if ( songs.get( k ).getId() == participant2 ) {
                participant2ID = participant2;
                participant2Rating = songs.get( k ).getCurrentRating();
            }
        }

        return new Duel( participant1ID, participant2ID, participant1Rating, participant2Rating );
    }

    private Integer getDuelsMatchMax( List<Song> songs ) {
        Integer max = 0, currentMax = 0;
        for ( int i = 0; i < songs.size(); i++ ) {
            currentMax = songs.get( i ).getAmmountOfMatches();
            if ( max < currentMax ) {
                max = currentMax;
            }
        }
        return max;
    }

}
