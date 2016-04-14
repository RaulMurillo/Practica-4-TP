package es.ucm.fdi.tp.practica5.views;

import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class PlayerModes extends JPanel {

	private PlayerModesListener controlsListener;

	public interface PlayerModesListener {
		void changeModePressed(Piece p, String mode);
	}

	public PlayerModes(List<Piece> pieces) {
		initialize(pieces);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(List<Piece> pieces) {
		setBorder(new TitledBorder(null, "Player Modes", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JComboBox<Piece> jcbPlayer = new JComboBox(pieces.toArray());
		add(jcbPlayer);

		String[] modesStrings = { "Manual", "Random", "Intelligent" };
		JComboBox<String> jcbMode = new JComboBox<String>(modesStrings);
		add(jcbMode);

		JButton jbSet = new JButton("Set");
		jbSet.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				int playerIndex = jcbPlayer.getSelectedIndex();
				int modeIndex = jcbMode.getSelectedIndex();
				controlsListener.changeModePressed(jcbPlayer.getItemAt(playerIndex), jcbMode.getItemAt(modeIndex));
				;
			}
		});
		add(jbSet);
	}

}
