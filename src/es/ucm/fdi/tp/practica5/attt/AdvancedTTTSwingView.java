package es.ucm.fdi.tp.practica5.attt;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxMove;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

public class AdvancedTTTSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static boolean advancedMode;
	private int iniCol;
	private int iniRow;
	private static int turnCount;
	@SuppressWarnings("unused")
	private GameRules rules;

	public AdvancedTTTSwingView(Observable<GameObserver> g, Controller c, Piece p, Player random, Player ai) {
		super(g, c, p, random, ai);
		advancedMode = false;
		turnCount = 0;
		resetMove();
	}

	@Override
	public void leftButtonPressed(int row, int col) {
		if (viewPiece == null || viewPiece.equals(lastTurn)) {
			if (!advancedMode) {
				simpleMove(row, col);
			} else {
				complexMove(row, col);
			}
			System.err.println("Turn count = " + turnCount);
		}
	}

	private void complexMove(int row, int col) {
		if (iniCol == -1) {
			if (lastTurn.equals(lastBoard.getPosition(row, col))) {
				settings.setEnabled(false, true, true);
				iniCol = col;
				iniRow = row;
				boardUI.selectSquare(row, col);
				showHelp();
				System.err.println("First square move: " + iniRow + iniCol);
			}
		} else {
			boardUI.deselectSquare(iniRow, iniCol);
			if (lastTurn.equals(lastBoard.getPosition(row, col))) {
				resetMove();
				iniCol = col;
				iniRow = row;
				boardUI.selectSquare(row, col);
			} else {
				if (lastBoard.getPosition(row, col) == null && AtaxxMove.distance(row, col, iniRow, iniCol) <= 2) {
					move = new AdvancedTTTMove(iniRow, iniCol, row, col, lastTurn);
					controller.makeMove(players.get(lastTurn));
					enablePanels();
				} else {
					settings.setMessage("Invalid move");
					resetMove();
				}
			}
		}
	}

	private void simpleMove(int row, int col) {
		if(lastBoard.getPosition(row, col) == null){
		move = new AdvancedTTTMove(iniRow, iniCol, row, col, lastTurn);
		controller.makeMove(players.get(lastTurn));
		}
		else settings.setMessage("Invalid move");
		resetMove();
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		if (advancedMode) {
			if (row == iniRow && col == iniCol) {
				resetMove();
				enablePanels();
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
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		super.onMoveEnd(board, turn, success);
		if (success && turn.equals(viewPiece) || viewPiece == null) {
			turnCount++;
		}
		if (turnCount > 5) {
			advancedMode = true;
		}
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		super.onChangeTurn(board, turn);
	}

	@Override
	protected void showHelp() {
		if (advancedMode) {
			if (iniCol == -1) {
				settings.setMessage("Click on an origin piece");
			} else {
				settings.setMessage("Click on the destination position");
			}
		} else {
			settings.setMessage("Click on an empty cell");
		}

	}

	@Override
	protected void showStartingHelp() {
		settings.setMessage("Click on an empty cell");
	}
}
