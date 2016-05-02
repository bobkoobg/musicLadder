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

public class SongMapper {

    private static String ERROR_NOELEM = "Error - not element found";
    private static String ERROR_QUERYEXEC = "Error - during query execution";

    Integer songID;

    public Integer getSongID() {
        return songID;
    }

    public <T> T getSong( Logger logger, Connection connection, int songId ) {
        Song song = new Song();
        PreparedStatement preparedStatement = null;

        try {
            String SQLString = "SELECT * FROM ML_SONG_TBL song "
                    + "JOIN ML_SONG_RANKING_TBL songranking "
                    + "ON song.song_id = songranking.song_id "
                    + "WHERE song.SONG_ID = ?";

            preparedStatement = connection.prepareStatement( SQLString );
            preparedStatement.setInt( 1, songId );

            ResultSet rs = preparedStatement.executeQuery();
            try {
                if ( rs.next() ) {

                    song.setId( rs.getInt( 1 ) );
                    song.setName( rs.getString( 2 ) );
                    song.setLadderId( rs.getInt( 4 ) );
                    song.setWins( rs.getInt( 5 ) );
                    song.setDraws( rs.getInt( 6 ) );
                    song.setLoses( rs.getInt( 7 ) );
                    song.setCurrentRating( rs.getFloat( 9 ) );
                    song.setFormerRating( rs.getFloat( 10 ) );
                } else {
                    logger.warning( "Element with song id " + songId + " does not exist." );
                    return ( T ) ERROR_NOELEM;
                }
            } finally {
                try {
                    rs.close();
                } catch ( Exception e ) {
                    logger.warning( "getSong : Result set not closed succesfully : " + e );
                }
            }
        } catch ( Exception e ) {
            logger.severe( "Prepared Statement Exception (getSong) " + e );
            return ( T ) ERROR_QUERYEXEC;
        } finally {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.warning( "SQL Exception - Prepared statement not closed succesfully : " + e );
            }
        }
        return ( T ) song;
    }

    public <T> T insertSong( Logger logger, Connection connection, String name ) {
        PreparedStatement preparedStatement = null;
        String insertTableSQL;
        songID = null;

        //Step 1 - Extract next SONG ID
        try {
            String SQLString = "select SONG_ID.nextval from dual";
            preparedStatement = connection.prepareStatement( SQLString );
            ResultSet rs = preparedStatement.executeQuery();
            try {
                if ( rs.next() ) {
                    songID = rs.getInt( 1 );
                } else {
                    logger.severe( "Sequence not found in the database (No result set) " );
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
            logger.severe( "Statement Exception while retrieving next song id " + e );
            return ( T ) ( Boolean ) false;
        } finally {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.warning( "insertSong SQL Exception - Prepared statement not closed succesfully : " + e );
            }
        }

        if ( songID != null ) {
            //Step 2 - Insert a new Song into Song Table
            try {
                insertTableSQL = "INSERT INTO ML_SONG_TBL"
                        + "(SONG_ID, SONG_NAME, SONG_YOUTUBE_LINK, SONG_LADDER, "
                        + "SONG_WINS, SONG_DRAWS, SONG_LOSSES) VALUES"
                        + "(?, ?, ?, ?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement( insertTableSQL );

                preparedStatement.setInt( 1, songID );
                preparedStatement.setString( 2, name );
                preparedStatement.setString( 3, "" );
                preparedStatement.setInt( 4, 1 );
                preparedStatement.setInt( 5, 0 );
                preparedStatement.setInt( 6, 0 );
                preparedStatement.setInt( 7, 0 );

                // execute insert SQL stetement
                preparedStatement.executeUpdate();
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while inserting song " + name + " into song table " + e );
                return ( T ) ( Boolean ) false;
            } finally {
                try {
                    if ( preparedStatement != null ) {
                        preparedStatement.close();
                    }
                } catch ( SQLException e ) {
                    logger.warning( "insertSong SQL Exception - Prepared statement not closed succesfully : " + e );
                }
            }

            //Step 2 - Insert a new record into Song Ranking Table 
            try {
                insertTableSQL = "INSERT INTO ML_SONG_RANKING_TBL"
                        + "(SONG_ID, SONG_CURRENTRATING, SONG_PREVIOUSRATING, SONG_RANKCHANGE) VALUES"
                        + "(?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement( insertTableSQL );

                preparedStatement.setInt( 1, songID );
                preparedStatement.setFloat( 2, 1000f );
                preparedStatement.setFloat( 3, 1000f );
                preparedStatement.setInt( 4, 0 );

                // execute insert SQL stetement
                preparedStatement.executeUpdate();
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while inserting song " + name + " into song ranking table " + e );
                return ( T ) ( Boolean ) false;
            } finally {
                try {
                    if ( preparedStatement != null ) {
                        preparedStatement.close();
                    }
                } catch ( SQLException e ) {
                    logger.warning( "insertSong SQL Exception - Prepared statement not closed succesfully : " + e );
                }
            }

            logger.info( "Successfully inserted song with name " + name );
            return ( T ) songID;
        }
        logger.severe( "SQL Sequence error ID is NULL" );
        return ( T ) ( Boolean ) false;
    }

    public List<Song> getSongs( Logger logger, Connection connection, Integer ladderId ) {
        List<Song> songs = new ArrayList();
        PreparedStatement preparedStatement = null;
        Song song = null;

        try {
            String SQLString = "SELECT * "
                    + "FROM ML_SONG_TBL song "
                    + "JOIN ML_SONG_RANKING_TBL songranking "
                    + "ON song.song_id = songranking.song_id "
                    + "WHERE song.SONG_LADDER = ? "
                    + "ORDER BY songranking.SONG_CURRENTRATING DESC";

            preparedStatement = connection.prepareStatement( SQLString );
            preparedStatement.setInt( 1, ladderId );

            ResultSet rs = preparedStatement.executeQuery();
            try {
                while ( rs.next() ) {

                    song = new Song();

                    song.setId( rs.getInt( 1 ) );
                    song.setName( rs.getString( 2 ) );
                    song.setLadderId( rs.getInt( 4 ) );
                    song.setWins( rs.getInt( 5 ) );
                    song.setDraws( rs.getInt( 6 ) );
                    song.setLoses( rs.getInt( 7 ) );
                    song.setCurrentRating( rs.getFloat( 9 ) );
                    song.setFormerRating( rs.getFloat( 10 ) );

                    songs.add( song );
                }

            } finally {
                try {
                    rs.close();
                } catch ( Exception ignore ) {
                }
            }
        } catch ( Exception e ) {
            logger.severe( "Statement Exception while trying to close the prepared statement while taking next song id " + e );
            return null;
        } finally {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while trying to close the prepared statement while taking next song id " + e );
                return null;
            }
        }
        return songs;
    }

    /*
     *   Not working...
     insertTableSQL = "UPDATE ( " +
     "SELECT * FROM ML_SONG_TBL song " +
     "JOIN ML_SONG_RANKING_TBL ranking ON song.SONG_ID = ranking.SONG_ID " +
     "WHERE song.SONG_ID = ? " +
     " ) mergedSongTBL " +
     "SET mergedSongTBL.SONG_WINS = ?, mergedSongTBL.SONG_DRAWS = ?, "
     + "mergedSongTBL.SONG_LOSSES = ?, mergedSongTBL.SONG_PREVIOUSRATING = ?, "
     + "mergedSongTBL.SONG_CURRENTRATING = ?";
     Possible solution : 
     http://www.cla5h.com/ora-01776-i-can-t-insert-on-a-view-comprised-by-two-tables.html
     */
    public boolean updateSong( Logger logger, Connection connection, Song song ) {
        PreparedStatement preparedStatement = null;
        String insertTableSQL;

        try {
            insertTableSQL = "UPDATE ML_SONG_TBL "
                    + "SET SONG_NAME = ?, SONG_WINS = ?, SONG_DRAWS = ?, "
                    + "SONG_LOSSES = ? "
                    + "WHERE SONG_ID = ?";

            preparedStatement = connection.prepareStatement( insertTableSQL );

            preparedStatement.setString( 1, song.getName() );
            preparedStatement.setInt( 2, song.getWins() );
            preparedStatement.setInt( 3, song.getDraws() );
            preparedStatement.setInt( 4, song.getLoses() );
            preparedStatement.setInt( 5, song.getId().intValue() );

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            logger.severe( "SQL Exception while updating song " + song.toString() + " into song tbl " + e );
            return false;
        } finally {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while trying to close the prepared statement while updating song tbl " + e );
                return false;
            }
        }
        logger.info( "Successfully updated SONG TBL, ID : " + song.getId() );

        try {
            insertTableSQL = "UPDATE ML_SONG_RANKING_TBL "
                    + "SET SONG_PREVIOUSRATING = ?, SONG_CURRENTRATING = ? "
                    + "WHERE SONG_ID = ?";

            preparedStatement = connection.prepareStatement( insertTableSQL );

            preparedStatement.setFloat( 1, song.getFormerRating() );
            preparedStatement.setFloat( 2, song.getCurrentRating() );
            preparedStatement.setInt( 3, song.getId().intValue() );

            preparedStatement.executeUpdate();
        } catch ( SQLException e ) {
            logger.severe( "SQL Exception while updating song " + song.toString() + " into song ranking tbl " + e );
            return false;
        } finally {
            try {
                if ( preparedStatement != null ) {
                    preparedStatement.close();
                }
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while trying to close the prepared statement while updating song ranking tbl " + e );
                return false;
            }
        }

        logger.info( "Successfully updated SONG RANKING TBL, ID : " + song.getId() );
        return true;
    }

    public boolean wipeDatabase( Connection connection, Logger logger ) {
        Statement statement = null;
        String sql;
        String[] databasesToWipe = { "ML_SONG_RANKING_TBL", "ML_SONG_TBL" };

        for ( int i = 0; i < databasesToWipe.length; i++ ) {
            try {
                statement = connection.createStatement();
                sql = "DELETE FROM " + databasesToWipe[ i ];
                statement.executeUpdate( sql );
            } catch ( SQLException e ) {
                logger.severe( "SQL Exception while trying to wipe out table "
                        + databasesToWipe[ i ] + " : " + e );
                return false;
            } finally {
                try {
                    if ( statement != null ) {
                        statement.close();
                    }
                } catch ( SQLException e ) {
                    logger.warning( "SQL Exception - Statement not closed succesfully : "
                            + e );
                }
            }
            logger.info( "Successfully wiped out database " + databasesToWipe[ i ] );
        }
        return true;
    }
}
