package es.ucm.fdi.tp.practica6.connectn;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNFactory;

/**
 * A class that extends ConnectNFactory and that adds the new functionality
 * needed to create a swingview.
 * <p>
 * Una clase que extiende ConnectNFactory y que incorpora la nueva funcionalidad
 * necesaria para crear una vista de ventana.
 * 
 * @author Antonio Valdivia y Raul Murillo
 */
public class ConnectNFactoryExt extends ConnectNFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5844143618137029963L;

	/**
	 * Default constructor of the class
	 * <p>
	 * Contructor por defecto
	 */
	public ConnectNFactoryExt() {
		super();
	}

	/**
	 * Constructor with parameters of the class
	 * <p>
	 * Constructor con parametros de la clase
	 * 
	 * @param dim
	 *            The dimension of the board
	 *            <p>
	 *            La dimension del tablero
	 */
	public ConnectNFactoryExt(int dim) {
		super(dim);
	}

	@Override
	public void createSwingView(final Observable<GameObserver> game, final Controller ctrl, final Piece viewPiece,
			Player randPlayer, Player aiPlayer) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new ConnectNSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new GameError(e.getMessage());
		}
	}

}
