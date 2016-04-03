package server;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.hubspot.jinjava.Jinjava;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MusicLadderHandler implements HttpHandler {

    private static String filesDirectory = "src/pages/musicLadder/";

    @Override
    public void handle( HttpExchange he ) throws IOException {
        
        int responseCode = 200;
        String response = "";
        String method = he.getRequestMethod().toUpperCase();
        byte[] bytesToSend = null;

        switch ( method ) {
            case "GET":
                String requestedURL = he.getRequestURI().toString();
                String delimeter = "musicLadder/";
                String requestedFile = requestedURL.substring( requestedURL.lastIndexOf( delimeter ) + delimeter.length() );

                File file = null;
                int fileType = requestedFile.lastIndexOf( "." );
                String mime = "";
                if ( fileType != -1 ) {
                    mime = getMime( requestedFile.substring( fileType ) );

                    try {
                        file = new File( filesDirectory + requestedFile );

                        if ( !file.exists() || file.isDirectory() ) {
                            mime = (".html");
                            file = new File( filesDirectory + "musicLadderIndex.html" );
                            responseCode = 404;

                            if ( !file.exists() || file.isDirectory() ) {
                                //very interesting to log (hackers may try to access admin.php? :D MLG!!)
                                responseCode = 500;
                            }

                        } else {
                            file = new File( filesDirectory + requestedFile );
                            responseCode = 200;
                        }
                    } catch ( Exception e ) {
                        response = "Exception : " + e;
                        responseCode = 404;
                    }

                } else {
                    mime = (".html");
                    file = new File( filesDirectory + "musicLadderIndex.html" );
                }

                bytesToSend = new byte[ ( int ) file.length() ];

                BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
                bis.read( bytesToSend, 0, bytesToSend.length );

                responseCode = 200;

                if ( responseCode == 200 ) {
                    Headers h = he.getResponseHeaders();
                    h.set( "Content-Type", mime );
                } else {
                    bytesToSend = response.getBytes();
                }
                break;

            case "POST":
                responseCode = 500;
                response = "not supported";
                break;
            case "DELETE":
                responseCode = 500;
                response = "not supported";
                break;
            default:
                responseCode = 500;
                response = "not supported";
                break;
        }

        he.sendResponseHeaders( responseCode, bytesToSend.length );

        try ( OutputStream os = he.getResponseBody() ) {
            os.write( bytesToSend, 0, bytesToSend.length );
        }
    }

    private String getMime( String extension ) {
        String mime = "";
        switch ( extension ) {
            case ".pdf":
                mime = "application/pdf";
                break;
            case ".png":
                mime = "image/png";
                break;
            case ".js":
                mime = "text/javascript";
                break;
            case ".html":
                mime = "text/html";
                break;
            case ".jar":
                mime = "application/java-archive";
                break;
            default:
                mime = "text/html";
                break;
        }
        return mime;
    }

}
