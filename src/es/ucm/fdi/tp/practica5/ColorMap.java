package es.ucm.fdi.tp.practica5;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.Utils;

public class ColorMap implements PieceColorMap{

	private HashMap<Piece, Color> mapaColores;

	public ColorMap(List<Piece> listaPiezas){
		mapaColores = new HashMap();
		for(Piece p: listaPiezas){
			setColor(p, Utils.randomColor());
		}	
	}
	@Override
	public Color getColorFor(Piece p) {
		return mapaColores.get(p);
	}
	
	public void setColor(Piece p, Color c){
			if(mapaColores.replace(p, c) == null){
				mapaColores.put(p, c);
			}
	}

}
