package practice3;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerQueue {

    public static final Queue<AddressedPacket> QUEUE = new ConcurrentLinkedQueue<>();

}
