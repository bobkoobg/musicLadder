package mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SongMapper
{
    public Boolean insertNewSong(Logger logger, Connection connection, String name)
    {
        PreparedStatement preparedStatement;
        String insertTableSQL;

        //Step 1 - Insert a new Song into Song Table
        try
        {
            insertTableSQL = "INSERT INTO ML_SONG_TBL"
                    + "(SONG_ID, SONG_NAME, SONG_YOUTUBE_LINK, SONG_LADDER, "
                    + "SONG_WINS, SONG_DRAWS, SONG_LOSSES) VALUES"
                    + "(song_id.nextval, ?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, "");
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            logger.severe("SQL Exception while inserting song " + name + " into song table " + e);
            return false;
        }

        //Step 2 - Get the ID of the inserted song
        Integer userId = null;
        try
        {
            String selectSQL = "SELECT SONG_ID FROM ML_SONG_TBL WHERE SONG_NAME = ?";

            preparedStatement = connection.prepareStatement(selectSQL);

            preparedStatement.setString(1, name);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
            {
                userId = rs.getInt("SONG_ID");
            }
        }
        catch (SQLException e)
        {
            logger.severe("SQL Exception while inserting song " + name + " into song table " + e);
            return false;
        }

        //Step 3 - Insert a new record into Song Ranking Table 
        try
        {
            insertTableSQL = "INSERT INTO ML_SONG_RANKING_TBL"
                    + "(SONG_ID, SONG_CURRENTRATING, SONG_PREVIOUSRATING, SONG_RANKCHANGE) VALUES"
                    + "(?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertTableSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.setFloat(2, 1000f);
            preparedStatement.setFloat(3, 1000f);
            preparedStatement.setInt(4, 0);
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            logger.severe("SQL Exception while inserting song " + name + " into song table " + e);
            return false;
        }
        finally // must close statement
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
                return false;
            }
        }

        System.out.println("Insert into Song ranking table!");

        logger.info("Successfully inserted song with name " + name);
        return true;
    }
}
