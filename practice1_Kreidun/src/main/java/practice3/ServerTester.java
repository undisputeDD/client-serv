package practice3;

import practice1.Packet;

import java.net.SocketException;

public class ServerTester {

    public static void main(String[] args) throws Exception {
        ServerUDP server = new ServerUDP();
        while (true) {
            server.receive();
        }
    }

}
