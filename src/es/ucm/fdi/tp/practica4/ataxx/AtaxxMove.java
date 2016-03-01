package es.ucm.fdi.tp.practica4.ataxx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.UtilsPr4;

public class AtaxxMove extends GameMove {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Fila en la que se encuentra la pieza inicialmente antes del movimiento.
	 */
	protected int iniRow;

	/**
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
	 * @param row
	 *            Number of row.
	 *            <p>
	 *            Numero de fila.
	 * @param destCol
	 *            Number of column.
	 *            <p>
	 *            Numero de columna.
	 * @param p
	 *            A piece to be place at ({@code row},{@code col}).
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
		// REVISAR
		if (board.getPieceCount(p) <= 0) { // Se debe saltar el turno.
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
				int cont = 1;
				board.setPosition(destRow, destCol, p);
				// Se elimina la pieza de origen si la distancia del movimiento
				// es dos
				if (distance == 2) {
					board.setPosition(iniRow, iniCol, null);
					cont--; // La pieza no se duplica
				}
				// Se cambian las piezas circundantes de color
				int a = destRow - 1, b = destCol - 1;
				Map<Piece, Integer> counter = new HashMap<Piece, Integer>(pieces.size(), 0);
				counter.put(p, cont);
				cont = 0;
				if (a < 0)
					a++;
				if (b < 0)
					b++;
				for (int i = a; (i < board.getRows()) || (i < destRow + 2); i++) {
					for (int j = b; (j < board.getCols()) || (j < destCol + 2); j++) {
						if (board.getPosition(i, j) != null && board.getPosition(i, j) != p) {
							// OJO con los obstaculos
							// NO se tienen en cuenta en este algoritmo.
							cont = counter.get(board.getPosition(i, j));
							counter.put(board.getPosition(i, j), cont - 1);
							board.setPosition(i, j, p);
							cont++;
						}
					}
				}
				// REVISAR REASIGNACION DEL CONTADOR DE PIEZAS
				for (int i = 0; i < pieces.size(); i++) {
					board.setPieceCount(pieces.get(i), board.getPieceCount(pieces.get(i)) + counter.get(pieces.get(i)));
				}
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

}
