package es.ucm.fdi.tp.practica6.server;

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
	 * in a call to {@link dataReceived}.
	 * <p>
	 * Inicia la escucha sobre el socket. Cualquier dato entrante desencadenara
	 * una llamada a {@link dataReceived}.
	 * 
	 * @param socket
	 *            The socket on which they want to listen.
	 *            <p>
	 *            El socket sobre el que se quiere escuchar.
	 * @param timeout
	 *            Time limit for awaiting connection.
	 *            <p>
	 *            Plazo para la espera de conexion.
	 * @throws IOException
	 */
	void startConnection(Socket socket, int timeout) throws IOException;

	/**
	 * Call this to stop communication.
	 * <p>
	 * Para la comunicacion por el socket.
	 */
	void stopConnection();

	/**
	 * Will be called when data is received.
	 * <p>
	 * Gestiona los datos recibidos.
	 * 
	 * @param data
	 *            The receibed data.
	 *            <p>
	 *            Los datos recibidos.
	 */
	void dataReceived(Object data);

	/**
	 * Call this to send data to the other side.
	 * <p>
	 * Permite enviar datos al otro lado.
	 * 
	 * @param data
	 *            The data to send.
	 *            <p>
	 *            Los datos a enviar.
	 */
	void sendData(Object data);
}
