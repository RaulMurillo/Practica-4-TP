package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.UtilsPr4;

/**
 * A Class representing a move for Ataxx.
 * <p>
 * Clase para representar un movimiento del juego Ataxx.
 * 
 */
public class AtaxxMove extends GameMove {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Row in which initially is the piece before movement.
	 * <p>
	 * Fila en la que se encuentra la pieza inicialmente antes del movimiento.
	 */
	protected int iniRow;

	/**
	 * Column in which initially is the piece before movement.
	 * <p>
	 * Columna en la que se encuentra la pieza inicialmente antes del
	 * movimiento.
	 */
	protected int iniCol;

	/**
	 * The row where to place the piece return by {@link GameMove#getPiece()}.
	 * <p>
	 * Fila en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int destRow;

	/**
	 * The column where to place the piece return by {@link GameMove#getPiece()}
	 * .
	 * <p>
	 * Columna en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int destCol;

	/**
	 * This constructor should be used ONLY to get an instance of
	 * {@link AtaxxMove} to generate game moves from strings by calling
	 * {@link #fromString(String)}.
	 * 
	 * <p>
	 * Solo se debe usar este constructor para obtener objetos de
	 * {@link AtaxxMove} para generar movimientos a partir de strings usando el
	 * metodo {@link #fromString(String)}.
	 * 
	 */

	public AtaxxMove() {
	}

	/**
	 * Constructs a move for placing a piece of the type referenced by {@code p}
	 * at position ({@code row},{@code col}).
	 * 
	 * <p>
	 * Construye un movimiento para colocar una ficha del tipo referenciado por
	 * {@code p} en la posicion ({@code row},{@code col}).
	 * 
	 * @param iniRow Number of row where the piece actually is.
	 *            <p>
	 *            Numero de fila donde se encuentra la pieza.
	 * @param iniCol Number of column where the piece actually is.
	 *            <p>
	 *            Numero de columna donde se encuentra la pieza.
	 * @param destRow Number of row where the piece is moving.
	 *            <p>
	 *            Numero de fila donde se la pieza se va a mover.
	 * @param destCol Number of column where the piece is moving.
	 *            <p>
	 *            Numero de columna donde se la pieza se va a mover.
	 * @param p  A piece to be place at ({@code row},{@code col}).
	 *            <p>
	 *            Ficha a colocar en ({@code row},{@code col}).
	 */
	public AtaxxMove(int iniRow, int iniCol, int destRow, int destCol, Piece p) {
		super(p);
		this.destRow = destRow;
		this.destCol = destCol;
		this.iniRow = iniRow;
		this.iniCol = iniCol;
	}

	@Override
	public void execute(Board board, List<Piece> pieces) {
		Piece p = getPiece();
		if (board.getPieceCount(p) <= 0) { // It should not come in.
											// Just in case, this skips turn
											// and shows a message.
			throw new GameError("There are no pieces of type " + p + " available.");
		} else if (board.getPosition(iniRow, iniCol) != p) {
			throw new GameError(
					"Position (" + iniRow + "," + iniCol + ") does not contains a piece of type " + p + ".");
		} else if (board.getPosition(destRow, destCol) != null) {
			throw new GameError("Position (" + destRow + "," + destCol + ") is already occupied");
		} else {
			int distance = UtilsPr4.distancia(iniRow, iniCol, destRow, destCol);
			if (distance > 2) {
				throw new GameError("Position (" + destRow + "," + destCol + ") is not valid for piece in (" + iniRow
						+ "," + iniCol + ").");
			} else {
				// Variable that will count the amount of pieces that the turn
				// piece will increase
				int cont = 1;
				board.setPosition(destRow, destCol, p);
				// The piece is not cloned if destiny if farther than 2
				if (distance == 2) {
					board.setPosition(iniRow, iniCol, null);
					cont--;
				}
				// Different neighbors pieces are changed.
				int a = destRow - 1, b = destCol - 1;
				if (a < 0)
					a++;
				if (b < 0)
					b++;
				// We explore the surroundings of the piece (p) and make the
				// pertinent changes if necessary in the surrounding pieces
				for (int i = a; (i < board.getRows()) && (i <= destRow + 1); i++) {
					for (int j = b; (j < board.getCols()) && (j <= destCol + 1); j++) {
						Piece aux = board.getPosition(i, j);
						if (aux != null && !p.equals(aux) && !AtaxxRules.obstacle.equals(aux)) {
							board.setPieceCount(aux, board.getPieceCount(aux) - 1);
							board.setPosition(i, j, p);
							cont++;
						}
					}
				}
				board.setPieceCount(p, board.getPieceCount(p) + cont);
			}
		}
	}

	@Override
	public GameMove fromString(Piece p, String str) {
		String[] words = str.split(" ");
		if (words.length != 4) {
			return null;
		}
		try {
			int destRow, destCol, iniRow, iniCol;
			iniRow = Integer.parseInt(words[0]);
			iniCol = Integer.parseInt(words[1]);
			destRow = Integer.parseInt(words[2]);
			destCol = Integer.parseInt(words[3]);
			return new AtaxxMove(iniRow, iniCol, destRow, destCol, p);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String help() {
		return "Row and column for origin and for destination, separated by spaces (four numbers).";
	}

	@Override
	public String toString() {
		if (getPiece() == null) {
			return help();
		} else {
			return "Move piece '" + getPiece() + "' from (" + iniRow + "," + iniCol + ")" + " at (" + destRow + ","
					+ destCol + ")";
		}
	}

}
