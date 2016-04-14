package es.ucm.fdi.tp.practica5.views;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class AutomaticMoves extends JPanel {

	private AutoMovesListener controlsListener;

	public interface AutoMovesListener {
		void randomPressed();

		void aiPressed();
	}

	/**
	 * Create the panel.
	 */
	public AutomaticMoves() {
		initialize();
	}

	/**
	 * Initialize the contents of the panel.
	 */
	private void initialize() {
		setBorder(new TitledBorder(null, "Automatic Moves", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JButton jbRandom = new JButton("Random");
		jbRandom.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.randomPressed();
			}
		});
		add(jbRandom);

		JButton jbIntelligent = new JButton("Intelligent");
		jbIntelligent.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.aiPressed();
			}
		});
		add(jbIntelligent);

	}

}
