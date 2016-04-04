package es.ucm.fdi.tp.practica5;

import java.util.ArrayList;
import java.util.List;

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

		GameRules rules = new AtaxxRules(5, 0);
		Board board = rules.createBoard(pieces);
		JFrame jf = new JFrame();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.setSize(400, 400);
                jf.setVisible(true);
                JPanel p = new BoardUI(board, pieces);
                jf.add(p);
                
            }
        });
    }
}
