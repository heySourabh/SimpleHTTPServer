package simplehttpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleHTTPServer {

    static final String BASE_PATH = "./webpage/";
    static final String BASE_PAGE = "index.html";

    static final int DEFAULT_PORT = 8000;

    public static void main(String[] args) throws Exception {
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            port = DEFAULT_PORT;
        }
        System.out.println("Starting server at port: " + port);
        System.out.println("Serving page: " + BASE_PATH + BASE_PAGE);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class RequestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            File file;
            String req = httpExchange.getRequestURI().getPath();
            System.out.println(req);
            if (req.equals("/")) {
                file = new File(BASE_PATH, BASE_PAGE);
            } else {
                file = new File(BASE_PATH, req);
            }
            FileInputStream fis = new FileInputStream(file);
            httpExchange.sendResponseHeaders(200, fis.available());
            try (OutputStream os = httpExchange.getResponseBody()) {
                Files.copy(Paths.get(file.toURI()), os);
            }
        }
    }
}