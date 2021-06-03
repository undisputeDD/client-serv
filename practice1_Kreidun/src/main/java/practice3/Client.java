package practice3;

import practice1.Packet;

public interface Client {

    void send(byte[] bytes) throws Exception;

    Packet receive() throws Exception;

    boolean isConnectionAvailable();

}
