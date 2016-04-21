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

public class BoardGUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Square[][] squares;
	protected Board board;
	private Map<Piece, Color> colorMap;
	final public Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.WHITE, 2);
	final public Border SELECTED_BORDER = BorderFactory.createLoweredBevelBorder();
	final public Color OBS_COLOR = Color.BLACK;
	final public String OBS_PATH = "Block.png";
	final public String PIECE_PATH = "Piece.png";
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
		private boolean obstacle;
		private boolean empty;

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
		removeAll(); // discard previous squares
		this.board = board;
		setLayout(new GridLayout(board.getRows(), board.getCols()));
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
					squares[i][j].empty = true;
				} else if (colorMap.containsKey(p)) { // Paint pieces
					if (pieceImage != null) {
						// squares[i][j].setIcon(pieceImage);
					}
					squares[i][j].setBackground(colorMap.get(p));
					squares[i][j].obstacle = false;
					squares[i][j].empty = false;
				} else { // paint obstacles
					squares[i][j].obstacle = true;
					squares[i][j].empty = false;
					if (obsImage != null) {
						// Utils.setImageOnJLabel(squares[i][j], obsImage);
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
