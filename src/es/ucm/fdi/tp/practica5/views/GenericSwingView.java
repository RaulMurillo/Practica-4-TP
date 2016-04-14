package es.ucm.fdi.tp.practica5.views;

import java.awt.BorderLayout;
import java.awt.Color;
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
import es.ucm.fdi.tp.practica5.control.SwingController;

public class GenericSwingView extends JFrame
		implements PieceColorChooser.PieceColorsListener, BoardGUI.BoardGUIListener, GameObserver {

	/**
	 * The list of pieces involved in the game. It is stored when the game
	 * starts and used when the state is printed.
	 */
	private List<Piece> pieces;

	private SwingController controller;

	private Map<Piece, Color> colorMap;

	private Map<Piece, Player> players;

	final private static List<Color> DEFAULT_COLORS = new ArrayList<Color>() {
		{
			add(Color.red);
			add(Color.green);
			add(Color.blue);
			add(Color.yellow);
		}
	};

	private Piece viewPiece;
	private Player randomPlayer;
	private Player aiPlayer;

	private BoardGUI boardUI;

	private SettingsPanel settings;

	private boolean doubleClick;

	public GenericSwingView(Observable<GameObserver> g, SwingController c, Piece p, Player random, Player ai) {
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
		setSize(600, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		String view = "";
		if (viewPiece != null)
			view = "(" + viewPiece + ")";
		this.setTitle("Board Games: " + gameDesc + " " + view);

		JPanel jpBoard = new JPanel(new BorderLayout());
		boardUI = new BoardGUI(board, colorMap);
		jpBoard.add(boardUI);
		add(jpBoard, BorderLayout.CENTER);

		settings = new SettingsPanel(pieces, colorMap, board);
		add(settings, BorderLayout.EAST);
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
		settings.write("\n Game Over!!\n \n");
		settings.write("Game Status: " + state);
		if (state == State.Won) {
			settings.write("Winner: " + winner);
		}
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		enableButtons();
		if (players.get(turn) != randomPlayer && players.get(turn) != aiPlayer) {
			settings.write(controller.help());
		}
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		disablePanels();
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
			return controller.firstPartMove(col, fila);
		} else {
			controller.secondPartMove(col, fila);
			return false;
		}
	}

	public void casillaDeseleccionada(int col, int fila) {
		if (doubleClick) {
			controller.casillaDeseleccionada(col, fila);
		}
	}

	private void enableButtons() {
		boardUI.setEnabled(true);
		settings.setEnabled(true);
	}

	private void disablePanels() {
		boardUI.setEnabled(false);
		settings.setEnabled(false);
	}

	/*
	 * public void selectedColor(Piece p, Color c) { colorMap.put(p, c);
	 * boardUI.update(); settings.updateTableColor(colorMap); }
	 */

	@Override
	public void changeColorPressed(Piece p, Color c) {
		colorMap.put(p, c);
		boardUI.update();
		settings.updateTableColor(colorMap);
	}

	@Override
	public boolean leftButtonPressed(int row, int col) {
		// TODO Auto-generated method stub
		return doubleClick;
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		// TODO Auto-generated method stub

	}

}
