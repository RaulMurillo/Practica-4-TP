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

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public abstract class GenericSwingView extends JFrame
		implements PieceColorChooser.PieceColorsListener, BoardGUI.BoardGUIListener, AutomaticMoves.AutoMovesListener,
		QuitPanel.QuitPanelListener, PlayerModes.PlayerModesListener, GameObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The list of pieces involved in the game. It is stored when the game
	 * starts and used when the state is printed.
	 */
	private List<Piece> pieces;
	protected Controller controller;
	private Map<Piece, Color> colorMap;

	protected static Map<Piece, Player> players = new HashMap<Piece, Player>();

	protected Piece viewPiece;
	private Player randomPlayer;
	private Player aiPlayer;
	protected BoardGUI boardUI;
	protected SettingsPanel settings;

	protected Piece lastTurn;
	protected Board lastBoard;
	protected String move;

	final private static List<Color> DEFAULT_COLORS = new ArrayList<Color>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(Color.red);
			add(Color.green);
			add(Color.blue);
			add(Color.yellow);
		}
	};

	public GenericSwingView(Observable<GameObserver> g, Controller c, Piece p, Player random, Player ai) {
		controller = c;
		viewPiece = p;
		randomPlayer = random;
		aiPlayer = ai;
		move = "";
		g.addObserver(this);
	}

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

		// Set GameStart comments
		setStartingActions(board, gameDesc, turn);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				boardUI.update();
			}
		});

	}

	/**
	 * Initialize the pieces list, the pieces-players map and the color map of
	 * the frame.
	 * 
	 * @param pieces
	 *            List of pieces on the game.
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
	 * Initialize the graphic components of the frame.
	 * 
	 * @param board
	 *            Board of the game.
	 * @param gameDesc
	 *            Short description of the game.
	 */
	private void initWindow(Board board, String gameDesc) {
		setSize(650, 500);
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
	 * 
	 * @param board
	 *            Board of the game.
	 * @param gameDesc
	 *            Short description of the game.
	 * @param turn
	 *            First turn piece.
	 */
	private void setStartingActions(Board board, String gameDesc, Piece turn) {
		settings.write("Starting '" + gameDesc + "'");
		settings.write("----------------------");
		String pieceTurn = turn.toString();
		if (turn.equals(viewPiece))
			pieceTurn += " (You)";
		settings.write("Turn for " + pieceTurn);
		setLastState(board, turn);
		if (viewPiece != null && !viewPiece.equals(turn)) {
			disablePanels();
		}
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		settings.write("\n Game Over!!");
		settings.write("Game Status: " + state);
		if (state == State.Won) {
			settings.write("Winner: " + winner);
		}
		settings.setEnabled(false, true, false);
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		disablePanels();
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		enablePanels();
		boardUI.update();
		settings.updateTablePieces();
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		// if(lastTurn.equals(viewPiece))toFront();
		String pieceTurn = turn.toString();
		if (turn.equals(viewPiece))
			pieceTurn += " (You)";
		settings.write("Turn for " + pieceTurn);
		setLastState(board, turn);
		if (viewPiece != null && !viewPiece.equals(turn)) {
			disablePanels();
		} else
			enablePanels();
		if (players.get(turn).equals(randomPlayer) || players.get(turn).equals(aiPlayer)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (players.get(turn).equals(randomPlayer)) {
						controller.makeMove(randomPlayer);
					} else if (players.get(turn).equals(aiPlayer)) {
						controller.makeMove(aiPlayer);
					}
				}
			});
		}
	}

	@Override
	public void onError(String msg) {
		if (viewPiece == null || viewPiece.equals(lastTurn)) {
			settings.write(msg);
			resetMove();
		}
	}

	public void setColorMap(List<Color> colors) {
		for (int i = 0; i < pieces.size(); i++) {
			this.colorMap.put(pieces.get(i), colors.get(i));
		}
	}

	private void enablePanels() {
		settings.setEnabled(true, true, true);
	}

	private void disablePanels() {
		settings.setEnabled(false, false, true);
	}

	@Override
	public void changeColorPressed(Piece p, Color c) {
		colorMap.put(p, c);
		boardUI.setMap(colorMap);
		boardUI.update();
		settings.updateTableColor(colorMap);
	}

	@Override
	public abstract void leftButtonPressed(int row, int col);

	@Override
	public abstract void rightButtonPressed(int row, int col);

	@Override
	public void randomPressed() {
		resetMove();
		controller.makeMove(randomPlayer);
	}

	@Override
	public void aiPressed() {
		resetMove();
		controller.makeMove(aiPlayer);
	}

	@Override
	public void changeModePressed(Piece p, String mode) {
		if (viewPiece == null || viewPiece.equals(p)) {
			switch (mode) {
			case "Manual":
				setManualPlayer(p);
				break;
			case "Intelligent":
				players.put(p, aiPlayer);
				if (lastTurn.equals(viewPiece) || (viewPiece == null && lastTurn.equals(p))) {
					//////
					resetMove();
					controller.makeMove(aiPlayer);
				}
				break;
			case "Random":
				players.put(p, randomPlayer);
				if (lastTurn.equals(viewPiece) || (viewPiece == null && lastTurn.equals(p))) {
					///////
					resetMove();
					controller.makeMove(randomPlayer);
				}
				break;
			default:
				throw new UnsupportedOperationException(
						"Something went wrong! This program point should be unreachable!");
			}
			settings.updateTableMode(p, mode);
		}
	}

	public abstract void setManualPlayer(Piece p);

	@Override
	public void quitPressed() {
		int response = JOptionPane.showConfirmDialog(null,
				"Are sure you want to quit the game?\n (You will lose the game)", "Quit confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.err.println("Yes button clicked. Quit");
			controller.stop();
			// System.exit(0);
		}
	}

	public void restartPressed() {
		int response = JOptionPane.showConfirmDialog(null,
				"Are sure you want to restart the game?\n (You will lose the game)", "Restart confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.err.println("Yes button clicked. Restart");
			controller.restart();
			boardUI.update();

		}
	}

	/**
	 * Resizes a panel to a square form.
	 * 
	 * @param innerPanel
	 *            Panel to resize.
	 * @param container
	 *            Panel that contains the panel witch will be resized.
	 */
	private static void resizePreview(JPanel innerPanel, JPanel container) {
		int w = container.getWidth();
		int h = container.getHeight();
		int size = Math.min(w, h);
		innerPanel.setPreferredSize(new Dimension(size, size));
		container.revalidate();
	}

	public void setPlayer(Piece piece, Player player) {
		players.put(piece, player);
	}

	public String getMove() {
		return move;
	}

	public void setMove(int row, int col) {
		move += row + " " + col + " ";
	}

	public void resetMove() {
		move = "";
	}

	public Piece getViewPiece() {
		return viewPiece;
	}

}
