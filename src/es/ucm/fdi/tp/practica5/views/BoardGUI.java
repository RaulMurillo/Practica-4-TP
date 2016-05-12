package es.ucm.fdi.tp.practica5.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * This class implements a graphic board of the game.
 * <p>
 * Esta clase implenta un tablero grafico del juego.
 */
public class BoardGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Matrix of squares ({@link JLabel}s) that represents the cells of the
	 * board.
	 * <p>
	 * Matriz de squares ({@link JLabel}s) que representa las celdas del
	 * tablero.
	 */
	protected Square[][] squares;

	/**
	 * Internal board of the game.
	 * <p>
	 * Tablero interno del juego.
	 */
	protected Board board;

	/**
	 * A map that associates pieces with colors.
	 * <p>
	 * Mapa que asocia fichas con colores.
	 */
	private Map<Piece, Color> colorMap;

	/**
	 * Default border for the board cells.
	 * <p>
	 * Borde por defecto para las celdas del tablero.
	 */
	final public Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.WHITE, 2);

	/**
	 * Border for the selected cells.
	 * <p>
	 * Borde para las celdas selccionadas.
	 */
	final public Border SELECTED_BORDER = BorderFactory.createLoweredBevelBorder();

	/**
	 * Default color for the obstacles in the board.
	 * <p>
	 * Color por defecto para los obstaculos del tablero.
	 */
	final public Color OBS_COLOR = Color.BLACK;

	/**
	 * Path of the obstacles image.
	 * <p>
	 * Ruta de la imagen de los obstaculos.
	 */
	final public String OBS_PATH = "Block.png";

	/**
	 * Path of the pieces image.
	 * <p>
	 * Ruta de la imagen de las piezas.
	 */
	final public String PIECE_PATH = "Piece.png";

	/**
	 * Image for the obstacles.
	 * <p>
	 * Imagen para los obstaculos.
	 */
	private BufferedImage obsImage;

	/**
	 * Image for the pieces.
	 * <p>
	 * Imagen para las piezas.
	 */
	private BufferedImage pieceImage;

	/**
	 * Listener of the class.
	 * <p>
	 * "Listener" de la clase.
	 */
	private BoardGUIListener controlsListener;

	/**
	 * The listener interface for sending action events.
	 * <p>
	 * Interfaz "listener" de la clase para enviar eventos.
	 */
	public interface BoardGUIListener {

		/**
		 * Notifies left button of the mouse was pressed in a square.
		 * <p>
		 * Notifica que el boton izquierdo del mouse fue pulsado en una celda.
		 * 
		 * @param row
		 *            Row of the square where mouse was clicked.
		 *            <p>
		 *            Fila de la celda en la que el mouse fue pulsado.
		 * @param col
		 *            Column of the square where mouse was clicked.
		 *            <p>
		 *            Columna de la celda en la que el mouse fue pulsado.
		 */
		void leftButtonPressed(int row, int col);

		/**
		 * Notifies right button of the mouse was pressed in a square.
		 * <p>
		 * Notifica que el boton derecho del mouse fue pulsado en una celda.
		 * 
		 * @param row
		 *            Row of the square where mouse was clicked.
		 *            <p>
		 *            Fila de la celda en la que el mouse fue pulsado.
		 * @param col
		 *            Column of the square where mouse was clicked.
		 *            <p>
		 *            Columna de la celda en la que el mouse fue pulsado.
		 */
		void rightButtonPressed(int row, int col);
	}

	/**
	 * Class that represents a cell of the board.
	 * <p>
	 * Clase que representa una celda del tablero.
	 */
	public class Square extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Vertical position of the square.
		 * <p>
		 * Posicion vertical del square.
		 */
		private int row;

		/**
		 * Horizontal position of the square.
		 * <p>
		 * Posicion horizontal del square.
		 */
		private int col;

		/**
		 * Indicates the square contains an obstacle.
		 * <p>
		 * Indica que el square continene un obstaculo.
		 */
		private boolean obstacle;

		/**
		 * Indicates the square contains nothing.
		 * <p>
		 * Indica que el square no contiene nada.
		 */
		private boolean empty;

		/**
		 * Creates a square in the indicated position.
		 * 
		 * @param row
		 *            Vertical position of the square.
		 *            <p>
		 *            Posicion vertical del square.
		 * @param col
		 *            Horizontal position of the square.
		 *            <p>
		 *            Posicion horizontal del square.
		 */
		public Square(int row, int col) {
			this.row = row;
			this.col = col;
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					squareWasClicked(Square.this.row, Square.this.col, e);
				}
			});
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (!empty) {
				g.drawImage(obstacle ? obsImage : pieceImage, 2, 2, getWidth() - 4, getHeight() - 4, this);
			}
		}
	}

	/**
	 * Creates a graphic board according to a given board and a colorMap.
	 * <p>
	 * Crea un tablero grafico en funcion de un tablero dado y un mapa de
	 * coloreado.
	 * 
	 * @param board
	 *            Internal board of the game.
	 *            <p>
	 *            Tablero interno del juego.
	 * @param colorMap
	 *            Map that indicates the color of the pieces in the board.
	 *            <p>
	 *            Mapa que indica los colores de las piezas del tablero.
	 * @param controlsListener
	 *            Listener of the board.
	 *            <p>
	 *            "Listener" del tablero.
	 */
	public BoardGUI(Board board, Map<Piece, Color> colorMap, BoardGUIListener controlsListener) {
		this.controlsListener = controlsListener;
		this.colorMap = colorMap;
		try {
			pieceImage = ImageIO.read(new File(PIECE_PATH));
			obsImage = ImageIO.read(new File(OBS_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBoard(board);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createLoweredBevelBorder()));
	}

	/**
	 * Sets a graphic board from a game board.
	 * <p>
	 * Establece un tablero grafico a partir de un tablero de juego.
	 * 
	 * @param board
	 *            Board of the game.
	 *            <p>
	 *            Tablero del juego.
	 */
	public void setBoard(Board board) {
		removeAll(); // Discard previous squares
		this.board = board;
		setLayout(new GridLayout(board.getRows(), board.getCols()));
		initSquares();
		update();
	}

	/**
	 * Initialize the squares of the board.
	 * <p>
	 * Inicializa los squares del tablero.
	 */
	public void initSquares() {
		squares = new Square[board.getRows()][board.getCols()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				squares[i][j] = new Square(i, j);
				squares[i][j].setOpaque(true);
				squares[i][j].setBorder(DEFAULT_BORDER);
				squares[i][j].setLayout(new BorderLayout());
				add(squares[i][j]);
			}
		}
	}

	/**
	 * Updates the graphic board.
	 * <p>
	 * Actualiza el tablero grafico.
	 */
	public void update() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Piece p = board.getPosition(i, j);
				if (p == null) { // Blanks in board
					squares[i][j].setIcon(null);
					squares[i][j].setBackground(null);
					squares[i][j].empty = true;
				} else if (colorMap.containsKey(p)) { // Paint pieces
					squares[i][j].setBackground(colorMap.get(p));
					squares[i][j].obstacle = false;
					squares[i][j].empty = false;
				} else { // Paint obstacles
					squares[i][j].obstacle = true;
					squares[i][j].empty = false;
					if (obsImage == null) {
						squares[i][j].setBackground(OBS_COLOR);
					}
				}
			}
		}
		repaint(); // Repaint the board
		revalidate();
	}

	/**
	 * Selects a square of the board.
	 * <p>
	 * Selecciona un square del board.
	 * 
	 * @param row
	 *            Row of the square.
	 *            <p>
	 *            Fila del square.
	 * @param col
	 *            Column of the square.
	 *            <p>
	 *            Columna del square.
	 */
	public void selectSquare(int row, int col) {
		squares[row][col].setBorder(SELECTED_BORDER);
	}

	/**
	 * Deselects a square of the board.
	 * <p>
	 * Deselecciona un square del board.
	 * 
	 * @param row
	 *            Row of the square.
	 *            <p>
	 *            Fila del square.
	 * @param col
	 *            Column of the square.
	 *            <p>
	 *            Columna del square.
	 */
	public void deselectSquare(int row, int col) {
		squares[row][col].setBorder(DEFAULT_BORDER);
	}

	/**
	 * Calls the listener according to the mouse button and square clicked.
	 * <p>
	 * Llama al "listener" de acuerdo con el boton del mouse y el square
	 * pulsados.
	 * 
	 * @param row
	 *            Row of the square.
	 *            <p>
	 *            Fila del square.
	 * @param col
	 *            Column of the square.
	 *            <p>
	 *            Columna del square.
	 * @param e
	 *            Click event.
	 *            <p>
	 *            Evento de click.
	 */
	private void squareWasClicked(int row, int col, MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			controlsListener.leftButtonPressed(row, col);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			controlsListener.rightButtonPressed(row, col);
		}
	}

	/**
	 * Sets the pieces-colors map.
	 * <p>
	 * establece el mapa de piezas-colores.
	 * 
	 * @param colorMap
	 *            Map to set.
	 *            <p>
	 *            Mapa a establecer.
	 */
	public void setMap(Map<Piece, Color> colorMap) {
		this.colorMap = colorMap;
	}
	

}
