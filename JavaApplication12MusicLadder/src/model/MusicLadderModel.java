
package model;

import entity.Duel;
import entity.Song;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MusicLadderModel
{
    List<Song> songs;
    List<Duel> duels;

    public MusicLadderModel()
    {
        songs = new ArrayList();
        duels = new ArrayList();
        
        Song s1 = new Song( 1 ,"Carly Rae Jepsen - Call Me Maybe", 0, 2, 5, 950, 1000); //7
        Song s2 = new Song( 2, "KOLLEGAH - John Gotti (prod. von Alexis Troy)", 3, 1, 0, 1150, 1120); //4
        Song s22 = new Song( 7 , "Limitless - Limitless", 3, 1, 6, 930, 940); //10
        Song s3 = new Song( 3 , "Krisko - Nqma kvo da stane", 53, 23, 45, 1200, 1185); //121
        Song s4 = new Song( 4 , "Geri Nikol - Ela i si vzemi", 17, 2, 8, 1250, 1268); //27
        Song s44 = new Song( 8 , "Geri Nikol - Momiche kato men", 17, 3, 5, 1275, 1255); //25
        Song s5 = new Song( 5 , "Grafa - Nai-shtastliviqt chovek", 4, 3, 7, 970, 985); //14
        Song s6 = new Song( 6 , "Upsurt - Punta", 25, 0, 25, 1005, 990); //50
        Song s66 = new Song( 9, "Krisko - horata govorqt", 0, 0, 0, 1000, 1000); //0
        
        songs.add(s1);
        songs.add(s2);
        songs.add(s3);
        songs.add(s4);
        songs.add(s5);
        songs.add(s6);
        songs.add(s22);
        songs.add(s44);
        songs.add(s66);
    }
    
    
    public List<Song> getSongs() {
        Collections.sort( songs, new SongRatingComparator() );
        return songs;
    }
    
    public Integer getAmountOfDuels() {
        return duels.size();
    }
    
    public Boolean addDuel(Duel duel) {
        duels.add(duel);
        return true;
    }
    
    public Song getSongByID(Integer songId) {
        Song song = null;
        for (int i = 0; i < songs.size(); i++)
        {
            if( songs.get(i).getId() == songId ) {
                song = songs.get(i);
                break;
            }
        }
        return song;
    }
    
    public Boolean saveSong( Song song ) {
        for (int i = 0; i < songs.size(); i++)
        {
            if (song.getId() == songs.get(i).getId()) {
                songs.set(i, song);
                return true;
            }
        }
        return false;
    }
    
    public Integer getDuelsSum() {
        Integer sum = 0;
        for (int i = 0; i < songs.size(); i++)
        {
            sum += songs.get(i).getAmmountOfMatches();
        }
        return sum;
    }
    
    public Integer getDuelsMatchMax() {
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
    
    class SongRatingComparator implements Comparator<Song> {

        @Override
        public int compare(Song o1, Song o2)
        {
            Float song1Rating = o1.getCurrentRating();
            Float song2Rating = o2.getCurrentRating();

            //ascending order
            //return Float.compare( song1Rating, song2Rating );

            //descending order
            return Float.compare( song2Rating, song1Rating );
        }
        
    }
}
