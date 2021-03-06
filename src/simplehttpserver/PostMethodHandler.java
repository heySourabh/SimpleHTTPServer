package simplehttpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class PostMethodHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, String> queryMap = validateURLAndGetQueryMap(he);
        if (queryMap == null) {
            return;
        }

        // Put the logic to use the queryMap and respond, below this line.
        // Below is some logic for debugging which you may want to replace:
        String out = "You have sent " + queryMap.size() + " parameters using post: " + queryMap;
        he.getResponseHeaders().add("Content-type", "text/html");

        byte[] bytes = out.getBytes();

        he.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = he.getResponseBody()) {
            os.write(bytes);
        }
    }

    private Map<String, String> validateURLAndGetQueryMap(HttpExchange he) throws IOException {
        URI uri = he.getRequestURI();
        String path = uri.getPath();
        String context = he.getHttpContext().getPath();

        if (!context.equals(path) && !(context + "/").equals(path)) {
            new RequestHandler().handle(he);
            return null;
        }

        if (!he.getRequestMethod().equals("POST")) {
            return queryToMap("");
        }

        System.out.println(he.getRequestHeaders().entrySet());
        InputStream requestStream = he.getRequestBody();
        byte[] bytes = new byte[1024];
        int numRead;
        String query = "";
        while ((numRead = requestStream.read(bytes)) != -1) {
            query += new String(bytes, 0, numRead);
        }
        System.out.println("Raw query: " + query);
        query = URLDecoder.decode(query, "UTF-8");

        return queryToMap(query);
    }

    private static Map<String, String> queryToMap(String query) {
        Map<String, String> queryMap = new HashMap<>();
        if (query == null) {
            return queryMap;
        }

        String[] params = query.split("&");
        for (String param : params) {
            if (param.trim().isEmpty()) {
                continue;
            }
            if (param.contains("=")) {
                int loc = param.indexOf("=");
                String key = param.substring(0, loc).trim();
                if (key.isEmpty()) {
                    continue;
                }
                String val = param.substring(loc + 1);
                queryMap.put(key, val);
            } else {
                String key = param.trim();
                String val = "";
                queryMap.put(key, val);
            }
        }

        return queryMap;
    }
}
