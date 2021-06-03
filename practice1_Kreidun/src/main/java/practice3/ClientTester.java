package practice3;

import practice1.Packet;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientTester {

    private static final int SIZE = 4;
    private final static Map<Byte, ArrayList<Packet>> dataToTest = new HashMap<>();

    public static void main(String[] args) throws Exception {

        ArrayList<Packet> p1 = new ArrayList<>();
        p1.add(new Packet((byte) 0, 0L, 1, 1, "hello".getBytes(StandardCharsets.UTF_8)));
        p1.add(new Packet((byte) 0, 1L, 1, 1, " world".getBytes(StandardCharsets.UTF_8)));

        ArrayList<Packet> p2 = new ArrayList<>();
        p2.add(new Packet((byte) 1, 0L, 1, 1, "hello".getBytes(StandardCharsets.UTF_8)));
        p2.add(new Packet((byte) 1, 1L, 1, 1, " world".getBytes(StandardCharsets.UTF_8)));
        p2.add(new Packet((byte) 1, 2L, 1, 1, " 1".getBytes(StandardCharsets.UTF_8)));
        p2.add(new Packet((byte) 1, 3L, 1, 1, " 2".getBytes(StandardCharsets.UTF_8)));

        ArrayList<Packet> p3 = new ArrayList<>();
        p3.add(new Packet((byte) 2, 0L, 1, 1, "hello".getBytes(StandardCharsets.UTF_8)));
        p3.add(new Packet((byte) 2, 1L, 1, 1, " world".getBytes(StandardCharsets.UTF_8)));
        p3.add(new Packet((byte) 2, 2L, 1, 1, " 10".getBytes(StandardCharsets.UTF_8)));

        ArrayList<Packet> p4 = new ArrayList<>();
        p4.add(new Packet((byte) 3, 0L, 1, 1, "hello".getBytes(StandardCharsets.UTF_8)));
        p4.add(new Packet((byte) 3, 1L, 1, 1, " world".getBytes(StandardCharsets.UTF_8)));
        p4.add(new Packet((byte) 3, 2L, 1, 1, " 6".getBytes(StandardCharsets.UTF_8)));
        p4.add(new Packet((byte) 3, 3L, 1, 1, " 7".getBytes(StandardCharsets.UTF_8)));
        p4.add(new Packet((byte) 3, 4L, 1, 1, " 8".getBytes(StandardCharsets.UTF_8)));
        p4.add(new Packet((byte) 3, 5L, 1, 1, " 9".getBytes(StandardCharsets.UTF_8)));

        dataToTest.put((byte) 0, p1);
        dataToTest.put((byte) 1, p2);
        dataToTest.put((byte) 2, p3);
        dataToTest.put((byte) 3, p4);

        for (byte i = 0; i < SIZE; ++i) {
            client(dataToTest.get(i), i);
        }
    }

    private static void client(ArrayList<Packet> packets, byte i) {
        new Thread(() -> {
            try {
                Thread.sleep(new Random().nextInt(100));

                System.out.println(packets.size());
                ClientUDP client = new ClientUDP(packets);
                for (int j = 0; j < packets.size(); ++j) {
                    client.send(j);
                }
                System.out.println(client.receive());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        })
                .start();
    }

}
