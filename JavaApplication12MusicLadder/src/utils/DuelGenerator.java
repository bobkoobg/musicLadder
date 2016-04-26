package utils;

import entity.Duel;
import entity.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DuelGenerator {

    private static DuelGenerator instance = null;
    private static Integer probabilityRateLevels = 10;
    private Random rn;

    private static String errorMessage = "The duel generator needs at least 3 songs.";

    private DuelGenerator() {
        // Exists only to defeat instantiation.

        rn = new Random();
    }

    public static DuelGenerator getInstance() {
        if ( instance == null ) {
            instance = new DuelGenerator();

        }
        return instance;
    }

    /*
     * Probability generator
     */
    public <T> T generator( List<Song> songs ) {
        //If there are less than 3 elements, then don't bother generating - return error.
        if ( songs == null || songs.size() < 3 ) {
            return ( T ) errorMessage;
        }

        //Get the maximum ammount of duel by a specific song (in a specific ladder)
        int MaxSongDuelsSum = getDuelsMatchMax( songs );

        int probabilityRate, ammountOfMatches;

        //Calculate the range of a single probability level 
        // eg. : If MaxSongDuelsSum is 100 and levels are 10 then Prob.Range will be 10.
        // Therefore - Prob.Level 1 = 0-10, Prob. Level 2 = 10-20, etc.
        int probabilityRange = MaxSongDuelsSum / probabilityRateLevels;

        //For every song add X amount of the ID of the song to songProbabilityPool 
        //based on songs matchesSum and probability range (rounded)
        List<Integer> songProbabilityPool = new ArrayList();

        for ( int i = 0; i < songs.size(); i++ ) {
            ammountOfMatches = songs.get( i ).getAmmountOfMatches();
            if ( probabilityRange > 0 ) {
                probabilityRate = (10 - Math.round( (ammountOfMatches / probabilityRange) ));
            } else {
                probabilityRate = 10;
            }
            //Rank up songs with most songs to ProbabilityRate 1.
            probabilityRate = (probabilityRate == 0) ? probabilityRate += 1 : probabilityRate;

            //System.out.println( "SongID : " + songs.get( i ).getId() + " probabilityRate : " + probabilityRate );
            for ( int j = 0; j < probabilityRate; j++ ) {
                songProbabilityPool.add( songs.get( i ).getId() );
            }
        }

        //Choose a song id out of the pool and remove all occurrences of participant1
        int i = rn.nextInt( songProbabilityPool.size() );
        int participant1 = songProbabilityPool.get( i );
        while ( songProbabilityPool.remove( ( Integer ) participant1 ) );

        //Choose a song id again = this wil be participant2
        int j = rn.nextInt( songProbabilityPool.size() );
        int participant2 = songProbabilityPool.get( j );

        int participant1ID = 0, participant2ID = 0;
        float participant1Rating = 0, participant2Rating = 0;

        //Loop through songs and obtain other vital info for the generation of the duel
        for ( int k = 0; k < songs.size(); k++ ) {
            if ( songs.get( k ).getId() == participant1 ) {
                participant1ID = participant1;
                participant1Rating = songs.get( k ).getCurrentRating();
            } else if ( songs.get( k ).getId() == participant2 ) {
                participant2ID = participant2;
                participant2Rating = songs.get( k ).getCurrentRating();
            }
        }

        //return generated duel with participant1 and participant2
        return ( T ) new Duel( participant1ID, participant2ID, participant1Rating, participant2Rating );
    }

    //Find the duel, which has the most matches and return the sum of the max matches value
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
