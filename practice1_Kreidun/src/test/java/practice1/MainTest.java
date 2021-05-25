package practice1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void shouldEncodePackage() {
        Packet input = new Packet((byte) 1, 111, "Hello world".getBytes(StandardCharsets.UTF_8));
        byte[] encodedInput = Main.encodePackage(input);

        Packet decoded = Main.decodePackage(encodedInput);

        assertEquals(1, decoded.getClient());
        assertEquals(111, decoded.getPacketId());
        assertEquals("Hello world", new String(decoded.getMessage(), StandardCharsets.UTF_8));
    }

    @ParameterizedTest
    @CsvSource({
            "10, 222, hello world",
            "1, 222, some test",
            "10, 213, asfa asdasda",
            "5, 222, world"
    })
    void shouldEncodePackage_params(byte client, long packet, String message) {
        Packet input = new Packet(client, packet, message.getBytes(StandardCharsets.UTF_8));
        byte[] encodedInput = Main.encodePackage(input);

        Packet decoded = Main.decodePackage(encodedInput);

        assertEquals(client, decoded.getClient());
        assertEquals(packet, decoded.getPacketId());
        assertEquals(message, new String(decoded.getMessage(), StandardCharsets.UTF_8));
    }

}