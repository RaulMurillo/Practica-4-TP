package es.ucm.fdi.tp.practica5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class PlayerInformation extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
	}

	/**
	 * Create the application.
	 */
	public PlayerInformation() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, "Player Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		Object[][] data = setRowData(/* pieces, board */);
		String[] columnNames = { "Player", "Mode", "#Pieces" };

		table = new JTable(data, columnNames); // Table cols are editable -
												// they shouldn't

		// table.setFillsViewportHeight(true); //Rellena de blanco
		JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setEnabled(false);
		panel.add(scroll);

		getContentPane().add(panel);
		setSize(350, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Initialize the table row data according to the number of players.
	 * @return
	 */
	private Object[][] setRowData(/* List<Piece> pieces, Board board */) { // ReadOnlyBoard??
		Object[][] data = new Object[4/* pieces.size() */][3];
		for (int j = 0; j < 4 /* pieces.size() */; j++) {
			data[j][0] = "Pieza " + j; // pieces.get(j);
			data[j][1] = "Modo pieza " + j; // null; //mode
			data[j][2] = "Num. piezas tipo " + j; // board.getPieceCount(pieces.get(0));
		}
		return data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * public void setColors(List<Piece> pieces) { for (List it : Piece) {
	 * TableColumn column = table.getColumnModel().getColumn(it);
	 * column.setCellRenderer(centerRenderer); } }
	 */

}
