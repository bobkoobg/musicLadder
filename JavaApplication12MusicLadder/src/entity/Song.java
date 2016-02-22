package entity;

import java.util.List;

public class Song
{

    private Integer id;
    private String name;
    private String alias;
    private float currentRating;
    private float formerRating;
    private List<String> genres;
    private List<String> singers;
    private String description;
    private List<Duel> matches;

    public Song(Integer ID, String name, String alias, float currentRating,
            float formerRating, List<String> genres, List<String> singers,
            String description, List<Duel> matches)
    {
        this.id = ID;
        this.name = name;
        this.alias = alias;
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
    public Song(Integer ID, String name, float currentRating, float formerRating)
    {
        this.id = ID;
        this.name = name;
        this.currentRating = currentRating;
        this.formerRating = formerRating;
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

    @Override
    public String toString()
    {
        return "Song{" + "id=" + id + ", name=" + name + ", alias=" + alias
                + ", currentRating=" + currentRating + ", formerRating="
                + formerRating + ", genres=" + genres + ", singers=" + singers
                + ", description=" + description + ", matches=" + matches + '}';
    }

}
