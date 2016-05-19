package es.ucm.fdi.tp.practica6.lobby.demo.net;

import java.net.Socket;

/**
 * A very simple interface for listening and responding
 * to data on sockets
 */
public interface SocketEndpoint {
    /**
     * call this to start listening on the socket. Any incoming data will result in a call to dataReceived
     * @param socket
     * @param timeout
     */
    void start(Socket socket, int timeout);

    /**
     * will be called when communication is first established
     */
    void connectionEstablished();

    /**
     * call this to stop communication
     */
    void stop();

    /**
     * will be called when data is received
     * @param data
     */
    void dataReceived(Object data);

    /**
     * call this to send data to the other side
     * @param data
     */
    void sendData(Object data);
}
