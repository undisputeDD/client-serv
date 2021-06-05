package practice3;

import practice1.Main;
import practice1.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClientProcessorTCP extends Thread {

    private final Socket socket;

    public ClientProcessorTCP(Socket socket) {
        //System.out.println("created --------");
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            System.out.println(input);

            byte[] data = new byte[1000];
            int length = input.read(data);
            if (length != -1) {
                //System.out.println("0000000");
                Packet packet = Main.decodePackage(Arrays.copyOfRange(data, 0, length));
                System.out.println(packet);
                System.out.println(new String(packet.getMessage()));

                Packet response = new Packet(packet.getClient(), packet.getPacketId(), packet.getCode(), packet.getUser(), "successful".getBytes(StandardCharsets.UTF_8));
                OutputStream output = socket.getOutputStream();
                output.write(Main.encodePackage(response));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
