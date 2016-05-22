package es.ucm.fdi.tp.practica6.connectn;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;
import es.ucm.fdi.tp.practica6.views.GenericSwingView;

/**
 * 
 * This class that extends GenericSwingView, adds to the abstract class the new
 * functionality that allow us to play Connect N in swing.
 * <p>
 * Esta clase que extiende GenericSwingView añade a la clase abstracta la nueva
 * funcionalidad que permite jugar a Connect N en swing.
 * <p>
 * 
 * @author Antonio Valdivia y Raul Murillo
 *
 */
public class ConnectNSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4954287624460137224L;

	/**
	 * Construct a swing view for playing connect N{@code game}, with a
	 * {@code piece} associated to the view.
	 * <p>
	 * Construye una vista para jugar a connect N {@code game} con una pieza
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
	public ConnectNSwingView(Observable<GameObserver> g, Controller controller, Piece viewPiece, Player random,
			Player ai) {
		super(g, controller, viewPiece, random, ai);
	}

	@Override
	public void leftButtonPressed(int row, int col) {
		if (lastBoard.getPosition(row, col) == null) {
			move = new ConnectNMove(row, col, lastTurn);
			controller.makeMove(players.get(lastTurn));
		} else
			settings.setMessage("Invalid move");
		resetMove();
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void showHelp() {
		settings.setMessage("Click on an empty cell");
	}

	@Override
	protected void showStartingHelp() {
		showHelp();
	}

}
