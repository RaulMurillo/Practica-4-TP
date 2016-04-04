package es.ucm.fdi.tp.practica5;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class AutomaticMoves extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the application.
	 */
	public AutomaticMoves() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBorder(new TitledBorder(null, "Automatic Moves", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		/////////////////////
		JButton btnRandom = new JButton("Random");
		btnRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		add(btnRandom);
		//////////////////
		JButton btnIntelligent = new JButton("Intelligent");
		btnIntelligent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		add(btnIntelligent);
		
	}

}
