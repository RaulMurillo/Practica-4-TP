package es.ucm.fdi.tp.practica5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;

public class QuitPanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JButton btnQuit;
	// private JButton btnRestart;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					QuitPanel window = new QuitPanel();
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
	public QuitPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panel = new JPanel();
		/////////////////////
		btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				quit();
			}
		});
		/*
		 * if(!turn.gameMode().equals(MANUAL)){ btnQuit.setEnabled(false); }
		 */
		panel.add(btnQuit);
		//////////////////
		/*
		 * btnRestart = new JButton("Restart"); btnRestart.addActionListener(new
		 * ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) { // TODO
		 * Auto-generated method stub // game.restart();
		 * 
		 * } }); panel.add(btnRestart);
		 */
		//////////////////
		getContentPane().add(panel);
		setSize(350, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void quit() {
		int response = JOptionPane.showConfirmDialog(null,
				"Are sure you want to quit the game?\n (You will lose the game)", "Quit confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.out.println("Yes button clicked");
			// game.stop();
			System.exit(0);
		}
	}

}
