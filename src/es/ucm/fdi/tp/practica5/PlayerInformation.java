package es.ucm.fdi.tp.practica5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxRules;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;

public class PlayerInformation extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;


	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					PlayerInformation window = new PlayerInformation();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public PlayerInformation(Board b, List<Piece> p) {
		initialize(b, p);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Board board, List<Piece> pieces) {
		// frame = new JFrame();
		this.setBorder(
				new TitledBorder(null, "Player Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		Object[][] data = setRowData(pieces, board);
		String[] columnNames = { "Player", "Mode", "#Pieces" };

		table = new JTable(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false; // Table cels ARE NOT editable.
			}
		};
		
		table.setFillsViewportHeight(true); // Rellena de blanco
		JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setEnabled(false);
		this.add(scroll);
		// frame.setVisible(true);
	}

	/**
	 * Initialize the table row data according to the number of players.
	 * 
	 * @return
	 */
	private Object[][] setRowData(List<Piece> pieces, Board board) { // ReadOnlyBoard??
		Object[][] data = new Object[pieces.size()][3];
		for (int j = 0; j < pieces.size(); j++) {
			data[j][0] = pieces.get(j);// .toString();
			data[j][1] = "Mode of player " + (j + 1); // null; //mode
			data[j][2] = board.getPieceCount(pieces.get(j));
		}
		return data;
	}

	/*
	 * public void setColors(List<Piece> pieces, List<Color> colors) { for
	 * (Piece it : pieces) { TableColumn column =
	 * table.getColumnModel().getColumn(it); column.set
	 * column.setCellRenderer(colors.get()); } }
	 */

}
