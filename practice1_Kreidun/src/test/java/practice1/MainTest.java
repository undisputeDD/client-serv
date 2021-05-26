package practice1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void shouldEncodePackage() throws Exception {
        Packet input = new Packet((byte) 1, 111, 1, 1, "Hello world".getBytes(StandardCharsets.UTF_8));
        byte[] encodedInput = Main.encodePackage(input);

        Packet decoded = Main.decodePackage(encodedInput);

        assertEquals(1, decoded.getClient());
        assertEquals(111, decoded.getPacketId());
        assertEquals(1, decoded.getCode());
        assertEquals(1, decoded.getUser());
        assertEquals("Hello world", new String(decoded.getMessage(), StandardCharsets.UTF_8));
    }

    @ParameterizedTest
    @CsvSource({
            "10, 222, 1, 1, hello world",
            "1, 222, 2, 3, some test",
            "10, 213, 3, 4, asfa asdasda",
            "5, 222, 1, 1, world"
    })
    void shouldEncodePackage_params(byte client, long packet, int code, int user, String message) throws Exception {
        Packet input = new Packet(client, packet, code, user, message.getBytes(StandardCharsets.UTF_8));
        byte[] encodedInput = Main.encodePackage(input);

        Packet decoded = Main.decodePackage(encodedInput);

        assertEquals(client, decoded.getClient());
        assertEquals(packet, decoded.getPacketId());
        assertEquals(code, decoded.getCode());
        assertEquals(user, decoded.getUser());
        assertEquals(message, new String(decoded.getMessage(), StandardCharsets.UTF_8));
    }

}