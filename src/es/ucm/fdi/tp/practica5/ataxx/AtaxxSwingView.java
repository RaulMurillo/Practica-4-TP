package es.ucm.fdi.tp.practica5.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.control.SwingPlayer;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

public class AtaxxSwingView extends GenericSwingView {
	private int iniCol;
	private int iniRow;

	public AtaxxSwingView(Observable<GameObserver> g, Controller c, Piece p, Player random, Player ai) {
		super(g, c, p, random, ai);
		resetMove();
	}

	@Override
	public void leftButtonPressed(int row, int col) {
		if (iniCol == -1) {
			if (lastTurn.equals(lastBoard.getPosition(row, col))){
			iniCol = col;
			iniRow = row;
			setMove(row, col);
			boardUI.selectSquare(row, col);
			}
		}
		else{
			boardUI.deselectSquare(iniRow, iniCol);
			if(lastTurn.equals(lastBoard.getPosition(row, col))){	
				iniCol = col;
				iniRow = row;
				resetMove();
				setMove(row, col);
				boardUI.selectSquare(row, col);
			}
			else{
				setMove(row, col);
				controller.makeMove(players.get(lastTurn));
				resetMove();
			}
		}
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		if (row == iniRow && col == iniCol) {
			resetMove();
			boardUI.deselectSquare(row, col);
		}

	}

	public void resetMove() {
		super.resetMove();
		iniRow = -1;
		iniCol = -1;
	}

	@Override
	public void setManualPlayer(Piece p) {
		players.put(p, AtaxxFactoryExt.createSwingPlayer(this));
		
	}

}