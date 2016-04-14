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

		JButton jbWrite = new JButton("Write");
		jbWrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				///////
				Locale locale = Locale.getDefault();
				Date date = new Date(e.getWhen());
				String s = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(date);

				if (!message.isEmpty()) {
					message = "-----------------------\n";

				}

				if (e.getID() == ActionEvent.ACTION_PERFORMED) {
					message += " Event ID: TEST MESSAGE " + counter + "\n";
				}

				counter++;
				message += " Time: " + s + "\n";

				String source = e.getSource().getClass().getName();
				message += " Source: " + source + "\n";

				int mod = e.getModifiers();

				StringBuffer buffer = new StringBuffer(" Modifiers: ");

				if ((mod & ActionEvent.ALT_MASK) > 0) {
					buffer.append("Shift ");
				}
				if ((mod & ActionEvent.META_MASK) > 0) {
					buffer.append("Meta ");
				}
				if ((mod & ActionEvent.CTRL_MASK) > 0) {
					buffer.append("Ctrl ");
				}

				message += buffer;

				showMessage(message);
			}
		});

		jbWrite.setAlignmentX(Component.CENTER_ALIGNMENT);

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

		add(jbWrite);
		add(jbReset);
	}

	public void showMessage(String message) {
		jtaScreen.append(message + "\n");
	}
}
