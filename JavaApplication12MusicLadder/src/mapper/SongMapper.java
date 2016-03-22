package mapper;

import entity.Song;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SongMapper
{
    public Integer insertNewSong(Logger logger, Connection connection, String name)
    {
        PreparedStatement preparedStatement = null;
        String insertTableSQL;
        Integer songID = null;

        //Step 1 - Extract next SONG ID
        try {
            String SQLString = "select SONG_ID.nextval from dual";
            preparedStatement = connection.prepareStatement(SQLString);
            ResultSet rs = preparedStatement.executeQuery();
            try {
                if (rs.next()) {
                    songID = rs.getInt(1);
                }
            }
            finally {
                try { rs.close(); } catch (Exception ignore) { }
            }
        } catch (Exception e) {
            logger.severe("Statement Exception while trying to close the prepared statement while taking next song id " + e);
            return -1;
        } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
            }
            catch (SQLException e)
            {
                logger.severe("SQL Exception while trying to close the prepared statement while taking next song id " + e);
                return -1;
            }
        }
        
        if( songID != null ) {
            //Step 2 - Insert a new Song into Song Table
            try
            {
                insertTableSQL = "INSERT INTO ML_SONG_TBL"
                        + "(SONG_ID, SONG_NAME, SONG_YOUTUBE_LINK, SONG_LADDER, "
                        + "SONG_WINS, SONG_DRAWS, SONG_LOSSES) VALUES"
                        + "(?, ?, ?, ?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement(insertTableSQL);

                preparedStatement.setInt(1, songID);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, "");
                preparedStatement.setInt(4, 1);
                preparedStatement.setInt(5, 0);
                preparedStatement.setInt(6, 0);
                preparedStatement.setInt(7, 0);

                // execute insert SQL stetement
                preparedStatement.executeUpdate();
            }
            catch (SQLException e)
            {
                logger.severe("SQL Exception while inserting song " + name + " into song table " + e);
                return -1;
            } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
            {
                try
                {
                    if (preparedStatement != null)
                    {
                        preparedStatement.close();
                    }
                }
                catch (SQLException e)
                {
                    logger.severe("SQL Exception while trying to close the prepared statement song " + e);
                    return -1;
                }
            }

            //Step 2 - Insert a new record into Song Ranking Table 
            try
            {
                insertTableSQL = "INSERT INTO ML_SONG_RANKING_TBL"
                        + "(SONG_ID, SONG_CURRENTRATING, SONG_PREVIOUSRATING, SONG_RANKCHANGE) VALUES"
                        + "(?, ?, ?, ?)";
                
                preparedStatement = connection.prepareStatement(insertTableSQL);
                
                preparedStatement.setInt(1, songID);
                preparedStatement.setFloat(2, 1000f);
                preparedStatement.setFloat(3, 1000f);
                preparedStatement.setInt(4, 0);
                
                // execute insert SQL stetement
                preparedStatement.executeUpdate();
            }
            catch (SQLException e)
            {
                logger.severe("SQL Exception while inserting song " + name + " into song table " + e);
                return -1;
            }
            finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
            {
                try
                {
                    if (preparedStatement != null)
                    {
                        preparedStatement.close();
                    }
                }
                catch (SQLException e)
                {
                    logger.severe("SQL Exception while trying to close the prepared statement song " + e);
                    return -1;
                }
            }

            logger.info("Successfully inserted song with name " + name);
            return songID;
        }
        logger.severe("SQL Sequence error ID is NULL");
        return -1;
    }
    
    public List<Song> getAllSongs(Logger logger, Connection connection, Integer ladderId) {
        List<Song> songs = new ArrayList();
        PreparedStatement preparedStatement = null;
        Song song = null;
        
        try {
            String SQLString = "SELECT * "
                    + "FROM ML_SONG_TBL song "
                    + "JOIN ML_SONG_RANKING_TBL songranking "
                    + "ON song.song_id = songranking.song_id "
                    + "WHERE song.SONG_LADDER = ?";
            
            preparedStatement = connection.prepareStatement(SQLString);
            preparedStatement.setInt(1, ladderId);
            
            ResultSet rs = preparedStatement.executeQuery();
            try {
                while(rs.next()) {
                    
                        song = new Song();

                        song.setId(rs.getInt(1));
                        song.setName(rs.getString(2));
                        song.setLadderId(rs.getInt(4));
                        song.setWins(rs.getInt(5));
                        song.setDraws(rs.getInt(6));
                        song.setLoses(rs.getInt(7));
                        song.setCurrentRating(rs.getFloat(9));
                        song.setFormerRating(rs.getFloat(10));

                        songs.add(song);
                    } 
                
            }
            finally {
                try { rs.close(); } catch (Exception ignore) { }
            }
        } catch (Exception e) {
            logger.severe("Statement Exception while trying to close the prepared statement while taking next song id " + e);
            return null;
        } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
            }
            catch (SQLException e)
            {
                logger.severe("SQL Exception while trying to close the prepared statement while taking next song id " + e);
                return null;
            }
        }
        return songs;
    }
    
    public Song getSong(Logger logger, Connection connection, Integer songId) {
        Song song = new Song();
        PreparedStatement preparedStatement = null;
        
        try {
            String SQLString = "SELECT * FROM ML_SONG_TBL song " +
                    "JOIN ML_SONG_RANKING_TBL songranking " +
                    "ON song.song_id = songranking.song_id " +
                    "WHERE song.SONG_ID = ?";
            
            preparedStatement = connection.prepareStatement(SQLString);
            preparedStatement.setInt(1, songId);
            
            ResultSet rs = preparedStatement.executeQuery();
            try {
                while(rs.next()) {

                        song.setId(rs.getInt(1));
                        song.setName(rs.getString(2));
                        song.setLadderId(rs.getInt(4));
                        song.setWins(rs.getInt(5));
                        song.setDraws(rs.getInt(6));
                        song.setLoses(rs.getInt(7));
                        song.setCurrentRating(rs.getFloat(9));
                        song.setFormerRating(rs.getFloat(10));
                    } 
            }
            finally {
                try { rs.close(); } catch (Exception ignore) { }
            }
        } catch (Exception e) {
            logger.severe("Statement Exception (getSong) " + e);
            return null;
        } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
            }
            catch (SQLException e)
            {
                logger.severe("SQL Exception (getSong) " + e);
                return null;
            }
        }
        
        
        return song;
    }
    
    public Boolean wipeDatabase(Connection connection, Logger logger) {
        Statement statement = null;
        String sql;
        String[] databasesToWipe = {"ML_SONG_RANKING_TBL", "ML_SONG_TBL" };
        
        for (int i = 0; i < databasesToWipe.length; i++)
        {
            try
            {
                statement = connection.createStatement();
                sql = "DELETE FROM " + databasesToWipe[i]; 
                statement.executeUpdate(sql);
            }
            catch (SQLException e)
            {
                logger.severe("SQL Exception while trying to wipe out table " + databasesToWipe[i] + " : " + e);
                return false;
            } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
            {
                try
                {
                    if (statement != null)
                    {
                        statement.close();
                    }
                }
                catch (SQLException e)
                {
                    logger.severe("[SQL Exception while trying to close the prepared statement song for wiping out table " + databasesToWipe[i] + " : " + e);
                    return false;
                }
            }
            logger.info("Successfully wiped out database " + databasesToWipe[i]);
        
        }
        return true;
    }
}
