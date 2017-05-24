package simplehttpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        File file;
        String req = he.getRequestURI().getPath();
        System.out.println(req);
        if (req.equals("/")) {
            he.getResponseHeaders().add("Content-type", "text/html");
            file = new File(SimpleHTTPServer.BASE_PATH, SimpleHTTPServer.BASE_PAGE);
        } else {
            file = new File(SimpleHTTPServer.BASE_PATH, req);
        }

        if (!file.exists()) {
            System.out.println("The path: \""
                    + file.getPath() + "\" does not exist.");
            byte[] resBytes = getErrorPageBytes();
            he.getResponseHeaders().add("Content-type", "text/html");
            he.sendResponseHeaders(404, resBytes.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(resBytes);
            }
            return;
        }

        FileInputStream fis = new FileInputStream(file);
        he.sendResponseHeaders(200, fis.available());
        try (OutputStream os = he.getResponseBody()) {
            Files.copy(file.toPath(), os);
        }
    }

    private byte[] getErrorPageBytes() throws IOException {
        File errorPageFile = new File(SimpleHTTPServer.BASE_PATH, "404.html");
        if (errorPageFile.exists()) {
            return Files.readAllBytes(errorPageFile.toPath());
        } else {
            return "<h1>Does not exist.</h1>".getBytes();
        }
    }
}
