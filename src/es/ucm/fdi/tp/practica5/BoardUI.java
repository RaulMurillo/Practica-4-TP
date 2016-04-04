package es.ucm.fdi.tp.practica5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxRules;

public class BoardUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel[][] squares;
	private Board board;
	private ColorMap mapaColores;

	public BoardUI(Board b, List<Piece> pieces) {
		mapaColores = new ColorMap(pieces);
		setBoard(b);
		setSquares();
		update();
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				add(squares[i][j]);
			}
		}
	}

	public void setBoard(Board board) {
		removeAll(); // descartamos squares antiguos
		this.board = board;
		squares = new JLabel[board.getRows()][board.getCols()];
		setLayout(new GridLayout(board.getRows(), board.getCols(), 2, 2));
		
	}
	
	public void setSquares(){
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				squares[i][j] = new JLabel();
				squares[i][j].setOpaque(true);
				squares[i][j].addMouseListener(new MouseAdapter() {
		            public void mouseClicked(MouseEvent me) {
		            	//Escribir Codigo
		            }
		          });
			}
		}
		
	}

	public void update() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Piece p =board.getPosition(i, j);
				if(p!=null)squares[i][j].setBackground(mapaColores.getColorFor(p));
			}
		}
		repaint(); // obligas a que se repinte el tablero (ahora que
							// pintará lo que debe)
	}

	public void setColors(PieceColorMap colorMap) {
	};

	/*public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					BoardUI window = new BoardUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/


}
