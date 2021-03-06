package controller;

import entity.Duel;
import entity.Song;
import entity.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import mapper.DuelMapper;
import mapper.SongMapper;
import mapper.UserMapper;
import utilities.DatabaseConnector;

public class Facade {
    
    private static Facade instance = null;
    private Connection connection;
    private DatabaseConnector databaseConnector;
    private SongMapper songMapper;
    private DuelMapper duelMapper;
    private UserMapper userMapper;

    //Database authentication
    private static String[] databaseHost = { "jdbc:oracle:thin:@127.0.0.1:1521:XE", "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat" };
    private static String[] databaseUsername = { "bobkoo", "cphbs96" };
    private static String[] databasePassword = { "qwerty12345", "cphbs96" };

    private Facade() {
        // Exists only to defeat instantiation.
        databaseConnector = new DatabaseConnector( databaseHost[ 0 ], databaseUsername[ 0 ], databasePassword[ 0 ], null );
        songMapper = new SongMapper();
        duelMapper = new DuelMapper();
        userMapper = new UserMapper();
    }

    public static Facade getInstance() {
        if ( instance == null ) {
            instance = new Facade();
        }
        return instance;
    }

    public Boolean initializeConnection( Logger logger ) {
        if ( connection != null ) {
            System.out.println( "Connection already existing" );
            logger.info( "Connection with database is already existing!" );
            return true;
        } else {
            connection = databaseConnector.getConnection( logger );

            try {
                connection.setAutoCommit( true );
                // termination by the garbage collector
            } catch ( SQLException ex ) {
                logger.severe( "SQL Exception while trying to connect to db " + ex );
                return false;
            }
            logger.info( "Connection with database initialized" );
        }
        return true;
    }

    public Boolean closeConnection( Logger logger ) {
        try {
            connection.close();
        } catch ( SQLException e ) {
            logger.severe( "SQL Exception while trying to close the connection to db " + e );
            return false;
        }
        logger.info( "Connection with database closed successfully!" );
        return true;
    }

    public int registerUser( Logger logger, User user ) {
        return userMapper.registerUser( logger, connection, user );
    }

    public User getUser( Logger logger, String username, String hashedPassword ) {
        return userMapper.getUserByUNandPW( logger, connection, username, hashedPassword );
    }

    public Song getSong( Logger logger, Integer songId ) {
        return songMapper.getSong( logger, connection, songId );
    }

    public List<Song> getSongs( Logger logger, Integer ladderId ) {
        return songMapper.getSongs( logger, connection, ladderId );
    }

    public Integer insertSong( Logger logger, String name ) {
        return songMapper.insertSong( logger, connection, name );
    }

    public Boolean updateSong( Logger logger, Song song ) {
        return songMapper.updateSong( logger, connection, song );
    }

    //delete song
    public Duel getDuel( Logger logger, Integer duelID ) {
        return duelMapper.getDuel( logger, connection, duelID );
    }

    public List<Duel> getNPlayedDuels( Logger logger, Integer amount ) {
        return duelMapper.getNPlayedDuels( logger, connection, amount );
    }

    public List<Duel> getNDuelsToPlay( Logger logger, Integer amount ) {
        return duelMapper.getNDuelsToPlay( logger, connection, amount );
    }

    public Integer insertDuel( Logger logger, Duel duel ) {
        return duelMapper.insertDuel( logger, connection, duel );
    }

    public Boolean updateDuel( Logger logger, Duel duel ) {
        return duelMapper.updateDuel( logger, connection, duel );
    }

    //delete duel
    //Helpful, but useless functionality for the moment getSong, 
    //  wipeDuelDatabases, wipeSongDatabases
    public Boolean wipeDuelDatabases( Logger logger ) {
        return duelMapper.wipeDatabase( connection, logger );
    }

    public Boolean wipeSongDatabases( Logger logger ) {
        return songMapper.wipeDatabase( connection, logger );
    }
}
