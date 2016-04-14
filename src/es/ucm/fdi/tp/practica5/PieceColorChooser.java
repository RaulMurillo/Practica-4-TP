package es.ucm.fdi.tp.practica5;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class PieceColorChooser extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Piece> comboBox;
	
	public PieceColorChooser(List<Piece> piece, ColorMap m){
		comboBox = new JComboBox(piece.toArray());
		setLayout(new FlowLayout());
		add(comboBox);
		JButton okButton = new JButton("Change Color");
		okButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
            	Color c = JColorChooser.showDialog(PieceColorChooser.this, "Color chooser", null);
            	m.setColor((Piece)comboBox.getSelectedItem(), c);
            }
          });
		this.add(okButton);
	}
}
