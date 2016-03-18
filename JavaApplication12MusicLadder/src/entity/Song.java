package entity;

import java.util.List;

public class Song
{

    private Integer id;
    private String name;
    private String alias;
    private String youtubeLink;
    private Integer ladderId;
    private Integer wins;
    private Integer draws;
    private Integer loses;
    private float currentRating;
    private float formerRating;
    private List<String> genres;
    private List<String> singers;
    private String description;
    private List<Duel> matches;

    public Song(Integer id, String name, String alias, Integer wins, Integer draws, Integer loses, float currentRating, float formerRating, List<String> genres, List<String> singers, String description, List<Duel> matches)
    {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.wins = wins;
        this.draws = draws;
        this.loses = loses;
        this.currentRating = currentRating;
        this.formerRating = formerRating;
        this.genres = genres;
        this.singers = singers;
        this.description = description;
        this.matches = matches;
    }

    /*
     * Minimal requirements constructor
     */
    public Song(Integer id, String name, Integer wins, Integer draws, Integer loses, float currentRating, float formerRating)
    {
        this.id = id;
        this.name = name;
        this.wins = wins;
        this.draws = draws;
        this.loses = loses;
        this.currentRating = currentRating;
        this.formerRating = formerRating;
    }
    
    /*
     * Minimal requirements constructor
     */
    public Song(Integer id, String name)
    {
        this.id = id;
        this.name = name;
        this.wins = 0;
        this.draws = 0;
        this.loses = 0;
        this.currentRating = 1000;
        this.formerRating = 1000;
    }

    public Song()
    {
    }
    
    
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    public Integer getWins()
    {
        return wins;
    }

    public void setWins(Integer wins)
    {
        this.wins = wins;
    }
    
    public void incrementWins() {
        this.wins++;
    }

    public Integer getDraws()
    {
        return draws;
    }

    public void setDraws(Integer draws)
    {
        this.draws = draws;
    }
    
    public void incremenetDraws() {
        this.draws++;
    }

    public Integer getLoses()
    {
        return loses;
    }

    public void setLoses(Integer loses)
    {
        this.loses = loses;
    }
    
    public void incrementLoses() {
        this.loses++;
    }
    
    public Integer getAmmountOfMatches() {
        return ( this.wins + this.draws + this.loses );
    }

    public float getCurrentRating()
    {
        return currentRating;
    }

    public void setCurrentRating(float currentRating)
    {
        this.currentRating = currentRating;
    }

    public float getFormerRating()
    {
        return formerRating;
    }

    public void setFormerRating(float formerRating)
    {
        this.formerRating = formerRating;
    }

    public List<String> getGenres()
    {
        return genres;
    }

    public void setGenres(List<String> genres)
    {
        this.genres = genres;
    }

    public List<String> getSingers()
    {
        return singers;
    }

    public void setSingers(List<String> singers)
    {
        this.singers = singers;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<Duel> getMatches()
    {
        return matches;
    }

    public void setMatches(List<Duel> matches)
    {
        this.matches = matches;
    }

    public Integer getLadderId()
    {
        return ladderId;
    }

    public void setLadderId(Integer ladderId)
    {
        this.ladderId = ladderId;
    }

    public String getYoutubeLink()
    {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink)
    {
        this.youtubeLink = youtubeLink;
    }

    @Override
    public String toString()
    {
        return "Song{" + "id=" + id + ", name=" + name + ", alias=" + alias 
                + ", wins=" + wins + ", draws=" + draws + ", loses=" + loses 
                + ", currentRating=" + currentRating + ", formerRating=" 
                + formerRating + ", genres=" + genres + ", singers=" + singers 
                + ", description=" + description + ", matches=" + matches + '}';
    }

    

}
