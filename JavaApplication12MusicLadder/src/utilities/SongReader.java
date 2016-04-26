package utilities;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Logger;

/**
 * This class is used for reading song names locally from a user. It will be an
 * important tool in the initial live stages of the Music Ladder
 *
 */
public class SongReader {

    private static final String[] fileFormats = { "mp3", "MP3" };

    private static String errorMessage = "Incorrect path (directory).";

    public <T> T finder( String dirName, Logger logger ) {
        if ( dirName == null || dirName.isEmpty() ) {
            return ( T ) errorMessage;
        }
        File dir = new File( dirName );

        return ( T ) dir.listFiles( new FilenameFilter() {
            public boolean accept( File dir, String filename ) {

                boolean isAcceptable = false;
                for ( int i = 0; i < fileFormats.length; i++ ) {
                    isAcceptable = filename.endsWith( fileFormats[ i ] );
                    if ( isAcceptable ) {
                        break;
                    }
                }
                if ( !isAcceptable ) {
                    if ( logger != null ) {

                        logger.severe( "utils.SongReader : File not accepted"
                                + "because of format (extension) : " + filename );
                    } else {
                        System.out.println( "utils.SongReader : Logger not inited" );
                        System.out.println( "utils.SongReader : File not accepted"
                                + " because of format (extension) : " + filename );
                    }

                }
                return isAcceptable;
            }
        } );
    }
}
