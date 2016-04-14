package es.ucm.fdi.tp.practica5.views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.control.SwingController;

public class QuitPanel extends JPanel {

	private JButton jbRestart;
	
	private QuitPanelListener controlsListener;

	public interface QuitPanelListener {
        void quitPressed();
        void restartPressed();
    }
	
	/**
	 * Create the panel.
	 */
	public QuitPanel() {
		super();
		initialize();
	}

	/**
	 * Initialize the contents of the panel.
	 */
	private void initialize() {
		JButton jbQuit = new JButton("Quit");
		jbQuit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.quitPressed();
			}			
		});	
		add(jbQuit);		
	}

	public void setRestart() {
		jbRestart = new JButton("Restart");
		jbRestart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.restartPressed();
			}			
		});	
		add(jbRestart);
	}

	public void enableRestart() {
		jbRestart.setEnabled(true);
	}

	public void disableRestart() {
		jbRestart.setEnabled(false);
	}

}
