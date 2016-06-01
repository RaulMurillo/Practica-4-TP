package es.ucm.fdi.tp.practica6.ataxx;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
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

	protected static final Piece OBSTACLE = new Piece("*");

	private int numObstacles;

	private final int MIN_PLAYERS = 2;

	private final int MAX_PLAYERS = 4;

	//This is an experimental value
	private final double BLANK = 0.09;

	public AtaxxRules(int dim, int obstacles) {
		if (dim < 5 && dim % 2 == 0) {
			throw new GameError("Dimension must be at least 5 and odd: " + dim);
		} else {
			this.dim = dim;
			this.numObstacles = obstacles;
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
		setObstacles(b);
		return b;
	}

	/**
	 * Method that sets the obstacles symmetrically in the board.
	 * <p>
	 * Metodo que fija los obstaculos simetricamente en el tablero.
	 * 
	 * @param board
	 *            The initial board of the game.
	 *            <p>
	 *            El tablero inicial del juego.
	 */
	private void setObstacles(Board board) {
		int obs = numObstacles / 4;
		int c = dim / 2;
		for (int i = 0; i < obs; i++) {
			int rnd = Utils.randomInt(c * c);
			while (board.getPosition(rnd / c, rnd % c) != null) {
				rnd = Utils.randomInt(c * c);
			}
			// Set obstacles in board using axial symmetry with the middle
			// column and row as axes.
			board.setPosition(rnd / c, rnd % c, OBSTACLE);
			board.setPosition(dim - 1 - rnd / c, rnd % c, OBSTACLE);
			board.setPosition(rnd / c, dim - 1 - rnd % c, OBSTACLE);
			board.setPosition(dim - 1 - rnd / c, dim - 1 - rnd % c, OBSTACLE);
		}
	}

	@Override
	public Piece initialPlayer(Board board, List<Piece> pieces) {
		return pieces.get(0);
	}

	@Override
	public int minPlayers() {
		return MIN_PLAYERS;
	}

	@Override
	public int maxPlayers() {
		return MAX_PLAYERS;
	}

	@Override
	public Pair<State, Piece> updateState(Board board, List<Piece> pieces, Piece turn) {
		if (board.isFull()) {
			return checkGameOver(board, pieces);
		} else {
			return checkBoard(board, pieces, turn);
		}
	}

	/**
	 * Method that checks the game state.
	 * <p>
	 * Comprueba el estado de juego en un momento dado.
	 * 
	 * @param board
	 *            The current board of the game.
	 *            <p>
	 *            El tablero actual.
	 * @param pieces
	 *            The list of pieces involved in the game (each correspond to a
	 *            player).
	 *            <p>
	 *            Lista de fichas de todos los jugadores (cada una corresponde a
	 *            un jugador).
	 * @param turn
	 *            The piece that has been played last.
	 *            <p>
	 *            Ficha que ha jugado en el ultimo movimiento.
	 * 
	 * @return A pair that contains the new game state and the piece of the
	 *         winner if the game has finished or null in other case.
	 *         <p>
	 *         Un par que contiene el nuevo estado del juego y la ficha del
	 *         ganador si el juego ha acabado, o {@null} en caso contrario.
	 */
	private Pair<State, Piece> checkBoard(Board board, List<Piece> pieces, Piece turn) {
		boolean noMovesLeft = true;
		int playersWithPieces = 0;
		for (Piece p : pieces) {
			noMovesLeft = noMovesLeft && validMoves(board, pieces, p).isEmpty();
			if (board.getPieceCount(p) > 0)
				playersWithPieces++;
		}
		if (noMovesLeft || playersWithPieces < 2)
			return checkGameOver(board, pieces);
		else
			return gameInPlayResult;
	}

	/**
	 * It checks the game state ones it is not possible to continue playing.
	 * <p>
	 * Comprueba el estado del juego una vez no se pueda continuar.
	 * 
	 * @param board
	 *            The current board of the game.
	 *            <p>
	 *            El tablero actual.
	 * @param pieces
	 *            The list of pieces involved in the game (each correspond to a
	 *            player).
	 *            <p>
	 *            Lista de fichas de todos los jugadores (cada una corresponde a
	 *            un jugador).
	 * @return A pair that contains the new game state and the piece of the
	 *         winner if the game has finished or null in other case.
	 * 
	 *         Un par que contiene el nuevo estado del juego y la ficha del
	 *         ganador si el juego ha acabado, o {@null} en caso de empate.
	 */
	private Pair<State, Piece> checkGameOver(Board board, List<Piece> pieces) {
		int high1 = 0, high2 = 0;
		Piece winner = null;
		for (Piece p : pieces) {
			if (board.getPieceCount(p) > high1) {
				high1 = board.getPieceCount(p);
				winner = p;
			}
			// Evaluates draw situation
			else if (board.getPieceCount(p) == high1) {
				high2 = high1;
			}
		}
		if (high1 == high2)
			return new Pair<State, Piece>(State.Draw, null);
		else {
			return new Pair<State, Piece>(State.Won, winner);
		}
	}

	@Override
	public Piece nextPlayer(Board board, List<Piece> pieces, Piece turn) {
		int i = pieces.indexOf(turn);
		int j = 1;
		Piece next = null;
		boolean hasNext = false;
		while (j <= pieces.size() && !hasNext) {
			next = pieces.get((i + j) % pieces.size());
			if (!validMoves(board, pieces, next).isEmpty()) {
				hasNext = true;
			}
			j++;
		}
		return next;
	}
	//Practica 6
	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		if (board.getPieceCount(p) == 0) {
			return -1;
		} else {
			int losers = 0;
			for (Piece it : pieces) {
				losers = (it != p && board.getPieceCount(it) == 0) ? losers + 1 : losers;
			}
			if (losers == pieces.size() - 1) {
				return 1;
			} else {
				double s = 0;
				for (int row = 0; row < board.getRows(); row++) {
					for (int col = 0; col < board.getCols(); col++) {
						Piece c = board.getPosition(row, col);

						int sign = (c == null) ? 0 : (c.equals(p)) ? 1 : (c.equals(OBSTACLE)) ? 0 : -1;

						s += sign;
						// blank neighbors are bad for us,
						// good if neighboring enemies
						if (sign != 0) {
							int startRow = Math.max(0, row - 1);
							int startCol = Math.max(0, col - 1);
							int endRow = Math.min(board.getRows(), row + 2);
							int endCol = Math.min(board.getCols(), col + 2);
							for (int i = startRow; i < endRow; i++) {
								for (int j = startCol; j < endCol; j++) {
									if (board.getPosition(i, j) == null) {
										s -= BLANK * sign;
									}
								}
							}
						}

					}
				}

				return s / (board.getCols() * board.getRows() - numObstacles);
			}
		}

	}

	@Override
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces, Piece turn) {
		List<GameMove> moves = new ArrayList<GameMove>();
		// Generar movimientos validos;
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				if (turn.equals(board.getPosition(i, j))) {
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
		// We explore the surroundings of the piece (p) and make the
		// pertinent changes if necessary in the surrounding pieces
		int startRow = Math.max(0, row - 2);
		int startCol = Math.max(0, col - 2);
		int endRow = Math.min(board.getRows(), row + 3);
		int endCol = Math.min(board.getCols(), col + 3);
		for (int i = startRow; i < endRow; i++) {
			for (int j = startCol; j < endCol; j++) {
				if (board.getPosition(i, j) == null) {
					moves.add(new AtaxxMove(row, col, i, j, turn));
				}
			}
		}
		return moves;
	}
}
