package es.ucm.fdi.tp.practica5;

import java.awt.Component;
import java.awt.event.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class StatusMessages extends JFrame{

	private static final long serialVersionUID = 1L;

	private JTextArea textArea;
	private String model;
	private int counter;

	StatusMessages() {
		initUI();
	}

	public final void initUI() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Status Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		model = new String();
		counter = 1;
		textArea = new JTextArea(model);
		textArea.setEditable(false);

		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scroll);

		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				///////
				Locale locale = Locale.getDefault();
				Date date = new Date(e.getWhen());
				String s = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(date);

				if (!model.isEmpty()) {
					model = "-----------------------\n";

				}

				if (e.getID() == ActionEvent.ACTION_PERFORMED) {
					model += " Event ID: TEST MESSAGE " + counter + "\n";
				}

				counter++;
				model += " Time: " + s + "\n";

				String source = e.getSource().getClass().getName();
				model += " Source: " + source + "\n";

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

				model += buffer + "\n";

				showMessage(model);
			}
		});

		okButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Reset Action
				textArea.setText(null);
				model = "";
			}
		});
		resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel.add(okButton);
		panel.add(resetButton);
		getContentPane().add(panel);

		setTitle("Event example");
		setSize(239, 171);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					StatusMessages ex = new StatusMessages();
					ex.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void showMessage(String message) {
		textArea.append(message);
	}
}
