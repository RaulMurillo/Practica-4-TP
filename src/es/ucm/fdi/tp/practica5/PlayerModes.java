package es.ucm.fdi.tp.practica5;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PlayerModes extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JComboBox<String> cbPlayer;
	private JComboBox<String> cbMode;
	private JButton btnSet;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					PlayerModes window = new PlayerModes();
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
	public PlayerModes() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Player Modes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		/////////////////////
		String[] pieceStrings = getPieceStrings(/* pieces */);
		cbPlayer = new JComboBox<String>(pieceStrings);
		panel.add(cbPlayer);
		////////////////////
		String[] modesStrings = { "Manual", "Random", "Intelligent" };
		cbMode = new JComboBox<String>(modesStrings);
		panel.add(cbMode);
		//////////////////
		btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		panel.add(btnSet);
		//////////////////
		getContentPane().add(panel);
		setSize(350, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private String[] getPieceStrings(/* List<Piece> pieces */) {
		String[] p = new String[3/* pieces.size() */];
		for (int i = 0; i < p.length; i++) {
			p[i] = "Pieza " + i; // pieces.get(j);
		}
		return p;
	}

}
