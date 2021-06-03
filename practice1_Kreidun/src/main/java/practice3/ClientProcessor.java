package practice3;

import practice1.Main;
import practice1.Packet;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class ClientProcessor extends Thread {

    private final byte clientId;
    public static Deque<Packet> queue = new ConcurrentLinkedDeque<>();
    private SocketAddress socketAddress;
    long lastId;

    public ClientProcessor(byte clientId) {
        super("client processor " + clientId);
        this.clientId = clientId;
        lastId = -1;
        start();
    }

    public void acceptPacket(Packet requestPacket, SocketAddress socketAddress) {
        System.out.println("packet received: " + requestPacket);
        System.out.println(new String(requestPacket.getMessage()));
        this.socketAddress = socketAddress;
        if (lastId == -1)
            lastId = requestPacket.getPacketId();
        else if (lastId + 1 != requestPacket.getPacketId()) {
            //System.out.println("HEIHEIHEI");
            Packet responsePacket = new Packet(clientId, lastId + 1, -1, -1, ("Missed packet #" + lastId + 1).getBytes(StandardCharsets.UTF_8));
            ServerQueue.QUEUE.add(new AddressedPacket(responsePacket, socketAddress));
        } else {
            Packet responsePacket = new Packet(clientId, requestPacket.getPacketId(), requestPacket.getCode(), requestPacket.getUser(), "accepted".getBytes(StandardCharsets.UTF_8));
            ServerQueue.QUEUE.add(new AddressedPacket(responsePacket, socketAddress));
            lastId = requestPacket.getPacketId();
        }
    }

    @Override
    public void run() {
        while (true) {
            Packet packet = queue.poll();
            if (packet != null) {
                System.out.println(String.format("[client %s] Processing packet %s", clientId, new String(packet.getMessage())));
                if (packet.getCode() == -1 && packet.getUser() == -1) {
                    Packet responsePacket = new Packet(clientId, packet.getPacketId(), -1, -1, ("Missed packet #" + packet.getPacketId()).getBytes(StandardCharsets.UTF_8));
                    ServerQueue.QUEUE.add(new AddressedPacket(responsePacket, socketAddress));
                } else {
                    Packet responsePacket = new Packet(clientId, packet.getPacketId(), packet.getCode(), packet.getUser(), "accepted".getBytes(StandardCharsets.UTF_8));
                    ServerQueue.QUEUE.add(new AddressedPacket(responsePacket, socketAddress));
                }
            }
        }
    }

}
