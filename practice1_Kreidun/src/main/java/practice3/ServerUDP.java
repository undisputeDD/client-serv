package practice3;

import practice1.Main;
import practice1.Packet;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerUDP {

    public static final int PORT = 3000;
    private final DatagramSocket socket;
    private final ConcurrentMap<Byte, ClientProcessor> clientMap;

    public ServerUDP() throws SocketException {
        socket = new DatagramSocket(PORT);
        clientMap = new ConcurrentHashMap<>();
    }

    public void start() {
        new Thread(this::send, "Sender")
                .start();
        new Thread(this::receive, "Receiver")
                .start();
    }

    private void send() {
        while (true) {
            try {
                AddressedPacket packet = ServerQueue.QUEUE.poll();
                if (packet != null) {
                    byte[] packetBytes = Main.encodePackage(packet.getPacket());
                    DatagramPacket datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, packet.getSocketAddress());

                    socket.send(datagramPacket);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void receive() {
        while (true) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(new byte[1000], 1000);
                try {
                    socket.receive(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Packet packet = Main.decodePackage(Arrays.copyOfRange(datagramPacket.getData(), 0, datagramPacket.getLength()));

                System.out.println(packet.getClient());
                clientMap.computeIfAbsent(packet.getClient(), key -> new ClientProcessor(packet.getClient()))
                        .acceptPacket(packet, datagramPacket.getSocketAddress());
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
