package model;

import entity.Song;
import java.util.List;

public class MusicLadderModel
{    
    public Integer getDuelsMatchMax(List<Song> songs) {
        Integer max = 0, currentMax = 0;
        for (int i = 0; i < songs.size(); i++)
        {
            currentMax = songs.get(i).getAmmountOfMatches();
            if( max < currentMax ) {
                max = currentMax;
            }
        }
        return max;
    }
}
