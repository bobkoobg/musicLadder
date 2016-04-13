package entity;

import java.util.Date;

public class Duel {

    private Integer duelID;
    private Integer song1ID;
    private Integer song2ID;
    private float song1BeforeMatchRating;
    private float song2BeforeMatchRating;
    private Integer song1Score;
    private Integer song2Score;
    private float song1AfterMatchRating;
    private float song2AfterMatchRating;
    private Date matchTime;
    private String song1Name;
    private String song2Name;

    /*
     * Duel generation constructor
     */
    public Duel( Integer song1ID, Integer song2ID, float song1BeforeMatchRating, float song2BeforeMatchRating ) {
        this.song1ID = song1ID;
        this.song2ID = song2ID;
        this.song1BeforeMatchRating = song1BeforeMatchRating;
        this.song2BeforeMatchRating = song2BeforeMatchRating;
        this.matchTime = new Date();
    }

    //Testing constructor
    public Duel( float song1BeforeMatchRating, float song2BeforeMatchRating, Integer song1Score, Integer song2Score ) {
        this.song1BeforeMatchRating = song1BeforeMatchRating;
        this.song2BeforeMatchRating = song2BeforeMatchRating;
        this.song1Score = song1Score;
        this.song2Score = song2Score;
    }

    public Duel() {
    }

    public Integer getDuelID() {
        return duelID;
    }

    public void setDuelID( Integer duelID ) {
        this.duelID = duelID;
    }

    public Integer getSong1ID() {
        return song1ID;
    }

    public void setSong1ID( Integer song1ID ) {
        this.song1ID = song1ID;
    }

    public Integer getSong2ID() {
        return song2ID;
    }

    public void setSong2ID( Integer song2ID ) {
        this.song2ID = song2ID;
    }

    public float getSong1BeforeMatchRating() {
        return song1BeforeMatchRating;
    }

    public void setSong1BeforeMatchRating( float song1OldRating ) {
        this.song1BeforeMatchRating = song1OldRating;
    }

    public float getSong2BeforeMatchRating() {
        return song2BeforeMatchRating;
    }

    public void setSong2BeforeMatchRating( float song2OldRating ) {
        this.song2BeforeMatchRating = song2OldRating;
    }

    public Integer getSong1Score() {
        return song1Score;
    }

    public void setSong1Score( Integer song1Score ) {
        this.song1Score = song1Score;
    }

    public Integer getSong2Score() {
        return song2Score;
    }

    public void setSong2Score( Integer song2Score ) {
        this.song2Score = song2Score;
    }

    public float getSong1AfterMatchRating() {
        return song1AfterMatchRating;
    }

    public void setSong1AfterMatchRating( float song1NewRating ) {
        this.song1AfterMatchRating = song1NewRating;
    }

    public float getSong2AfterMatchRating() {
        return song2AfterMatchRating;
    }

    public void setSong2AfterMatchRating( float song2NewRating ) {
        this.song2AfterMatchRating = song2NewRating;
    }

    public void setSong1Name( String song1Name ) {
        this.song1Name = song1Name;
    }

    public void setSong2Name( String song2Name ) {
        this.song2Name = song2Name;
    }

    public String getSong1Name() {
        return song1Name;
    }

    public String getSong2Name() {
        return song2Name;
    }

    @Override
    public String toString() {
        return "Duel{" + "duelID=" + duelID + ", song1ID=" + song1ID + ", song2ID="
                + song2ID + ", song1BeforeMatchRating=" + song1BeforeMatchRating
                + ", song2BeforeMatchRating=" + song2BeforeMatchRating + ", song1Score="
                + song1Score + ", song2Score=" + song2Score + ", song1AfterMatchRating="
                + song1AfterMatchRating + ", song2AfterMatchRating=" + song2AfterMatchRating
                + ", matchTime=" + matchTime + ", song1Name=" + song1Name + ", song2Name="
                + song2Name + '}';
    }

}
