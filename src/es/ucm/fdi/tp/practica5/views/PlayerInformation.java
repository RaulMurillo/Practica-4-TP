package es.ucm.fdi.tp.practica5.views;

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
	private static final long serialVersionUID = 1L;

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

	
	public PlayerInformation(Board b, List<Piece> p, Map<Piece, Color> map, Piece viewPiece) {
		updateColors(map);
		initializeTable(p);
		updateNumPieces(p, b);
		for (int i = 0; i < p.size(); i++) {
			if (viewPiece == null || viewPiece.equals(p.get(i)))
				updateMode(i, "Manual");
		}
	}

	/**
	 * Initialize the table and the contents .
	 * <p>
	 * Inicializa la tabla y sus contenidos.
	 * 
	 * @param pieces
	 *            List of pieces in the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 */
	private void initializeTable(List<Piece> pieces) {
		this.setBorder(
				new TitledBorder(null, "Player Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		data = new Object[pieces.size()][3];
		setPieces(pieces);
		String[] columnNames = { "Player", "Mode", "#Pieces" };
		jtTable = new JTable(data, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
	 * Initialize the table row data according to the number of players.
	 * 
	 * @return
	 */
	private void setPieces(List<Piece> pieces) {
		for (int j = 0; j < pieces.size(); j++) {
			data[j][0] = pieces.get(j);
		}
	}

	public void updateColors(Map<Piece, Color> map) {
		colorMap = map;
		repaint();
	}

	public void updateMode(int p, String mode) {
		data[p][1] = mode;
		repaint();
	}

	public void updateNumPieces(List<Piece> pieces, Board board) {
		for (int j = 0; j < pieces.size(); j++) {
			if (board.getPieceCount(pieces.get(j)) != null)
				data[j][2] = board.getPieceCount(pieces.get(j));
		}
		repaint();
	}

	/**
	 * Generates the opposite color by converting the RGB values into YIQ
	 * values.
	 * 
	 * @param color
	 *            Color to generate opposite.
	 * @return Opposite color in RGB.
	 */
	public Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}
}
