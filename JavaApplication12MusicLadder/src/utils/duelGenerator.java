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

    public List<Duel> getThreeDuels()
    {
        List<Duel> duelList = new ArrayList();
        for (int i = 0; i < 3; i++)
        {
            duelList.add( generator() );
        }
        System.out.println("size ? : " + duelList.size());
        return duelList;

    }

    public Duel getSigularDuel()
    {
        return generator();

    }
    
    /*
    * Probability rates as follows :
    *   - Less than 10 matches (including) - 10 times higher chance.
    *   - Less than 25 matches (including) - 7 times higher chance.
    *   - Less than 50 matches (including) - 4 times higher chance.
    *   - More than 50 matches (excluding) - 1 time - normal chance.
    */
    private Duel generator()
    {
        Integer generalMatchCounter = 0;
        Map<Integer, Integer> probabilitySongMap = new HashMap();
        Integer probabilityRate;
        Integer ammountOfMatches;
        for (int i = 0; i < songs.size(); i++)
        {
            ammountOfMatches = songs.get(i).getAmmountOfMatches();

            if (ammountOfMatches <= 10)
            {
                probabilityRate = 10;
            }
            else if (ammountOfMatches > 10 && ammountOfMatches <= 25)
            {
                probabilityRate = 7;
            }
            else if (ammountOfMatches > 25 && ammountOfMatches <= 50)
            {
                probabilityRate = 4;
            }
            else
            {
                probabilityRate = 1;
            }
            //System.out.println("Matches : " + ammountOfMatches + ", probabilityRate? : " + probabilityRate + " for : " + songs.get(i).getName() );
            for (int j = 0; j < probabilityRate; j++)
            {
                probabilitySongMap.put( ( probabilitySongMap.size()+1 ), songs.get(i).getId());
            }

            generalMatchCounter += ammountOfMatches;
        }
        
        //Testing purposes
        //System.out.println("Overal matches : " + generalMatchCounter);

        for (Map.Entry<Integer, Integer> entry : probabilitySongMap.entrySet()) 
        {

            Integer key = entry.getKey();
            Integer value = entry.getValue();
            //System.out.println(key + " " + value);

        }
        //end
        
        Random rn = new Random();
        int i = rn.nextInt( probabilitySongMap.size() ) + 1;
        //System.out.println("i is : " + i );
        
        Integer participant1 = probabilitySongMap.get(i).intValue();
        //System.out.println("id if participant #1 is : "+ participant1);
        
        probabilitySongMap.values().removeAll(Collections.singleton( participant1 ));
        
        //Testing purposes
        //System.out.println("Overal matches : " + generalMatchCounter);

        for (Map.Entry<Integer, Integer> entry : probabilitySongMap.entrySet()) 
        {

            Integer key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + " " + value);

        }
        //end
        
        //do random number while you catch exception
        //if not exception - continue
        int j = rn.nextInt( probabilitySongMap.size() );
        System.out.println("J is : " + j );
        
        Integer participant2 = probabilitySongMap.get(j).intValue();
        System.out.println("id if participant #2 is : "+ participant2);
        
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
        System.out.println("participant1ID : " + participant1ID );
        System.out.println("participant2ID : " + participant2ID );
        System.out.println("participant1Rating : " + participant1Rating );
        System.out.println("participant1Rating : " + participant2Rating );
        
        return new Duel(participant1ID, participant2ID, participant1Rating, participant2Rating);
    }

}
