package practice3;

import practice1.Packet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerQueue {

    public static final Queue<AddressedPacket> QUEUE = new ConcurrentLinkedQueue<>();

}
