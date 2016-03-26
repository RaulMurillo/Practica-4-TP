package es.ucm.fdi.tp.practica5;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;

public class PieceColors extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JComboBox comboBox;
	private JButton btnChooseColor;
	private JColorChooser tcc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					PieceColors window = new PieceColors();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PieceColors() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, "Player Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JLabel banner = new JLabel("Color change here", JLabel.CENTER);
		banner.setPreferredSize(new Dimension(200, 65));
		panel.add(banner);

		tcc = new JColorChooser(banner.getForeground());

		String[] pieceStrings = getPieceStrings(/* pieces */);
		comboBox = new JComboBox(pieceStrings);
		panel.add(comboBox);

		btnChooseColor = new JButton("Choose Color");
		btnChooseColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Choose color for piece type " + comboBox.getSelectedIndex());
				JFrame frame = new JFrame("Set Piece Color");
				// El JDialog no es modal (debería?).
				// El JDialog debería estar asociado al JFrame desde el que se activa.
				// Hacerlo en una clase distinta (?)
				JDialog dialog = new JDialog(frame, "Set Piece Color");
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				dialog.setSize(500, 500);
				dialog.setLocationRelativeTo(null);
				
				// tcc.getSelectionModel().addChangeListener(this);
				// tcc.setBorder(BorderFactory.createTitledBorder("Choose Text
				// Color"));
				dialog.add(tcc, BorderLayout.CENTER);

				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
				dialog.add(buttonPane, BorderLayout.SOUTH);
				{
					JButton okButton = new JButton("Ok");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// aquí van las acciones al hacer click en Vale
							// envía el diálogo al recolector de basura de Java
							Color newColor = tcc.getColor();
							banner.setForeground(newColor);
							tcc = new JColorChooser(banner.getForeground());
							dialog.dispose();
						}
					});
					okButton.setActionCommand("Ok");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("Cancel");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							// aquí van las acciones al hacer click en Vale
							// envía el diálogo al recolector de basura de Java
							dialog.dispose();
						}
					});
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}

			}
		});
		panel.add(btnChooseColor);

		getContentPane().add(panel);
		setSize(350, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private String[] getPieceStrings(/* List<Piece> pieces */) {
		String[] p = new String[3/* pieces.size() */];
		for (int i = 0; i < p.length; i++) {
			p[i] = "Pieza " + i; // pieces.get(j);
		}
		return p;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
