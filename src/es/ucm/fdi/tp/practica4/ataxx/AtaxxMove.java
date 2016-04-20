package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

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
	protected int targetRow;

	/**
	 * The column where to place the piece return by {@link GameMove#getPiece()}
	 * .
	 * <p>
	 * Columna en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int targetCol;

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
	 * @param iniRow
	 *            Number of row where the piece actually is.
	 *            <p>
	 *            Numero de fila donde se encuentra la pieza.
	 * @param iniCol
	 *            Number of column where the piece actually is.
	 *            <p>
	 *            Numero de columna donde se encuentra la pieza.
	 * @param targetRow
	 *            Number of row where the piece is moving.
	 *            <p>
	 *            Numero de fila donde se la pieza se va a mover.
	 * @param targetCol
	 *            Number of column where the piece is moving.
	 *            <p>
	 *            Numero de columna donde se la pieza se va a mover.
	 * @param p
	 *            A piece to be place at ({@code row},{@code col}).
	 *            <p>
	 *            Ficha a colocar en ({@code row},{@code col}).
	 */
	public AtaxxMove(int iniRow, int iniCol, int targetRow, int targetCol, Piece p) {
		super(p);
		this.targetRow = targetRow;
		this.targetCol = targetCol;
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
		} else if (board.getPosition(targetRow, targetCol) != null) {
			throw new GameError("Position (" + targetRow + "," + targetCol + ") is already occupied");
		} else {
			int distance = distance(iniRow, iniCol, targetRow, targetCol);
			if (distance > 2) {
				throw new GameError("Position (" + targetRow + "," + targetCol + ") is not valid for piece in ("
						+ iniRow + "," + iniCol + ").");
			} else {
				// Variable that will count the amount of pieces that the turn
				// piece will increase
				int cont = 1;
				board.setPosition(targetRow, targetCol, p);
				// The piece is not cloned if destiny if farther than 2
				if (distance == 2) {
					board.setPosition(iniRow, iniCol, null);
					cont--;
				}
				// Different neighbors pieces are changed.
				int startRow = Math.max(0, targetRow - 1);
                int startCol = Math.max(0, targetCol - 1);
                int endRow = Math.min(board.getRows(), targetRow+2);
                int endCol = Math.min(board.getCols(), targetCol+2);
				// We explore the surroundings of the piece (p) and make the
				// pertinent changes if necessary in the surrounding pieces
				for (int i = startRow; i < endRow; i++) {
					for (int j = startCol; j < endCol; j++) {
						Piece aux = board.getPosition(i, j);
						if (aux != null && !p.equals(aux) && !AtaxxRules.OBSTACLE.equals(aux)) {
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
			int iniRow = Integer.parseInt(words[0]);
			int iniCol = Integer.parseInt(words[1]);
			int targetRow = Integer.parseInt(words[2]);
			int targetCol = Integer.parseInt(words[3]);
			return new AtaxxMove(iniRow, iniCol, targetRow, targetCol, p);
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
			return "Move piece '" + getPiece() + "' from (" + iniRow + "," + iniCol + ")" + " at (" + targetRow + ","
					+ targetCol + ")";
		}
	}

	/**
	 * It gives the maximum between both the horizontal and vertical distance
	 * between two points given.
	 * <p>
	 * Proporciona el maximo entre la distancia lateral y vertical entre dos
	 * puntos dados por sus coordenadas.
	 * 
	 * @param row1
	 *            Row of point 1.
	 *            <p>
	 *            Fila del punto 1.
	 * @param col1
	 *            Column of point 1.
	 *            <p>
	 *            Columna del punto 1.
	 * @param row2
	 *            Row of point 2.
	 *            <p>
	 *            Fila del punto 2
	 * @param col2
	 *            Column of point 1.
	 *            <p>
	 *            Columna del punto 2.
	 * @return The distance between point one and two.
	 *         <p>
	 *         Distancia entre el punto 1 y el punto 2.
	 */
	private int distance(int row1, int col1, int row2, int col2) {
		return Math.max(Math.abs(row1 - row2), Math.abs(col1 - col2));
	}

}
