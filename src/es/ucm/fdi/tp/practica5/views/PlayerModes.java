package es.ucm.fdi.tp.practica5.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A panel for changing players' game mode.
 * <p>
 * Panel para cambiar el modo de juego de los jugadores.
 */
public class PlayerModes extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Listener of the class.
	 * <p>
	 * "Listener" de la clase.
	 */
	private PlayerModesListener controlsListener;

	/**
	 * Button for confirm selection.
	 * <p>
	 * Boton de confirmacion de seleccion.
	 */
	private JButton jbSet;

	/**
	 * List with available game modes.
	 * <p>
	 * Listado con los modos de juego disponibles.
	 */
	private JComboBox<String> jcbMode;

	/**
	 * The listener interface for sending action events.
	 * <p>
	 * Interfaz "listener" de la clase para enviar eventos.
	 */
	public interface PlayerModesListener {
		void changeModePressed(Piece p, String mode);
	}

	/**
	 * Creates a panel that allows players change their game mode.
	 * <p>
	 * Crea un panel que permite a los jugadores cambiar su modo de juego.
	 * 
	 * @param pieces
	 *            List of pieces on the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 * @param controlsListener
	 *            Listener of the class.
	 *            <p>
	 *            "Listener" de la clase.
	 */
	public PlayerModes(List<Piece> pieces, PlayerModesListener controlsListener) {
		this.controlsListener = controlsListener;
		setBorder(new TitledBorder(null, "Player Modes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		initialize(pieces);
	}

	/**
	 * Initialize the contents of the panel.
	 * <p>
	 * Inicializa los contenidos del panel.
	 * 
	 * @param pieces
	 *            List of pieces in the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 */
	private void initialize(List<Piece> pieces) {
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
		jbSet.setToolTipText("<html>Change the player game mode as selected");
	}

	@Override
	public void setEnabled(boolean b) {
		jbSet.setEnabled(b);
	}

	/**
	 * Adds RANDOM and INTELLIGENT mode option to the {@link JComboBox}, if they
	 * are available.
	 * <p>
	 * Añade las opciones de modo RANDOM e INTELLIGENT al {@link JComboBox}, si
	 * están disponibles.
	 * 
	 * @param rand
	 *            Indicates if RANDOM game mode is available.
	 *            <p>
	 *            Indica si el modo de juego RANDOM esta disponible.
	 * @param ai
	 *            Indicates if INTELLIGENT game mode is available.
	 *            <p>
	 *            Indica si el modo de juego INTELLIGENT esta disponible.
	 */
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
