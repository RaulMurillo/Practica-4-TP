package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Rules for Ataxx game.
 * <ul>
 * <li>The game is played on an NxN board (with N>=5 and odd).</li>
 * <li>The number of players is between 2 and 4.</li>
 * <li>The player turn in the given order, each moving a piece on an empty cell
 * remote 1 or 2. The winner is the one who owns more pieces when there are no
 * possible moves.</li>
 * </ul>
 * 
 * <p>
 * Reglas del juego Ataxx.
 * <ul>
 * <li>El juego se juega en un tablero NxN (con N>=5, impar).</li>
 * <li>El numero de jugadores esta entre 2 y 4.</li>
 * <li>Los jugadores juegan en el orden proporcionado, cada uno moviendo una
 * ficha en una casilla vacia a distancia 1 o 2. El ganador es el que posea mas
 * fichas cuando no queden movimientos posibles.</li>
 * </ul>
 *
 */
public class AtaxxRules implements GameRules {

	// This object is returned by gameOver to indicate that the game is not
	// over. Just to avoid creating it multiple times, etc.
	//
	protected final Pair<State, Piece> gameInPlayResult = new Pair<State, Piece>(State.InPlay, null);

	private int dim;

	public AtaxxRules(int dim) {
		if (dim < 5 && dim % 2 == 0) {
			throw new GameError("Dimension must be at least 5 and odd: " + dim);
		} else {
			this.dim = dim;
		}
	}

	@Override
	public String gameDesc() {
		return "Ataxx " + dim + "x" + dim;
	}

	@Override
	public Board createBoard(List<Piece> pieces) {
		Board b = new FiniteRectBoard(dim, dim);
		switch (pieces.size()) {
		case 4: {
			b.setPosition(dim / 2, 0, pieces.get(3));
			b.setPosition(dim / 2, dim - 1, pieces.get(3));
			b.setPieceCount(pieces.get(3), 2);
		}
		case 3: {
			b.setPosition(0, dim / 2, pieces.get(2));
			b.setPosition(dim - 1, dim / 2, pieces.get(2));
			b.setPieceCount(pieces.get(2), 2);
		}
		case 2: {
			// Player 1
			b.setPosition(0, 0, pieces.get(0));
			b.setPosition(dim - 1, dim - 1, pieces.get(0));
			b.setPieceCount(pieces.get(0), 2);
			// Player 2
			b.setPosition(0, dim - 1, pieces.get(1));
			b.setPosition(dim - 1, 0, pieces.get(1));
			b.setPieceCount(pieces.get(1), 2);
		}
		}
		return b;
	}

	@Override
	public Piece initialPlayer(Board board, List<Piece> pieces) {
		return pieces.get(0);///////////
	}

	@Override
	public int minPlayers() {
		return 2;
	}

	@Override
	public int maxPlayers() {
		return 4;
	}

	@Override
	public Pair<State, Piece> updateState(Board board, List<Piece> pieces, Piece turn) {
		// TODO Auto-generated method stub
		// Revisar si validMoves se usa correctamente
		if (board.isFull() || validMoves(board, pieces, turn).equals(null)) {
			int winner = 0, high1 = 0, high2 = 0;
			for (int i = 0; i < pieces.size(); i++) { // Do with Iterator?
				if (board.getPieceCount(pieces.get(i)) > high1) {
					high1 = board.getPieceCount(pieces.get(i));
					winner = i;
				}
				// Se evalua si el juego termina en caso de empate
				else if (board.getPieceCount(pieces.get(i)) == high1) {
					high2 = high1;
				}
			}
			if (high1 == high2)
				return new Pair<State, Piece>(State.Draw, null);
			else {
				return new Pair<State, Piece>(State.Won, pieces.get(winner));
			}
		}
		return gameInPlayResult;
	}

	@Override
	public Piece nextPlayer(Board board, List<Piece> pieces, Piece turn) {
		int i = pieces.indexOf(turn);
		return pieces.get((i + 1) % pieces.size());
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces, Piece turn) {
		List<GameMove> moves = new ArrayList<GameMove>();
		// Generar movimientos validos;
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				if (board.getPosition(i, j).equals(turn)) {
					//
					moves.addAll(pieceMoves(board, playersPieces, turn, i, j));
				}
			}
		}

		return moves;
	}

	/**
	 * Generates a list of valid moves for a particular piece, or {@code null}
	 * if this operation is not supported.
	 * 
	 * <p>
	 * Genera una lista de movimientos validos para una determinda pieza, o
	 * {@code null} si no se permite esta operacion.
	 *
	 * @param board
	 *            The current board of the game.
	 *            <p>
	 *            El tablero actual.
	 * @param playersPieces
	 *            The list of pieces involved in the game (each correspond to a
	 *            player).
	 *            <p>
	 *            La lista de fichas de todos los jugadores (cada una
	 *            corresponde a un jugador).
	 * @param turn
	 *            The piece which have to play.
	 *            <p>
	 *            Ficha a la que le toca jugar.
	 * @param row
	 *            Piece's row to evaluate. Fila de la pieza a evaluar.
	 * @param col
	 *            Piece's column to evaluate. Columna de la pieza a evaluar.
	 * @return A list of instances of a subclass of {@link GameMove} that
	 *         represent all valid moves for the piece, or {@code null} if this
	 *         operation is not supported. The actual class depends on the
	 *         actual implementation of {@link GameRules}.
	 * 
	 *         <p>
	 *         Lista de objetos de una subclase de {@link GameMove} que
	 *         representa todos los movimientos validos para la pieza, o
	 *         {@code null} si no se permite esta operacion. La clase concreta
	 *         dependerá de la implementación de {@link GameRules} que
	 *         corresponda.
	 */
	private List<GameMove> pieceMoves(Board board, List<Piece> playersPieces, Piece turn, int row, int col) {
		List<GameMove> moves = new ArrayList<GameMove>();
		int a = row - 2, b = col - 2;
		if (a < 0)
			a = 0;
		if (b < 0)
			b = 0;
		for (int i = a; (i < board.getRows()) && (i <= row + 2); i++) {
			for (int j = b; (j < board.getCols()) && (j <= col + 2); j++) {
				if (board.getPosition(i, j) == null) {
					moves.add(new AtaxxMove(row, col, i, j, turn));
				}
			}
		}
		return moves;
	}
}
