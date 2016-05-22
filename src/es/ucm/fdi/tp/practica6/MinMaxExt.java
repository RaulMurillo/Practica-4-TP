package es.ucm.fdi.tp.practica6;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A class copied from MinMax that returns the best move calculated in the given
 * time or calculated in MAX_DEPTH
 * <p>
 * Clase copiada de MinMax que calcula el mejor movimiento en el tiempo dado o
 * en la profundidad maxima.
 * 
 * @author Antonio Valdivia y Raul Murillo (Copied from basecode)
 *
 */
public class MinMaxExt implements AIAlgorithm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1678144742530140520L;
	private GameMove bestMove = null;
	private static final int MAX_DEPTH = 11;
	private int depth;
	private boolean useAblphaBeta;

	public MinMaxExt() {
		this(MAX_DEPTH, true);
	}

	public MinMaxExt(boolean useAblphaBeta) {
		this(MAX_DEPTH, useAblphaBeta);
	}

	public MinMaxExt(int depth) {
		this(depth, true);
	}

	public MinMaxExt(int depth, boolean useAblphaBeta) {
		if (depth < 1) {
			throw new GameError("Invalid depth ('" + depth + "') for the MinMax algorithm, it should be at east 1");
		}
		this.depth = depth;
		this.useAblphaBeta = useAblphaBeta;
	}

	@Override
	public GameMove getMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		try {
			for (int i = 1; i < depth; i += 2) {
				Pair<GameMove, Double> m = minmax(i, -Double.MAX_VALUE, Double.MAX_VALUE, p, p, board, pieces, rules);
				bestMove = m.getFirst();
			}
		} catch (Exception e) {
			return bestMove;
		}
		return bestMove;
	}

	protected Pair<GameMove, Double> minmax(int d, Double alpha, Double beta, Piece p, Piece turn, Board board,
			List<Piece> pieces, GameRules rules) throws InterruptedException {

		if (Thread.interrupted()) {
			throw new InterruptedException();
		}

		Pair<State, Piece> r = rules.updateState(board, pieces, turn);

		switch (r.getFirst()) {
		case Won:
			if (p.equals(r.getSecond())) {
				return new Pair<GameMove, Double>(null, 1.5 * (d + 1));
			} else {
				return new Pair<GameMove, Double>(null, -1.5 * (d + 1));
			}
		case Draw:
			return new Pair<GameMove, Double>(null, 0.0);
		default:
			break;
		}

		if (d < 1) {
			return new Pair<GameMove, Double>(null, rules.evaluate(board, pieces, turn, p));
		}

		List<GameMove> moves = rules.validMoves(board, pieces, turn);
		assert (moves.size() > 0);

		// max is p, min are all other players -- just trying to generalize to
		// more than two players, don't know if it makes sense!
		//
		if (p.equals(turn)) {
			return max(d, alpha, beta, p, turn, board, pieces, rules, moves);
		} else {
			return min(d, alpha, beta, p, turn, board, pieces, rules, moves);
		}
	}

	private Pair<GameMove, Double> max(int d, Double alpha, Double beta, Piece p, Piece turn, Board board,
			List<Piece> pieces, GameRules rules, List<GameMove> moves) throws InterruptedException {

		GameMove selectedMove = null;

		for (GameMove m : moves) {
			Board b = board.copy();
			m.execute(b, pieces);
			Piece nextTurn = rules.nextPlayer(b, pieces, turn);
			Pair<GameMove, Double> res = minmax(d - 1, alpha, beta, p, nextTurn, b, pieces, rules);
			if (res.getSecond() > alpha) {
				alpha = res.getSecond();
				selectedMove = m;
			}
			if (useAblphaBeta && alpha >= beta) {
				return new Pair<GameMove, Double>(selectedMove, alpha);
			}
		}

		return new Pair<GameMove, Double>(selectedMove, alpha);
	}

	private Pair<GameMove, Double> min(int d, Double alpha, Double beta, Piece p, Piece turn, Board board,
			List<Piece> pieces, GameRules rules, List<GameMove> moves) throws InterruptedException {

		GameMove selectedMove = null;

		for (GameMove m : moves) {
			Board b = board.copy();
			m.execute(b, pieces);
			Piece nextTurn = rules.nextPlayer(b, pieces, turn);
			Pair<GameMove, Double> res = minmax(d - 1, alpha, beta, p, nextTurn, b, pieces, rules);
			if (res.getSecond() < beta) {
				beta = res.getSecond();
				selectedMove = m;
			}
			if (useAblphaBeta && beta <= alpha) {
				return new Pair<GameMove, Double>(selectedMove, beta);
			}

		}

		return new Pair<GameMove, Double>(selectedMove, beta);

	}
}
