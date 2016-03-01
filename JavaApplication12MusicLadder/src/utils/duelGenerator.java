package utils;

import entity.Duel;
import entity.Song;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author root
 */
public class duelGenerator
{

    private List<Song> songs;

    private static duelGenerator instance = null;
    private static Integer probabilityRateLevels = 10;

    private duelGenerator()
    {
        // Exists only to defeat instantiation.
    }

    public static duelGenerator getInstance()
    {
        if (instance == null)
        {
            instance = new duelGenerator();
        }
        return instance;
    }

    public void setSongs(List<Song> songs)
    {
        this.songs = songs;
    }

    public List<Duel> getThreeDuels( Integer duelsSum )
    {
        List<Duel> duelList = new ArrayList();
        for (int i = 0; i < 3; i++)
        {
            duelList.add( generator( duelsSum ) );
        }
        System.out.println("size ? : " + duelList.size());
        return duelList;

    }

    public Duel getSigularDuel( Integer duelsSum  )
    {
        return generator( duelsSum  );

    }
    
    /*
    * Probability rates as follows :
    *   - Less than 10 matches (including) - 10 times higher chance.
    *   - Less than 25 matches (including) - 7 times higher chance.
    *   - Less than 50 matches (including) - 4 times higher chance.
    *   - More than 50 matches (excluding) - 1 time - normal chance.
    */
    private Duel generator( Integer duelsSum )
    {
       
        List<Integer> probabilitySongMap = new ArrayList();
        Integer probabilityRange = duelsSum / probabilityRateLevels;
        Integer probabilityRate, ammountOfMatches;
        
        for (int i = 0; i < songs.size(); i++)
        {
            ammountOfMatches = songs.get(i).getAmmountOfMatches();
            
            probabilityRate = (10 - Math.round( ( ammountOfMatches / probabilityRange ) ) );
            //Rank up songs with most songs to ProbabilityRate 1.
            probabilityRate = (probabilityRate == 0) ? probabilityRate += 1 : probabilityRate;
            
            for (int j = 0; j < probabilityRate; j++)
            {
                probabilitySongMap.add( songs.get(i).getId() );
            }
        }
        
        Random rn = new Random();
        int i = rn.nextInt( probabilitySongMap.size() );
        Integer participant1 = probabilitySongMap.get(i);
        
        while(probabilitySongMap.remove( participant1 )) {};
        
        int j = rn.nextInt( probabilitySongMap.size() );
        Integer participant2 = probabilitySongMap.get(j);
        
        Integer participant1ID = null;
        Float participant1Rating = null;
        Integer participant2ID = null;
        Float participant2Rating = null;
        
        for (int k = 0; k < songs.size(); k++)
        {
            if( songs.get(k).getId() == participant1 ) {
                participant1ID = participant1;
                participant1Rating = songs.get(k).getCurrentRating();
            } else if( songs.get(k).getId() == participant2 ) {
                participant2ID = participant2;
                participant2Rating = songs.get(k).getCurrentRating();
            }
        }
        
        return new Duel(participant1ID, participant2ID, participant1Rating, participant2Rating);
    }

}
