
package model;

import entity.Song;
import java.util.ArrayList;
import java.util.List;

public class MusicLadderModel
{   
    public List<Song> getSongs() {
        List<Song> songs = new ArrayList();
        
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
    
        return songs;
    }
}
