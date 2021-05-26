package practice1;

import java.util.Arrays;

public class Packet {

    private final byte client;
    private final long packet;
    private final byte[] message;
    private final int code;
    private final int user;

    public Packet(byte client, long packet, int code, int user, byte[] message) {
        this.client = client;
        this.packet = packet;
        this.message = message;
        this.code = code;
        this.user = user;
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

    public int getCode() {
        return code;
    }

    public int getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "client=" + client +
                ", packet=" + packet +
                ", message=" + Arrays.toString(message) +
                ", code=" + code +
                ", user=" + user +
                '}';
    }
}
