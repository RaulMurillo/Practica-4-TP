package es.ucm.fdi.tp.practica5.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.control.SwingPlayer;

/**
 * Abstract class implementing a game observer that outputs all the events in
 * the game in a graphic window.
 * <p>
 * Clase abstracta que implementa un observador de juegos que muestra todos los
 * eventos del juego en una ventana grafica.
 */
public abstract class GenericSwingView extends JFrame
		implements PieceColors.PieceColorsListener, BoardGUI.BoardGUIListener, AutomaticMoves.AutoMovesListener,
		QuitPanel.QuitPanelListener, PlayerModes.PlayerModesListener, GameObserver {

	private static final Logger log = Logger.getLogger(Controller.class.getSimpleName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -5174399532851561136L;

	/**
	 * The list of pieces involved in the game. It is stored when the game
	 * starts and used when the state is printed.
	 * <p>
	 * La lista de piezas involucradas en el juego. Es almacenada al inicio del
	 * juego y se emplea a lo largo de este.
	 */
	protected List<Piece> pieces;

	/**
	 * Controller of the game. Realizes the internal processes of the game.
	 * <p>
	 * Controlador del juego que realiza los procesos internos.
	 */
	protected Controller controller;

	/**
	 * Map that indicates the color of the pieces in the board.
	 * <p>
	 * Mapa que indica los colores de las piezas del tablero.
	 */
	private Map<Piece, Color> colorMap;

	/**
	 * A map that associates pieces with players (manual, random, etc.). It is
	 * declared as static to maintain consistency on multi-view games.
	 * <p>
	 * Map que asocia fichas con jugadores (manual, random, etc.). Se declara
	 * estático para mantener la consistencia en juego con multiventana.
	 */
	protected Map<Piece, Player> players = new HashMap<Piece, Player>();

	/**
	 * The piece to which this view belongs ({@code null} means that it belongs
	 * to all pieces).
	 * <p>
	 * La ficha a la que pertenece la vista ({@code null} si la vista pertenece
	 * a todos los jugadores).
	 */
	protected Piece viewPiece;

	/**
	 * The player to be used for generating random moves, if {@code null} the
	 * view should not support random player.
	 * <p>
	 * El jugador que se va a utilizar para generar movimientos aleatorios. Si
	 * es {@code null}, la vista no permite jugadores aleatorios.
	 */
	private Player randomPlayer;

	/**
	 * The player to be used for generating automatics moves, if {@code null}
	 * the view should not support AI (automatic) player.
	 * <p>
	 * El jugador que se va a utilizar para generar movimientos automaticos. Si
	 * es {@code null}, la vista no permite jugadores IA (automaticos).
	 */
	private Player aiPlayer;

	/**
	 * Graphic board of the game.
	 * <p>
	 * Representacion grafica del tablero de juego.
	 */
	protected BoardGUI boardUI;

	/**
	 * A panel to be used for user's interactions with the game.
	 * <p>
	 * Panel que el usuario utilizara para interactuar con el juego.
	 */
	protected SettingsPanel settings;

	/**
	 * The piece is playing next. Might be null if the game has not started yet.
	 * <p>
	 * Ficha que juega a continuacion. Puede ser null si el juego todavia no ha
	 * comenzado.
	 */
	protected Piece lastTurn;

	/**
	 * Board of the game before the current turn's move is done.
	 * <p>
	 * Tablero previo a la realización del movimiento correspondiente al turno
	 * actual.
	 */
	protected Board lastBoard;

	/**
	 * Move of the current turn.
	 * <p>
	 * Movimiento a ejecutar en el turno actual.
	 */
	protected GameMove move;

	/**
	 * Default list of colors for painting the pieces.
	 * <p>
	 * Lista de coloreado de piezas por defecto.
	 */
	final private static List<Color> DEFAULT_COLORS = new ArrayList<Color>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(Color.RED);
			add(Color.GREEN);
			add(Color.BLUE);
			add(Color.YELLOW);
		}
	};

	final private int timeout = 2;

	/**
	 * Construct a generic view for playing {@code game}, with a {@code piece}
	 * associated to the view.
	 * <p>
	 * Construye una vista generica para jugar al juego {@code game} con una
	 * pieza asociada a la vista.
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
	 * @param randomPlayer
	 *            The player to be used for generating random moves, if
	 *            {@code null} the view should not support random player.
	 *            <p>
	 *            El jugador que se va a utilizar para generar movimientos
	 *            aleatorios. Si es {@code null}, la vista no permite jugadores
	 *            aleatorios.
	 * @param aiPlayer
	 *            The player to be used for generating automatics moves, if
	 *            {@code null} the view should not support AI (automatic)
	 *            player.
	 *            <p>
	 *            El jugador que se va a utilizar para generar movimientos
	 *            automaticos. Si es {@code null}, la vista no permite jugadores
	 *            IA (automaticos).
	 */
	public GenericSwingView(Observable<GameObserver> g, Controller controller, Piece viewPiece, Player randomPlayer,
			Player aiPlayer) {
		this.controller = controller;
		this.viewPiece = viewPiece;
		this.randomPlayer = randomPlayer;
		this.aiPlayer = aiPlayer;
		this.move = null;
		g.addObserver(this);
	}

	/**
	 * Sets the state of the game according to the beginning of a turn.
	 * <p>
	 * Fija el estado del juego correspondiente al inicio de un turno.
	 */
	private void setLastState(Board board, Piece turn) {
		this.lastBoard = board;
		this.lastTurn = turn;
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		initialize(pieces);
		// Creates a new window
		if (!this.isVisible())
			initWindow(board, gameDesc);
		else {
			boardUI.setBoard(board);
			settings.setBoard(board);
		}
		enablePanels();
		// Set GameStart comments
		setStartingActions(board, gameDesc, turn);
		boardUI.update();
		if (viewPiece != null && !turn.equals(viewPiece))
			toBack();
	}

	/**
	 * Initialize the pieces list and the {@code pieces-players} and
	 * {@code pieces-colors} maps of the view.
	 * <p>
	 * Inicializa la lista de fichas y los mapas de {@code pieces-players} y
	 * {@code pieces-colors} de la vista.
	 * 
	 * @param pieces
	 *            List of pieces on the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 */
	private void initialize(List<Piece> pieces) {
		this.pieces = pieces;
		// Generate a HashMap that associates pieces with players.
		for (Piece p : pieces) {
			if (viewPiece == null || viewPiece.equals(p))
				setManualPlayer(p);
		}
		colorMap = new HashMap<Piece, Color>();
		setColorMap(DEFAULT_COLORS);
	}

	/**
	 * Initialize the graphic components of the view.
	 * <p>
	 * Inicializa los componentes graficos de la vista.
	 * 
	 * @param board
	 *            Board of the game.
	 * @param gameDesc
	 *            Short description of the game.
	 */
	private void initWindow(Board board, String gameDesc) {
		setSize(680, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		String view = "";
		if (viewPiece != null)
			view = "(" + viewPiece + ")";
		this.setTitle("Board Games: " + gameDesc + " " + view);

		JPanel jpBoard = new JPanel(new GridBagLayout());
		boardUI = new BoardGUI(board, colorMap, this);
		jpBoard.add(boardUI);
		add(jpBoard, BorderLayout.CENTER);

		settings = new SettingsPanel(pieces, colorMap, board, this, viewPiece);
		settings.configAutoMoves(randomPlayer != null, aiPlayer != null);
		settings.configPlayerModes(randomPlayer != null, aiPlayer != null);
		add(settings, BorderLayout.EAST);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizePreview(boardUI, jpBoard);
			}
		});
		revalidate();
		setVisible(true);
	}

	/**
	 * Make necessary changes when game starts.
	 * <p>
	 * Realiza los cambios necesarios al iniciar el juego.
	 * 
	 * @param board
	 *            Board of the game.
	 *            <p>
	 *            Tablero interno del juego.
	 * @param gameDesc
	 *            Brief description of the game.
	 *            <p>
	 *            Breve descripcion del juego.
	 * @param turn
	 *            First turn piece.
	 *            <p>
	 *            Ficha del primer turno de juego.
	 */
	protected void setStartingActions(Board board, String gameDesc, Piece turn) {
		settings.setMessage("Starting '" + gameDesc + "'");
		settings.setMessage("----------------------");
		String pieceTurn = turn.toString();
		if (turn.equals(viewPiece)) {
			pieceTurn += " (You)";
		}
		settings.setMessage("Turn for " + pieceTurn);
		if (turn.equals(viewPiece) || viewPiece == null) {
			showStartingHelp();
		}
		setLastState(board, turn);
		for (Piece p : pieces) {
			if (viewPiece == null || viewPiece.equals(p))
				settings.updateTableMode(p, "Manual");
		}
		if (viewPiece != null && !viewPiece.equals(turn)) {
			disablePanels();
		}
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		boardUI.setBoard(board);
		settings.setBoard(board);
		settings.setMessage("\n Game Over!!");
		settings.setMessage("Game Status: " + state);
		if (state == State.Won) {
			settings.setMessage("Winner: " + winner);
		}
		settings.setEnabled(false, true, false);
		lastTurn = null;
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		disablePanels();
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		// enablePanels();
		boardUI.update();
		settings.updateTablePieces();
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		boardUI.setBoard(board);
		settings.setBoard(board);
		resetMove();
		String pieceTurn = turn.toString();
		if (turn.equals(viewPiece))
			pieceTurn += " (You)";
		settings.setMessage("Turn for " + pieceTurn);
		setLastState(board, turn);
		// Bring window to the front
		if (lastTurn.equals(viewPiece)
				&& !(players.get(lastTurn).equals(randomPlayer) || players.get(lastTurn).equals(aiPlayer))) {
			toFront();
		}
		if ((viewPiece == null || lastTurn.equals(viewPiece))
				&& !(players.get(lastTurn).equals(randomPlayer) || players.get(lastTurn).equals(aiPlayer))) {
			enablePanels();
			showHelp();
		} else
			disablePanels();
		if (players.containsKey(turn)
				&& (players.get(turn).equals(randomPlayer) || players.get(turn).equals(aiPlayer))) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (players.get(turn).equals(randomPlayer)) {
						randomPressed();
					} else if (players.get(turn).equals(aiPlayer)) {
						aiPressed();
					}
				}
			});
		}
	}

	@Override
	public void onError(String msg) {
		if (viewPiece == null || viewPiece.equals(lastTurn)) {
			settings.setMessage(msg);
			resetMove();
			boardUI.update();
		}
	}

	/**
	 * Show a message to {@code MANUAL} players about how to make a move.
	 * <p>
	 * Muestra información a los jugadores de modo {@code MANUAL} indicando como
	 * realizar un movimiento.
	 */
	protected abstract void showHelp();

	/**
	 * Show a message to at the beginning of the game about how to make a move.
	 * <p>
	 * Muestra información al cominezo de la partida indicando como realizar un
	 * movimiento.
	 */
	protected abstract void showStartingHelp();

	/**
	 * Set the {@code colorMap} of the view.
	 * <p>
	 * Establece el mapa {@code colorMap} de la vista.
	 * 
	 * @param colors
	 *            List that indicates the color of the pieces, in the same
	 *            order.
	 *            <p>
	 *            Lista que indica los colores de las piezas, con igual orden.
	 */
	public void setColorMap(List<Color> colors) {
		for (int i = 0; i < pieces.size(); i++) {
			this.colorMap.put(pieces.get(i), colors.get(i));
		}
	}

	/**
	 * Enable all the sub-panels that can be disabled.
	 * <p>
	 * Habilita todos los subpaneles que puedan ser deshabilitados.
	 */
	protected void enablePanels() {
		settings.setEnabled(true, true, true);
	}

	/**
	 * Disable all the sub-panels except the {@link QuitPanel}.
	 * <p>
	 * Deshabilita todos los subpaneles que puedan a escepcion del
	 * {@link QuitPanel}.
	 */
	private void disablePanels() {
		settings.setEnabled(false, false, true);
	}

	@Override
	public void changeColorPressed(Piece p, Color c) {
		if (c != null) {
			colorMap.put(p, c);
			boardUI.setMap(colorMap);
			boardUI.update();
			settings.updateTableColor(colorMap);
		}
	}

	@Override
	public abstract void leftButtonPressed(int row, int col);

	@Override
	public abstract void rightButtonPressed(int row, int col);

	@Override
	public void randomPressed() {
		disablePanels();
		resetMove();
		final SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				controller.makeMove(randomPlayer);
				return null;
			}
		};
		worker.execute();
	}

	@Override
	public void aiPressed() {
		disablePanels();
		resetMove();
		final SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				log.log(Level.INFO, "Hilo ejecutado");
				controller.makeMove(aiPlayer);
				log.log(Level.INFO, "Hilo terminado");
				return null;
			}
		};
		worker.execute();
		try {
			worker.get(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			worker.cancel(true);
			/////////////////
			log.log(Level.INFO, "Hilo cancelado");
			changeModePressed(lastTurn, "Manual");
			boardUI.update();
			settings.updateUI();
		}
	}

	@Override
	public void changeModePressed(Piece p, String mode) {
		if (viewPiece == null || viewPiece.equals(p)) {
			switch (mode) {
			case "Manual":
				setManualPlayer(p);
				enablePanels();
				break;
			case "Intelligent":
				players.put(p, aiPlayer);
				if (lastTurn.equals(viewPiece) || (viewPiece == null && lastTurn.equals(p))) {
					disablePanels();
					aiPressed();
				}
				break;
			case "Random":
				players.put(p, randomPlayer);
				if (lastTurn.equals(viewPiece) || (viewPiece == null && lastTurn.equals(p))) {
					disablePanels();
					randomPressed();
				}
				break;
			default:
				throw new UnsupportedOperationException(
						"Something went wrong! This program point should be unreachable!");
			}
			settings.updateTableMode(p, mode);
		}
	}

	/**
	 * Set the mode of a {@code piece} as {@code MANUAL}.
	 * <p>
	 * Establece el modo de una {@code piece} como {@code MANUAL}.
	 * 
	 * @param piece
	 *            Piece to set {@code MANUAL} game mode.
	 *            <p>
	 *            Piece a establecer en modo {@code MANUAL}.
	 */
	public void setManualPlayer(Piece piece) {
		players.put(piece, new SwingPlayer(this));
	}

	@Override
	public void quitPressed() {
		int response = JOptionPane.showConfirmDialog(null,
				"Are sure you want to quit the game?\n (You will lose the game)", "Quit confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			controller.stop();
			System.exit(0);
		}
	}

	@Override
	public void restartPressed() {
		int response = JOptionPane.showConfirmDialog(null,
				"Are sure you want to restart the game?\n (You will lose the game)", "Restart confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			controller.restart();
			boardUI.update();
		}
	}

	/**
	 * Resizes a panel to a square form.
	 * <p>
	 * Cambia el tamaño de un panel a forma cuadrada
	 * 
	 * @param innerPanel
	 *            Panel to resize.
	 *            <p>
	 *            Panel interior a reescalar.
	 * @param container
	 *            Panel that contains the panel witch will be resized.
	 *            <p>
	 *            Panel contenedor del panel a cambiar a tamaño cuadrado.
	 */
	private static void resizePreview(JPanel innerPanel, JPanel container) {
		int w = container.getWidth();
		int h = container.getHeight();
		int size = Math.min(w, h);
		innerPanel.setPreferredSize(new Dimension(size, size));
		container.revalidate();
	}

	/**
	 * Set the {@code player} of a {@code piece} as indicated.
	 * <p>
	 * Establece el jugador ({@code player}) de una {@code piece} como se
	 * indique.
	 * 
	 * @param piece
	 *            Piece to set {@code player}.
	 *            <p>
	 *            Pieza a establecer el jugador {@code player}.
	 * @param player
	 *            Player to associate to the {@code piece}.
	 *            <p>
	 *            Jugador a asociar con la ficha ({@code piece}).
	 */
	public void setPlayer(Piece piece, Player player) {
		players.put(piece, player);
	}

	/**
	 * Consults the last {@code move} selected by the user.
	 * <p>
	 * Proporciona el ultimo movimiento seleccionado por el usuario.
	 * 
	 * @return Last {@code move} selected.
	 *         <p>
	 *         Ultimo movimiento seleccionado.
	 */
	public GameMove getMove() {
		return move;
	}

	/**
	 * Resets the last {@code move} selected by the user.
	 * <p>
	 * Borra el ultimo movimiento seleccionado por el usuario.
	 */
	public void resetMove() {
		move = null;
	}

	/**
	 * Consults the piece to which this view belongs.
	 * <p>
	 * Proporciona la ficha a la que pertenece la vista.
	 * 
	 * @return The piece to which this view belongs.
	 *         <p>
	 *         La ficha a la que pertenece la vista.
	 */
	public Piece getViewPiece() {
		return viewPiece;
	}

	/**
	 * 
	 * @return a copy of the current board
	 *         <p>
	 *         devuelve una copia del tablero actual
	 */
	public Board getLastBoard() {
		return lastBoard;
	}
}
