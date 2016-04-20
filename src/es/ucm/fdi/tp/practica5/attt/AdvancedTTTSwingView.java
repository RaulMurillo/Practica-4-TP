package es.ucm.fdi.tp.practica5.attt;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
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
		if (viewPiece == null || viewPiece.equals(lastTurn)) {
			if (!mode) {
				simpleMove(row, col);
				if (turnCount > 5) {
					mode = true;
				}
			} else {
				complexMove(row, col);
			}
			System.err.println("Turn count = " +turnCount);
		}
	}

	private void complexMove(int row, int col) {
		System.err.println("Begining complex move");
		if (iniCol == -1) {
			if (lastTurn.equals(lastBoard.getPosition(row, col))) {
				settings.setEnabled(false, true, true);
				iniCol = col;
				iniRow = row;
				boardUI.selectSquare(row, col);
				System.err.println("First square move: "+iniRow + iniCol);
			}
		} else {
			boardUI.deselectSquare(iniRow, iniCol);
			if (lastTurn.equals(lastBoard.getPosition(row, col))) {
				resetMove();
				iniCol = col;
				iniRow = row;
				boardUI.selectSquare(row, col);
				System.err.println("First square changed: " + iniRow+ iniCol);
			} else {
				move = new AdvancedTTTMove(iniRow, iniCol, row, col, lastTurn);
				controller.makeMove(players.get(lastTurn));
				settings.setEnabled(true, true, true);
				resetMove();
			}
		}
	}

	private void simpleMove(int row, int col) {
		move = new AdvancedTTTMove(iniRow, iniCol, row, col, lastTurn);
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
	
	@Override
	public void onChangeTurn(Board board, Piece turn){
		super.onChangeTurn(board, turn);
		turnCount++;
	}

	/*@Override
	public void setMove(int row, int col) {
		move += "("+row + ") (" + col + ") ";
	}*/
}
