package practice3;

import practice1.Main;
import practice1.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTCP {

    private final Socket socket;
    private static final int ATTEMPTS = 5;

    public ClientTCP() throws IOException {
        this.socket = new Socket(InetAddress.getByName(null), ServerTCP.PORT);
    }

    public void send(Packet packet) throws Exception {
        System.out.println(packet);
        System.out.println(new String(packet.getMessage()));

        OutputStream output = socket.getOutputStream();
        boolean ok = true;

        if (isAvailable()) {
            output.write(Main.encodePackage(packet));
        } else {
            System.out.println("Unreachable host");
            for (int i = 0; i < ATTEMPTS; i++) {
                Thread.sleep(5000);
                if (isAvailable()) {
                    System.out.println("Successfully sent");
                    output.write(Main.encodePackage(packet));
                    ok = false;
                    break;
                } else {
                    System.out.println("Attempt failed");
                }
            }
        }
        if (!ok) {
            System.out.println("Error while connecting");
        } else {
            System.out.println("Data has been transferred successfully");
        }
    }

    public Packet receive() throws IOException {
        byte[] data = new byte[1000];
        InputStream input = socket.getInputStream();
        input.read(data);
        Packet packet = Main.decodePackage(data);
        System.out.println("From server:");
        System.out.println(packet);
        System.out.println(new String(packet.getMessage()));
        return packet;
    }

    public static boolean isAvailable() {
        try (Socket socket = new Socket(InetAddress.getByName(null), ServerUDP.PORT)) {
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
