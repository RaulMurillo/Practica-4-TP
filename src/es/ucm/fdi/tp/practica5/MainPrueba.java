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
		ColorMap m = new ColorMap(pieces);
		GameRules rules = new AtaxxRules(5, 0);
		Board board = rules.createBoard(pieces);
        BoardUI b = new BoardUI(board, pieces);
        JPanel p = new PieceColorChooser(pieces, m);
    	
		JButton b1 = new JButton("update");
 		b1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
            	b.update(m);
            }
          });
 		JPanel principal = new JPanel(new BorderLayout());
		JFrame jf = new JFrame();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.setSize(400, 400);
                principal.add(b, BorderLayout.CENTER);
               jf.add(p, BorderLayout.EAST);
               jf.add(b1, BorderLayout.WEST);
                jf.add(principal);
                jf.setVisible(true);
            }
        });
    }
}
