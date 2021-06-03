package practice3;

import practice1.Main;
import practice1.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class ClientUDP implements Client {

    private final DatagramSocket socket;
    private final byte clientId;

    public ClientUDP(byte clientId) throws SocketException {
        socket = new DatagramSocket();
        this.clientId = clientId;
    }

    @Override
    public void send(byte[] bytes) throws Exception {
        byte[] encodePackage = Main.encodePackage(new Packet(clientId, 10L, 1, 1, bytes));
        DatagramPacket datagramPacket = new DatagramPacket(encodePackage, encodePackage.length, InetAddress.getByName(null), ServerUDP.PORT);
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Packet receive() throws Exception {
        DatagramPacket packet = new DatagramPacket(new byte [1000], 1000);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Main.decodePackage(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
    }

    @Override
    public boolean isConnectionAvailable() {
        return false;
    }

}
