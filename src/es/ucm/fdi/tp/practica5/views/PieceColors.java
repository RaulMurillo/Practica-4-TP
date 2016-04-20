package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class PieceColors extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public interface PieceColorsListener {
		void changeColorPressed(Piece p, Color c);
	}

	public PieceColors(List<Piece> piece, PieceColorsListener controlsListener) {
		setBorder(new TitledBorder(null, "Piece Colors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JComboBox<Piece> jcbPieces = new JComboBox(piece.toArray());
		setLayout(new FlowLayout());
		add(jcbPieces);
		JButton jbChange = new JButton("Change Color");
		jbChange.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				Color c = JColorChooser.showDialog(PieceColors.this, "Color chooser", null);
				int pieceIndex = jcbPieces.getSelectedIndex();
				controlsListener.changeColorPressed(jcbPieces.getItemAt(pieceIndex), c);
			}
		});
		this.add(jbChange);
	}
}
