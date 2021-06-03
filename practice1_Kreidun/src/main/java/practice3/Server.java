package practice3;

import practice1.Packet;

public interface Server {

    void send(Packet packet);

    Packet receive() throws Exception;

}
