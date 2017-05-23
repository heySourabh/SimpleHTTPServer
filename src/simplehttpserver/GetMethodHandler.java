package simplehttpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
class GetMethodHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, String> queryMap = validateURLAndGetQueryMap(he);
        if (queryMap == null) {
            return;
        }

        // Put the logic to use the queryMap and respond, below this line.
        // Below is some logic for debugging which you may want to replace:
        String out = "You have sent " + queryMap.size() + " parameters: " + queryMap;
        he.getResponseHeaders().add("Content-Type", "text/html");

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

        String query = uri.getQuery();

        System.out.println("URI: " + uri);
        System.out.println("Query: " + query);

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
