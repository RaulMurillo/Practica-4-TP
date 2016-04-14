package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxRules;
import es.ucm.fdi.tp.practica5.control.SwingController;
import es.ucm.fdi.tp.practica5.views.PieceColorChooser.PieceColorsListener;

public class BoardGUI extends JPanel {

	protected JLabel[][] squares;
	protected Board board;
	private Map<Piece, Color> colorMap;
	final public Color DEFAULT_BORDER = Color.white;

	private BoardGUIListener controlsListener;

	public interface BoardGUIListener {
		boolean leftButtonPressed(int row, int col);

		void rightButtonPressed(int row, int col);
	}

	public BoardGUI(Board b, Map<Piece, Color> color) {
		setBoard(b);
		colorMap = color;
		update();
	}

	public void setBoard(Board board) {
		removeAll(); // descartamos squares antiguos
		this.board = board;
		setLayout(new GridLayout(board.getRows(), board.getCols(), 1, 1));
		initSquares();
	}

	public void initSquares() {
		squares = new JLabel[board.getRows()][board.getCols()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				squares[i][j] = new JLabel();
				squares[i][j].setOpaque(true);
				squares[i][j].setBorder(BorderFactory.createLineBorder(DEFAULT_BORDER));
				final int col = i;
				final int row = j;
				squares[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (me.getButton() == MouseEvent.BUTTON1) {
							if (controlsListener.leftButtonPressed(col, row)) {
								selectSquare(col, row);
							}
						} else if (me.getButton() == MouseEvent.BUTTON3) {
							controlsListener.rightButtonPressed(col, row);
							deselectSquare(col, row);
						}
					}
				});
				add(squares[i][j]);
			}
		}
	}

	public void update() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Piece p = board.getPosition(i, j);
				if (p != null)
					squares[i][j].setBackground(colorMap.get(p));
			}
		}
		repaint(); // obligas a que se repinte el tablero (ahora que
					// pintar lo que debe)
	}

	public void selectSquare(int col, int row) {
		squares[col][row].setBorder(BorderFactory.createLineBorder(Color.red));
	}

	public void deselectSquare(int col, int row) {
		squares[col][row].setBorder(BorderFactory.createLineBorder(DEFAULT_BORDER));
	}

}
