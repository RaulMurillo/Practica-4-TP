package es.ucm.fdi.tp.practica5.views;

import java.awt.BorderLayout;
import java.awt.Color;
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
import es.ucm.fdi.tp.practica5.Utils;

public class BoardGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Square[][] squares;
	protected Board board;
	private Map<Piece, Color> colorMap;
	final public Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.WHITE);
	final public Border SELECTED_BORDER = BorderFactory.createLoweredBevelBorder();
	final public Color OBS_COLOR = Color.BLACK;
	final public String OBS_PATH = "8Block.png";
	final public String PIECE_PATH = "1Piece.png";
	private BufferedImage obsImage;
	private BufferedImage pieceImage;

	private BoardGUIListener controlsListener;

	public interface BoardGUIListener {

		void leftButtonPressed(int row, int col);

		void rightButtonPressed(int row, int col);
	}

	public class Square extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int row, col;

		public Square(int row, int col) {
			this.row = row;
			this.col = col;
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					squareWasClicked(Square.this.row, Square.this.col, e);
				}
			});
		}
	}

	public BoardGUI(Board b, Map<Piece, Color> colorMap, BoardGUIListener controlsListener) {
		this.controlsListener = controlsListener;
		this.colorMap = colorMap;
		try {
			pieceImage = ImageIO.read(new File(PIECE_PATH));
			obsImage = ImageIO.read(new File(OBS_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBoard(b);
	}

	public void setBoard(Board board) {
		removeAll(); // descartamos squares antiguos
		this.board = board;
		setLayout(new GridLayout(board.getRows(), board.getCols(), 1, 1));
		initSquares();
	}

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

	public void update() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Piece p = board.getPosition(i, j);
				if (p == null) { // Blanks in board
					squares[i][j].setIcon(null);
					squares[i][j].setBackground(null);
				} else if (colorMap.containsKey(p)) { // Paint pieces
					if (pieceImage != null) {
						Utils.setImageOnJLabel(squares[i][j], pieceImage);
					}
					squares[i][j].setBackground(colorMap.get(p));
				} else { // paint obstacles
					if (obsImage != null) {
						Utils.setImageOnJLabel(squares[i][j], obsImage);
					} else
						squares[i][j].setBackground(OBS_COLOR);
				}
			}
		}
		repaint(); // obligas a que se repinte el tablero
		revalidate();
	}

	public void selectSquare(int row, int col) {
		squares[row][col].setBorder(SELECTED_BORDER);
	}

	public void deselectSquare(int row, int col) {
		squares[row][col].setBorder(DEFAULT_BORDER);
	}

	private void squareWasClicked(int row, int col, MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			controlsListener.leftButtonPressed(row, col);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			controlsListener.rightButtonPressed(row, col);
		}
	}

	public void setMap(Map<Piece, Color> colorMap) {
		this.colorMap = colorMap;
	}

}
