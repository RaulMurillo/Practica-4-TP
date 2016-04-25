package es.ucm.fdi.tp.practica5.attt;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

/**
 * 
 * This class that extends GenericSwingView, adds to the abstract class the new
 * functionality that allow us to play Advanced Tic-Tac-Toe in swing.
 * <p>
 * Esta clase que extiende GenericSwingView a�ade a la clase abstracta la nueva
 * funcionalidad que permite jugar a Advanced Tic-Tac-Toe en swing.
 * <p>
 * 
 * @author Antonio Valdivia y Raul Murillo
 *
 */
public class AdvancedTTTSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A boolean that its only true when the game has reached the advanced mode
	 * <p>
	 * Un boolean que se pone a cierto cuando el juego a alcanzado el modo
	 * avanzado
	 */
	private static boolean advancedMode;
	/**
	 * Origin colum
	 * <p>
	 * Columna de origen
	 */
	private int iniCol;
	/**
	 * Origin row
	 * <p>
	 * Fila de origen
	 */
	private int iniRow;
	/**
	 * The counter of moves that controls when the game should change from
	 * simple to advace mode.
	 * <p>
	 * El contador the movimientos que controla cuando el juego debe cambiar de
	 * modo simple a avanzado.
	 */
	private static int turnCount;

	/**
	 * Construct a swing view for playing advanced tic-tac-toe{@code game}, with
	 * a {@code piece} associated to the view.
	 * <p>
	 * Construye una vista para jugar a advanced tic-tac-toe {@code game} con
	 * una pieza asociada a la vista.
	 * 
	 * @param g
	 *            Observer of the view.
	 *            <p>
	 *            Observador de la vista.
	 * @param controller
	 *            Controller of the view.
	 *            <p>
	 *            Controlador de la vista.
	 * @param viewPiece
	 *            The piece to which this view belongs ({@code null} means that
	 *            it belongs to all pieces).
	 *            <p>
	 *            La ficha a la que pertenece la vista ({@code null} si la vista
	 *            pertenece a todos los jugadores).
	 * @param random
	 *            The player to be used for generating random moves, if
	 *            {@code null} the view should not support random player.
	 *            <p>
	 *            El jugador que se va a utilizar para generar movimientos
	 *            aleatorios. Si es {@code null}, la vista no permite jugadores
	 *            aleatorios.
	 * @param ai
	 *            The player to be used for generating automatics moves, if
	 *            {@code null} the view should not support AI (automatic)
	 *            player.
	 *            <p>
	 *            El jugador que se va a utilizar para generar movimientos
	 *            automaticos. Si es {@code null}, la vista no permite jugadores
	 *            IA (automaticos).
	 */
	public AdvancedTTTSwingView(Observable<GameObserver> g, Controller controller, Piece viewPiece, Player random,
			Player ai) {
		super(g, controller, viewPiece, random, ai);
	}

	@Override
	public void leftButtonPressed(int row, int col) {
		// If its your turn
		if (viewPiece == null || viewPiece.equals(lastTurn)) {
			if (!advancedMode) {
				simpleMove(row, col);
			} else {
				complexMove(row, col);
			}
		}
	}

	/**
	 * It is responsible for performing complex movements.
	 * <p>
	 * Se encarga de realizar movimientos complejos.
	 * <p>
	 * 
	 * @param row
	 *            The row selected by the user
	 *            <p>
	 *            La fila seleccionada por el usuario
	 * @param col
	 *            The column selected by the user
	 *            <p>
	 *            La columna seleccionada por el usuario
	 */
	private void complexMove(int row, int col) {
		// There was no piece selected yet
		if (iniCol == -1) {
			if (lastTurn.equals(lastBoard.getPosition(row, col))) {
				settings.setEnabled(false, true, true);
				iniCol = col;
				iniRow = row;
				boardUI.selectSquare(row, col);
				showHelp();
			}
		}
		// There was a selected piece
		else {
			boardUI.deselectSquare(iniRow, iniCol);
			if (lastTurn.equals(lastBoard.getPosition(row, col))) {
				resetMove();
				iniCol = col;
				iniRow = row;
				boardUI.selectSquare(row, col);
			} else {
				if (lastBoard.getPosition(row, col) == null) {
					move = new AdvancedTTTMove(iniRow, iniCol, row, col, lastTurn);
					controller.makeMove(players.get(lastTurn));
					enablePanels();
				} else {
					settings.setMessage("Invalid move");
					resetMove();
				}
			}
		}
	}

	/**
	 * It is responsible for performing simple movements.
	 * <p>
	 * Se encarga de realizar movimientos simples.
	 * <p>
	 * 
	 * @param row
	 *            The row selected by the user
	 *            <p>
	 *            La fila seleccionada por el usuario
	 * @param col
	 *            The column selected by the user
	 *            <p>
	 *            La columna seleccionada por el usuario
	 */
	private void simpleMove(int row, int col) {
		if (lastBoard.getPosition(row, col) == null) {
			move = new AdvancedTTTMove(iniRow, iniCol, row, col, lastTurn);
			controller.makeMove(players.get(lastTurn));
		} else
			settings.setMessage("Invalid move");
		resetMove();
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		if (advancedMode) {
			if (row == iniRow && col == iniCol) {
				resetMove();
				enablePanels();
				boardUI.deselectSquare(row, col);
			}
		}
	}

	@Override
	public void resetMove() {
		super.resetMove();
		if (iniRow != -1 && boardUI != null) {
			boardUI.deselectSquare(iniRow, iniCol);
		}
		iniRow = -1;
		iniCol = -1;
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		super.onMoveEnd(board, turn, success);
		if (success && turn.equals(viewPiece) || viewPiece == null) {
			turnCount++;
		}
		if (turnCount > 5) {
			advancedMode = true;
		}
	}

	@Override
	protected void setStartingActions(Board board, String gameDesc, Piece turn) {
		super.setStartingActions(board, gameDesc, turn);
		turnCount = 0;
		resetMove();
		advancedMode = false;
	}

	@Override
	protected void showHelp() {
		if (advancedMode) {
			if (iniCol == -1) {
				settings.setMessage("Click on an origin piece");
			} else {
				settings.setMessage("Click on the destination position");
			}
		} else {
			settings.setMessage("Click on an empty cell");
		}
	}

	@Override
	protected void showStartingHelp() {
		settings.setMessage("Click on an empty cell");
	}
}
