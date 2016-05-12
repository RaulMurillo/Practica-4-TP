package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.net.Socket;

/**
 * A very simple interface for listening and responding to data on sockets.
 * <p>
 * Interfaz simple para recibir y responder peticiones en sockets.
 */
public interface SocketEndpoint {
	/**
	 * Call this to start listening on the socket. Any incoming data will result
	 * in a call to dataReceived
	 * 
	 * @param socket
	 * @param timeout
	 */
	void startConnection(Socket socket, int timeout) throws IOException;

	/**
	 * call this to stop communication
	 */
	void stopConnection();

	/**
	 * will be called when data is received
	 * 
	 * @param data
	 */
	void dataReceived(Object data);

	/**
	 * call this to send data to the other side
	 * 
	 * @param data
	 */
	void sendData(Object data);
}
