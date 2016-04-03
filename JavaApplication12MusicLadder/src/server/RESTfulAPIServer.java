package server;

import com.sun.net.httpserver.HttpServer;
import controller.MusicLadderController;
import java.io.IOException;
import java.net.InetSocketAddress;

public class RESTfulAPIServer {

    private static int port = 8084;
    private static String ip = "127.0.0.1";

    private static MusicLadderController controller;

    public void run() throws IOException {

        controller = MusicLadderController.getInstance();

        HttpServer server = HttpServer.create( new InetSocketAddress( ip, port ), 0 );

        //REST Routes
        server.createContext( "/musicLadder", new MusicLadderHandler() );
        server.createContext( "/musicLadderAPI", new MusicLadderAPIHandler( controller ) );
        //server.createContext("/whatever", new whateverHandler());

        //HTTP Server Routes 
        server.createContext( "/", new ServerHandler() );

        server.start();
        System.out.println( "Server started, listening on port: " + port );

    }

    public static void main( String[] args ) throws IOException {
        if ( args.length >= 3 ) {
            port = Integer.parseInt( args[ 0 ] );
            ip = args[ 1 ];
        }
        new RESTfulAPIServer().run();
    }
}
