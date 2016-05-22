package es.ucm.fdi.tp.practica6.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A panel for quit or restart the game.
 * <p>
 * Panel para abandonar o reiniciar el juego.
 */
public class QuitPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7557239787399722655L;

	/**
	 * Button for quit the game.
	 * <p>
	 * Boton para salir el juego.
	 */
	private JButton jbQuit;

	/**
	 * Button for restart the game.
	 * <p>
	 * Boton para reiniciar el juego.
	 */
	private JButton jbRestart;

	/**
	 * Listener of the class.
	 * <p>
	 * "Listener" de la clase.
	 */
	private QuitPanelListener controlsListener;

	/**
	 * The listener interface for sending action events.
	 * <p>
	 * Interfaz "listener" de la clase para enviar eventos.
	 */
	public interface QuitPanelListener {

		/**
		 * Notifies that Quit button has been pressed.
		 * <p>
		 * Notifica que el boton de abandono ha sido pulsado.
		 */
		void quitPressed();

		/**
		 * Notifies that Restart button has been pressed.
		 * <p>
		 * Notifica que el boton de reinicio ha sido pulsado.
		 */
		void restartPressed();
	}

	/**
	 * Creates a panel with one (or two) {@link JButton} for quit (or restart)
	 * the game.
	 * <p>
	 * Crea un panel con un (o dos) {@link JButton} para salir (o reiniciar) el
	 * 
	 * @param controlsListener
	 *            Listener of the class.
	 *            <p>
	 *            "Listener" de la clase.
	 */
	public QuitPanel(QuitPanelListener controlsListener) {
		this.controlsListener = controlsListener;
		initialize();
	}

	/**
	 * Initialize the default contents of the panel.
	 * <p>
	 * Inicializa los contenidos predeterminados del panel.
	 */
	private void initialize() {
		jbQuit = new JButton("Quit");
		jbQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlsListener.quitPressed();
			}
		});
		jbQuit.setToolTipText("<html>Quit the game");
		add(jbQuit);
	}

	/**
	 * Sets the Restart {@link JButton} in the panel.
	 * <p>
	 * Establece el boton de reinicio en el panel.
	 */
	public void setRestartButton() {
		jbRestart = new JButton("Restart");
		jbRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlsListener.restartPressed();
			}
		});
		jbRestart.setToolTipText("<html>Restart the game");
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
