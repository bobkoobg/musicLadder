package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServerHandler implements HttpHandler
{

    private String publicFolder = RESTfulAPIServer.publicFolder;

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        int responseCode = 500;
        //Set initial error values if an un expected problem occurs
        String errorMsg = null;
        byte[] bytesToSend = "<h1>Internal Error </h1><p>We are sorry. The server encountered an unexpected problem</p>".getBytes();
        String mime = null;

        String requestedFile = he.getRequestURI().toString();
        System.out.println("ServerHandler Req. file : " + requestedFile);
        String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);
        System.out.println("ServerHandler f is : " + f);
        
        File file;
        BufferedInputStream bis;

        System.out.println("hi");
        if( f == null || f.isEmpty() ) {
            System.out.println("I am empty");
            try
            {
                mime = ".html";
                file = new File(publicFolder + "index.html");
                
                bytesToSend = new byte[(int) file.length()];
                
                bis = new BufferedInputStream( new FileInputStream( file ) );
                bis.read(bytesToSend, 0, bytesToSend.length);
                
                responseCode = 200;
            }
            catch (Exception e)
            {
                responseCode = 500;
                errorMsg = "<h1>500 Server Error</h1>" 
                        + "<span>The main page does not exist. Please slap me.</span>";
                //Log the exception!
            }
        } else {
            int lastIndex = f.lastIndexOf(".");
            
            if( lastIndex > -1) {
                mime = f.substring(f.lastIndexOf("."));
            } else {
                mime = getMime(".html");
            }
            System.out.println("ServerHandler I am not empty" + mime);
            try
            {
                file = new File(publicFolder + f);
                
                bytesToSend = new byte[(int) file.length()];
                
                bis = new BufferedInputStream( new FileInputStream( file ) );
                bis.read(bytesToSend, 0, bytesToSend.length);
                
                responseCode = 200;
            }
            catch (Exception e)
            {
                responseCode = 404;
                errorMsg = "<h1>404 Not Found</h1>" 
                        + "<span>No context erfound for request" + he.getRequestURI().toString() + "</span>";
                //Log the exception!
            }
        }
        
        if (responseCode == 200)
        {
            Headers h = he.getResponseHeaders();
            h.set("Content-Type", mime);
        }
        else
        {
            bytesToSend = errorMsg.getBytes();
        }
        
        he.sendResponseHeaders(responseCode, bytesToSend.length);
        
        try (OutputStream os = he.getResponseBody())
        {
            os.write(bytesToSend, 0, bytesToSend.length);
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
            case ".css":
                mime = "text/css";
                break;
            default :
                mime = "text/html";
                break;
        }
        return mime;
    }

}
