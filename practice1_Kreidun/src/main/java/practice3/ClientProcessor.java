package practice3;

import practice1.Main;
import practice1.Packet;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class ClientProcessor extends Thread {

    private final byte clientId;
    public static final Queue<Packet> queue = new ConcurrentLinkedQueue<>();
    private SocketAddress socketAddress;

    public ClientProcessor(byte clientId) {
        super("client processor " + clientId);
        this.clientId = clientId;
        start();
    }

    public void acceptPacket(Packet requestPacket, SocketAddress socketAddress) {
        System.out.println("packet received: " + requestPacket);
        this.socketAddress = socketAddress;
        queue.add(requestPacket);

        Packet responsePacket = new Packet(clientId, 10L, 1, 1, "accepted".getBytes(StandardCharsets.UTF_8));
        ServerQueue.QUEUE.add(new AddressedPacket(responsePacket, socketAddress));
    }

    @Override
    public void run() {
        while (true) {
            Packet packet = queue.poll();
            if (packet != null) {
                System.out.println(String.format("[client %s] Processing packet %s", clientId, new String(packet.getMessage())));

                Packet responsePacket = new Packet(clientId, 10L, 1, 1, "accepted".getBytes(StandardCharsets.UTF_8));
                ServerQueue.QUEUE.add(new AddressedPacket(responsePacket, socketAddress));
            }
        }
    }

}
