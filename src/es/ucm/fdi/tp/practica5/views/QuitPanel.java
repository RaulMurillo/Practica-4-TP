package es.ucm.fdi.tp.practica5.views;

import java.awt.event.*;
import javax.swing.*;

public class QuitPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton jbQuit;
	private JButton jbRestart;

	private QuitPanelListener controlsListener;

	public interface QuitPanelListener {
		void quitPressed();

		void restartPressed();
	}

	/**
	 * Create the panel.
	 */
	public QuitPanel(QuitPanelListener controlsListener) {
		this.controlsListener = controlsListener;
		initialize();
	}

	/**
	 * Initialize the contents of the panel.
	 */
	private void initialize() {
		jbQuit = new JButton("Quit");
		jbQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlsListener.quitPressed();
			}
		});
		add(jbQuit);
	}

	public void setRestartButton() {
		jbRestart = new JButton("Restart");
		jbRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlsListener.restartPressed();
			}
		});
		add(jbRestart);
	}

	@Override
	public void setEnabled(boolean b) {
		jbQuit.setEnabled(b);
		if (jbRestart != null) {
			jbRestart.setEnabled(b);
		}
	}

}
