package es.ucm.fdi.tp.practica5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxRules;

public class TestTable2 {

	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	private class TableRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setBackground(null);
			Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			// if (getSearch() != null && getSearch().length() > 0 &&
			// value.toString().contains(getSearch())) {
			if (row % 2 == 0 && column % 2 == 0 || row % 2 == 1 && column % 2 == 1) {
				setBackground(Color.RED);
			}
			else setBackground(new Color(row, row, column, row));
			return tableCellRendererComponent;
		}
	}

	protected void initUI() {
		DefaultTableModel model = new DefaultTableModel();
		//Columns
		String[] columnNames = { "Player", "Mode", "#Pieces" };
		for (int i = 0; i < columnNames.length; i++) {
			model.addColumn(columnNames[i]);
		}
		//Row data	
		//
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		pieces.add(new Piece("R"));

		GameRules rules = new AtaxxRules(5, 0);
		Board board = rules.createBoard(pieces);
		
		for (int i = 0; i < pieces.size(); i++) {
			Vector<Object> row = new Vector<Object>();
			for (int j = 0; j < 3; j++) {
				row.add(pieces.get(i));
				row.add("Mode of player " + (i + 1));
				row.add(board.getPieceCount(pieces.get(i)));
			}
			model.addRow(row);
		}
		table = new JTable(model){
			public boolean isCellEditable(int row, int column) {
				return false; // Table cels ARE NOT editable.
			}
		};
		TableRenderer renderer = new TableRenderer();
		table.setDefaultRenderer(Object.class, renderer);
		table.setFillsViewportHeight(true);
		//textField = new JTextField(30);
		/*textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateSearch();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateSearch();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateSearch();
			}
		});*/
		JFrame frame = new JFrame(TestTable2.class.getSimpleName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane scrollpane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(scrollpane, BorderLayout.CENTER);
		//frame.add(textField, BorderLayout.NORTH);
		frame.setSize(500, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	//protected void updateSearch() {
		//setSearch(textField.getText());
	//	table.repaint();
	//}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new TestTable2().initUI();
			}
		});
	}

	private static final String[] WORDS = { "art", "australia", "baby", "beach", "birthday", "blue", "bw", "california",
			"canada", "canon", "cat", "chicago", "china", "christmas", "city", "dog", "england", "europe", "family",
			"festival", "flower", "flowers", "food", "france", "friends", "fun", "germany", "holiday", "india", "italy",
			"japan", "london", "me", "mexico", "music", "nature", "new", "newyork", "night", "nikon", "nyc", "paris",
			"park", "party", "people", "portrait", "sanfrancisco", "sky", "snow", "spain", "summer", "sunset", "taiwan",
			"tokyo", "travel", "trip", "uk", "usa", "vacation", "water", "wedding" };
	private JTable table;
	//private JTextField textField;
	

}