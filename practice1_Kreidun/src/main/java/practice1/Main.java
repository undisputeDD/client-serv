package practice1;

import static practice1.CRC16.crc16;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {

    private static final byte MAGIC_BYTE = 0x13;

    public static void main(String[] args) {

        byte[] packet = encodePackage(new Packet((byte) 1, 10L, "hello world".getBytes(StandardCharsets.UTF_8)));
        Packet packet1 = decodePackage(packet);
        System.out.println(new String(packet1.getMessage(), StandardCharsets.UTF_8));
        System.out.println(packet1.toString());
    }

    public static Packet decodePackage(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);

        if (bb.get() != MAGIC_BYTE) {
            throw new IllegalArgumentException("Magic Byte");
        }

        byte client = bb.get();
        System.out.println("client: " + client);
        long packet = bb.getLong();
        System.out.println("packetId: " + packet);
        int messageLength = bb.getInt();
        System.out.println("length: " + messageLength);
        short crc16Head = bb.getShort();
        System.out.println("crc16 head: " + crc16Head);

        byte[] head = ByteBuffer.allocate(14)
                .order(ByteOrder.BIG_ENDIAN)
                .put(MAGIC_BYTE)
                .put(client)
                .putLong(packet)
                .putInt(messageLength)
                .array();

        if (crc16(head) != crc16Head) {
            throw new IllegalArgumentException("CRC16 Head");
        }

        byte[] message = Arrays.copyOfRange(bytes, 16, 16 + messageLength);
        short crc16Message = bb.getShort(16 + messageLength);

        if (crc16(message) != crc16Message) {
            throw new IllegalArgumentException("CRC16 Message");
        }

        return new Packet(client, packet, message);
    }

    public static byte[] encodePackage(Packet packet) {
        byte[] message = packet.getMessage();

        byte[] head = ByteBuffer.allocate(14)
                .order(ByteOrder.BIG_ENDIAN)
                .put(MAGIC_BYTE)
                .put(packet.getClient())
                .putLong(packet.getPacketId())
                .putInt(message.length)
                .array();

        return ByteBuffer.allocate(16 + message.length + 2)
                .order(ByteOrder.BIG_ENDIAN)
                .put(head)
                .putShort(crc16(head))
                .put(message)
                .putShort(crc16(message))
                .array();
    }
}
