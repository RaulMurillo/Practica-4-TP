package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
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

	public SettingsPanel(List<Piece> p, Map<Piece, Color> map, Board b) {
		pieces = p;
		board = b;
		this.players = players;
		initComponents(map);
	}

	private void initComponents(Map<Piece, Color> map) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		textArea = new StatusMessages(); // Revisar esta clase
		add(textArea);

		// Player Information Table
		table = new PlayerInformation(board, pieces, map);
		add(table);

		// Piece Colors Chooser
		colorChooser = new PieceColorChooser(pieces);
		colorChooser
				.setBorder(new TitledBorder(null, "Piece Colors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(colorChooser);

		// Player Mode Selector
		modeSelector = new PlayerModes(pieces);
		add(modeSelector);

		// Automatic Moves Panel
		autoMovPane = new AutomaticMoves();
		add(autoMovPane);

		// Quit Selection Panel
		quitPanel = new QuitPanel();
		add(quitPanel);
	}

	public void write(String text) {
		textArea.showMessage(text);
	}

	@Override
	public void setEnabled(boolean b) {
		autoMovPane.setEnabled(b);
		quitPanel.setEnabled(b);
	}

	public void updateTableColor(Map<Piece, Color> map) {
		table.updateColors(map);
	}

	public void updateTableData() {
		// table.updateModes(Piece p, String mode)
		table.updateNumPieces(pieces, board);
	}
}
