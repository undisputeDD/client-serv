package practice1;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static practice1.CRC16.crc16;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    private static final byte MAGIC_BYTE = 0x13;
    private static final String algorithm = "AES";
    private static final byte[] keyValue=new byte[] {'0','2','3','4','5','6','7','8','9','1','2','3','4','5','6','7'};

    public static void main(String[] args) throws Exception {

        byte[] packet = encodePackage(new Packet((byte) 1, 10L, 1, 1, "hello world".getBytes(StandardCharsets.UTF_8)));
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
        //System.out.println("client: " + client);
        long packet = bb.getLong();
        //System.out.println("packetId: " + packet);
        int messageLength = bb.getInt();
        //System.out.println("length: " + messageLength);
        short crc16Head = bb.getShort();
        //System.out.println("crc16 head: " + crc16Head);
        int code = bb.getInt();
        //System.out.println("code: " + code);
        int user = bb.getInt();
        //System.out.println("user: " + user);

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

        byte[] encryptedMessage = Arrays.copyOfRange(bytes, 16 + 4 + 4, 16 + messageLength);
        short crc16Message = bb.getShort(16 + messageLength);
        byte[] message = decryptMessage(new String(encryptedMessage)).getBytes(StandardCharsets.UTF_8);

        if (crc16(encryptedMessage) != crc16Message) {
            throw new IllegalArgumentException("CRC16 Message");
        }

        return new Packet(client, packet, code, user, message);
    }

    public static byte[] encodePackage(Packet packet) throws Exception {
        byte[] message = packet.getMessage();
        byte[] encryptedMessage = encryptMessage(new String(message)).getBytes(StandardCharsets.UTF_8);
        byte[] head = ByteBuffer.allocate(14)
                .order(ByteOrder.BIG_ENDIAN)
                .put(MAGIC_BYTE)
                .put(packet.getClient())
                .putLong(packet.getPacketId())
                .putInt(encryptedMessage.length + 4 + 4)
                .array();

        return ByteBuffer.allocate(16 + 4 + 4 + encryptedMessage.length + 2)
                .order(ByteOrder.BIG_ENDIAN)
                .put(head)
                .putShort(crc16(head))
                .putInt(packet.getCode())
                .putInt(packet.getUser())
                .put(encryptedMessage)
                .putShort(crc16(encryptedMessage))
                .array();
    }

    public static String encryptMessage(String message) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    //generateKey() is used to generate a secret key for AES algorithm
    private static Key generateKey()
    {
        return new SecretKeySpec(keyValue, algorithm);
    }

    public static String decryptMessage(String message) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(message);
            byte[] decValue = cipher.doFinal(decodedValue);
            return new String(decValue);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
