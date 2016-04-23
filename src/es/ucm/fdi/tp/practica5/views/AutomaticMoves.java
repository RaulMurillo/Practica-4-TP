package es.ucm.fdi.tp.practica5.views;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * A panel for making automatic moves when playing MANUAL mode.
 * <p>
 * Panel para realizar movimientos automaticos cuando se juega en modo MANUAL.
 */
public class AutomaticMoves extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Listener of the class.
	 * <p>
	 * "Listener" de la clase.
	 */
	private AutoMovesListener controlsListener;

	/**
	 * Button for AI moves.
	 * <p>
	 * Boton para movimientos inteligentes.
	 */
	private JButton jbIntelligent;

	/**
	 * Button for random moves.
	 * <p>
	 * Boton para movimientos aleatorios.
	 */
	private JButton jbRandom;

	/**
	 * The listener interface for sending action events.
	 * <p>
	 * Interfaz "listener" de la clase para enviar eventos.
	 */
	public interface AutoMovesListener {

		/**
		 * Notifies that RANDOM move button has been pressed.
		 * <p>
		 * Notifica que el boton de movimiento aleatorio ha sido pulsado.
		 */
		void randomPressed();

		/**
		 * Notifies that INTELLIGENT move button has been pressed.
		 * <p>
		 * Notifica que el boton de movimiento inteligente ha sido pulsado.
		 */
		void aiPressed();
	}

	/**
	 * Creates a panel with ({@link JButton})s for making automatics move while playing
	 * MANUAL mode.
	 * <p>
	 * Crea un panel con botones ({@link JButton}) para realizar movimientos automaticos mientras
	 * se juega en modo MANUAL.
	 * 
	 * @param controlsListener
	 *            Listener of the class.
	 *            <p>
	 *            "Listener" de la clase.
	 */
	public AutomaticMoves(AutoMovesListener controlsListener) {
		this.controlsListener = controlsListener;
		setBorder(new TitledBorder(null, "Automatic Moves", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	}

	/**
	 * Adds a button to the panel for making RANDOM moves.
	 * <p>
	 * Añade al panel un boton para realizar movimientos aleatorios.
	 */
	public void addRandomButton() {
		jbRandom = new JButton("Random");
		jbRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlsListener.randomPressed();
			}
		});
		jbRandom.setToolTipText("<html>Make a random move");
		add(jbRandom);
	}

	/**
	 * Adds a button to the panel for making AI moves.
	 * <p>
	 * Añade al panel un boton para realizar movimientos inteligentes.
	 */
	public void addIntelligentButton() {
		jbIntelligent = new JButton("Intelligent");
		jbIntelligent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlsListener.aiPressed();
			}
		});
		jbIntelligent.setToolTipText("<html>Make an intelligent move");
		add(jbIntelligent);
	}

	@Override
	public void setEnabled(boolean b) {
		if (jbIntelligent != null) {
			jbIntelligent.setEnabled(b);
		}
		if (jbRandom != null) {
			jbRandom.setEnabled(b);
		}
	}

}
