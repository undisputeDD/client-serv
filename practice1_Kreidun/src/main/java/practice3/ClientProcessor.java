package practice3;

import practice1.Packet;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class ClientProcessor extends Thread {

    private final byte clientId;
    private SocketAddress socketAddress;

    public ClientProcessor(byte clientId) {
        this.clientId = clientId;
    }

    public void acceptPacket(Packet requestPacket, SocketAddress socketAddress) {
        System.out.println("packet received: " + requestPacket);
        this.socketAddress = socketAddress;

        Packet responsePacket = new Packet(clientId, 10L, 1, 1, "accepted".getBytes(StandardCharsets.UTF_8));
        ServerQueue.QUEUE.add(new AddressedPacket(responsePacket, socketAddress));
    }

}
