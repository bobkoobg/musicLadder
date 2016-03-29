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
import static server.RESTfulAPIServer.publicFolder;

public class MusicLadderHandler implements HttpHandler
{

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        
        String response = "";
        String method = he.getRequestMethod().toUpperCase();
        
        String path = he.getRequestURI().getPath();
        int lastIndex = path.lastIndexOf("/");
        
        int responseCode = 200;
        File file;
        byte[] bytesToSend = null;
        BufferedInputStream bis;
        
        if( lastIndex == 0 ) {
            file = new File(publicFolder + "musicLadder/musicLadderIndex.html");
            bytesToSend = new byte[(int) file.length()];
            bis = new BufferedInputStream( new FileInputStream( file ) );
            bis.read(bytesToSend, 0, bytesToSend.length);

//            Jinjava jinjava = new Jinjava();
//            Map<String, Object> context = new HashMap<String, Object>();
//            context.put("name", "Jared");
//
//            String template = Resources.toString(Resources.getResource(publicFolder + "musicLadder/musicLadderIndex.html"), Charsets.UTF_8);
//
//            String renderedTemplate = jinjava.render(template, context);
//            
//            System.out.println("*so HI * : " + renderedTemplate);
            
            Headers h = he.getResponseHeaders();
            h.set("Content-Type", "text/html");

            he.sendResponseHeaders(responseCode, bytesToSend.length);
            try (OutputStream os = he.getResponseBody())
            {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        } else {
            switch (method)
            {
                case "GET":
                    System.out.println("Get start");
                    String requestedFile = he.getRequestURI().toString();
                    System.out.println("requestedFile is : " + requestedFile);
                    String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);
                    System.out.println("f is : " + f);

                    System.out.println("Make an IF");
                    if ( "api".equals( f ) ) {
                        System.out.println("API Called");
                        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        String jsonQuery = br.readLine();
                        //Person p = RestFileServer.facade.addPersonFromJSON(jsonQuery);
                        //Gson gson = new Gson();
                        //String yolo = gson.fromJson( jsonQuery );
                        //response = new Gson().toJson( jsonQuery );
                        response = "<h1>Music Ladder GET Request</h1>Success!";
                        he.getResponseHeaders().add("Content-Type", "application/json");
                        he.sendResponseHeaders(responseCode, 0);
                        try (OutputStream os = he.getResponseBody())
                        {
                            os.write(response.getBytes());
                        }
                        
                    } else {
                        String extension = f.substring(f.lastIndexOf("."));
                        System.out.println("Extension is : " + extension );
                        String mime = getMime(extension);
                        System.out.println("NOT API Called");
                        try
                        {
                            file = new File(publicFolder + requestedFile);

                            bytesToSend = new byte[(int) file.length()];

                            bis = new BufferedInputStream( new FileInputStream( file ) );
                            bis.read(bytesToSend, 0, bytesToSend.length);

                            responseCode = 200;
                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception : " + e);
                            response = "Error dudeee";
                            responseCode = 404;
                        }

                        if (responseCode == 200)
                        {
                            Headers h = he.getResponseHeaders();
                            h.set("Content-Type", mime);
                        }
                        else
                        {
                            bytesToSend = response.getBytes();
                        }

                        he.sendResponseHeaders(responseCode, bytesToSend.length);

                        try (OutputStream os = he.getResponseBody())
                        {
                            os.write(bytesToSend, 0, bytesToSend.length);
                        }
                    }
        
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
                    he.getResponseHeaders().add("Content-Type", "application/json");
                    he.sendResponseHeaders(responseCode, 0);
                    try (OutputStream os = he.getResponseBody())
                    {
                        os.write(response.getBytes());
                    }
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
                    
                    he.getResponseHeaders().add("Content-Type", "application/json");
                    he.sendResponseHeaders(responseCode, 0);
                    try (OutputStream os = he.getResponseBody())
                    {
                        os.write(response.getBytes());
                    }
                    break;
            }
        }
    }
    
    private String getMime(String extension)
    {
        String mime = "";
        switch (extension)
        {
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
            default :
                mime = "text/html";
                break;
        }
        return mime;
    }

}
