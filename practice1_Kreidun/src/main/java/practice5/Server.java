package practice5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import practice1.Product;

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
        sqlTest.insertProduct(new Product("product1", 10.5, 5.5));
        sqlTest.insertProduct(new Product("product2", 10, 5.5));
        sqlTest.insertProduct(new Product("product3", 10, 5));
        sqlTest.insertProduct(new Product("some", 100, 7));

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
                        String jwt = createJWT(dbUser.getLogin());
                        exchange.getResponseHeaders().set("authorization", jwt);
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

        HttpContext context = server.createContext("/api/good", exchange -> {
            if (exchange.getRequestMethod().equals("GET")) {
                String id = exchange.getRequestURI().toString().split("/")[3];
                try {
                    Product product = sqlTest.getProduct(id);
                    if (product != null) {
                        byte[] response = objectMapper.writeValueAsBytes(product);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, response.length);
                        exchange.getResponseBody().write(response);
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                    }
                } catch (Exception e) {
                    exchange.sendResponseHeaders(404, 0);
                    e.printStackTrace();
                }
            } else if (exchange.getRequestMethod().equals("PUT")) {
                String body = new String(exchange.getRequestBody().readAllBytes());

                try {
                    Product product = objectMapper.readValue(body, Product.class);
                    if (product != null && sqlTest.checkProduct(product)) {
                        int id = sqlTest.insertProduct(product).getId();
                        byte[] response = objectMapper.writeValueAsBytes("id: " + id);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(201, response.length);
                        exchange.getResponseBody().write(response);
                    } else {
                        exchange.sendResponseHeaders(409, 0);
                    }
                } catch (Exception e) {
                    exchange.sendResponseHeaders(409, 0);
                    e.printStackTrace();
                }
            } else if (exchange.getRequestMethod().equals("POST")) {
                String id = exchange.getRequestURI().toString().split("/")[3];
                String body = new String(exchange.getRequestBody().readAllBytes());
                try {
                    Product product = objectMapper.readValue(body, Product.class);
                    product.setId(Integer.parseInt(id));
                    if (product != null && sqlTest.checkProduct(product)) {
                        Product product1 = sqlTest.updateProduct(product);

                        if (product1 != null && sqlTest.checkProduct(product1)) {
                            byte[] response = objectMapper.writeValueAsBytes(product);
                            exchange.getResponseHeaders().set("Content-Type", "application/json");
                            exchange.sendResponseHeaders(204, 0);
                        } else {
                            exchange.sendResponseHeaders(404, 0);
                        }
                    } else {
                        exchange.sendResponseHeaders(409, 0);
                    }
                } catch (Exception e) {
                    exchange.sendResponseHeaders(409, 0);
                    e.printStackTrace();
                }
            } else if (exchange.getRequestMethod().equals("DELETE")) {
                String id = exchange.getRequestURI().toString().split("/")[3];
                try {
                    sqlTest.deleteProductById(Integer.parseInt(id));
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(204, 0);
                } catch (Exception e) {
                    exchange.sendResponseHeaders(404, 0);
                    e.printStackTrace();
                }
            }
            exchange.close();
        });

        context.setAuthenticator(new Authenticator() {
            @Override
            public Result authenticate(HttpExchange exch) {
                String jwt = exch.getRequestHeaders().getFirst("Authorization");
                if (jwt != null) {
                    try {
                        String login = getUserLoginFromJwt(jwt);
                        User user = sqlTest.getUserByLogin(login);

                        if (user != null) {
                            return new Success(new HttpPrincipal(login, "admin"));
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                return new Failure(403);
            }
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

    private static String getUserLoginFromJwt(String jwt) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //We will sign our JWT with our ApiKey secret
        Key signingKey = new SecretKeySpec(API_KEY_SECRET_BYTES, signatureAlgorithm.getJcaName());

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt).getBody();
        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());

        return claims.getSubject();
    }

}
