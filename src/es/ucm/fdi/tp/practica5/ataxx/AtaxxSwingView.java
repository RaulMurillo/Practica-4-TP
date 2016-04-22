package es.ucm.fdi.tp.practica5.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxMove;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

public class AtaxxSwingView extends GenericSwingView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int iniCol = -1;
	private int iniRow = -1;

	public AtaxxSwingView(Observable<GameObserver> g, Controller c, Piece p, Player random, Player ai) {
		super(g, c, p, random, ai);
		resetMove();
	}

	@Override
	public void leftButtonPressed(int row, int col) {
		if (viewPiece == null || viewPiece.equals(lastTurn)) {
			if (iniCol == -1) {
				if (lastTurn.equals(lastBoard.getPosition(row, col))) {
					settings.setEnabled(false, true, true);
					iniCol = col;
					iniRow = row;
					boardUI.selectSquare(row, col);
					showHelp();
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
						move = new AtaxxMove(iniRow, iniCol, row, col, lastTurn);
						controller.makeMove(players.get(lastTurn));
						enablePanels();
					} else {
						settings.setMessage("Invalid move");
						resetMove();
					}
				}
			}
		}
	}

	@Override
	public void rightButtonPressed(int row, int col) {
		if (row == iniRow && col == iniCol) {
			resetMove();
			enablePanels();
			boardUI.deselectSquare(row, col);
		}
	}

	@Override
	public void setManualPlayer(Piece p) {
		players.put(p, AtaxxFactoryExt.createSwingPlayer(this));
	}

	public void resetMove() {
		super.resetMove();
		if (iniRow != -1)
			boardUI.deselectSquare(iniRow, iniCol);
		iniRow = -1;
		iniCol = -1;
	}

	@Override
	protected void showHelp() {
		System.err.println(iniCol+""+iniRow);
		if (iniCol == -1) {
			settings.setMessage("Click on an origin piece");
		} else {
			settings.setMessage("Click on the destination position");
		}
	}

	@Override
	protected void showStartingHelp() {
		settings.setMessage("Click on an origin piece");
	}

}
