package es.ucm.fdi.tp.practica5.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.views.GenericConsoleView;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxFactory;
import es.ucm.fdi.tp.practica5.control.SwingController;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

public class AtaxxFactoryExt extends AtaxxFactory {

	public AtaxxFactoryExt() {
		super();
	}
	
	public AtaxxFactoryExt(int dim, int obstacles) {
		super(dim,obstacles);
	}
	
	@Override
	public void createSwingView(final Observable<GameObserver> game, final Controller ctrl, final Piece viewPiece,
			Player randPlayer, Player aiPlayer) {
		new GenericSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
	}

}
