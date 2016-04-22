package es.ucm.fdi.tp.practica5.views;

import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class PlayerModes extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PlayerModesListener controlsListener;
	private JButton jbSet;
	private JComboBox<String> jcbMode;

	public interface PlayerModesListener {
		void changeModePressed(Piece p, String mode);
	}

	public PlayerModes(List<Piece> pieces, PlayerModesListener controlsListener) {
		this.controlsListener = controlsListener;
		initialize(pieces);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(List<Piece> pieces) {
		setBorder(new TitledBorder(null, "Player Modes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		@SuppressWarnings({ "unchecked", "rawtypes" })
		JComboBox<Piece> jcbPlayer = new JComboBox(pieces.toArray());
		add(jcbPlayer);
		jbSet = new JButton("Set");
		jbSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int playerIndex = jcbPlayer.getSelectedIndex();
				int modeIndex = jcbMode.getSelectedIndex();
				controlsListener.changeModePressed(jcbPlayer.getItemAt(playerIndex), jcbMode.getItemAt(modeIndex));
				;
			}
		});
	}

	public void setEnabled(boolean b) {
		jbSet.setEnabled(b);
	}

	public void addModes(boolean rand, boolean ai) {
		if (!rand || !ai) {
			if (!ai) {
				String[] modesStrings = { "Manual", "Random" };
				jcbMode = new JComboBox<String>(modesStrings);
			} else {
				String[] modesStrings = { "Manual", "Intelligent" };
				jcbMode = new JComboBox<String>(modesStrings);
			}
		} else {
			String[] modesStrings = { "Manual", "Random", "Intelligent" };
			jcbMode = new JComboBox<String>(modesStrings);
		}
		add(jcbMode);
		add(jbSet);
	}

}
