package es.ucm.fdi.tp.practica6.views;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A panel for showing the players' information.
 * <p>
 * Panel para mostrar la informacion de los jugadores.
 */
public class PlayerInformation extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3287961108657319461L;

	/**
	 * Table where the information is shown.
	 * <p>
	 * Tabla donde se muestra la informacion.
	 */
	private JTable jtTable;

	/**
	 * A double array with the information to show in the table. Different rows
	 * and columns indicates different players and fields.
	 * <p>
	 * Array doble con la informacion que se muestra en la tabla. Los jugadores
	 * se ordenan por filas y los campos por columnas.
	 */
	private Object[][] data;

	/**
	 * A map that associates pieces with colors.
	 * <p>
	 * Mapa que asocia fichas con colores.
	 */
	private Map<Piece, Color> colorMap;

	/**
	 * Creates a panel with a {@link JTable} that shows the game mode and the
	 * number of pieces of each player. Each row is painted in same color as
	 * pieces in the board.
	 * <p>
	 * Crea un panel con un {@link JTable} que muestra el modo de juego y el
	 * numero de piezas de cada jugador. Cada fila es pintada en el mismo color
	 * que las piezas en el tablero.
	 * 
	 * @param board
	 *            Internal board of the game.
	 *            <p>
	 *            Tablero interno del juego.
	 * @param pieces
	 *            List of pieces on the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 * @param colorMap
	 *            Map that indicates the color of the pieces in the board.
	 *            <p>
	 *            Mapa que indica los colores de las piezas del tablero.
	 * @param viewPiece
	 *            The piece to which this view belongs ({@code null} means that
	 *            it belongs to all pieces).
	 *            <p>
	 *            La ficha a la que pertenece la vista ({@code null} si la vista
	 *            pertenece a todos los jugadores).
	 */
	public PlayerInformation(Board board, List<Piece> pieces, Map<Piece, Color> colorMap, Piece viewPiece) {
		setBorder(new TitledBorder(null, "Player Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		updateColors(colorMap);
		initializeTable(pieces);
		updateNumPieces(pieces, board);
		for (int i = 0; i < pieces.size(); i++) {
			if (viewPiece == null || viewPiece.equals(pieces.get(i))) {
				updateMode(i, "Manual");
			}
		}
	}

	/**
	 * Initialize the table.
	 * <p>
	 * Inicializa la tabla.
	 * 
	 * @param pieces
	 *            List of pieces in the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 */
	private void initializeTable(List<Piece> pieces) {
		data = new Object[pieces.size()][3];
		setPieces(pieces);
		String[] columnNames = { "Player", "Mode", "#Pieces" };
		jtTable = new JTable(data, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Table cels ARE NOT editable.
			}
		};

		jtTable.setPreferredScrollableViewportSize(jtTable.getPreferredSize());
		jtTable.setFillsViewportHeight(true); // Fills the empty space with
												// white
		// Paint table rows
		jtTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int col) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
				Color rowColor = colorMap.get(pieces.get(row));
				setBackground(rowColor);
				setForeground(getContrastColor(rowColor));
				return this;
			}
		});

		JScrollPane jspScroll = new JScrollPane(jtTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(jspScroll);
	}

	/**
	 * Sets the table's pieces' field.
	 * <p>
	 * Establece el campo/columna de piezas de la tabla.
	 * 
	 * @param pieces
	 *            List of pieces in the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 */
	private void setPieces(List<Piece> pieces) {
		for (int j = 0; j < pieces.size(); j++) {
			data[j][0] = pieces.get(j);
		}
	}

	/**
	 * Updates the pieces-colors map for painting rows.
	 * <p>
	 * Actualiza el mapa de piezas-colores para pintar filas.
	 * 
	 * @param colorMap
	 *            New pieces-colors map.
	 *            <p>
	 *            Nuevo mapa de piezas-colores.
	 */
	public void updateColors(Map<Piece, Color> colorMap) {
		this.colorMap = colorMap;
		repaint();
	}

	/**
	 * Updates a player's game mode in the table.
	 * <p>
	 * Actualiza el modo de juego de un jugador en la tabla.
	 * 
	 * @param player
	 *            Number of table row where the player to change mode is.
	 *            <p>
	 *            Numero de fila de la tabla en la que el jugador en cuestion se
	 *            encuentra.
	 * @param mode
	 *            New game mode of the player.
	 *            <p>
	 *            Nuevo modo de juego del jugador.
	 */
	public void updateMode(int player, String mode) {
		data[player][1] = mode;
		repaint();
	}

	/**
	 * Updates the number of pieces on board of all the players in game.
	 * <p>
	 * Actualiza el numero de piezas en el tablero de todos los jugadores.
	 * 
	 * @param pieces
	 *            List of pieces in the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 * @param board
	 *            Board of the game.
	 *            <p>
	 *            Tablero del juego.
	 */
	public void updateNumPieces(List<Piece> pieces, Board board) {
		for (int j = 0; j < pieces.size(); j++) {
			if (board.getPieceCount(pieces.get(j)) != null) {
				data[j][2] = board.getPieceCount(pieces.get(j));
			}
		}
		repaint();
	}

	/**
	 * Generates a contrast color by converting the RGB values into YIQ values.
	 * <p>
	 * Genera un color de contraste para un color dado.
	 * 
	 * @param color
	 *            Color to generate contrast.
	 *            <p>
	 *            Color a contrastar.
	 * @return Opposite color in RGB.
	 *         <p>
	 *         Color de contraste respecto al codigo RGB.
	 */
	private Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}
}
