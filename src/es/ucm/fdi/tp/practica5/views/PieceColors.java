package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A panel for changing pieces' color.
 * <p>
 * Panel para cambiar el color de las piezas.
 */
public class PieceColors extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A {@link JComboBox} with the pieces of the game.
	 * <p>
	 * Un {@link JComboBox} con las piezas del juego.
	 */
	private JComboBox<Piece> jcbPieces;

	/**
	 * The listener interface for sending action events.
	 * <p>
	 * Interfaz "listener" de la clase para enviar eventos.
	 */
	public interface PieceColorsListener {
		/**
		 * Notifies that the user wants to change the color of a piece.
		 * <p>
		 * Notifica que el usuario quere cambiar una pieza de color.
		 * 
		 * @param piece
		 *            Piece the user wants to change color.
		 *            <p>
		 *            Pieza que el usuario quiere cambiar de color.
		 * @param color
		 *            Color chosen for change the piece.
		 *            <p>
		 *            Color elegido para cambiar la pieza.
		 */
		void changeColorPressed(Piece piece, Color color);
	}

	/**
	 * Creates a panel that shows a {@link JColorChooser} for changing pieces
	 * color.
	 * <p>
	 * Crea un panel que muestra un {@link JColorChooser} para cambiar el color
	 * de las piezas.
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
	public PieceColors(List<Piece> pieces, PieceColorsListener controlsListener) {
		setBorder(new TitledBorder(null, "Piece Colors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new FlowLayout());
		initComboBox(pieces);
		initChangeButton(controlsListener);
	}

	/**
	 * Initializes the {@link JComboBox} with the pieces of the game.
	 * <p>
	 * Inicializa el {@link JComboBox} con las piezas del juego.
	 * 
	 * @param pieces
	 *            List of pieces in the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComboBox(List<Piece> pieces) {
		jcbPieces = new JComboBox(pieces.toArray());
		add(jcbPieces);
	}

	/**
	 * Initialize the button for changing the pieces' colors.
	 * <p>
	 * Inicializa el boton para cambiar el color de las piezas.
	 * 
	 * @param controlsListener
	 *            Listener of the class.
	 *            <p>
	 *            "Listener" de la clase.
	 */
	private void initChangeButton(PieceColorsListener controlsListener) {
		JButton jbChange = new JButton("Change Color");
		jbChange.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				Color c = JColorChooser.showDialog(PieceColors.this, "Color chooser", null);
				int pieceIndex = jcbPieces.getSelectedIndex();
				controlsListener.changeColorPressed(jcbPieces.getItemAt(pieceIndex), c);
			}
		});
		jbChange.setToolTipText("<html>Change the color of the selected pieces");
		add(jbChange);
	}
}
