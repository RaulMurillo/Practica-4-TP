package es.ucm.fdi.tp.practica6.lobby.demo;

import java.util.ArrayList;

import es.ucm.fdi.tp.practica6.lobby.demo.net.AbstractServer;
import es.ucm.fdi.tp.practica6.lobby.demo.net.ObjectEndpoint;
import es.ucm.fdi.tp.practica6.lobby.demo.net.SocketEndpoint;

/**
 * Created by mfreire on 28/04/16.
 */
public class ServerLauncher {

    public static void main(String ... args) {
        final ArrayList<ObjectEndpoint> endpoints = new ArrayList<>();
        AbstractServer as = new AbstractServer() {
            @Override
            protected SocketEndpoint createEndpoint(final String name) {
                return new ObjectEndpoint(name) {
                    @Override
                    public synchronized void connectionEstablished() {
                        log.info("Ok, link to client seems to be up and running");
                        endpoints.add(this);
                    }

                    @Override
                    public void dataReceived(Object data) {
                        log.info("Received data: " + data);
                        for (ObjectEndpoint oe : endpoints) {
                            if (oe != this) {
                                oe.sendData(data);
                            }
                        }
                        if (data.equals("stop")) {
                            stop();
                        }
                    }
                };
            }
        };
        as.start();
    }
}
