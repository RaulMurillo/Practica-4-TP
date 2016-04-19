package es.ucm.fdi.tp.practica5.connectn;

import java.util.ArrayList;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNFactory;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;
import es.ucm.fdi.tp.practica5.control.SwingPlayer;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

public class ConnectNFactoryExt extends ConnectNFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectNFactoryExt() {
		super();
	}

	public ConnectNFactoryExt(int dim) {
		super(dim);
	}

	@Override
	public void createSwingView(final Observable<GameObserver> game, final Controller ctrl, final Piece viewPiece,
			Player randPlayer, Player aiPlayer) {
		new ConnectNSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
	}

	public static Player createSwingPlayer(final GenericSwingView view) {
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new ConnectNMove());
		return new SwingPlayer(possibleMoves, view);
	}

}
