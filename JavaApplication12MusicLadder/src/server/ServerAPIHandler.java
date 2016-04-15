package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controller.MusicLadderController;
import entity.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Random;

public class ServerAPIHandler implements HttpHandler {

    private MusicLadderController controller;
    private Random random;

    public ServerAPIHandler( MusicLadderController controller ) {
        this.controller = controller;
        random = new Random();
    }

    @Override
    public void handle( HttpExchange he ) throws IOException {
        //ALL
        String response = "";
        int status = 200;
        String method = he.getRequestMethod().toUpperCase();
        String path = he.getRequestURI().getPath();
        String[] parts = path.split( "/" );
        String address = he.getRemoteAddress().toString();

        //POST, PUT, (DELETE?)
        InputStreamReader isr;
        BufferedReader br;
        String jsonQuery;

        switch ( method ) {
            case "GET":
                /*
                 * Generate server identifier and send to client
                 * URL : http://localhost:8084/api/loginId
                 */
                if ( parts.length > 2 && parts[ 2 ] != null && "loginServerId".equals( parts[ 2 ] ) ) {

                    int curServerId = random.nextInt( 10 - 1 ) + 1;
                    if ( controller.createUserIdentifierObj( address, curServerId, "login" ) ) {
                        response = new Gson().toJson( curServerId );
                        status = 201;
                    }
                } else if ( parts.length > 2 && parts[ 2 ] != null && "registerServerId".equals( parts[ 2 ] ) ) {

                    int curServerId = random.nextInt( 10 - 1 ) + 1;
                    if ( controller.createUserIdentifierObj( address, curServerId, "register" ) ) {
                        response = new Gson().toJson( curServerId );
                        status = 201;
                    }
                }

                break;
            case "POST":
                //use PUT to create resources, or use POST to update resources.

                isr = new InputStreamReader( he.getRequestBody(), "utf-8" );
                br = new BufferedReader( isr );
                jsonQuery = br.readLine();
                /*
                 * Save Client Identifier 
                 * URL : http://localhost:8084/api/loginId
                 * JSON : {"clientRN": 8 }
                 */
                if ( parts.length > 2 && parts[ 2 ] != null && "clientId".equals( parts[ 2 ] ) ) {
                    //response = new Gson().toJson( controller.createSongAPI( jsonQuery ) );
                    int curClientId = Integer.parseInt( jsonQuery );
                    if ( controller.addClientId( address, curClientId ) ) {
                        response = new Gson().toJson( curClientId );
                        status = 201;
                    }
                } /*
                 * Evaluate username and password from user
                 * URL : http://localhost:8084/api/login
                 * JSON : {"username": "adminuser", "password":"$2a$05$zOsBcOSp9gpn1np..." }
                 */ else if ( parts.length > 2 && parts[ 2 ] != null && "login".equals( parts[ 2 ] ) ) {
                    User user = controller.loginUser( address, jsonQuery );
                    if ( user != null ) {
                        response = new Gson().toJson( user );
                        status = 201;
                    } else {
                        response = "{\"error\":\"Incorrect login info, Unauthorized\"}";
                        status = 401;
                    }
                } /*
                 * Evaluate username and password from user
                 * URL : http://localhost:8084/api/login
                 * JSON : {"username": "adminuser", "password":"$2a$05$zOsBcOSp9gpn1np..." }
                 */ else if ( parts.length > 2 && parts[ 2 ] != null && "register".equals( parts[ 2 ] ) ) {
                    boolean dbStatus = controller.registerUser( address, jsonQuery );
                    if ( dbStatus ) {
                        response = "{\"response\":\"Successfull registration\"}";
                        status = 201;
                    } else {
                        response = "{\"response\":\"Internal server error\"}";
                        status = 500;
                    }
                } else {
                    response = "404 Not found";
                    status = 404;
                }
                break;
            case "PUT":
                status = 500;
                response = "not supported";
                break;
            case "DELETE":
                status = 500;
                response = "not supported";
                break;
            default:
                status = 500;
                response = "not supported";
                break;
        }

        he.getResponseHeaders().add( "Content-Type", "application/json" );
        he.sendResponseHeaders( status, 0 );
        try ( OutputStream os = he.getResponseBody() ) {
            os.write( response.getBytes() );
        }
    }

    private static Boolean isNumeric( String str ) {
        try {
            Integer d = Integer.parseInt( str );
        } catch ( NumberFormatException nfe ) {
            return false;
        }
        return true;
    }

}
