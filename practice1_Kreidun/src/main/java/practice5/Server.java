package practice5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.start();
        ObjectMapper objectMapper = new ObjectMapper();

        server.createContext("/", exchange -> {
            if (exchange.getRequestMethod().equals("GET")) {
                byte[] response = "{\"status\": \"ok\"}".getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length);
                exchange.getResponseBody().write(response);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });

        server.createContext("/login", exchange -> {
            if (exchange.getRequestMethod().equals("POST")) {
                User user = objectMapper.readValue(exchange.getRequestBody(), User.class);
                System.out.println(user);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
    }

}
