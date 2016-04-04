package es.ucm.fdi.tp.practica5;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxRules;

import javax.swing.border.BevelBorder;

public class VentanaPrincipalPrueba2 extends JFrame{
	
	private JTable table;
	private Board board;
	private ColorMap map;
	private List<Piece> pieces;
	
	

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		List<Piece> p = new ArrayList<Piece>();
		p.add(new Piece("X"));
		p.add(new Piece("O"));
		p.add(new Piece("R"));
		GameRules rules = new AtaxxRules(5, 0);
		Board b = rules.createBoard(p);
		construye(b, p );
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipalPrueba2 window = new VentanaPrincipalPrueba2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public VentanaPrincipalPrueba2(Board b, List<Piece> p) {
		super();
		board = b;
		pieces = p;
		map = new ColorMap(p);
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 600, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		this.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		BoardUI tablero = new BoardUI(board, map);
		panel.add(tablero);

		JPanel panel_1 = new JPanel();
		this.getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JTextArea textArea = new JTextArea("Status messages here!");

		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setViewportBorder(
				new TitledBorder(null, "Status Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(scroll);
		
		
		//PlayerInformation panel_3 = new PlayerInformation(board, pieces);
		//panel_1.add(panel_3);
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(
				new TitledBorder(null, "Player Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_3);

		Object[][] rowData = { { "Hola", null, null }, { null, null, null }};

		String[] columnNames = { "#Pieces", "Mode", "Player" };

		table = new JTable(rowData, columnNames);
		//table.setAutoResizeMode(5);
		
		panel_3.setLayout(new BorderLayout());
		panel_3.add(table.getTableHeader(), BorderLayout.PAGE_START);
		panel_3.add(table, BorderLayout.CENTER);
		
		
		
		PieceColorChooser panel_4 = new PieceColorChooser(pieces, map);
		panel_4.setBorder(new TitledBorder(null, "Piece Colors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_4);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Player Modes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_5);

		JComboBox comboBox_1 = new JComboBox();
		panel_5.add(comboBox_1);

		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] { "Manual", "Random", "Intelligent" }));
		panel_5.add(comboBox_2);

		JButton btnNewButton = new JButton("Set");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		panel_5.add(btnNewButton);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(
				new TitledBorder(null, "Automatic Moves", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_6);

		JButton btnA = new JButton("Random");
		panel_6.add(btnA);

		JButton btnNewButton_1 = new JButton("Intelligent");
		panel_6.add(btnNewButton_1);

		JPanel panel_7 = new QuitPanel();
		panel_1.add(panel_7);

		
	}
}
