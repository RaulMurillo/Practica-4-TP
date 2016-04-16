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
import es.ucm.fdi.tp.practica5.VentanaPrincipalPrueba2;
import es.ucm.fdi.tp.practica5.control.SwingController;

public class GenericSwingView extends JFrame
		implements PieceColorChooser.PieceColorsListener, BoardGUI.BoardGUIListener, AutomaticMoves.AutoMovesListener,
		QuitPanel.QuitPanelListener, PlayerModes.PlayerModesListener, GameObserver {

	/**
	 * The list of pieces involved in the game. It is stored when the game
	 * starts and used when the state is printed.
	 */
	private List<Piece> pieces;
	private Controller controller;
	private Map<Piece, Color> colorMap;
	private Map<Piece, Player> players;
	private Piece viewPiece;
	private Player randomPlayer;
	private Player aiPlayer;
	private BoardGUI boardUI;
	private SettingsPanel settings;
	private boolean doubleClick;

	final private static List<Color> DEFAULT_COLORS = new ArrayList<Color>() {
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
		doubleClick = false;
		g.addObserver(this); // register as an observer

	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.pieces = pieces;
		colorMap = new HashMap<Piece, Color>();
		setColorMap(DEFAULT_COLORS);

		// Creates a new window
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

		visible();

		// Set GameStart comments
		settings.write("Starting '" + gameDesc + "'");
		settings.write("----------------------");
		String pieceTurn = turn.toString();
		if (turn.equals(viewPiece))
			pieceTurn += " (You)";
		settings.write("Turn for " + pieceTurn);
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		settings.write("\n Game Over!!");
		settings.write("Game Status: " + state);
		if (state == State.Won) {
			settings.write("Winner: " + winner);
		}
		boardUI.setEnabled(false);
		settings.setEnabled(false, true);
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
		String pieceTurn = turn.toString();
		if (turn.equals(viewPiece))
			pieceTurn += " (You)";
		settings.write("Turn for " + pieceTurn);
	}

	@Override
	public void onError(String msg) {
		JOptionPane.showInternalMessageDialog(null, msg, "Error Message", JOptionPane.ERROR_MESSAGE);
	}

	private void visible() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
	}

	public void setColorMap(List<Color> colors) {
		for (int i = 0; i < pieces.size(); i++) {
			this.colorMap.put(pieces.get(i), colors.get(i));
		}
	}

	public boolean getDoubleClick() {
		return doubleClick;
	}

	public void setDoubleClick(boolean set) {
		doubleClick = set;
	}

	public boolean casillaSeleccionada(int col, int fila) {
		if (!doubleClick) {
			// return controller.firstPartMove(col, fila);
		} else {
			// controller.secondPartMove(col, fila);
			return false;
		}
		return doubleClick;
	}

	public void casillaDeseleccionada(int col, int fila) {
		if (doubleClick) {
			// controller.casillaDeseleccionada(col, fila);
		}
	}

	private void enablePanels() {
		boardUI.setEnabled(true);
		settings.setEnabled(true, true);
	}

	private void disablePanels() {
		boardUI.setEnabled(false);
		settings.setEnabled(false, false);
	}

	@Override
	public void changeColorPressed(Piece p, Color c) {
		colorMap.put(p, c);
		boardUI.update();
		settings.updateTableColor(colorMap);
	}

	@Override
	public boolean leftButtonPressed(int row, int col) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		// TODO Auto-generated method stub

	}

	@Override
	public void randomPressed() {
		//if(viewPiece==null || viewPiece.equals(turn))
		controller.makeMove(randomPlayer);
	}

	@Override
	public void aiPressed() {
		controller.makeMove(aiPlayer);
	}

	@Override
	public void changeModePressed(Piece p, String mode) {
		if (viewPiece == null || viewPiece.equals(p)) {
			switch (mode) {
			case "Manual":
				// players.add(gameFactory.createConsolePlayer());
				break;
			case "Intelligent":
				// players.put(p, gameFactory.createAIPlayer(aiPlayerAlg));
				break;
			case "Random":
				// players.add(gameFactory.createRandomPlayer());
				break;
			default:
				throw new UnsupportedOperationException(
						"Something went wrong! This program point should be unreachable!");
			}
			settings.updateTableMode(p, mode);
		}
	}

	@Override
	public void quitPressed() {
		int response = JOptionPane.showConfirmDialog(null,
				"Are sure you want to quit the game?\n (You will lose the game)", "Quit confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.out.println("Yes button clicked");
			controller.stop();
			System.exit(0);
		}
	}

	public void restartPressed() {
		int response = JOptionPane.showConfirmDialog(null,
				"Are sure you want to restart the game?\n (You will lose the game)", "Restart confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.out.println("Yes button clicked");
			// BUGS!
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
		// System.err.println("Size changed to " + container.getSize());
		int w = container.getWidth();
		int h = container.getHeight();
		int size = Math.min(w, h);
		innerPanel.setPreferredSize(new Dimension(size, size));
		container.revalidate();
	}

}
