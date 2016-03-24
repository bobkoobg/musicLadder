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
        System.out.println("Hi");
        int responseCode = 500;
        //Set initial error values if an un expected problem occurs
        String errorMsg = null;
        byte[] bytesToSend = "<h1>Internal Error </h1><p>We are sorry. The server encountered an unexpected problem</p>".getBytes();
        String mime = null;

        String requestedFile = he.getRequestURI().toString();
        String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);
        try
        {
            String extension = f.substring(f.lastIndexOf("."));
            mime = getMime(extension);
            System.out.println("My mime : " + mime);
            File file = new File(publicFolder + f);
            System.out.println(publicFolder + f);
            bytesToSend = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
            bis.read(bytesToSend, 0, bytesToSend.length);
            responseCode = 200;
        }
        catch (Exception e)
        {
            responseCode = 404;
            errorMsg = "<h1>404 Not Found Hint, try : http://localhost:8084/pages/index.html </h1>No context found for request" + e;
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
        }
        return mime;
    }

}
