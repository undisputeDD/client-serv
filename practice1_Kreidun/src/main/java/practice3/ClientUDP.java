package practice3;

import practice1.Main;
import practice1.Packet;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientUDP {

    private final DatagramSocket socket;
    private final byte clientId;
    private final ArrayList<Packet> packetsToSend;
    private final ArrayList<Packet> packets;

    public ClientUDP(ArrayList<Packet> packets) throws SocketException {
        socket = new DatagramSocket();
        this.packets = new ArrayList<>();
        this.clientId = packets.get(0).getClient();
        this.packetsToSend = packets;
    }

    public void send(int i) throws Exception {
        byte[] encodePackage = Main.encodePackage(packetsToSend.get(i));
        DatagramPacket datagramPacket = new DatagramPacket(encodePackage, encodePackage.length, InetAddress.getByName(null), ServerUDP.PORT);
        try {
            socket.send(datagramPacket);
            packetsToSend.add(Main.decodePackage(Arrays.copyOfRange(datagramPacket.getData(), 0, datagramPacket.getLength())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Packet receive() throws Exception {
        DatagramPacket packet = new DatagramPacket(new byte [1000], 1000);
        try {
            socket.receive(packet);
            Packet receivedPacket = Main.decodePackage(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
            if (receivedPacket.getUser() == -1 && receivedPacket.getCode() == -1) {
                // should resend packet with packetId == receivedPacket.packetId
                for (int i = 0; i < packets.size(); ++i) {
                    if (packets.get(i).getPacketId() == receivedPacket.getPacketId()) {
                        byte[] encodePackage = Main.encodePackage(packets.get(i));
                        DatagramPacket datagramPacket = new DatagramPacket(encodePackage, encodePackage.length, InetAddress.getByName(null), ServerUDP.PORT);
                        packets.clear();
                        try {
                            socket.send(datagramPacket);
                            packets.add(Main.decodePackage(Arrays.copyOfRange(datagramPacket.getData(), 0, datagramPacket.getLength())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Main.decodePackage(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
    }

    public boolean isConnectionAvailable() {
        return false;
    }

}
