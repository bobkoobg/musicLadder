
package model;

import entity.Duel;
import entity.Song;
import java.util.ArrayList;
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
    }
      
    public List<Song> getSongs() {
        Collections.sort( songs, new SongRatingComparator() );
        return songs;
    }
    
    public List<Duel> getPlayedDuels() {
        List<Duel> playedDuels = new ArrayList();

        for(Duel d : duels) {
            if( d.getSong1Score() != null && d.getSong2Score() != null
                && d.getSong1AfterMatchRating() != 0.0f && d.getSong2AfterMatchRating() != 0.0f ) {
                playedDuels.add(d);
            }
        }
        return playedDuels;
    }

    public void setSongs(List<Song> songs)
    {
        this.songs = songs;
    }

    public void setDuels(List<Duel> duels)
    {
        this.duels = duels;
    }
    
    public void joinDuelsLists(List<Duel> duels) {
        this.duels.addAll(duels);
    }
    
    public List<Duel> getDuels() {
        return duels;
    }
    
    public Integer getSongsCount() {
        return songs.size();
    }
    
    public Song getSongByID(Integer songId) {
        Song song = null;
        for (int i = 0; i < songs.size(); i++)
        {
            if( songId == songs.get(i).getId().intValue() ) {
                song = songs.get(i);
                System.out.println("found it!");
                break;
            }
        }
        return song;
    }
    
    public Boolean updateSong( Song song ) {
        for (int i = 0; i < songs.size(); i++)
        {
            if ( song.getId().intValue() == songs.get(i).getId().intValue() ) {
                songs.set(i, song);
                return true;
            }
        }
        return false;
    }
    
    public Boolean saveSong( Song song ) {
        songs.add(song);
        return true;
    }
    
    public List<Duel> getDuels( Integer amount ) {
        List<Duel> playedDuels = new ArrayList();
        for (int i = 0; i < duels.size(); i++)
        {
            if( duels.get(i).getSong1AfterMatchRating() != 0.0f &&
                    duels.get(i).getSong2AfterMatchRating() != 0.0f ) {
                playedDuels.add( duels.get(i) );
            }
        }
        Collections.sort( playedDuels, new DuelRatingComparator() );
        return playedDuels;
    }
    
    public Integer getAmountOfDuels() {
        return duels.size();
    }
    
    public Boolean addDuel(Duel duel) {
        duels.add(duel);
        return true;
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
    
    //cleanup
    public void clearSongs() {
        songs = new ArrayList<Song>();
    }
    
    public void clearDuels() {
        duels = new ArrayList<Duel>();
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
            if( song2Rating == 1000 && song1Rating != 1000 ) {
                return -1;
            } else if( song2Rating != 1000 && song1Rating == 1000 ) {
                return 1;
            } 
            return Float.compare( song2Rating, song1Rating );
        }
        
    }
    
    class DuelRatingComparator implements Comparator<Duel> {

        @Override
        public int compare(Duel o1, Duel o2)
        {
            Integer song1Rating = o1.getDuelID();
            Integer song2Rating = o2.getDuelID();

            //ascending order
            //return Integer.compare( song1Rating, song2Rating );

            //descending order
            return Integer.compare( song2Rating, song1Rating );
        }
        
    }
}
