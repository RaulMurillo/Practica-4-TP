package es.ucm.fdi.tp.practica5;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class AutomaticMoves extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JButton btnRandom;
	private JButton btnIntelligent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					AutomaticMoves window = new AutomaticMoves();
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
	public AutomaticMoves() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Automatic Moves", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		/////////////////////
		btnRandom = new JButton("Random");
		btnRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		panel.add(btnRandom);
		//////////////////
		btnIntelligent = new JButton("Intelligent");
		btnIntelligent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		panel.add(btnIntelligent);
		//////////////////
		getContentPane().add(panel);
		setSize(350, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
