package mapper;

import entity.Duel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class DuelMapper
{
    public Duel insertNewDuel(Logger logger, Connection connection, Duel duel)
    {
        PreparedStatement preparedStatement = null;
        String insertTableSQL;
        Integer duelID = null;
        String SQLString = "select DUEL_ID.nextval from dual";
        PreparedStatement statement = null;
        
        
        try {
            statement = connection.prepareStatement(SQLString);
            ResultSet rs = statement.executeQuery();
            try {
                if (rs.next()) {
                    duelID = rs.getInt(1);
                }
            }
            finally {
                try { rs.close(); } catch (Exception ignore) { }
            }
        } catch (Exception e) {
            logger.severe("Statement Exception while trying to close the prepared statement while taking next duel id " + e);
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
                logger.severe("SQL Exception while trying to close the prepared statement while taking next duel id " + e);
                return null;
            }
        }
        
        if ( duelID != null ) {
            duel.setDuelID( duelID );
            
            java.util.Date date = new java.util.Date();
            long t = date.getTime();
            java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);
            //System.out.println("sqlTimestamp=" + sqlTimestamp); << Includes miliseconds (looks ugly)
            
            try
            {
                insertTableSQL = "INSERT INTO ML_DUEL_TBL VALUES"
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement(insertTableSQL);
                
                preparedStatement.setInt( 1, duel.getDuelID() );
                preparedStatement.setInt( 2, duel.getSong1ID() );
                preparedStatement.setInt( 3, duel.getSong2ID() );
                preparedStatement.setFloat( 4, duel.getSong1BeforeMatchRating() );
                preparedStatement.setFloat( 5, duel.getSong2BeforeMatchRating() );
                preparedStatement.setNull( 6, java.sql.Types.INTEGER );
                preparedStatement.setNull( 7, java.sql.Types.INTEGER );
                preparedStatement.setNull( 8, java.sql.Types.FLOAT );
                preparedStatement.setNull( 9, java.sql.Types.FLOAT );
                preparedStatement.setTimestamp(10, sqlTimestamp );

                // execute insert SQL stetement
                preparedStatement.executeUpdate();
            }
            catch (SQLException e)
            {
                logger.severe("SQL Exception while inserting duel " + duel.toString() + " into duel table " + e);
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
                    logger.severe("SQL Exception while trying to close the prepared statement while inserting duel into duel table " + e);
                    return null;
                }
            }

            logger.info( "Successfully inserted duel, ID : " + duel.getDuelID() );
            return duel;
        }
        return null;
    }
    
//    private static java.sql.Date getCurrentDate() {
//        
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
//        
//        java.util.Date today = new java.util.Date();
//        
//        today.
//        return new java.sql.Date(dateFormat.format(date));
//    }
    
    public Boolean wipeDatabase(Connection connection, Logger logger) {
        Statement statement = null;
        String sql;
        String[] databasesToWipe = {"ML_DUEL_TBL" };
        
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
