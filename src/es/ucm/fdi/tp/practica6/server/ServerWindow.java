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

public class ServerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8202802850365314184L;

	private JTextArea jtaInfo;
	private JButton jbStop;

	/**
	 * The listener interface for sending action events.
	 * <p>
	 * Interfaz "listener" de la clase para enviar eventos.
	 */
	public interface WindowEventListener {
		void stopPressed();
	}

	public ServerWindow(WindowEventListener listener) {
		initialize(listener);
	}

	private void initialize(WindowEventListener listener) {
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setTitle("Server information");
		// Information Text Area
		initInfoArea();
		// Stop and Close Button
		initStopButton(listener);
		setVisible(true);
	}

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

	private void initStopButton(WindowEventListener listener) {
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
