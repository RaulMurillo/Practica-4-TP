package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

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
	final public Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.white);
	final public Border SELECTED_BORDER = BorderFactory.createLoweredBevelBorder();// createLineBorder(Color.red)
	final public Color OBS_COLOR = Color.getHSBColor(25, 25, 0);

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

	public BoardGUI(Board b, Map<Piece, Color> color, BoardGUIListener controlsListener) {
		this.controlsListener = controlsListener;
		colorMap = color;
		setBoard(b);
	}

	public void setBoard(Board board) {
		removeAll(); // descartamos squares antiguos
		this.board = board;
		setLayout(new GridLayout(board.getRows(), board.getCols(), 1, 1));
		initSquares();
		update();
	}

	public void initSquares() {
		squares = new Square[board.getRows()][board.getCols()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				squares[i][j] = new Square(i, j);
				squares[i][j].setOpaque(true);
				squares[i][j].setBorder(DEFAULT_BORDER);
				add(squares[i][j]);
			}
		}
	}

	public void update() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Piece p = board.getPosition(i, j);
				if (p == null) {
					squares[i][j].setBackground(null);
				} else if (colorMap.containsKey(p)) {
					squares[i][j].setBackground(colorMap.get(p));
				} else { // paint obstacles
					squares[i][j].setBackground(OBS_COLOR);
				}
			}
		}
		repaint(); // obligas a que se repinte el tablero
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
	public void setMap(Map<Piece, Color> colorMap){
		
		this.colorMap = colorMap;
	}
}
