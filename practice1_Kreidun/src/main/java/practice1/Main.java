package practice1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        byte[] message = "hello world".getBytes(StandardCharsets.UTF_8);
        ByteBuffer.allocate(16 + message.length + 2)
                .order(ByteOrder.BIG_ENDIAN);
    }
}
