package es.ucm.fdi.tp.practica5.views;

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
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;

public class PlayerInformation extends JPanel {

	private JTable jtTable;
	private Object[][] data;

	/**
	 * Create the application.
	 */
	public PlayerInformation(Board b, List<Piece> p, Map<Piece, Color> map, Piece viewPiece) {
		initializeTable(p);
		updateColors(map);
		updateNumPieces(p, b);
		for (int i = 0; i < p.size(); i++) {
			if (viewPiece==null || viewPiece.equals(p.get(i)))
				updateMode(i, "Manual");
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeTable(List<Piece> pieces) {
		this.setBorder(
				new TitledBorder(null, "Player Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		data = new Object[pieces.size()][3];
		setPieces(pieces);
		String[] columnNames = { "Player", "Mode", "#Pieces" };
		jtTable = new JTable(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false; // Table cels ARE NOT editable.
			}
		};

		jtTable.setPreferredScrollableViewportSize(jtTable.getPreferredSize());
		jtTable.setFillsViewportHeight(true); // Fills the empty space with
												// white
		JScrollPane jspScroll = new JScrollPane(jtTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(jspScroll);
	}

	/**
	 * Initialize the table row data according to the number of players.
	 * 
	 * @return
	 */
	private void setPieces(List<Piece> pieces) {
		for (int j = 0; j < pieces.size(); j++) {
			data[j][0] = pieces.get(j);
		}
	}

	public void updateColors(Map<Piece, Color> map) {

		repaint();
	}

	public void updateMode(int p, String mode) {
		data[p][1] = mode;
		repaint();
	}

	public void updateNumPieces(List<Piece> pieces, Board board) {
		for (int j = 0; j < pieces.size(); j++) {
			if (board.getPieceCount(pieces.get(j)) != null)
				data[j][2] = board.getPieceCount(pieces.get(j));
		}
		repaint();
	}
	
}
