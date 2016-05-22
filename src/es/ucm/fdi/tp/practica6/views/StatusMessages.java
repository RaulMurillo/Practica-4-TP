package es.ucm.fdi.tp.practica6.views;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

/**
 * A panel for showing information about the game state, notifying players about
 * making move, errors, etc.
 * <p>
 * Panel para mostrar la información del estado del juego, pedir a cada jugador
 * que realice un movimiento, etc.
 */
public class StatusMessages extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7754166442554125979L;
	/**
	 * Area where messages are shown.
	 * <p>
	 * Area donde se muestran los mensajes.
	 */
	private JTextArea jtaScreen;

	/**
	 * Creates a panel for showing messages about the game.
	 * <p>
	 * Crea un panel para mostrar mensajes del juego.
	 */
	public StatusMessages() {
		setBorder(new TitledBorder(null, "Status Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initTextArea();
		initResetButton();
	}

	/**
	 * Initializes the {@JTextArea} where messages are shown.
	 * <p>
	 * Inicializa la {@JTextArea} donde se muestran los mensajes.
	 */
	private void initTextArea() {
		jtaScreen = new JTextArea();
		jtaScreen.setEditable(false);
		jtaScreen.setRows(4);
		// Set AutoScroll
		DefaultCaret caret = (DefaultCaret) jtaScreen.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane jspScroll = new JScrollPane(jtaScreen, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jspScroll);
	}

	/**
	 * Initializes a {@JButton} to clear the content from the {@JTextArea}.
	 * <p>
	 * Inicializa un {@JButton} para borrar el contenido de la {@JTextArea}.
	 */
	private void initResetButton() {
		JButton jbReset = new JButton("Reset");
		jbReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Reset Action
				jtaScreen.setText(null);
			}
		});
		jbReset.setAlignmentX(Component.CENTER_ALIGNMENT);
		jbReset.setToolTipText("<html>Clear the panel from messages");
		add(jbReset);
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
		jtaScreen.append(message + "\n");
	}
}
