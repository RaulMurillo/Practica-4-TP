package es.ucm.fdi.tp.practica6.views;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.server.Server;

/**
 * A panel with multiple {@link JPanel} that allows the user to make changes in
 * the game and get information about it.
 * <p>
 * Panel con multiples {@link JPanel} que permite al usuario interactuar con el
 * juego.
 */
public class SettingsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Panel to show information about the game state.
	 * <p>
	 * Panel que muestra informacion sobre el estasdo del juego.
	 */
	private StatusMessages textArea;

	/**
	 * Table that shows information about the game players.
	 * <p>
	 * Tabla que muestra informacion sobre los jugadores.
	 */
	private PlayerInformation table;

	/**
	 * Panel for changing the color of the pieces.
	 * <p>
	 * Panel para cambiar el color de las piezas.
	 */
	private PieceColors colorChooser;

	/**
	 * Panel for changing the players' game mode.
	 * <p>
	 * Panel para cambiar el modo de juego.
	 */
	private PlayerModes playerModes;

	/**
	 * Panel for making automatic moves.
	 * <p>
	 * Panel para realizar movimientos automaticos.
	 */
	private AutomaticMoves autoMovPane;

	/**
	 * Panel for quit/restart the game.
	 * <p>
	 * Panel para abandonar/reiniciar el juego.
	 */
	private QuitPanel quitPanel;

	/**
	 * Board of the game in play.
	 * <p>
	 * Tablero del juego en curso.
	 */
	private Board board;

	/**
	 * List of pieces on the game.
	 * <p>
	 * Lista de piezas del juego.
	 */
	private List<Piece> pieces;

	/**
	 * Creates a panel that allows the user to interact with the game.
	 * <p>
	 * Crea un pane que permite al usuario interactuar con el juego.
	 * 
	 * @param pieces
	 *            List of pieces in the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 * @param colorMap
	 *            Map that indicates the color of the pieces in the board.
	 *            <p>
	 *            Mapa que indica los colores de las piezas del tablero.
	 * @param board
	 *            Internal board of the game.
	 *            <p>
	 *            Tablero interno del juego.
	 * @param listener
	 *            Listener of the class.
	 *            <p>
	 *            "Listener" de la clase.
	 * @param viewPiece
	 *            The piece to which this view belongs ({@code null} means that
	 *            it belongs to all pieces).
	 *            <p>
	 *            La ficha a la que pertenece la vista ({@code null} si la vista
	 *            pertenece a todos los jugadores).
	 */
	public SettingsPanel(List<Piece> pieces, Map<Piece, Color> colorMap, Board board, GenericSwingView listener,
			Piece viewPiece) {
		this.pieces = pieces;
		this.board = board;
		initComponents(colorMap, listener, viewPiece);
	}

	/**
	 * Initialize the components of the panel.
	 * <p>
	 * Inicializa los componentes del panel.
	 * 
	 * @param colorMap
	 *            Map that indicates the color of the pieces in the board.
	 *            <p>
	 *            Mapa que indica los colores de las piezas del tablero.
	 * @param listener
	 *            Listener of the class.
	 *            <p>
	 *            "Listener" de la clase.
	 * @param viewPiece
	 *            The piece to which this view belongs ({@code null} means that
	 *            it belongs to all pieces).
	 *            <p>
	 *            La ficha a la que pertenece la vista ({@code null} si la vista
	 *            pertenece a todos los jugadores).
	 */
	private void initComponents(Map<Piece, Color> colorMap, GenericSwingView listener, Piece viewPiece) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Status Messages text area
		textArea = new StatusMessages();
		add(textArea);

		// Player Information Table
		table = new PlayerInformation(board, pieces, colorMap, viewPiece);
		add(table);

		// Piece Colors Chooser
		colorChooser = new PieceColors(pieces, listener);
		add(colorChooser);

		// Player Mode Selector
		playerModes = new PlayerModes(pieces, listener);
		add(playerModes);

		// Automatic Moves Panel
		autoMovPane = new AutomaticMoves(listener);
		add(autoMovPane);

		// Quit Selection Panel
		quitPanel = new QuitPanel(listener);
		if (viewPiece == null) {
			quitPanel.setRestartButton();
		}
		add(quitPanel);
	}

	/**
	 * Shows a message in the {@link StatusMessages}.
	 * <p>
	 * Muestra un mensaje en el {@link StatusMessages}.
	 * 
	 * @param msg
	 *            Message to show.
	 *            <p>
	 *            Mensaje a mostrar.
	 */
	public void setMessage(String msg) {
		textArea.showMessage(msg);
	}

	/**
	 * Enables or disables specific panels of this component, depending on the
	 * value of the parameters.
	 * <p>
	 * Activa o desactiva paneles especificos de este componente, dependiendo
	 * del valor de los parametros.
	 * 
	 * @param moves
	 *            Indicates if enable the {@link AutomaticMoves} panel.
	 *            <p>
	 *            Indica si activar el panel {@link AutomaticMoves}.
	 * @param quit
	 *            Indicates if enable the {@link QuitPanel} panel.
	 *            <p>
	 *            Indica si activar el panel {@link QuitPanel}.
	 * @param modes
	 *            Indicates if enable the {@link PlayerModes} panel.
	 *            <p>
	 *            Indica si activar el panel {@link PlayerModes}.
	 */
	public void setEnabled(boolean moves, boolean quit, boolean modes) {
		autoMovPane.setEnabled(moves);

		quitPanel.setEnabled(quit);

		playerModes.setEnabled(modes);
	}

	/**
	 * Updates the pieces-colors map for painting rows.
	 * <p>
	 * Actualiza el mapa de piezas-colores para pintar filas.
	 * 
	 * @param colorMap
	 *            Map that indicates the color of the pieces in the board.
	 *            <p>
	 *            Mapa que indica los colores de las piezas del tablero.
	 */
	public void updateTableColor(Map<Piece, Color> colorMap) {
		table.updateColors(colorMap);
	}

	/**
	 * Updates the number of pieces on board of all the players in game.
	 * <p>
	 * Actualiza el numero de piezas en el tablero de todos los jugadores.
	 */
	public void updateTablePieces() {
		table.updateNumPieces(pieces, board);
	}

	/**
	 * Updates a player's game mode in the table.
	 * <p>
	 * Actualiza el modo de juego de un jugador en la tabla.
	 * 
	 * @param piece
	 *            Piece of the player to change mode.
	 *            <p>
	 *            Pieza del jugador a cambiar de modo.
	 * @param mode
	 *            New game mode of the player.
	 *            <p>
	 *            Nuevo modo del jugador.
	 */
	public void updateTableMode(Piece piece, String mode) {
		table.updateMode(pieces.indexOf(piece), mode);
	}

	/**
	 * Enable the correspondent {@link AutomaticMoves} buttons if RANDOM and
	 * INTELLIGENT modes are available. If none is available, correspondent
	 * panel turns invisible.
	 * <p>
	 * Habilita los botones de {@link AutomaticMoves} correspondientes con los
	 * modos de juego RANDOM e INTELLIGENT si estan disponibles. Si ninguno de
	 * ellos lo esta el panel no se mostrara.
	 * 
	 * @param rand
	 *            Indicates if RANDOM mode is available.
	 *            <p>
	 *            Indica si el RANDOM modo esta disponible.
	 * @param ai
	 *            Indicates if INTELLIGENT mode is available.
	 *            <p>
	 *            Indica si el modo INTELLIGENT esta disponible.
	 */
	public void configAutoMoves(boolean rand, boolean ai, Piece viewPiece) {
		if (!rand && !ai) {
			autoMovPane.setVisible(false);
		} else {
			if (rand) {
				autoMovPane.addRandomButton();
			}
			if (ai) {
				autoMovPane.addIntelligentButton();
			}
			if (viewPiece == null || !viewPiece.equals(Server.observerPiece)) {
				autoMovPane.addTimelimit();
			}
		}
	}

	/**
	 * Sets the board of the game.
	 * <p>
	 * Establece el tablero interno del juego.
	 * 
	 * @param board
	 *            Internal board of the game.
	 *            <p>
	 *            Tablero interno del juego.
	 */
	public void setBoard(Board board) {
		this.board = board;
		updateTablePieces();
	}

	/**
	 * Enable the correspondent {@link PlayerModes} selections if RANDOM and
	 * INTELLIGENT modes are available. If none is available, correspondent
	 * panel turns invisible.
	 * <p>
	 * Habilita las selecciones de {@link PlayerModes} correspondientes con los
	 * modos de juego RANDOM e INTELLIGENT si estan disponibles. Si ninguno de
	 * ellos lo esta el panel no se mostrara.
	 * 
	 * @param rand
	 *            Indicates if RANDOM mode is available.
	 *            <p>
	 *            Indica si el RANDOM modo esta disponible.
	 * @param ai
	 *            Indicates if INTELLIGENT mode is available.
	 *            <p>
	 *            Indica si el modo INTELLIGENT esta disponible.
	 */
	public void configPlayerModes(boolean rand, boolean ai) {
		if (!rand && !ai) {
			playerModes.setVisible(false);
		} else {
			playerModes.addModes(rand, ai);
		}
	}
}
