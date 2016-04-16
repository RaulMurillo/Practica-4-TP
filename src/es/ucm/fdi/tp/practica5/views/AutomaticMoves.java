package es.ucm.fdi.tp.practica5.views;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class AutomaticMoves extends JPanel {

	private AutoMovesListener controlsListener;
	private JButton jbIntelligent;
	private JButton jbRandom;

	public interface AutoMovesListener {
		void randomPressed();

		void aiPressed();
	}

	/**
	 * Create the panel.
	 */
	public AutomaticMoves(AutoMovesListener controlsListener) {
		this.controlsListener = controlsListener;
		setBorder(new TitledBorder(null, "Automatic Moves", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	}

	public void enableRandomButton() {
		jbRandom = new JButton("Random");
		jbRandom.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.randomPressed();
			}
		});
		add(jbRandom);
	}

	public void enableIntelligentButton() {
		jbIntelligent = new JButton("Intelligent");
		jbIntelligent.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.aiPressed();
			}
		});
		add(jbIntelligent);
	}
	
	@Override
	public void setEnabled(boolean b){
		jbIntelligent.setEnabled(b);
		jbRandom.setEnabled(b);
	}

}
