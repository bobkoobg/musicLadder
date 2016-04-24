package controller;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import entity.Duel;
import entity.Song;
import entity.User;
import entity.UserIdentifiers;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import java.util.logging.Logger;
import utils.DuelGenerator;
import utils.EloRatingSystemCalculator;
import utils.PerformanceLogger;
import utils.SessionIDsGenerator;
import utils.SongRatingComparator;
import utils.SongReader;

public class MusicLadderController {

    private static MusicLadderController instance = null;

    private PerformanceLogger pl = null;
    private Logger logger = null;
    private Facade facade = null;
    private EloRatingSystemCalculator eloRSC = null;
    private DuelGenerator dG = null;
    private SongReader sr = null;
    private Gson gson = null;
    private SongRatingComparator src = null;
    private List<UserIdentifiers> userIdentifiers;
    private SessionIDsGenerator sessionIdsGen = null;

    private MusicLadderController() {
        // Exists only to defeat instantiation.

        //Logger functionality
        pl = new PerformanceLogger();
        logger = pl.logMessage();

        facade = Facade.getInstance();
        facade.initializeConnection( logger );

        eloRSC = new EloRatingSystemCalculator();
        dG = DuelGenerator.getInstance();
        sr = new SongReader();
        src = new SongRatingComparator();

        userIdentifiers = new ArrayList();

        sessionIdsGen = new SessionIDsGenerator();

        gson = new Gson();

    }

    public static MusicLadderController getInstance() {
        if ( instance == null ) {
            instance = new MusicLadderController();
        }
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean registerUser( String clientReqIP, String jQueryObject ) {
        User jsonObject = gson.fromJson( jQueryObject, User.class );

        long MAX_DURATION = MILLISECONDS.convert( 1, MINUTES );
        Date now = new Date();

        String clientSHA256PlusIdsPW = jsonObject.getPassword();

        if ( clientSHA256PlusIdsPW.length() != (64 + 2) ) {
            return false;
        }

        //decompose password
        for ( int i = 0; i < userIdentifiers.size(); i++ ) {
            if ( userIdentifiers.get( i ).getClientReqIP().equals( clientReqIP )
                    && "register".equals( userIdentifiers.get( i ).getType() ) ) {
                clientSHA256PlusIdsPW = clientSHA256PlusIdsPW.substring( 1, clientSHA256PlusIdsPW.length() - 1 );
                jsonObject.setPassword( clientSHA256PlusIdsPW );
            }
            if ( now.getTime() - userIdentifiers.get( i ).getCurDate().getTime() >= MAX_DURATION ) {
                userIdentifiers.remove( i );
            }
        }

        int status = facade.registerUser( logger, jsonObject );

        if ( status == 0 ) {
            return false;
        }

        return true;
    }

    public boolean createUserIdentifierObj( String clientReqIP, int curServerID, String type ) {
        boolean found = false;
        long MAX_DURATION = MILLISECONDS.convert( 1, MINUTES );
        Date now = new Date();

        for ( int i = 0; i < userIdentifiers.size(); i++ ) {
            if ( userIdentifiers.get( i ).getClientReqIP().equals( clientReqIP )
                    && userIdentifiers.get( i ).getType().equals( type ) ) {

                userIdentifiers.remove( i );
                userIdentifiers.add( new UserIdentifiers( clientReqIP, curServerID, new Date(), type ) );
                found = true;
            }
            if ( now.getTime() - userIdentifiers.get( i ).getCurDate().getTime() >= MAX_DURATION ) {
                userIdentifiers.remove( i );
            }
        }
        if ( !found ) {
            userIdentifiers.add( new UserIdentifiers( clientReqIP, curServerID, new Date(), type ) );
        }
        return true;
    }

    public boolean addClientId( String clientReqIP, int curClientID ) {
        long MAX_DURATION = MILLISECONDS.convert( 1, MINUTES );
        Date now = new Date();
        boolean isFound = false;
        for ( int i = 0; i < userIdentifiers.size(); i++ ) {
            if ( userIdentifiers.get( i ).getClientReqIP().equals( clientReqIP ) ) {
                userIdentifiers.get( i ).setCurClientId( curClientID );
                isFound = true;
            }
            if ( now.getTime() - userIdentifiers.get( i ).getCurDate().getTime() >= MAX_DURATION ) {
                userIdentifiers.remove( i );
            }
        }
        if ( isFound ) {
            return true;
        }
        return false;
    }

    public User loginUser( String clientReqIP, String jQueryObject ) {
        User jsonObject = gson.fromJson( jQueryObject, User.class );

        long MAX_DURATION = MILLISECONDS.convert( 1, MINUTES );
        Date now = new Date();

        String clientSHA256PlusIdsPW = jsonObject.getPassword();

        if ( clientSHA256PlusIdsPW.length() != (64 + 2) ) {
            return null;
        }

        //decompose password
        for ( int i = 0; i < userIdentifiers.size(); i++ ) {
            if ( userIdentifiers.get( i ).getClientReqIP().equals( clientReqIP )
                    && "login".equals( userIdentifiers.get( i ).getType() ) ) {
                clientSHA256PlusIdsPW = clientSHA256PlusIdsPW.substring( 1, clientSHA256PlusIdsPW.length() - 1 );
                jsonObject.setPassword( clientSHA256PlusIdsPW );
            }
            if ( now.getTime() - userIdentifiers.get( i ).getCurDate().getTime() >= MAX_DURATION ) {
                userIdentifiers.remove( i );
            }
        }

        User currUser = facade.getUser( logger, jsonObject.getUsername(), jsonObject.getPassword() );

        if ( currUser != null ) {
            currUser.setSessionId( sessionIdsGen.registerSession( clientReqIP, currUser.getUsername() ) );
        }

        return currUser;
    }

    public boolean authenticateSession( String address, String sessionId ) {
        sessionId = sessionId.replaceAll( "^\"|\"$", "" );
        return sessionIdsGen.checkSession( sessionId, address );
    }

    /*
     * Correct way of loading songs
     *   Missing : Error handling
     */
    public List<Song> loadSongs( Integer ladderId ) {
        List<Song> songs = facade.getSongs( logger, ladderId );
        Collections.sort( songs, new SongRatingComparator() );
        return songs;
    }

    /*
     * Correct way of loading songs
     *   Missing : Error handling
     */
    public List<Duel> loadNPlayedDuels( Integer amount ) {
        return facade.getNPlayedDuels( logger, amount );
    }

    /*
     * Correct way of loading songs
     *   1 ) Load from DB
     *     Missing : Error handling
     */
    public List<Duel> loadNDuelsToPlay( Integer amount ) {
        return facade.getNDuelsToPlay( logger, amount );
    }

    /*
     * Correct way of creating songs from file
     *   1 ) Find in local folder
     *   2 ) Loop through result
     *   3 ) Insert elem by elem in db
     *   4 ) Check if correct ***MISSING***
     *   5 ) Error handling ***MISSING***
     */
    public Boolean createSongs( String path ) {
        File[] songFiles = sr.finder( path );
        for ( int i = 0; i < songFiles.length; i++ ) {
            Integer songId = facade.insertSong( logger, songFiles[ i ].getName() );
            //Error handling
        }
        return true;
    }

    /*
     * Correct way of creating a song via API
     *   1 ) Create object from Json
     *   2 ) Insert it in the db
     *   3 ) Check if correct ***MISSING***
     *   4 ) Error handling ***MISSING***
     */
    public Boolean createSongAPI( String jQueryObject ) {
        Song jsonObject = gson.fromJson( jQueryObject, Song.class );

        Integer songId = facade.insertSong( logger, jsonObject.getName() );
        //Error handling
        return true;
    }

    /*
     * Correct way of generating duels via api
     *   1 ) load songs from db
     *   2 ) iterate N amount of times
     *   3 ) generate a duel
     *   3.1 ) Error handling ***MISSING***
     *   4 ) save it
     *   4.1 ) Error handling ***MISSING***
     */
    public List<Integer> generateDuels( Integer ladderId, Integer amount ) {
        List<Integer> duelIds = new ArrayList();
        List<Song> songs = facade.getSongs( logger, ladderId );
        for ( int i = 0; i < amount; i++ ) {
            Duel duel = dG.generator( songs );
            if ( duel == null ) {
                System.out.println( "Error : generateDuels no duel generated." );
                return null;
            }
            Integer duelId = facade.insertDuel( logger, duel );
            if ( duelId == 0 ) {
                System.out.println( "Error : duel not inserted in database" );
                return null;
            } else {
                duelIds.add( duelId );
            }
        }
        return duelIds;
    }

    /*
     * Correct way of loading a duel from db
     */
    public Duel getDuel( Integer duelID ) {
        return facade.getDuel( logger, duelID );
    }

    /*
     * Correct way of updating song
     *   1 ) load new song content from json
     *   2 ) load actual song content from db
     *   3 ) check for differences
     *   4 ) update if necessary
     */
    public Boolean updateSong( String jQueryObject ) {
        Song jsonObject = gson.fromJson( jQueryObject, Song.class );

        if ( jsonObject.getId() == null ) {
            return false;
        }

        Song song = facade.getSong( logger, jsonObject.getId() );

        if ( song == null ) {
            return false;
        }

        boolean isChanged = false;
        if ( jsonObject.getName() != null && !song.getName().equals( jsonObject.getName() ) ) {
            song.setName( jsonObject.getName() );
            isChanged = true;
        }

        if ( isChanged ) {
            if ( facade.updateSong( logger, song ) ) {
                return true;
            }
        }

        return false;
    }

    /*
     * Correct way of matchmaking via API
     *   (core functionality in musicLadder 1.0)
     */
    public Boolean generateResultsAndUpdateDuel( Integer duelID, Integer song1Score, Integer song2Score ) {
        //No inner error handling at any point
        Duel duel = getDuel( duelID );

        if ( duel == null ) {
            return false;
        }

        Song song1 = facade.getSong( logger, duel.getSong1ID() );
        Song song2 = facade.getSong( logger, duel.getSong2ID() );

        if ( song1 == null || song2 == null ) {
            return false;
        }

        duel.setSong1Score( song1Score );
        duel.setSong2Score( song2Score );

        float[] newSongRatings = eloRSC.calculate(
                duel.getSong1BeforeMatchRating(), duel.getSong2BeforeMatchRating(),
                duel.getSong1Score(), duel.getSong2Score() );

        duel.setSong1AfterMatchRating( newSongRatings[ 0 ] );
        duel.setSong2AfterMatchRating( newSongRatings[ 1 ] );

        if ( song1Score > song2Score ) {
            song1.incrementWins();
            song2.incrementLoses();
        } else if ( song1Score < song2Score ) {
            song1.incrementLoses();
            song2.incrementWins();
        } else {
            song1.incremenetDraws();
            song2.incremenetDraws();
        }

        song1.setFormerRating( song1.getCurrentRating() );
        song1.setCurrentRating( newSongRatings[ 0 ] );
        song2.setFormerRating( song2.getCurrentRating() );
        song2.setCurrentRating( newSongRatings[ 1 ] );

        boolean isUpdatedDuel = facade.updateDuel( logger, duel );
        if ( isUpdatedDuel ) {
            boolean isUpdatedSong1 = facade.updateSong( logger, song1 );
            boolean isUpdatedSong2 = facade.updateSong( logger, song2 );
            return (isUpdatedSong1 && isUpdatedSong2);
        }
        return false;
    }

    /*
     * Correct way of loading a song by id
     */
    public Song getSongByID( Integer songID ) {
        return facade.getSong( logger, songID );
    }

    /*
     * Help function for predicting duel results
     */
    private List<Float> generateResults( Duel duel ) {
        List<Float> possibilities = new ArrayList();
        Integer[] song1Scores = { 10, 5, 0 };
        Integer[] song2Scores = { 0, 5, 10 };

        for ( int i = 0; i < song1Scores.length; i++ ) {
            duel.setSong1Score( song1Scores[ i ] );
            duel.setSong2Score( song2Scores[ i ] );

            float[] calcResults = eloRSC.calculate(
                    duel.getSong1BeforeMatchRating(), duel.getSong2BeforeMatchRating(),
                    duel.getSong1Score(), duel.getSong2Score() );

            possibilities.add( calcResults[ 0 ] - duel.getSong1BeforeMatchRating() );
            possibilities.add( calcResults[ 1 ] - duel.getSong2BeforeMatchRating() );
        }
        return possibilities;
    }

    /*
     * Correct way of predicting duel results
     */
    public String predictDuelResults( String jQueryObject ) {
        //No inner error handling at any point
        Duel duel = null;
        try {
            duel = gson.fromJson( jQueryObject, Duel.class );
        } catch ( JsonParseException e ) {
            System.out.println( "exception : " + e );
        }

        List<Float> possibilities = generateResults( duel );

        return gson.toJson( possibilities );
    }

    public void closeConnection() {
        facade.closeConnection( logger );
    }
}
