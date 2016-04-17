package es.ucm.fdi.tp.practica5.views;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * A panel for making automatic moves when playing MANUAL mode.
 * <p>
 * Panel para realizar movimientos automaticos cuando se juega en modo manual.
 */
public class AutomaticMoves extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Listener of the class.
	 */
	private AutoMovesListener controlsListener;

	/**
	 * Button for AI moves.
	 */
	private JButton jbIntelligent;

	/**
	 * Button for random moves.
	 */
	private JButton jbRandom;

	/**
	 * Listener of the class.
	 */
	public interface AutoMovesListener {

		/**
		 * Notifies Random move button has been pressed.
		 */
		void randomPressed();

		/**
		 * Notifies AI move button has been pressed.
		 */
		void aiPressed();
	}

	/**
	 * Create the panel.
	 * 
	 * @param controlsListener
	 *            Listener of the class.
	 */
	public AutomaticMoves(AutoMovesListener controlsListener) {
		this.controlsListener = controlsListener;
		setBorder(new TitledBorder(null, "Automatic Moves", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	}

	/**
	 * Add a button to the panel for making random moves.
	 */
	public void addRandomButton() {
		jbRandom = new JButton("Random");
		jbRandom.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.randomPressed();
			}
		});
		add(jbRandom);
	}

	/**
	 * Add a button to the panel for making AI moves.
	 */
	public void addIntelligentButton() {
		jbIntelligent = new JButton("Intelligent");
		jbIntelligent.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controlsListener.aiPressed();
			}
		});
		add(jbIntelligent);
	}

	@Override
	public void setEnabled(boolean b) {
		jbIntelligent.setEnabled(b);
		jbRandom.setEnabled(b);
	}

}
