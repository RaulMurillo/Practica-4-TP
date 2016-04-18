package es.ucm.fdi.tp.practica5.attt;

import java.util.ArrayList;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTFactory;
import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxMove;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxSwingView;
import es.ucm.fdi.tp.practica5.control.SwingPlayer;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

public class AdvancedTTTFactoryExt extends AdvancedTTTFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdvancedTTTFactoryExt() {
		super();
	}

	@Override
	public void createSwingView(final Observable<GameObserver> game, final Controller ctrl, final Piece viewPiece,
			Player randPlayer, Player aiPlayer) {
		new AdvancedTTTSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
	}

	public static Player createSwingPlayer(final GenericSwingView view) {
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new AdvancedTTTMove());
		return new SwingPlayer(possibleMoves, view);
	}
}
