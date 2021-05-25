package practice1;

import static practice1.CRC16.crc16;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Main {

    private static final byte MAGIC_BYTE = 0x13;

    public static void main(String[] args) {

        byte[] packet = encodePackage("Hello world");
        decodePackage(packet);

    }

    public static String decodePackage(byte[] bytes) {
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

        return null;
    }

    public static byte[] encodePackage(String messageS) {
        byte[] message = messageS.getBytes(StandardCharsets.UTF_8);

        byte[] head = ByteBuffer.allocate(14)
                .order(ByteOrder.BIG_ENDIAN)
                .put(MAGIC_BYTE)
                .put((byte)0x05)
                .putLong(10)
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
