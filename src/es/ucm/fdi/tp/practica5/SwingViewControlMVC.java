package es.ucm.fdi.tp.practica5;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class SwingViewControlMVC extends Controller {

	boolean clicked;
	
	public SwingViewControlMVC(Game game, List<Piece> pieces) {
		super(game, pieces);
		clicked = false;
	}
	
	public void cellClicked(int i, int j){
		if(clicked){
			if(game.getTurn().equals())
			
		}
		else{
			
		}
		
	}
	
	
	
}
