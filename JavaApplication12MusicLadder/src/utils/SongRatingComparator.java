package utils;

import entity.Song;
import java.util.Comparator;

public class SongRatingComparator implements Comparator<Song>
{

    @Override
    public int compare(Song o1, Song o2)
    {
        Float song1Rating = o1.getCurrentRating();
        Float song2Rating = o2.getCurrentRating();

		
        //If the rating is 1000, then the song should be treated as low rated.		
        if (song2Rating == 1000 && song1Rating != 1000)
        {
            return -1;
        }
        else if (song2Rating != 1000 && song1Rating == 1000)
        {
            return 1;
        }
        
        //ascending order		
        //return Float.compare( song1Rating, song2Rating );
        
        //descending order
        return Float.compare(song2Rating, song1Rating);
    }

}
