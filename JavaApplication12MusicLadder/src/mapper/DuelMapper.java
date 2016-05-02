package mapper;

import entity.Duel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DuelMapper {

    private static String ERROR_NOELEM = "Error - not element found";
    private static String ERROR_QUERYEXEC = "Error - during query execution";
    private Integer duelID;

    public Integer getDuelID() {
        return duelID;
    }

    public <T> T getDuel( Logger logger, Connection connection, int duelID ) {
        PreparedStatement preparedStatement = null;
        Duel duel = null;

        try {
            String sqlQuery = "SELECT duel.DUEL_ID, duel.SONG1_ID, duel.SONG2_ID, duel.SONG1_RATING_BEFORE, "
                    + "duel.SONG2_RATING_BEFORE, songA.SONG_NAME, songB.SONG_NAME, duel.SONG1_RATING_AFTER, "
                    + "duel.SONG2_RATING_AFTER, duel.SONG1_SCORE, duel.SONG2_SCORE "
                    + "FROM ML_DUEL_TBL duel "
                    + "JOIN ML_SONG_TBL songA "
                    + "ON duel.SONG1_ID = songA.SONG_ID "
                    + "JOIN ML_SONG_TBL songB "
                    + "ON duel.SONG2_ID = songB.SONG_ID "
                    + "WHERE DUEL_ID = ?";

            preparedStatement = connection.prepareStatement( sqlQuery );
            preparedStatement.setInt( 1, duelID );

            ResultSet rs = preparedStatement.executeQuery();
            try {
                if ( rs.next() ) {

                    duel = new Duel();

                    duel.setDuelID( rs.getInt( 1 ) );
                    duel.setSong1ID( rs.getInt( 2 ) );
                    duel.setSong2ID( rs.getInt( 3 ) );
                    duel.setSong1BeforeMatchRating( rs.getFloat( 4 ) );
                    duel.setSong2BeforeMatchRating( rs.getFloat( 5 ) );
                    duel.setSong1Name( rs.getString( 6 ) );
                    duel.setSong2Name( rs.getString( 7 ) );
                    duel.setSong1AfterMatchRating( rs.getFloat( 8 ) );
                    duel.setSong2AfterMatchRating( rs.getFloat( 9 ) );
                    duel.setSong1Score( rs.getInt( 10 ) );
                    duel.setSong2Score( rs.getInt( 11 ) );
                } else {
                    logger.warning( ERROR_NOELEM + " duel ID : " + duelID );
                    return ( T ) ( Boolean ) false;
                }
            } finally {
                try {
                    rs.close();
                } catch ( Exception e ) {
                    logger.warning( "Result set not closed succesfully : " + e );
                }
            }
        } catch ( Exception e ) {
            logger.warning( ERROR_QUERYEXEC + " duel ID : " + duelID );
            return ( T ) ( Boolean ) false;
        } finally {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.warning( "SQL Exception - Prepared statement not closed succesfully : " + e );
            }
        }
        return ( T ) duel;
    }

    public <T> T insertDuel( Logger logger, Connection connection, Duel duel ) {
        PreparedStatement preparedStatement = null;
        duelID = null;

        String SQLString = "select DUEL_ID.nextval from dual";
        try {
            preparedStatement = connection.prepareStatement( SQLString );
            ResultSet rs = preparedStatement.executeQuery();
            try {
                if ( rs.next() ) {
                    duelID = rs.getInt( 1 );
                }
            } finally {
                try {
                    rs.close();
                } catch ( Exception e ) {
                    logger.warning( "Exception - during retrieval of next duel id : " + e );
                }
            }
        } catch ( SQLException e ) {
            logger.severe( "Method insertDuel (Part1) - Execution SQL Exception : [ " + e + " ]" );
            return ( T ) ( Boolean ) false;
        } finally {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "Method insertDuel (Part1) - Closing SQL Exception : [ " + e + " ]" );
                return ( T ) ( Boolean ) false;
            }
        }

        if ( duelID != null ) {
            String insertTableSQL;

            duel.setDuelID( duelID );

            java.util.Date date = new java.util.Date();
            long t = 1000 * (date.getTime() / 1000);
            java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp( t );

            try {
                insertTableSQL = "INSERT INTO ML_DUEL_TBL VALUES"
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement( insertTableSQL );

                preparedStatement.setInt( 1, duel.getDuelID() );
                preparedStatement.setInt( 2, duel.getSong1ID() );
                preparedStatement.setInt( 3, duel.getSong2ID() );
                preparedStatement.setFloat( 4, duel.getSong1BeforeMatchRating() );
                preparedStatement.setFloat( 5, duel.getSong2BeforeMatchRating() );
                preparedStatement.setNull( 6, java.sql.Types.INTEGER );
                preparedStatement.setNull( 7, java.sql.Types.INTEGER );
                preparedStatement.setNull( 8, java.sql.Types.FLOAT );
                preparedStatement.setNull( 9, java.sql.Types.FLOAT );
                preparedStatement.setTimestamp( 10, sqlTimestamp );

                preparedStatement.executeUpdate();
            } catch ( SQLException e ) {
                logger.severe( "Method insertDuel (Part2) - Execution SQL Exception : [ " + e + " ], Duel content : [" + duel.toString() + "]" );
                return ( T ) ( Boolean ) false;
            } finally {
                try {
                    if ( preparedStatement != null ) {
                        preparedStatement.close();
                    }
                } catch ( SQLException e ) {
                    logger.severe( "Method insertDuel (Part2) - Closing SQL Exception : [ " + e + " ], Duel content : [" + duel.toString() + "]" );
                    return ( T ) ( Boolean ) false;
                }
            }

            logger.info( "Method insertDuel success! : [ Duel ID : " + duel.getDuelID() + "]" );
            return ( T ) duelID;
        }
        logger.severe( "Method insertDuel (Part1) - [ Duel ID : " + duelID + ", Duel : " + duel.toString() + " ]" );
        return ( T ) ( Boolean ) false;
    }

    public List<Duel> getNPlayedDuels( Logger logger, Connection connection, Integer amount ) {
        List<Duel> duels = new ArrayList();
        Statement preparedStatement = null;
        Duel duel = null;

        try {
            //this may become pain when you include ladders 
//            String SQLString = "SELECT * "
//                    + "FROM ML_DUEL_TBL "
//                    + "WHERE SONG1_SCORE IS NOT NULL "
//                    + "AND SONG2_SCORE IS NOT NULL "
//                    + "ORDER BY DUEL_ID DESC";

            String SQLString = "SELECT duel.DUEL_ID, duel.SONG1_ID, duel.SONG2_ID, duel.SONG1_RATING_BEFORE, "
                    + "duel.SONG2_RATING_BEFORE, duel.SONG1_SCORE, duel.SONG2_SCORE, duel.SONG1_RATING_AFTER, "
                    + "duel.SONG2_RATING_AFTER, duel.DUEL_DATETIME, songA.SONG_NAME, songB.SONG_NAME "
                    + "FROM ML_DUEL_TBL duel "
                    + "JOIN ML_SONG_TBL songA "
                    + "ON duel.SONG1_ID = songA.SONG_ID "
                    + "JOIN ML_SONG_TBL songB "
                    + "ON duel.SONG2_ID = songB.SONG_ID "
                    + "WHERE SONG1_SCORE IS NOT NULL "
                    + "AND SONG2_SCORE IS NOT NULL "
                    + "ORDER BY DUEL_ID DESC";

            preparedStatement = connection.createStatement();
            ResultSet rs = preparedStatement.executeQuery( SQLString );

            try {
                Integer loops = 0;
                while ( rs.next() ) {
                    loops++;
                    duel = new Duel();

                    duel.setDuelID( rs.getInt( 1 ) );
                    duel.setSong1ID( rs.getInt( 2 ) );
                    duel.setSong2ID( rs.getInt( 3 ) );
                    duel.setSong1BeforeMatchRating( rs.getFloat( 4 ) );
                    duel.setSong2BeforeMatchRating( rs.getFloat( 5 ) );
                    duel.setSong1Score( rs.getInt( 6 ) );
                    duel.setSong2Score( rs.getInt( 7 ) );
                    duel.setSong1AfterMatchRating( rs.getFloat( 8 ) );
                    duel.setSong2AfterMatchRating( rs.getFloat( 9 ) );
                    duel.setSong1Name( rs.getString( 11 ) );
                    duel.setSong2Name( rs.getString( 12 ) );
                    //and also the date... eventually!

                    duels.add( duel );
                    if ( loops.intValue() == amount ) {
                        break;
                    }
                }
            } finally {
                try {
                    rs.close();
                } catch ( Exception ignore ) {
                }
            }
        } catch ( Exception e ) {
            logger.severe( "Statement Exception while trying to close the prepared statement while taking next duel id " + e );
            return null;
        } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
        {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while trying to close the prepared statement while taking next duel id " + e );
                return null;
            }
        }
        return duels;
    }

    public List<Duel> getNDuelsToPlay( Logger logger, Connection connection, Integer amount ) {
        List<Duel> duels = new ArrayList();
        Statement preparedStatement = null;
        Duel duel = null;

        try {
            //this may become pain when you include ladders 
            String SQLString = "SELECT duel.DUEL_ID, duel.SONG1_ID, duel.SONG2_ID, duel.SONG1_RATING_BEFORE, "
                    + "duel.SONG2_RATING_BEFORE, songA.SONG_NAME, songB.SONG_NAME "
                    + "FROM ML_DUEL_TBL duel "
                    + "JOIN ML_SONG_TBL songA "
                    + "ON duel.SONG1_ID = songA.SONG_ID "
                    + "JOIN ML_SONG_TBL songB "
                    + "ON duel.SONG2_ID = songB.SONG_ID "
                    + "WHERE SONG1_SCORE IS NULL "
                    + "AND SONG2_SCORE IS NULL "
                    + "AND ROWNUM <= " + amount + " "
                    + "ORDER BY DUEL_ID ASC";

            preparedStatement = connection.createStatement();
            ResultSet rs = preparedStatement.executeQuery( SQLString );

            try {
                while ( rs.next() ) {
                    duel = new Duel();

                    duel.setDuelID( rs.getInt( 1 ) );
                    duel.setSong1ID( rs.getInt( 2 ) );
                    duel.setSong2ID( rs.getInt( 3 ) );
                    duel.setSong1BeforeMatchRating( rs.getFloat( 4 ) );
                    duel.setSong2BeforeMatchRating( rs.getFloat( 5 ) );
                    duel.setSong1Name( rs.getString( 6 ) );
                    duel.setSong2Name( rs.getString( 7 ) );
                    //and also the date... eventually!

                    duels.add( duel );
                }
            } finally {
                try {
                    rs.close();
                } catch ( Exception ignore ) {
                }
            }
        } catch ( Exception e ) {
            logger.severe( "Statement Exception while trying to close the prepared statement while taking next duel id (duels to play) " + e );
            return null;
        } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
        {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while trying to close the prepared statement while taking next duel id (duels to play)" + e );
                return null;
            }
        }
        return duels;
    }

    public boolean updateDuel( Logger logger, Connection connection, Duel duel ) {
        PreparedStatement preparedStatement = null;
        String insertTableSQL;

        java.util.Date date = new java.util.Date();
        long t = date.getTime();
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp( t );
        //System.out.println("sqlTimestamp=" + sqlTimestamp); << Includes miliseconds (looks ugly)

        try {
            insertTableSQL = "UPDATE ML_DUEL_TBL "
                    + "SET SONG1_SCORE = ?, SONG2_SCORE = ?, "
                    + "SONG1_RATING_AFTER = ?, SONG2_RATING_AFTER = ?, "
                    + "DUEL_DATETIME = ? "
                    + "WHERE DUEL_ID = ?";

            preparedStatement = connection.prepareStatement( insertTableSQL );

            preparedStatement.setInt( 1, duel.getSong1Score() );
            preparedStatement.setInt( 2, duel.getSong2Score() );
            preparedStatement.setFloat( 3, duel.getSong1AfterMatchRating() );
            preparedStatement.setFloat( 4, duel.getSong2AfterMatchRating() );
            preparedStatement.setTimestamp( 5, sqlTimestamp );
            preparedStatement.setInt( 6, duel.getDuelID() );

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            logger.severe( "SQL Exception while updating duel " + duel.toString() + " into duel table " + e );
            return false;
        } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
        {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while trying to close the prepared statement while updating duel into duel table " + e );
                return false;
            }
        }

        logger.info( "Successfully updated duel, ID : " + duel.getDuelID() );
        return true;
    }

    public boolean wipeDatabase( Connection connection, Logger logger ) {
        Statement statement = null;
        String sql;
        String[] databasesToWipe = { "ML_DUEL_TBL" };

        for ( int i = 0; i < databasesToWipe.length; i++ ) {
            try {
                statement = connection.createStatement();
                sql = "DELETE FROM " + databasesToWipe[ i ];
                statement.executeUpdate( sql );
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while trying to wipe out table " + databasesToWipe[ i ] + " : " + e );
                return false;
            } finally //The statement must be closed, because of : java.sql.SQLException: ORA-01000
            {
                try {
                    if ( statement != null ) {
                        statement.close();
                    }
                } catch ( SQLException e ) {
                    logger.severe( "[SQL Exception while trying to close the prepared statement song for wiping out table " + databasesToWipe[ i ] + " : " + e );
                    return false;
                }
            }
            logger.info( "Successfully wiped out database " + databasesToWipe[ i ] );

        }
        return true;
    }
}
