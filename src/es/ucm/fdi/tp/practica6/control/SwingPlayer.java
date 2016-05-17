package es.ucm.fdi.tp.practica6.control;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.views.GenericSwingView;

/**
 * A class that implements a player that inputs its moves by swing.
 * 
 * <p>
 * Clase que implementa un jugador que introduce sus jugadas por swing.
 */
public class SwingPlayer extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Swing view associated to a manual swing player.
	 * <p>
	 * Vista de ventana asociada a un jugador manual de swing.
	 */
	private GenericSwingView view;

	/**
	 * Constructor of the class.
	 * <p>
	 * Constructor de la clase.
	 * 
	 * @param view
	 *            View asociated to the player
	 *            <p>
	 *            Vista asociada al jugador
	 */
	public SwingPlayer(GenericSwingView view) {
		this.view = view;
	}

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		GameMove newMove = view.getMove();
		if (newMove != null) {
			return newMove;
		}
		// This point should be unreachable
		throw new GameError("Invalid move");
	}
}
