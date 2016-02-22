package entity;

import java.util.Date;

public class Duel
{

    private Integer duelID;
    private Integer song1ID;
    private Integer song2ID;
    private float song1OldRating;
    private float song2OldRating;
    private Integer song1Score;
    private Integer song2Score;
    private float song1NewRating;
    private float song2NewRating;
    private Date matchTime;

    public Duel(Integer duelID, Integer song1ID, Integer song2ID,
            float song1OldRating, float song2OldRating, Integer song1Score,
            Integer song2Score, float song1NewRating, float song2NewRating)
    {
        this.duelID = duelID;
        this.song1ID = song1ID;
        this.song2ID = song2ID;
        this.song1OldRating = song1OldRating;
        this.song2OldRating = song2OldRating;
        this.song1Score = song1Score;
        this.song2Score = song2Score;
        this.song1NewRating = song1NewRating;
        this.song2NewRating = song2NewRating;
        this.matchTime = new Date();
    }

    /*
     * Minimal requirements constructor
     */
    public Duel(Integer duelID, Integer song1ID, Integer song2ID, float song1OldRating, float song2OldRating)
    {
        this.duelID = duelID;
        this.song1ID = song1ID;
        this.song2ID = song2ID;
        this.song1OldRating = song1OldRating;
        this.song2OldRating = song2OldRating;
        this.matchTime = new Date();
    }

    public Integer getSong1ID()
    {
        return song1ID;
    }

    public void setSong1ID(Integer song1ID)
    {
        this.song1ID = song1ID;
    }

    public Integer getSong2ID()
    {
        return song2ID;
    }

    public void setSong2ID(Integer song2ID)
    {
        this.song2ID = song2ID;
    }

    public float getSong1OldRating()
    {
        return song1OldRating;
    }

    public void setSong1OldRating(float song1OldRating)
    {
        this.song1OldRating = song1OldRating;
    }

    public float getSong2OldRating()
    {
        return song2OldRating;
    }

    public void setSong2OldRating(float song2OldRating)
    {
        this.song2OldRating = song2OldRating;
    }

    public Integer getSong1Score()
    {
        return song1Score;
    }

    public void setSong1Score(Integer song1Score)
    {
        this.song1Score = song1Score;
    }

    public Integer getSong2Score()
    {
        return song2Score;
    }

    public void setSong2Score(Integer song2Score)
    {
        this.song2Score = song2Score;
    }

    public float getSong1NewRating()
    {
        return song1NewRating;
    }

    public void setSong1NewRating(float song1NewRating)
    {
        this.song1NewRating = song1NewRating;
    }

    public float getSong2NewRating()
    {
        return song2NewRating;
    }

    public void setSong2NewRating(float song2NewRating)
    {
        this.song2NewRating = song2NewRating;
    }

    @Override
    public String toString()
    {
        return "Duel{" + "duelID=" + duelID + ", song1ID=" + song1ID
                + ", song2ID=" + song2ID + ", song1OldRating=" + song1OldRating
                + ", song2OldRating=" + song2OldRating + ", song1Score=" + song1Score
                + ", song2Score=" + song2Score + ", song1NewRating=" + song1NewRating
                + ", song2NewRating=" + song2NewRating + ", matchTime=" + matchTime + '}';
    }

}
