package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServerHandler implements HttpHandler {

    private static String filesDirectory = "src/pages/";

    @Override
    public void handle( HttpExchange he ) throws IOException {
        //ALL
        int status = 200;
        String method = he.getRequestMethod().toUpperCase();
        String address = he.getRemoteAddress().toString();

        String mime = null;

        String path = he.getRequestURI().getPath();
        String[] parts = path.split( "/" );

        File file = null;
        byte[] bytesToSend = null;
        String errorMsg = null;
        System.out.println( "Hey #M#" + method + " #PL# " + parts.length + " #P# " + path );

        switch ( method ) {
            case "GET":
                /*
                 * Startup page
                 * URL : http://localhost:8084/ OR http://localhost:8084/index
                 */
                if ( parts.length == 0
                        || (parts.length == 2 && parts[ 1 ] != null && "index".equals( parts[ 1 ] )) ) {

                    mime = getMime( ".html" );
                    file = new File( filesDirectory + "index.html" );

                } /*
                 * Register page
                 * URL : http://localhost:8084/register
                 */ else if ( parts.length == 2 && parts[ 1 ] != null && "register".equals( parts[ 1 ] ) ) {

                    mime = getMime( ".html" );
                    file = new File( filesDirectory + "register.html" );

                }/*
                 * Any other js, html, css, ico file
                 */ else {

                    String lastElemStr = parts[ (parts.length - 1) ];
                    int lastElemIndex = lastElemStr.lastIndexOf( "." );

                    if ( lastElemIndex > -1 ) {
                        mime = lastElemStr.substring( lastElemStr.lastIndexOf( "." ) );
                    } else {
                        mime = getMime( ".html" );
                    }
                    file = new File( filesDirectory + lastElemStr );
                    /*
                     * If error on file show index page
                     */
                    if ( !file.exists() || file.isDirectory() ) {

                        if ( "text/html".equals( mime ) ) {
                            status = 404;
                            file = new File( filesDirectory + "index.html" );
                        } else {
                            status = 404;
                            file = new File( filesDirectory + "notfound.html" );
                        }
                    }
                }
                break;
            case "POST":
                status = 500;
                break;
            case "PUT":
                status = 500;
                break;
            case "DELETE":
                status = 500;
                break;
            default:
                status = 500;
                break;
        }
        
        if ( status == 200 || status == 404 ) {
            bytesToSend = new byte[ ( int ) file.length() ];
            he.sendResponseHeaders( status, bytesToSend.length );

        } else if ( status == 500 ) {

            bytesToSend = "<h1>500 Not supported</h1>".getBytes();

            he.getResponseHeaders().add( "Content-Type", "application/json" );
            he.sendResponseHeaders( status, 0 );
        }

        //***BLACK MAGIC - START ***
        BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
        bis.read( bytesToSend, 0, bytesToSend.length );
        //***BLACK MAGIC - END ***

        try ( OutputStream os = he.getResponseBody() ) {
            os.write( bytesToSend, 0, bytesToSend.length );
        }
    }

    private String getMime( String extension ) {
        String mime = "";
        switch ( extension ) {
//            case ".pdf":
//                mime = "application/pdf";
//                break;
//            case ".png":
//                mime = "image/png";
//                break;
//            case ".jar":
//                mime = "application/java-archive";
//                break;
            case ".js":
                mime = "text/javascript";
                break;
            case ".html":
                mime = "text/html";
                break;
            case ".css":
                mime = "text/css";
                break;
            case ".ico":
                mime = "image/x-icon";
                break;
            default:
                mime = "text/html";
                break;
        }
        return mime;
    }

}
