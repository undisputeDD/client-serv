package practice3;

import java.nio.charset.StandardCharsets;

public class ClientTester {

    public static void main(String[] args) throws Exception {
        new ClientUDP((byte) 1)
                .send(("hello world".getBytes(StandardCharsets.UTF_8)));
        new ClientUDP((byte) 2)
                .send(("hello world from 2".getBytes(StandardCharsets.UTF_8)));
    }

}
