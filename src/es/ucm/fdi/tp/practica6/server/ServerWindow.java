package es.ucm.fdi.tp.practica6.server;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * Class to create a graphic window for showing messages.
 * <p>
 * Clase para crear una ventan grafica para mostrar mensajes.
 * 
 * @author Raúl Murillo & Antonio Valdivia
 *
 */
public class ServerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8202802850365314184L;

	/**
	 * The area for showing the messages.
	 * <p>
	 * El area para mostral los mensajes.
	 */
	private JTextArea jtaInfo;

	/**
	 * Stop button.
	 * <p>
	 * Boton de stop.
	 */
	private JButton jbStop;

	/**
	 * The listener interface for sending action events.
	 * <p>
	 * Interfaz "listener" de la clase para enviar eventos.
	 */
	public interface WindowEventListener {

		/**
		 * Indicates that the {@code jbStop} has been pressed.
		 * <p>
		 * Indica que el boton {@code jbStop} ha sido presionado.
		 */
		void stopPressed();
	}

	/**
	 * Constructor of the class.
	 * <p>
	 * Constructor de la clase.
	 * 
	 * @param listener
	 *            Listener of the class.
	 *            <p>
	 *            Listener de la clase.
	 */
	public ServerWindow(WindowEventListener listener) {
		initialize(listener);
	}

	/**
	 * Creates a graphic window for showing messages. Also includes a Stop
	 * button.
	 * <p>
	 * Crea una ventana grafica para mostrar mensajes. Tambien incluye un boton
	 * de Stop.
	 * 
	 * @param listener
	 *            Listener of the class.
	 *            <p>
	 *            Listener de la clase.
	 */
	private void initialize(WindowEventListener listener) {
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setTitle("Server information");
		// Information Text Area
		initInfoArea();
		// Stop and Close Button
		initStopButton(listener);
		setVisible(true);
	}

	/**
	 * Initializes the {@link jtaInfo}.
	 * <p>
	 * Inicializa el {@link jtaInfo}.
	 */
	private void initInfoArea() {
		jtaInfo = new JTextArea();
		jtaInfo.setEditable(false);
		jtaInfo.setRows(10);
		// Set AutoScroll
		DefaultCaret caret = (DefaultCaret) jtaInfo.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane jspScroll = new JScrollPane(jtaInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jspScroll, BorderLayout.CENTER);
	}

	/**
	 * Initializes the {@link jbStop}.
	 * <p>
	 * Inicializa el {@link jbStop}.
	 */
	private void initStopButton(final WindowEventListener listener) {
		jbStop = new JButton("STOP");
		jbStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Stop Action
				listener.stopPressed();
			}
		});
		jbStop.setAlignmentX(Component.CENTER_ALIGNMENT);
		jbStop.setToolTipText("<html>Stop the server and close the application");
		add(jbStop, BorderLayout.SOUTH);
	}

	/**
	 * Shows a message in the {@link JTextArea}.
	 * <p>
	 * Muestra un mensaje en la {@link JTextArea}.
	 * 
	 * @param message
	 *            Text to show.
	 *            <p>
	 *            Texto a mostrar.
	 */
	public void showMessage(String message) {
		jtaInfo.append(message + "\n");
	}

	/**
	 * Enable or disable the Stop button.
	 * <p>
	 * Habilita o desabilita el boton de Stop.
	 * 
	 * @param b
	 *            {@code true} for enable button. {@code false} for disable.
	 *            <p>
	 *            {@code true} para habilitar el boton. {@code false} para
	 *            deshabilitar.
	 */
	public void enableButton(boolean b) {
		jbStop.setEnabled(b);
	}
}
