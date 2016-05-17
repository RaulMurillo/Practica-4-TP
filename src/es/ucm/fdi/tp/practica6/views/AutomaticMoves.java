package es.ucm.fdi.tp.practica6.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
	 * A panel for aligning the buttons.
	 * <p>
	 * Panel para alinear los botones.
	 */
	private JPanel jpButtons;

	/**
	 * A panel for aligning timelimit components.
	 * <p>
	 * Panel para alinear los componentes de timelimit.
	 */
	private JPanel jpTimelimit;

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

		/**
		 * Sets the timelimit for teh AI moves.
		 * <p>
		 * Establece el limite de timepo para los movimientos inteligentes.
		 * 
		 * @param timeout
		 *            Timelimit.
		 */
		void setTimelimit(int timeout);
	}

	/**
	 * Creates a panel with ({@link JButton})s for making automatics move while
	 * playing MANUAL mode.
	 * <p>
	 * Crea un panel con botones ({@link JButton}) para realizar movimientos
	 * automaticos mientras se juega en modo MANUAL.
	 * 
	 * @param controlsListener
	 *            Listener of the class.
	 *            <p>
	 *            "Listener" de la clase.
	 */
	public AutomaticMoves(AutoMovesListener controlsListener) {
		this.controlsListener = controlsListener;
		setBorder(new TitledBorder(null, "Automatic Moves", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		jpButtons = new JPanel();
		add(jpButtons);
		jpTimelimit = new JPanel();
		add(jpTimelimit);
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
		jpButtons.add(jbRandom);
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
		jpButtons.add(jbIntelligent);
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

	/**
	 * Adds a spinner and a button to the panel for choosing AI moves timelimit.
	 * <p>
	 * Añade al panel un selector y un boton para realizar seleccionar limite de
	 * tiempo en los movimientos inteligentes.
	 */
	public void addTimelimit() {
		jpTimelimit.add(new JLabel("Timelimit (ms)"), BorderLayout.SOUTH);
		JSpinner jsTime = new JSpinner(new SpinnerNumberModel(2000, 100, 10000, 100));
		jsTime.setToolTipText("<html>Maximum number of milliseconds that the intelligent move can last");
		jpTimelimit.add(jsTime);
		JButton jbSet = new JButton("Set");
		jbSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlsListener.setTimelimit((int) jsTime.getValue());
			}
		});
		jbSet.setToolTipText("<html>Set the timelimit");
		jpTimelimit.add(jbSet);
	}

}
