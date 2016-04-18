package es.ucm.fdi.tp.practica5.attt;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

public class AdvancedTTTSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean mode;
	private int iniCol;
	private int iniRow;
	private static int turnCount;

	public AdvancedTTTSwingView(Observable<GameObserver> g, Controller c, Piece p, Player random, Player ai) {
		super(g, c, p, random, ai);
		mode = false;
		turnCount = 0;
		resetMove();
	}

	@Override
	public void leftButtonPressed(int row, int col) {
		if (!mode) {
			simpleMove(row, col);
			turnCount++;
			if (turnCount > 5) {
				mode = true;
			}
		} else {
			complexMove(row, col);
		}

	}

	private void complexMove(int row, int col) {
		if (iniCol == -1) {
			if (lastTurn.equals(lastBoard.getPosition(row, col))) {
				iniCol = col;
				iniRow = row;
				setMove(row, col);
				boardUI.selectSquare(row, col);
			}
		} else {
			boardUI.deselectSquare(iniRow, iniCol);
			if (lastTurn.equals(lastBoard.getPosition(row, col))) {
				resetMove();
				iniCol = col;
				iniRow = row;
				setMove(row, col);
				boardUI.selectSquare(row, col);
			} else {
				move += "> ";
				setMove(row, col);
				move = move.substring(0, move.length() - 1);
				controller.makeMove(players.get(lastTurn));
				resetMove();
			}
		}
	}

	private void simpleMove(int row, int col) {
		setMove(row, col);
		controller.makeMove(players.get(lastTurn));
		resetMove();
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		if (mode) {
			if (row == iniRow && col == iniCol) {
				resetMove();
				boardUI.deselectSquare(row, col);
			}
		}
	}

	@Override
	public void setManualPlayer(Piece p) {
		players.put(p, AdvancedTTTFactoryExt.createSwingPlayer(this));
	}

	public void resetMove() {
		super.resetMove();
		iniRow = -1;
		iniCol = -1;
	}

}
