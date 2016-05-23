package es.ucm.fdi.tp.practica6.ttt;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.connectn.ConnectNFactoryExt;

/**
 * A class that extends Tic-Tac-Toe and that adds the new functionality needed
 * to create a swingview.
 * <p>
 * Una clase que extiende Tic-Tac-Toe y que la nueva funcionalidad necesaria
 * para crear una vista de ventana.
 *
 * @author Antonio Valdivia y Raul Murillo
 */
public class TicTacToeFactoryExt extends ConnectNFactoryExt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9143583576335526013L;

	/**
	 * Constructor of the class
	 * <p>
	 * Constructor de la clase
	 */
	public TicTacToeFactoryExt() {
		super(3);
	}

	@Override
	public void createSwingView(final Observable<GameObserver> game, final Controller ctrl, final Piece viewPiece,
			final Player randPlayer, final Player aiPlayer) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new TicTacToeSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new GameError(e.getMessage());
		}
	}
}
