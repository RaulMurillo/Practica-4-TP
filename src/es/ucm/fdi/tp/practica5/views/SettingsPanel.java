package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;


public class SettingsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StatusMessages textArea;
	private PlayerInformation table;
	private PieceColors colorChooser;
	private PlayerModes modeSelector;
	private AutomaticMoves autoMovPane;
	private QuitPanel quitPanel;

	private Board board;
	private List<Piece> pieces;

	public SettingsPanel(List<Piece> pieces, Map<Piece, Color> map, Board board, GenericSwingView listener, Piece viewPiece) {
		this.pieces = pieces;
		this.board = board;
		initComponents(map, listener, viewPiece);
	}

	private void initComponents(Map<Piece, Color> map, GenericSwingView listener, Piece viewPiece) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Status Messages text area
		textArea = new StatusMessages();
		add(textArea);

		// Player Information Table
		table = new PlayerInformation(board, pieces, map, viewPiece);
		add(table);

		// Piece Colors Chooser
		colorChooser = new PieceColors(pieces, listener);
		add(colorChooser);

		// Player Mode Selector
		modeSelector = new PlayerModes(pieces, listener);
		add(modeSelector);

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

	public void setMessage(String msg) {
		textArea.showMessage(msg);
	}

	public void setEnabled(boolean moves, boolean quit, boolean modes) {
		autoMovPane.setEnabled(moves);

		quitPanel.setEnabled(quit);
		
		modeSelector.setEnabled(modes);
	}

	public void updateTableColor(Map<Piece, Color> map) {
		table.updateColors(map);
	}

	public void updateTablePieces() {
		table.updateNumPieces(pieces, board);
	}

	public void updateTableMode(Piece p, String mode) {
		table.updateMode(pieces.indexOf(p), mode);
	}

	/**
	 * Enable the correspondent Automatic Moves buttons if RANDOM and
	 * INTELLIGENT modes are available. If none is available, correspondent
	 * panel turns invisible.
	 * 
	 * @param rand
	 *            Indicates if RANDOM mode is available.
	 * @param ai
	 *            Indicates if INTELLIGENT mode is available.
	 */
	public void configAutoMoves(boolean rand, boolean ai) {
		if (!rand && !ai) {
			autoMovPane.setVisible(false);
		} else {
			if (rand)
				autoMovPane.addRandomButton();
			if (ai)
				autoMovPane.addIntelligentButton();
		}
	}
	public void setBoard(Board board) {
		this.board = board;
		updateTablePieces();
		for(Piece p:pieces){
			updateTableMode(p, "Manual");
			
		}
	}
}
