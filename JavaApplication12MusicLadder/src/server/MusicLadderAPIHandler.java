package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.MusicLadderController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MusicLadderAPIHandler implements HttpHandler
{
    
    private MusicLadderController controller;

    public MusicLadderAPIHandler(MusicLadderController controller)
    {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        //ALL
        String response = "";
        int status = 200;
        String method = he.getRequestMethod().toUpperCase();
        String path = he.getRequestURI().getPath();
        String[] parts = path.split("/");
        
        //POST, PUT, (DELETE?)
        InputStreamReader isr;
        BufferedReader br;
        String jsonQuery;
        
        switch ( method )
        {
            case "GET":
                
                /*
                 * "all" handling
                 * URL (1) : http://localhost:8084/musicLadderAPI/all/songs/1
                 * URL (2) : http://localhost:8084/musicLadderAPI/all/duelsToPlay/5
                 * URL (3) : http://localhost:8084/musicLadderAPI/all/duelsPlayed/25
                */
                if ( parts.length == 5 && parts[2] != null && "all".equals( parts[2] )
                    && parts[3] != null && parts[4] != null && isNumeric( parts[4] ) 
                    && ( Integer.parseInt( parts[4] ) > 0 ) ) {
                    
                        switch( parts[3] ) {
                            case ("songs") :
                                response = new Gson().toJson( controller.loadSongs( Integer.parseInt( parts[4] ) ) );
                                break;
                            case ("duelsToPlay") :
                                response = new Gson().toJson( controller.loadNDuelsToPlay( Integer.parseInt( parts[4] ) ) );
                                break;
                            case ("duelsPlayed") :
                                response = new Gson().toJson( controller.loadNPlayedDuels( Integer.parseInt( parts[4] ) ) );
                                break;
                            default :
                                response = "404 Not found";
                                status = 404;
                                break; 
                        }
                } 
                /*
                 * "song" handling
                 * URL : http://localhost:8084/musicLadderAPI/song/23
                */
                else if ( parts.length == 4 && parts[2] != null && "song".equals( parts[2] ) 
                    && parts[3] != null && isNumeric( parts[3] ) && ( Integer.parseInt( parts[3] ) > 0 ) ) {
                    
                        response = new Gson().toJson( controller.getSongByID( Integer.parseInt( parts[3] ) ) );
                } 
                /*
                 * "duel" handling
                 * URL : http://localhost:8084/musicLadderAPI/duel/104
                */
                else if ( parts.length == 4 && parts[2] != null && "duel".equals( parts[2] ) 
                    && parts[3] != null && isNumeric( parts[3] ) && ( Integer.parseInt( parts[3] ) > 0 ) ) {
                    
                        response = new Gson().toJson( controller.getDuel( Integer.parseInt( parts[3] ) ) );
                } else {
                    response = "404 Not found";
                    status = 404;
                }
                
                break;
            case "POST":
                //use PUT to create resources, or use POST to update resources.
                
                isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                br = new BufferedReader(isr);
                jsonQuery = br.readLine();
                
                /*
                * Update of duel ( actual matchmaking )
                * URL : http://localhost:8084/musicLadderAPI/duel/112/6/4
                *       where 112 is the duelID, 6 is the song1 score
                *       and 4 is the song2 score
                * JSON : -none-
                */
                if ( parts.length == 6 && parts[2] != null && "duel".equals( parts[2] ) 
                    && parts[3] != null && isNumeric( parts[3] ) && ( Integer.parseInt( parts[3] ) > 0 ) 
                    && parts[4] != null && isNumeric( parts[4] ) 
                    && parts[5] != null && isNumeric( parts[5] ) ) {
                    
                        Boolean actualResponse = controller.generateResultsAndUpdateDuel( Integer.parseInt( parts[3] ), Integer.parseInt( parts[4] ), Integer.parseInt( parts[5] ) );
                        if ( actualResponse ) {
                            status = 200;
                            response = "{\"response\":\"Song updated successfully!\"}";
                        } else {
                            status = 400;
                            response = "{\"response\":\"Bad request!\"}";
                        }
                }
                /*
                * Update of song 
                * URL : http://localhost:8084/musicLadderAPI/song
                * JSON : {"id":183,"name":"Singer - very cool song"}
                */
                else if ( parts[2] != null && "song".equals( parts[2] )  ) {
                    Boolean actualResponse = controller.updateSong( jsonQuery );
                        if ( actualResponse ) {
                            status = 200;
                            response = "{\"response\":\"Song updated successfully!\"}";
                        } else {
                            status = 400;
                            response = "{\"response\":\"Bad request!\"}";
                        }
                }
                /*
                * probabilities for possible points after a duel
                * URL : http://localhost:8084/musicLadderAPI/probability
                * JSON : {"song1BeforeMatchRating":800,"song2BeforeMatchRating":1200}
                */
                else if ( parts.length == 3 && parts[2] != null && "probability".equals( parts[2] ) ) {
                    response = controller.predictDuelResults( jsonQuery );
                    status = 201;
                } 
                // Update of Song missing (functionality exists)
                else {
                    response = "404 Not found";
                    status = 404;
                }
                break;
            case "PUT":
                //use PUT to create resources, or use POST to update resources.
                
                isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                br = new BufferedReader(isr);
                jsonQuery = br.readLine();
                /*
                * Create new song
                * URL : http://localhost:8084/musicLadderAPI/song
                * JSON : {"name":"Singer - Song title ?"}
                */
                if ( parts.length > 2 && parts[2] != null && "song".equals( parts[2] ) ) {
                    response = new Gson().toJson( controller.createSongAPI( jsonQuery ) );
                    status = 201;
                 } 
                /*
                * Create new duel
                * URL : http://localhost:8084/musicLadderAPI/duel/1/4
                *       where "1" is the ladderId and
                        "4" is the amount of duels to be generated.
                * JSON : -none-
                */
                else if ( parts.length == 5 && parts[2] != null && "duel".equals( parts[2] ) 
                    && parts[3] != null && isNumeric( parts[3] ) && ( Integer.parseInt( parts[3] ) > 0 ) 
                    && parts[4] != null && isNumeric( parts[4] ) && ( Integer.parseInt( parts[4] ) > 0 ) ) {
                        boolean actualResponse = controller.generateDuels( Integer.parseInt( parts[3] ) , Integer.parseInt( parts[4] ) );

                        if ( actualResponse ) {
                            status = 201;
                            response = "{\"response\":\"Duel created successfully!\"}";
                        } else {
                            status = 400;
                            response = "{\"response\":\"Bad request!\"}";
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
