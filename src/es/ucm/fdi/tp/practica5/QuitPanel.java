package es.ucm.fdi.tp.practica5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;

public class QuitPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton btnRestart;

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { SwingUtilities.invokeLater(new
	 * Runnable() { public void run() { try { QuitPanel window = new
	 * QuitPanel(); window.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	/**
	 * Create the application.
	 */
	public QuitPanel() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				quit();
			}
		});
		/*
		 * if(!turn.gameMode().equals(MANUAL)){ btnQuit.setEnabled(false); }
		 */
		add(btnQuit);
		//////////////////

		//////////////////
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

	public void setRestart() {
		btnRestart = new JButton("Restart");
		btnRestart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// game.restart();

			}
		});
		add(btnRestart);
	}

	public void enableRestart() {
		btnRestart.setEnabled(true);
	}

	public void disableRestart() {
		btnRestart.setEnabled(false);
	}

}
