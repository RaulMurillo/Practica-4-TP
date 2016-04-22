package es.ucm.fdi.tp.practica5.views;

import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

public class StatusMessages extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea jtaScreen;
	private String message;

	public StatusMessages() {
		setBorder(new TitledBorder(null, "Status Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		message = new String();
		jtaScreen = new JTextArea(message);
		jtaScreen.setEditable(false);
		jtaScreen.setRows(4);
		// AutoScroll
		DefaultCaret caret = (DefaultCaret) jtaScreen.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane jspScroll = new JScrollPane(jtaScreen, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jspScroll);

		JButton jbReset = new JButton("Reset");
		jbReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Reset Action
				jtaScreen.setText(null);
				message = "";
			}
		});
		jbReset.setAlignmentX(Component.CENTER_ALIGNMENT);

		add(jbReset);
	}

	public void showMessage(String message) {
		jtaScreen.append(message + "\n");
	}
}
