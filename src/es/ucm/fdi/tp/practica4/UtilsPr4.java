package es.ucm.fdi.tp.practica4;

public class UtilsPr4 {
	
	/**
	 * Proporciona el maximo entre la distancia lateral y vertical
	 * entre dos puntos dados por sus coordenadas
	 * @param row1 fila del punto 1
	 * @param col1 columna del punto 1
	 * @param row2 fila del punto 2
	 * @param col2 columna del punto 2
	 * @return distancia a devolver
	 */
	public static int distancia(int row1, int col1, int row2, int col2){
		return Math.max(Math.abs(row1-row2), Math.abs(col1-col2));
	}
}
