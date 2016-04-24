package es.ucm.fdi.tp.practica5.ttt;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.connectn.ConnectNFactoryExt;

/**
 * A class that extends Tic-Tac-Toe and that adds the new functionality needed
 * to create a swingview.
 * <p>
 * Una clase que extiende Tic-Tac-Toe y que a�ade la nueva funcionalidad
 * necesaria para crear una vista de ventana.
 *
 * @author Antonio Valdivia y Raul Murillo
 */
public class TicTacToeFactoryExt extends ConnectNFactoryExt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			Player randPlayer, Player aiPlayer) {
		new TicTacToeSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
	}
}
