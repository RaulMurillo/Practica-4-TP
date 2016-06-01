package es.ucm.fdi.tp.practica6;

/**
 * A class to launch the program as server.
 * <p>
 * Clase para iniciar el programa como servidor.
 * 
 * @author Raul Murillo and Antonio Valdivia
 *
 */
public class TestServer {
	public static void main(String[] args) {
		String[] as = { "-am", "server", "-g", "ataxx", "-d", "5x5", "-p", "X,O", "-o", "4" };
		Main.main(as);
	}
}
