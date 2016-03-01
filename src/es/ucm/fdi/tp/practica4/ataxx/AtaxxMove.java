package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.UtilsPr4;;

public class AtaxxMove extends GameMove {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Fila en la que se encuentra la pieza inicialmente antes del movimiento
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
	protected int row;

	/**
	 * The column where to place the piece return by {@link GameMove#getPiece()}
	 * .
	 * <p>
	 * Columna en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int col;

	/**
	 * This constructor should be used ONLY to get an instance of
	 * {@link AtaxxMove} to generate game moves from strings by calling
	 * {@link #fromString(String)}
	 * 
	 * <p>
	 * Solo se debe usar este constructor para obtener objetos de
	 * {@link AtaxxMove} para generar movimientos a partir de strings usando el
	 * metodo {@link #fromString(String)}
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
	 * @param col
	 *            Number of column.
	 *            <p>
	 *            Numero de columna.
	 * @param p
	 *            A piece to be place at ({@code row},{@code col}).
	 *            <p>
	 *            Ficha a colocar en ({@code row},{@code col}).
	 */
	public AtaxxMove(int iniRow, int iniCol, int row, int col, Piece p) {
		super(p);
		this.row = row;
		this.col = col;
		this.iniRow = iniRow;
		this.iniCol = iniCol;
	}

	@Override
	public void execute(Board board, List<Piece> pieces) {
		Piece p = getPiece();
		// REVISAR
		if (board.getPieceCount(p) <= 0) {
			throw new GameError("There are no pieces of type " + p + " available");
		} else if (board.getPosition(row, col) != null) {
			throw new GameError("Position (" + row + "," + col + ") is already occupied");
		} else {
			int cont = 1;
			board.setPosition(row, col, p);
			//Se elimina la pieza de origen si la distancia del movimiento es dos
			if (UtilsPr4.distancia(iniRow, iniCol, row, col) == 2){
				board.setPosition(iniRow, iniCol, null);
				cont--; //La pieza no se duplica
			}
			// Se cambian las piezas circundantes de color
			int a = row - 1, b = col - 1;
			if (a < 0)
				a++;
			if (b < 0)
				b++;
			for (int i = a; (i < board.getRows()) || (i < row + 2); i++) {
				for (int j = b; (j < board.getCols()) || (j < col + 2); j++) {
					if (board.getPosition(i, j) != null && board.getPosition(i, j) != p) {
						board.setPosition(i, j, p);
						cont++;
					}
				}
			}
			//REVISAR REASIGNACION DEL CONTADOR DE PIEZAS
			board.setPieceCount(p, board.getPieceCount(p) + cont);
		}

	}

	@Override
	public GameMove fromString(Piece p, String str) {
		String[] words = str.split(" ");
		if (words.length != 4) {
			return null;
		}
		try {
			int row, col, iniRow, iniCol;
			iniRow = Integer.parseInt(words[0]);
			iniCol = Integer.parseInt(words[1]);
			row = Integer.parseInt(words[2]);
			col = Integer.parseInt(words[3]);
			return new AtaxxMove(iniRow, iniCol, row, col, p);
		} catch (NumberFormatException e) {
			return null;
		}

	}

	@Override
	public String help() {
		return "'row column', to place a piece at the corresponding position.";
	}

}
