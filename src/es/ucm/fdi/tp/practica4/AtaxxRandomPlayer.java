package es.ucm.fdi.tp.practica4;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A random player for Ataxx.
 * 
 * <p>
 * Un jugador aleatorio para Ataxx.
 *
 */
public class AtaxxRandomPlayer extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7249760460504882300L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		if (board.isFull()) {
			throw new GameError("The board is full, cannot make a random move!!");
		}
		// We generate a list of available moves and choose one randomly
		List<GameMove> availableMoves = rules.validMoves(board, pieces, p);
		int rnd = Utils.randomInt(availableMoves.size());
		return availableMoves.get(rnd);
	}

}
