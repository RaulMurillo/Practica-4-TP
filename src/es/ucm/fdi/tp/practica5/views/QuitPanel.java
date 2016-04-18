package es.ucm.fdi.tp.practica5.views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class QuitPanel extends JPanel {

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
		jbQuit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.quitPressed();
			}
		});
		add(jbQuit);
	}

	public void setRestartButton() {
		jbRestart = new JButton("Restart");
		jbRestart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
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
		//this.setEnabled(b);
	}

}
