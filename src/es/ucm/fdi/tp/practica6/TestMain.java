package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.Main;

/**
 * A class to launch the program in normal mode.
 * <p>
 * Clase para iniciar el programa en modo normal.
 * 
 * @author Raul Murillo and Antonio Valdivia
 *
 */
public class TestMain {
	public static void main(String[] args) {
		String[] as = { "-am", "normal", "-g", "ataxx", "-d", "7x7", "-p", "X,O", "-o", "0", "-aialg", "minmaxab" };
		Main.main(as);
	}
}
