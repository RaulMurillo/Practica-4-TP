package es.ucm.fdi.tp.practica5.control;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.*;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.practica5.views.PlayerModes;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;
import es.ucm.fdi.tp.practica5.views.QuitPanel;
import es.ucm.fdi.tp.practica5.views.AutomaticMoves;

public /* abstract */class SwingController extends Controller {

	/**
	 * A map that associates pieces with players (manual, random, etc.).
	 * 
	 * <p>
	 * Map que asocia fichas con jugadores (manual, random, etc.).
	 */
	protected Map<Piece, Player> players;

	/**
	 * A map that associates pieces with player modes (manual, random, etc.).
	 * 
	 * <p>
	 * Map que asocia fichas con modos de juego (manual, random, etc.).
	 */
	// protected Map<Piece, PlayerMode> modes;

	protected GameMove move;

	// private Map <Piece, GenericSwingView> view;

	public SwingController(Game g, List<Piece> pieces, List<Player> players) {
		super(g, pieces);

		// Generate a HashMap that associates pieces with players.
		this.players = new HashMap<Piece, Player>();
		for (int i = 0; i < pieces.size(); i++) {
			this.players.put(pieces.get(i), players.get(i));
		}

	}

	@Override
	public void start() {
		if (game == null || pieces == null) {
			throw new GameError("There is no game or pieces to start");
		}

		// start the game
		game.start(pieces);
		// Bucle interno del juego
		/*while (game.getState() == State.InPlay) {
			// Notify help
			Command cmd = new PlayCommand();
			//
			if (cmd != null) {
				try {
					cmd.execute(this);
				} catch (GameError e) {
				}
			} else {
				System.err.println("Uknown command");
				System.err.flush();
			}
		}*/

		//
		// update()??

	}

	/**
	 * Begins a move. This may directly realize a {@link}secondPartMove.
	 * 
	 * @param col
	 *            Possible origin column
	 * @param fila
	 * @return
	 */
	// public abstract boolean firstPartMove(int col, int fila);

	/**
	 * Ends a move
	 * 
	 * @param col
	 *            Destination column
	 * @param fila
	 */
	// public abstract void secondPartMove(int col, int fila);

	// public abstract void casillaDeseleccionada(int col, int fila);

	public Player getPiecePlayer(Piece p) {
		return players.get(p);
	}

	/**
	 * We override {@link Controller#makeMove(Player)} to ignore its parameter
	 * and fetch the player from the {@link #players} table.
	 * 
	 * <p>
	 * Se sobrescribe {@link Controller#makeMove(Player)} para ignorar su
	 * parametro y obtener el jugador de la tabla de jugadores.
	 * 
	 * @param p
	 *            Typically {@code null} since it is ignored.
	 *            <p>
	 *            Normalmente es {@code null} pues se ignora.
	 */
	@Override
	public void makeMove(Player player) {
		if (game != null && player != null) {
			game.makeMove(player);
		}
	}

	public GameMove getUserMove() {
		return move;
	}

	// public abstract String help();

}
