package simplehttpserver;

import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import java.io.File;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
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
        System.out.println("Serving page: "
                + new File(BASE_PATH, BASE_PAGE).getPath());
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RequestHandler());
        server.createContext("/q", new GetMethodHandler());
        server.createContext("/form", new PostMethodHandler());
        server.setExecutor(null);
        server.start();
    }
}
