package es.ucm.fdi.tp.practica5;

import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class PlayerModes extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the application.
	 */
	public PlayerModes(List<Piece> pieces) {
		initialize(pieces);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(List<Piece> pieces) {
		setBorder(new TitledBorder(null, "Player Modes", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JComboBox<Piece> cbPlayer = new JComboBox(pieces.toArray());
		add(cbPlayer);
		
		String[] modesStrings = { "Manual", "Random", "Intelligent" };
		JComboBox<String> cbMode = new JComboBox<String>(modesStrings);
		add(cbMode);
		
		JButton btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		add(btnSet);
	}

}
