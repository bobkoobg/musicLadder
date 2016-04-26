package utilities;

import entity.Song;
import java.util.Comparator;

public class SongRatingComparator implements Comparator<Song> {

    @Override
    public int compare( Song song1, Song song2 ) {
        Float song1Rating = song1.getCurrentRating();
        Float song2Rating = song2.getCurrentRating();

        int song1Matches = (song1.getWins() + song1.getDraws() + song1.getLoses());
        int song2Matches = (song2.getWins() + song2.getDraws() + song2.getLoses());

        //If the rating is 1000, then the song should be treated as low rated, but
        //only if it the sum of the song's matches is 0.
        boolean isSong1Newbie = song1Rating == 1000 && song1Matches == 0 ? true : false;
        boolean isSong2Newbie = song2Rating == 1000 && song2Matches == 0 ? true : false;

        if ( isSong1Newbie && !isSong2Newbie ) {
            return -1;
        } else if ( !isSong1Newbie && isSong2Newbie ) {
            return 1;
        }

        //ascending order (lower rating first)
        //return Float.compare( song1Rating, song2Rating );
        //descending order (higher rating first)
        return Float.compare( song2Rating, song1Rating );
    }
}
