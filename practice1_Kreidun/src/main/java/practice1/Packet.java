package practice1;

import java.util.Arrays;

public class Packet {

    private final byte client;
    private final long packet;
    private final byte[] message;

    public Packet(byte client, long packet, byte[] message) {
        this.client = client;
        this.packet = packet;
        this.message = message;
    }

    public byte getClient() {
        return client;
    }

    public long getPacketId() {
        return packet;
    }

    public byte[] getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "client=" + client +
                ", packet=" + packet +
                ", message=" + Arrays.toString(message) +
                '}';
    }

}
