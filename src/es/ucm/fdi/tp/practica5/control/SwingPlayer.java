package es.ucm.fdi.tp.practica5.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

/**
 * A class that implements a player that inputs its moves by swing.
 * 
 * <p>
 * Clase que implementa un jugador que introduce sus jugadas por swing.
 */
public class SwingPlayer extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of moves that can be played by the player (typically include one
	 * type of move for simple games, e.g., Tic-Tac-Toe).
	 * 
	 * <p>
	 * Lista de tipos de movimientos que puede jugar el jugador (solo un tipo de
	 * movimientos para juegos simples).
	 */
	private List<GameMove> availableMoves;

	private GenericSwingView view;

	public SwingPlayer(List<GameMove> availableMoves, GenericSwingView view) {
		// this.view = view;
		this.view = view;
		this.availableMoves = new ArrayList<GameMove>(availableMoves);
	}

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		for (GameMove m : availableMoves) {
			GameMove newMove = m.fromString(p, view.getMove());
			if (newMove != null) {
				return newMove;
			}
		}
		throw new GameError("Invalid move");
	}
}
