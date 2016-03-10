package es.ucm.fdi.tp.practica4;


/**
 * 
 * @author Antonio Valdivia y Raul Murillo
 * Class where util methods for the project will be implementated
 * 
 * Clase en la que se implementarán metodos de utilidad para la practica
 */
public class UtilsPr4 {
	
	
	/**
	 * It gives the maximun between both the horizontal and vertical distance
	 * between two points given.
	 * 
	 * Proporciona el maximo entre la distancia lateral y vertical
	 * entre dos puntos dados por sus coordenadas
	 * @param row1 fila del punto 1
	 * @param col1 columna del punto 1
	 * @param row2 fila del punto 2
	 * @param col2 columna del punto 2
	 * @return the distance between point one and two
	 */
	public static int distancia(int row1, int col1, int row2, int col2){
		return Math.max(Math.abs(row1-row2), Math.abs(col1-col2));
	}
}
