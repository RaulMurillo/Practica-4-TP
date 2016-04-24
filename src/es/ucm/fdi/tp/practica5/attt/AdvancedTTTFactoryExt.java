package es.ucm.fdi.tp.practica5.attt;


import es.ucm.fdi.tp.basecode.attt.AdvancedTTTFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;


/**
 * 
 * @author Antonio Valdivia y Raul Murillo
 *<p>
 *A class that extends AdvancedTTTFactory and that adds the new functionality needed to create a swingview.
 *<p>
 *Una clase que extiende AdvancedTTTFactory y que añade la nueva funcionalidad necesaria para crear una vista de ventana.
 *
 */
public class AdvancedTTTFactoryExt extends AdvancedTTTFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of the class
	 * <p>
	 * Constructor de la clase
	 */
	public AdvancedTTTFactoryExt() {
		super();
	}

	@Override
	public void createSwingView(final Observable<GameObserver> game, final Controller ctrl, final Piece viewPiece,
			Player randPlayer, Player aiPlayer) {
		new AdvancedTTTSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
	}
}
