package es.ucm.fdi.tp.practica6.attt;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A class that extends AdvancedTTTFactory and that adds the new functionality
 * needed to create a swingview.
 * <p>
 * Una clase que extiende AdvancedTTTFactory y que a�ade la nueva funcionalidad
 * necesaria para crear una vista de ventana.
 *
 * @author Antonio Valdivia y Raul Murillo
 */
public class AdvancedTTTFactoryExt extends AdvancedTTTFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the class
	 * <p>
	 * Constructor de la clase
	 */
	public AdvancedTTTFactoryExt() {
		super();
	}

	@Override
	public void createSwingView(final Observable<GameObserver> game, final Controller ctrl, final Piece viewPiece,
			Player randPlayer, Player aiPlayer) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new AdvancedTTTSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new GameError(e.getMessage());
		}
	}
}
