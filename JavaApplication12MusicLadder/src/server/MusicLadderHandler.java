package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MusicLadderHandler implements HttpHandler
{

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        String response = "";
        int status = 200;
        String method = he.getRequestMethod().toUpperCase();
        
        switch (method)
        {
            case "GET":
//                try
//                {
//                    String path = he.getRequestURI().getPath();
//                    int lastIndex = path.lastIndexOf("/");
//                    if (lastIndex > 0)
//                    {
//                        String idString = path.substring(lastIndex + 1);
//
//                        int id = Integer.parseInt(idString);
//                        response = RestFileServer.facade.getPersonAsJSON(id);
//                    }
//                    else
//                    {
//                        response = RestFileServer.facade.getPersonsAsJSON();
//                    }
//                }
//                catch (NumberFormatException nfe)
//                {
//                    response = "Id is not a number";
//                    status = 404;
//                }
//                catch (NotFoundException nfe) //***** WTF??
//                {
//                    response = nfe.getMessage();
//                    status = 404;
//                }
                response = "<h1>Music Ladder GET Request</h1>Success!";
                break;
            case "POST":
                InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String jsonQuery = br.readLine();
                //Person p = RestFileServer.facade.addPersonFromJSON(jsonQuery);
                //Gson gson = new Gson();
                //String yolo = gson.fromJson( jsonQuery );
                //response = new Gson().toJson( jsonQuery );
                response = "<h1>Music Ladder POST Request</h1>Success!";
                break;
            case "DELETE":
//                try
//                {
//                    String path = he.getRequestURI().getPath();
//                    int lastIndex = path.lastIndexOf("/");
//                    if (lastIndex > 0)
//                    {  //person/id
//                        int id = Integer.parseInt(path.substring(lastIndex + 1));
//                        Person pDeleted = RestFileServer.facade.deletePersonFromJSON(id);
//                        response = new Gson().toJson(pDeleted);
//                    }
//                    else
//                    {
//                        status = 400;
//                        response = "<h1>Bad Request</h1>No id supplied with request";
//                    }
//                }
//                catch (NotFoundException nfe)
//                {
//                    status = 404;
//                    response = nfe.getMessage();
//                }
//                catch (NumberFormatException nfe)
//                {
//                    response = "Id is not a number";
//                    status = 404;
//                }
                response = "<h1>Music Ladder DELETE Request</h1>Success!";
                break;
        }
        he.getResponseHeaders().add("Content-Type", "application/json");
        he.sendResponseHeaders(status, 0);
        try (OutputStream os = he.getResponseBody())
        {
            os.write(response.getBytes());
        }
    }

}
