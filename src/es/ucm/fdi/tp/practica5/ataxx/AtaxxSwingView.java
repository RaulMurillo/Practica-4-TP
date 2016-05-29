package es.ucm.fdi.tp.practica5.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.AtaxxMove;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

/**
 * 
 * This class that extends GenericSwingView, adds to the abstract class the new
 * functionality that allow us to play ataxx in swing.
 * <p>
 * Esta clase que extiende GenericSwingView añade a la clase abstracta la nueva
 * funcionalidad que permite jugar a ataxx en swing.
 * <p>
 * 
 * @author Antonio Valdivia y Raul Murillo
 *
 */
public class AtaxxSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4094760789226107211L;
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
	 * Construct a swing view for playing ataxx{@code game}, with a
	 * {@code piece} associated to the view.
	 * <p>
	 * Construye una vista para jugar a ataxx {@code game} con una pieza
	 * asociada a la vista.
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
	public AtaxxSwingView(Observable<GameObserver> g, Controller controller, Piece viewPiece, Player random,
			Player ai) {
		super(g, controller, viewPiece, random, ai);
		resetMove();
	}

	@Override
	public void leftButtonPressed(int row, int col) {
		// If it is your turn
		if (lastTurn != null && (viewPiece == null || viewPiece.equals(lastTurn))) {
			// We have not selected a piece to move yet
			if (iniCol == -1) {
				if (lastTurn.equals(lastBoard.getPosition(row, col))) {
					settings.setEnabled(false, true, true);
					iniCol = col;
					iniRow = row;
					boardUI.selectSquare(row, col);
					showHelp();
				}
			}
			// We have a piece selected
			else {
				boardUI.deselectSquare(iniRow, iniCol);
				if (lastTurn.equals(lastBoard.getPosition(row, col))) {
					resetMove();
					iniCol = col;
					iniRow = row;
					boardUI.selectSquare(row, col);
				} else {
					if (lastBoard.getPosition(row, col) == null && AtaxxMove.distance(row, col, iniRow, iniCol) <= 2) {
						move = new AtaxxMove(iniRow, iniCol, row, col, lastTurn);
						controller.makeMove(players.get(lastTurn));
						enablePanels();
					} else {
						settings.setMessage("Invalid move");
						resetMove();
					}
				}
			}
		}
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		if (row == iniRow && col == iniCol) {
			resetMove();
			enablePanels();
			boardUI.deselectSquare(row, col);
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
	protected void showHelp() {
		if (iniCol == -1) {
			settings.setMessage("Click on an origin piece");
		} else {
			settings.setMessage("Click on the destination position");
		}
	}

	@Override
	protected void showStartingHelp() {
		settings.setMessage("Click on an origin piece");
	}

}
