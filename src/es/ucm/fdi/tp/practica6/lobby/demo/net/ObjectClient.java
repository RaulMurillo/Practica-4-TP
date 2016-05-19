package es.ucm.fdi.tp.practica6.lobby.demo.net;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Creates a generic client for socket-based communication
 * where objects are exchanged via serialization
 */
public class ObjectClient extends ObjectEndpoint {
    private static final Logger log = Logger.getLogger(AbstractServer.class.getSimpleName());

    private String hostname;
    private int port;
    private int timeout;

    public ObjectClient() throws IOException {
        this("localhost", 2021, 2000);
    }

    public ObjectClient(String hostname, int port, int timeout) throws IOException {
        super("Client");
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;
    }

    public void start() throws IOException {
        start(new Socket(hostname, port), timeout);
    }

    @Override
    public void connectionEstablished() {}

    @Override
    public void dataReceived(Object data) {
        log.info("Object received: " + data);
    }
}
