package practice3;

import practice1.Main;
import practice1.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ServerTCP {

    private final ServerSocket serverSocket;
    private static final int MAX_CLIENTS = 50;
    private static int NUM_CLIENTS;

    public static final int PORT = 3000;

    public ServerTCP() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on port " + PORT + "...");

        NUM_CLIENTS = 0;
    }

    public void start() throws IOException, InterruptedException {
        while (true) {
            //System.out.println("AAAA");
            Socket clientSocket = serverSocket.accept();
            // WHY EVERY TIME HERE NEW PORT???
            // EVEN IF CLIENT IS THE SAME....
            // System.out.println("PORT = " + clientSocket.getPort());

            if (NUM_CLIENTS < MAX_CLIENTS) {
                System.out.println("New Client is trying to connect...");

                // Creating new ClientProcessorTCP for handling current connected user
                ClientProcessorTCP cpTCP = new ClientProcessorTCP(clientSocket);
                cpTCP.start();
                NUM_CLIENTS += 1;
                Thread.sleep(100);
            } else {
                System.out.println("Max number of clients reached!");
            }
        }
    }

}
