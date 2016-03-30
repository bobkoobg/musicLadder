package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MusicLadderAPIHandler implements HttpHandler
{

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        //ALL
        String response = "";
        int status = 200;
        String method = he.getRequestMethod().toUpperCase();
        String path;
        String[] parts;
        
        //POST, PUT, (DELETE?)
        InputStreamReader isr;
        BufferedReader br;
        String jsonQuery;
        switch (method)
        {
            case "GET":
                path = he.getRequestURI().getPath();
                parts = path.split("/");
                
                if ( parts.length > 2 && parts[2] != null && "all".equals( parts[2] ) ) {
                    if( parts.length > 3 && parts[3] != null && "songs".equals( parts[3] ) ) {
                        if ( parts.length > 4 && parts[4] != null && isNumeric( parts[4] ) ) {
                            response = new Gson().toJson( RESTfulAPIServer.controller.loadSongs( Integer.parseInt( parts[4] ) ) );
                        } else {
                            response = "404 Not found";
                            status = 404;
                        }
                    } else if( parts.length > 3 && parts[3] != null && "duelsToPlay".equals( parts[3] ) ) {
                        if ( parts.length > 4 && parts[4] != null && isNumeric( parts[4] ) ) {
                            response = new Gson().toJson( RESTfulAPIServer.controller.loadNDuelsToPlay( Integer.parseInt( parts[4] ) ) );
                        } else {
                            response = "404 Not found";
                            status = 404;
                        }
                    } else if( parts.length > 3 && parts[3] != null && "duelsPlayed".equals( parts[3] ) ) {
                        if ( parts.length > 4 && parts[4] != null && isNumeric( parts[4] ) ) {
                            response = new Gson().toJson( RESTfulAPIServer.controller.loadNPlayedDuels( Integer.parseInt( parts[4] ) ) );
                        } else {
                            response = "404 Not found";
                            status = 404;
                        }
                    } else {
                        response = "404 Not found";
                        status = 404;
                    }
                } else if ( parts.length > 2 && parts[2] != null && "song".equals( parts[2] ) ) {
                    if ( parts.length > 3 && parts[3] != null && isNumeric( parts[3] ) ) {
                        response = new Gson().toJson( RESTfulAPIServer.controller.getSongByID( Integer.parseInt( parts[3] ) ) );
                    } else {
                        response = "404 Not found";
                        status = 404;
                    }
                } else if ( parts.length > 2 && parts[2] != null && "duel".equals( parts[2] ) ) {
                    if ( parts.length > 3 && parts[3] != null && isNumeric( parts[3] ) ) {
                        response = "500 Not supported";
                        status = 500;
                    } else {
                        response = "404 Not found";
                        status = 404;
                    }
                } else {
                    response = "404 Not found";
                    status = 404;
                }
                break;
            case "POST":
                path = he.getRequestURI().getPath();
                parts = path.split("/");
                
                isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                br = new BufferedReader(isr);
                jsonQuery = br.readLine();
                
                 if ( parts.length > 2 && parts[2] != null && "song".equals( parts[2] ) ) {
                    response = new Gson().toJson( RESTfulAPIServer.controller.createAndLoadSongs( jsonQuery ) );
                    status = 201;
                 } else if ( parts.length > 2 && parts[2] != null && "duel".equals( parts[2] ) ) {
                     if ( parts.length > 3 && parts[3] != null && isNumeric( parts[3] ) ) {
                        response = new Gson().toJson( RESTfulAPIServer.controller.generateDuels( Integer.parseInt( parts[3] ) ) );
                        status = 201;
                     } else {
                         response = "404 Not found";
                        status = 404;
                     }
                 } else {
                    response = "404 Not found";
                    status = 404;
                 }
                break;
            case "PUT":
                path = he.getRequestURI().getPath();
                parts = path.split("/");
                
                isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                br = new BufferedReader(isr);
                jsonQuery = br.readLine();
                
                if ( parts.length > 2 && parts[2] != null && "duel".equals( parts[2] ) ) {
                    if ( parts.length > 4 && parts[3] != null && isNumeric( parts[3] ) &&
                            parts[4] != null && isNumeric( parts[4] ) ) {
                        response = new Gson().toJson( RESTfulAPIServer.controller.generateResultsAndUpdateDuel( jsonQuery, Integer.parseInt( parts[3] ), Integer.parseInt( parts[4] ) ) );
                        status = 201;
                    } else {
                        response = "404 Not found";
                        status = 404;
                    }
                } else {
                    response = "404 Not found";
                    status = 404;
                }
                break;
            case "DELETE":
                status = 500;
                response = "not supported";
                break;
        }
        
        he.getResponseHeaders().add("Content-Type", "application/json");
        he.sendResponseHeaders(status, 0);
        try (OutputStream os = he.getResponseBody())
        {
            os.write(response.getBytes());
        }
    }
    
    private static Boolean isNumeric(String str)  
    {  
      try  
      {  
        Integer d = Integer.parseInt( str );  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }

}
