package es.ucm.fdi.tp.practica5.ataxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.basecode.bgame.control.AIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.DummyAIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.views.GenericConsoleView;

/**
 * A factory for creating Ataxx games. See {@link AtaxxRules} for the
 * description of the game.
 * <p>
 * Factoria para la creacion de juegos Ataxx. Vease {@link AtaxxRules} para la
 * descripcion del juego.
 */
public class AtaxxFactory implements GameFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6107386896988476429L;
	private int dim;
	private int obstacles;
	private static final int MIN_DIM = 5;

	/**
	 * Default constructor of the class that gives a game with dimension 5 and
	 * no obstacles.
	 * <p>
	 * Constructor predeterminado de la clase que da un juego con dimension y 5
	 * sin obstaculos.
	 */
	public AtaxxFactory() {
		this(MIN_DIM, 0);
	}

	/**
	 * Constructor of the class that gives a game with dimension 5 and many
	 * obstacles as given.
	 * <p>
	 * Constructor predeterminado de la clase que da un juego con dimension y 5
	 * tantos obstaculos se indiquen.
	 * 
	 * @param obstacles
	 *            Number of obstacles in the board.
	 *            <p>
	 *            Numero de obstaculos en el tablero.
	 */
	public AtaxxFactory(int obstacles) {
		this(MIN_DIM, obstacles);
	}

	/**
	 * Class constructor with parameters.
	 * <p>
	 * Constructor de la clase con parametros.
	 * 
	 * @param dim
	 *            Number of rows and columns of the board.
	 *            <p>
	 *            Numero de filas y columnas del tablero.
	 * @param obstacles
	 *            Number of obstacles in the board.
	 *            <p>
	 *            Numero de obstaculos en el tablero.
	 */
	public AtaxxFactory(int dim, int obstacles) {
		if (dim < MIN_DIM && dim % 2 == 0) {
			throw new GameError("Dimension must be at least 5 and odd: " + dim);
		} else {
			this.dim = dim;
			this.obstacles = obstacles;
		}
	}

	@Override
	public GameRules gameRules() {
		return new AtaxxRules(dim, obstacles);
	}

	@Override
	public Player createConsolePlayer() {

		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new AtaxxMove());
		return new ConsolePlayer(new Scanner(System.in), possibleMoves);
	}

	@Override
	public Player createRandomPlayer() {
		return new AtaxxRandomPlayer();
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		if (alg != null) {
			return new AIPlayer(alg);
		} else {
			return new DummyAIPlayer(createRandomPlayer(), 1000);
		}
	}

	@Override
	public List<Piece> createDefaultPieces() {
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		return pieces;
	}

	@Override
	public void createConsoleView(Observable<GameObserver> game, Controller ctrl) {
		new GenericConsoleView(game, ctrl);
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl, Piece viewPiece, Player randPlayer,
			Player aiPlayer) {
		throw new UnsupportedOperationException("There is no swing view");

	}
}
