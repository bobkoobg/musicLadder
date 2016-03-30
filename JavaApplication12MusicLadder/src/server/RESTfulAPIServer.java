
package server;

import com.sun.net.httpserver.HttpServer;
import controller.MusicLadderController;
import java.io.IOException;
import java.net.InetSocketAddress;

public class RESTfulAPIServer
{
    static int port = 8084;
    static String ip = "127.0.0.1";
    static String publicFolder = "src/pages/";//"src/html/";
    static String startFile = "index.html";
    static String filesUri = "/";
    static final boolean DEVELOPMENT_MODE = true;

    static MusicLadderController controller;

    public void run() throws IOException {
        
        controller = MusicLadderController.getInstance();
        if (RESTfulAPIServer.DEVELOPMENT_MODE) {
            //facade.testingCode();
            System.out.println("Development!");
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        //REST Routes
        server.createContext("/musicLadder", new MusicLadderHandler());
        server.createContext("/musicLadderAPI", new MusicLadderAPIHandler());
        //server.createContext("/whatever", new whateverHandler());

        //HTTP Server Routes 
        server.createContext(filesUri, new ServerHandler());

        server.start();
        System.out.println("Server started, listening on port: " + port);

    }
//The magic starts here ;)
    public static void main(String[] args) throws IOException {
        if (args.length >= 3) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
            publicFolder = args[2];
        }
        new RESTfulAPIServer().run();
    }
}
