package practice3;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ClientTester {

    public static void main(String[] args) throws Exception {
        for (byte i = 0; i < 30; ++i) {
            client(i);
        }
    }

    private static void client(byte id) {
        new Thread(() -> {
            try {
                Thread.sleep(new Random().nextInt(100));

                Client client = new ClientUDP(id);
                client.send(("hello world " + id).getBytes(StandardCharsets.UTF_8));
                System.out.println(client.receive());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        })
                .start();
    }

}
