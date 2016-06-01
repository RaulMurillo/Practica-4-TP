package es.ucm.fdi.tp.practica6;

/**
 * A class to launch the program as a client.
 * <p>
 * Clase para iniciar el programa como un cliente.
 * 
 * @author Raul Murillo and Antonio Valdivia
 *
 */
public class TestClient {
	public static void main(String[] args) {
		String[] as = { "-am", "client", "-aialg", "minmaxab", "-md", "3" };
		Main.main(as);
	}
}
