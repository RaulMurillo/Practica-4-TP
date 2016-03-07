package es.ucm.fdi.tp.practica4.ataxx;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;


public class AtaxxConsolePlayer extends Player {

	private static final long serialVersionUID = 1L;

	/**
	 * A scanner used to read the user's input.
	 * 
	 * <p>
	 * Scanner utilizado para leer los comandos del usuario.
	 */
	private Scanner in;

	/**
	 * Constructs a console player.
	 * 
	 * <p>
	 * Construye un jugador de modo consola.
	 * 
	 * @param in
	 *            A scanner to be used to read the user's input.
	 *            <p>
	 *            Scanner desde el que se leen los comandos del usuario.
	 * 
	 * @param availableMoves
	 *            A list of available moves.
	 *            <p>
	 *            Lista de movimientos disponibles.
	 */
	public AtaxxConsolePlayer(Scanner in) {
		this.in = in;
	}

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		
		// generate the valid moves:
		List<GameMove> availableMoves = rules.validMoves(board, pieces, p);
		// print a text describing all available moves.
		//
		/*
		System.out.println();
		System.out.println("The possible moves are:");
		for (GameMove m : availableMoves) {
			System.out.println("  " + m.help());
		}
		*/
	

		// read the user input
		System.out.print("Please type your move:");
		String cmd = getUserInput().trim();

		// the fir GameMove that succeeds to parse the user's input is returned
		for (GameMove m : availableMoves) {
			GameMove newMove = m.fromString(p, cmd);
			if (newMove != null) {
				return newMove;
			}
		}

		throw new GameError("Uknown move: " + cmd);
	}

	/**
	 * Reads input from the input Scanner. It is in a separated method in order
	 * to be able to override to change the behavior. E.g., the default one
	 * reads a line, but we can override it to read several lines, etc.
	 * 
	 * <p>
	 * Lectura del texto introducido por el Scanner de entrada. Esta en un
	 * metodo separado para poder sobrescribirlo si se quiere cambiar su
	 * comportamiento. Por ejemplo, el comportamiento por defecto lee una linea,
	 * pero se puede sobrescribir para leer varias lineas, etc.
	 * 
	 * @return Text representing the user's move.
	 *         <p>
	 *         Texto que representa el movimiento del usuario.
	 */
	protected String getUserInput() {
		return in.nextLine();
	}

}

