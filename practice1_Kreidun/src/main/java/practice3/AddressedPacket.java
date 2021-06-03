package practice3;

import practice1.Packet;

import java.net.SocketAddress;

public class AddressedPacket {

    private final Packet packet;
    private final SocketAddress socketAddress;

    public AddressedPacket(Packet packet, SocketAddress socketAddress) {
        this.packet = packet;
        this.socketAddress = socketAddress;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public Packet getPacket() {
        return packet;
    }
}
