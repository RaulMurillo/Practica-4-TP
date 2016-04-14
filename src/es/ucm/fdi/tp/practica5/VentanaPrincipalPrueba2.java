package es.ucm.fdi.tp.practica5;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

public class VentanaPrincipalPrueba2 extends JFrame {

	// private JTable table;
	private Board board;
	private ColorMap map;
	private List<Piece> pieces;

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { List<Piece> p = new
	 * ArrayList<Piece>(); p.add(new Piece("X")); p.add(new Piece("O"));
	 * p.add(new Piece("R")); GameRules rules = new AtaxxRules(5, 0); Board b =
	 * rules.createBoard(p); construye(b, p );
	 * 
	 * EventQueue.invokeLater(new Runnable() { public void run() { try {
	 * VentanaPrincipalPrueba2 window = new VentanaPrincipalPrueba2();
	 * window.frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	/**
	 * Create the application.
	 */
	public VentanaPrincipalPrueba2(Board board, List<Piece> pieces) {
		super();
		this.board = board;
		this.pieces = pieces;
		this.map = new ColorMap(pieces);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setSize(600, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel(new BorderLayout()); // This Layout makes
															// board be in the
															// middle
		//this.getContentPane().add(panel_1, BorderLayout.CENTER);

		BoardUI tablero = new BoardUI(board, map);
		panel_1.add(tablero);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizePreview(tablero, VentanaPrincipalPrueba2.this);
			}
		});

		JPanel panel_2 = new JPanel();
		//this.getContentPane().add(panel_2, BorderLayout.EAST);
		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                panel_1, panel_2);
		//mainPanel.add(panel_1);
		//mainPanel.add(panel_2);
		Dimension minimumSize = new Dimension(200, 50);
		panel_1.setMinimumSize(minimumSize);
		//panel_2.setMinimumSize(minimumSize);	//No needed		
		this.getContentPane().add(mainPanel);
		
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		/* Control panels */
		// Status Messages Panel
		StatusMessages textArea = new StatusMessages(); // Revisar esta clase
		panel_2.add(textArea);

		// Player Information Table
		PlayerInformation table = new PlayerInformation(board, pieces);
		panel_2.add(table);

		// Piece Colors Chooser
		PieceColorChooser colorChooser = new PieceColorChooser(pieces, map);
		colorChooser
				.setBorder(new TitledBorder(null, "Piece Colors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(colorChooser);

		// Player Mode Selector
		PlayerModes modeSelector = new PlayerModes(pieces);
		panel_2.add(modeSelector);

		// Automatic Moves Panel
		AutomaticMoves autoMovPane = new AutomaticMoves();
		panel_2.add(autoMovPane);

		// Quit Selection Panel
		JPanel panel_7 = new QuitPanel();
		panel_2.add(panel_7);
	}

	/**
	 * Resizes a panel to a square form.
	 * 
	 * @param innerPanel
	 *            Panel to resize.
	 * @param container
	 *            Panel that contains the panel witch will be resized.
	 */
	private static void resizePreview(JPanel innerPanel, JFrame container) {
		System.err.println("Size changed to " + container.getSize());
		int w = container.getWidth();
		int h = container.getHeight();
		int size = Math.min(w, h);
		innerPanel.setPreferredSize(new Dimension(size, size));
		container.revalidate();
	}

}
