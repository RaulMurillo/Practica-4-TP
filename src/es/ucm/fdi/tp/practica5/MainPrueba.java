package es.ucm.fdi.tp.practica5;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxRules;

public class MainPrueba {

	public static void main(String ... args) {
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		pieces.add(new Piece("R"));
		pieces.add(new Piece("J"));
		GameRules rules = new AtaxxRules(5, 0);
		Board board = rules.createBoard(pieces);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
              VentanaPrincipalPrueba2 jf = new VentanaPrincipalPrueba2(board, pieces);
              jf.setVisible(true);
            }
        });
    }
}
