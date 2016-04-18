package es.ucm.fdi.tp.practica5;

import java.awt.Color;

/**
 * Class where utilities methods for the project will be implemented.
 * <p>
 * Clase en la que se implementaran metodos de utilidad para la practica.
 * 
 * @author Antonio Valdivia y Raul Murillo
 */
public class Utils {

	/**
	 * Generates the opposite color by converting the RGB values into YIQ
	 * values.
	 * 
	 * @param color
	 *            Color to generate opposite.
	 * @return Opposite color in RGB.
	 */
	public static Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}
}
