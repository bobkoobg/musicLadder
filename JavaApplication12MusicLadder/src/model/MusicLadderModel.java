
package model;

import entity.Song;
import java.util.ArrayList;
import java.util.List;

public class MusicLadderModel
{   
    public List<Song> getSongs() {
        List<Song> songs = new ArrayList();
        
        Song s1 = new Song(0001,"Carly Rae Jepsen - Call Me Maybe", 950, 1000);
        Song s2 = new Song(0002, "KOLLEGAH - John Gotti (prod. von Alexis Troy)", 1150, 1120);
        
        songs.add(s1);
        songs.add(s2);
        
        return songs;
    }
}
