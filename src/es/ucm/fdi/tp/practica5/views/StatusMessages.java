package es.ucm.fdi.tp.practica5.views;

import java.awt.Component;
import java.awt.event.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class StatusMessages extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea jtaScreen;
	private String message;
	private int counter;

	StatusMessages() {
		initUI();
	}

	public final void initUI() {
		setBorder(new TitledBorder(null, "Status Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		message = new String();
		counter = 1;
		jtaScreen = new JTextArea(message);
		jtaScreen.setEditable(false);

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
