package practice5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Server {

    public static final byte[] API_KEY_SECRET_BYTES = "my-token-secret-key-asfjsb164t21bd19783b562189b5nst1278hsnsfuuiyn".getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.start();
        ObjectMapper objectMapper = new ObjectMapper();

        SQLTest sqlTest = new SQLTest();
        sqlTest.initialization("DB");
        sqlTest.insertUser(new User("login1", "password1"));
        sqlTest.insertUser(new User("login2", "password2"));
        sqlTest.insertUser(new User("login3", "password3"));

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
                User dbUser = sqlTest.getUserByLogin(user.getLogin());

                if (dbUser != null) {
                    if (dbUser.getPassword().equals(user.getPassword())) {
                        exchange.getResponseHeaders().set("authorization", createJWT(dbUser.getLogin()));
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        exchange.sendResponseHeaders(401, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(401, 0);
                }
                System.out.println(user);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
    }

    private static String createJWT(String login) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Date now = new Date();

        //We will sign our JWT with our ApiKey secret
        Key signingKey = new SecretKeySpec(API_KEY_SECRET_BYTES, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TimeUnit.HOURS.toMillis(10)))
                .setSubject(login)
                .signWith(signingKey, signatureAlgorithm)
                .claim("username", "Andrii")
                .compact();
    }

}
