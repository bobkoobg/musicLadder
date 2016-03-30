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
                    String requestedFile = he.getRequestURI().toString();
                    String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);

                    String extension = f.substring(f.lastIndexOf("."));
                    String mime = getMime(extension);
                    
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
                        response = "Exception : " + e;
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
                    
        
                    break;
                    
                case "POST":
                    responseCode = 500;
                    response = "not supported";
                    break;
                case "DELETE":
                    responseCode = 500;
                    response = "not supported";
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
