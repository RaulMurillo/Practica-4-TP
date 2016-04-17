package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.control.SwingController;

public class SettingsPanel extends JPanel {
	private StatusMessages textArea;
	private PlayerInformation table;
	private PieceColorChooser colorChooser;
	private PlayerModes modeSelector;
	private AutomaticMoves autoMovPane;
	private QuitPanel quitPanel;

	private Board board;
	private List<Piece> pieces;
	private Map<Piece, Player> players;

	public SettingsPanel(List<Piece> p, Map<Piece, Color> map, Board b, GenericSwingView listener, Piece viewPiece) {
		pieces = p;
		board = b;
		this.players = players;
		initComponents(map, listener, viewPiece);
	}

	private void initComponents(Map<Piece, Color> map, GenericSwingView listener, Piece viewPiece) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		textArea = new StatusMessages(); // Revisar esta clase
		add(textArea);

		// Player Information Table
		table = new PlayerInformation(board, pieces, map, viewPiece);
		add(table);

		// Piece Colors Chooser
		colorChooser = new PieceColorChooser(pieces, listener);
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

	public void write(String text) {
		textArea.showMessage(text);
	}

	public void setEnabled(boolean moves, boolean quit) {
		autoMovPane.setEnabled(moves);
		quitPanel.setEnabled(quit);
	}

	public void updateTableColor(Map<Piece, Color> map) {
		table.updateColors(map);
	}

	public void updateTablePieces() {
		// table.updateModes(Piece p, String mode)
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
}
