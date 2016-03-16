
package mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SongMapper
{
    public Boolean insert(Connection connection, String name) {
        try
        {
            String insertTableSQL = "INSERT INTO ML_SONG_TBL"
                    + "(SONG_ID, SONG_NAME, SONG_YOUTUBE_LINK, SONG_LADDER, SONG_WINS, SONG_DRAWS, SONG_LOSSES) VALUES"
                    + "(song_id.nextval, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, name );
            preparedStatement.setString(2, "");
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            // execute insert SQL stetement
            preparedStatement .executeUpdate();
            
            System.out.println("Inserted into song table!");
            
            Integer userId = null;
            String selectSQL = "SELECT SONG_ID FROM ML_SONG_TBL WHERE SONG_NAME = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            while ( rs.next() ) {
                    userId = rs.getInt("SONG_ID");
            }
            
            System.out.println("Found : ID : " + userId);
            
            insertTableSQL = "INSERT INTO ML_SONG_RANKING_TBL"
                    + "(SONG_ID, SONG_CURRENTRATING, SONG_PREVIOUSRATING, SONG_RANKCHANGE) VALUES"
                    + "(?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertTableSQL);
            preparedStatement.setInt(1, userId );
            preparedStatement.setFloat(2, 1000f);
            preparedStatement.setFloat(3, 1000f);
            preparedStatement.setInt(4, 0);
            // execute insert SQL stetement
            preparedStatement .executeUpdate();
            
            System.out.println("Insert into Song ranking table!");
        }
        catch (SQLException ex)
        {
            System.out.println("Failed to insert");
            System.out.println(ex);
            //Logger.getLogger(SongMapper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
