package es.ucm.fdi.tp.practica5.connectn;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

public class ConnectNSwingView extends GenericSwingView{

	public ConnectNSwingView(Observable<GameObserver> g, Controller c, Piece p, Player random, Player ai) {
		super(g, c, p, random, ai);
	}

	@Override
	public void leftButtonPressed(int row, int col) {
		setMove(row, col);
		controller.makeMove(players.get(lastTurn));
		resetMove();
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setManualPlayer(Piece p) {
		players.put(p, ConnectNFactoryExt.createSwingPlayer(this));
	}

}
