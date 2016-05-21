package es.ucm.fdi.tp.practica6.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.practica6.server.ProxyController.ClientMessage;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A class to represent a {@link Player} from the client side. It works as an
 * endpoint to send and receive data with the server.
 * <p>
 * Clase que representa un {@link Player} desde el lado del cliente. Funciona
 * como un endpoint para enviar y recibir datos con el servidor.
 * 
 * @author Raul Murillo and Antonio Valdivia.
 *
 */
public class ProxyPlayer extends Player implements GameObserver, SocketEndpoint {

	private static final Logger log = Logger.getLogger(Controller.class.getSimpleName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 6734599552257109858L;

	/**
	 * Object with which data is sent.
	 * <p>
	 * Objeto con el que se envian datos.
	 */
	private ObjectOutputStream oos;

	/**
	 * Object with which data is received.
	 * <p>
	 * Objeto con el que se reciben datos.
	 */
	private ObjectInputStream ois;

	/**
	 * Indicates if the connection has been stopped.
	 * <p>
	 * Indica si la conexion ha sido parada.
	 */
	private volatile boolean stopped;

	/**
	 * Name of the object/endpoint.
	 * <p>
	 * Nombre del objeto/endpoint.
	 */
	private String hostname;

	/**
	 * Controller of the class.
	 * <p>
	 * Controlador de la clase.
	 */
	private Controller controller;

	/**
	 * List of pieces of the game.
	 * <p>
	 * Lista de piezas del juego.
	 */
	private List<Piece> pieces;

	/**
	 * Factory of the game.
	 * <p>
	 * Factoria del juego.
	 */
	private GameFactory gameFactory;

	/**
	 * The piece that handles this player.
	 * <p>
	 * La pieza que maneja este jugador.
	 */
	private Piece localPiece;

	/**
	 * Indicates if the game has finished.
	 * <p>
	 * Indica si el juego ha terminado.
	 */
	private boolean endGame;

	/**
	 * The last move made by this player.
	 * <p>
	 * El ultimo movimiento realizado por este jugador.
	 */
	private GameMove move;

	/**
	 * A generic, abstract message to send to the client.
	 * <p>
	 * Un mensaje generico y abstracto para enviar al cliente.
	 */
	public static abstract class ObservableMessage implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4393638228993967379L;

		/**
		 * Executes the content of the message.
		 * <p>
		 * Ejecuta el contenido del mensaje.
		 * 
		 * @param gameObserver
		 *            The observer from the client side to which the message is
		 *            sent.
		 *            <p>
		 *            El observador del lado del cliente al que se envia el
		 *            mensaje.
		 */
		public void notifyMessage(GameObserver gameObserver) {
		}

		/**
		 * Updates the necessary attributes of the client.
		 * <p>
		 * Actualiza los atributos del cliente necesarios.
		 * 
		 * @param proxyController
		 *            The client to update.
		 *            <p>
		 *            El cliente a actualizar.
		 */
		public void updateProxy(ProxyController proxyController) {
		}
	}

	/**
	 * A message to start the game.
	 * <p>
	 * Un mensaje para iniciar el juego.
	 */
	public static class StartMessage extends ObservableMessage {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6946294506044335505L;
		private Board board;
		private String gameDesc;
		private List<Piece> pieces;
		private Piece turn;

		public StartMessage(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
			this.board = board;
			this.gameDesc = gameDesc;
			this.pieces = pieces;
			this.turn = turn;
		}

		@Override
		public void notifyMessage(GameObserver gameObserver) {
			gameObserver.onGameStart(board, gameDesc, pieces, turn);
		}

		@Override
		public void updateProxy(ProxyController proxyController) {
			proxyController.updateBoard(board);
		}

	};

	/**
	 * A message to notify a move start.
	 * <p>
	 * Un mensaje para notificar el inicio de un movimiento.
	 */
	public static class MoveStartMessage extends ObservableMessage {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8032800548074100479L;
		private Board board;
		private Piece turn;

		public MoveStartMessage(Board board, Piece turn) {
			this.board = board;
			this.turn = turn;
		}

		@Override
		public void notifyMessage(GameObserver gameObserver) {
			gameObserver.onMoveStart(board, turn);
		}

		@Override
		public void updateProxy(ProxyController proxyController) {
			proxyController.updateBoard(board);
		}
	}

	/**
	 * A message to notify a move end.
	 * <p>
	 * Un mensaje para notificar el fin de un movimiento.
	 */
	public static class MoveEndMessage extends ObservableMessage {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7823571233081654420L;
		private Board board;
		private Piece turn;
		private boolean success;

		public MoveEndMessage(Board board, Piece turn, boolean success) {
			this.board = board;
			this.turn = turn;
			this.success = success;
		}

		@Override
		public void notifyMessage(GameObserver gameObserver) {
			gameObserver.onMoveEnd(board, turn, success);
		}

		@Override
		public void updateProxy(ProxyController proxyController) {
			proxyController.updateBoard(board);
		}
	}

	/**
	 * A message to notify a change of turn.
	 * <p>
	 * Un mensaje para notificar un cambio de turno.
	 */
	public static class ChangeTurnMessage extends ObservableMessage {

		/**
		 * 
		 */
		private static final long serialVersionUID = -809447129053328997L;
		private Board board;
		private Piece turn;

		public ChangeTurnMessage(Board board, Piece turn) {
			this.board = board;
			this.turn = turn;
		}

		@Override
		public void notifyMessage(GameObserver gameObserver) {
			gameObserver.onChangeTurn(board, turn);
		}

		@Override
		public void updateProxy(ProxyController proxyController) {
			proxyController.updateBoard(board);
		}

	}

	/**
	 * A message to notify an error.
	 * <p>
	 * Un mensaje para notificar un error.
	 */
	public static class ErrorMessage extends ObservableMessage {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6496896047536110780L;
		protected String msg;

		public ErrorMessage(String msg) {
			this.msg = msg;
		}

		@Override
		public void notifyMessage(GameObserver gameObserver) {
			gameObserver.onError(msg);
		}
	}

	/**
	 * A message to notify a fatal error.
	 * <p>
	 * Un mensaje para notificar un error fatal.
	 */
	public static class FatalErrorMessage extends ErrorMessage {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6496896047536110780L;

		public FatalErrorMessage(String msg) {
			super(msg);
		}

		public FatalErrorMessage() {
			this("");
		}

		@Override
		public void updateProxy(ProxyController proxyController) {
			proxyController.showFatalError(msg);
		}

		@Override
		public void notifyMessage(GameObserver gameObserver) {
		}
	}

	/**
	 * A message to notify the game over.
	 * <p>
	 * Un mensaje para notificar el fin del juego.
	 */
	public static class GameOverMessage extends ObservableMessage {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5985528805709245057L;
		private Board board;
		private State state;
		private Piece winner;

		public GameOverMessage(Board board, State state, Piece winner) {
			this.board = board;
			this.state = state;
			this.winner = winner;
		}

		@Override
		public void notifyMessage(GameObserver gameObserver) {
			gameObserver.onGameOver(board, state, winner);
		}

		@Override
		public void updateProxy(ProxyController proxyController) {
			proxyController.updateBoard(board);
		}

	}

	/**
	 * A message with the necessary information to initialize the game..
	 * <p>
	 * Un mensaje con la informacion necesaria para inicializar el juego.
	 */
	public static class InitializationMessage implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6544953671122140980L;
		private GameFactory gameFactory;
		private List<Piece> pieces;
		private Piece localPiece;

		public InitializationMessage(GameFactory gameFactory, List<Piece> pieces, Piece localPiece) {
			this.gameFactory = gameFactory;
			this.localPiece = localPiece;
			this.pieces = pieces;
		}

		public GameFactory getGameFactory() {
			return gameFactory;
		}

		public List<Piece> getPieces() {
			return pieces;
		}

		public Piece getLocalPiece() {
			return localPiece;
		}
	}

	/**
	 * Creates a {@code ProxyPlayer} with name {@code localserver}.
	 * <p>
	 * Crea un {@code ProxyPlayer} de nombre {@code localserver}.
	 * 
	 * @param controller
	 *            Controller of the player.
	 *            <p>
	 *            Controlador del jugador.
	 * @param gameFactory
	 *            Factory of the game.
	 *            <p>
	 *            Factoria del juego.
	 * @param pieces
	 *            List of pieces of the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 * @param localPiece
	 *            The piece that handles this player.
	 *            <p>
	 *            La pieza que maneja este jugador.
	 */
	public ProxyPlayer(Controller controller, GameFactory gameFactory, List<Piece> pieces, Piece localPiece) {
		this("localserver", controller, gameFactory, pieces, localPiece);
	};

	/**
	 * Creates a {@code ProxyPlayer} with the specified data.
	 * <p>
	 * Crea un {@code ProxyPlayer} con los datos especificados.
	 * 
	 * @param hostname
	 *            Name of the player.
	 *            <p>
	 *            Nobre del jugador.
	 * 
	 * @param controller
	 *            Controller of the player.
	 *            <p>
	 *            Controlador del jugador.
	 * @param gameFactory
	 *            Factory of the game.
	 *            <p>
	 *            Factoria del juego.
	 * @param pieces
	 *            List of pieces of the game.
	 *            <p>
	 *            Lista de piezas del juego.
	 * @param localPiece
	 *            The piece that handles this player.
	 *            <p>
	 *            La pieza que maneja este jugador.
	 */
	public ProxyPlayer(String hostname, Controller controller, GameFactory gameFactory, List<Piece> pieces,
			Piece localPiece) {
		this.hostname = hostname;
		this.controller = controller;
		this.gameFactory = gameFactory;
		this.pieces = pieces;
		this.localPiece = localPiece;
		this.endGame = false;
	}

	@Override
	public void startConnection(final Socket socket, int timeout) throws IOException {
		try {
			socket.setSoTimeout(timeout);
			oos = new ObjectOutputStream(socket.getOutputStream());

			new Thread(new Runnable() {
				public void run() {
					try {
						ois = new ObjectInputStream(socket.getInputStream());

					} catch (IOException e) {
						log.log(Level.WARNING, "Failed to read: could not create object input stream");
					}
					while (!stopped) {
						try {
							dataReceived(ois.readObject());
						} catch (SocketTimeoutException ste) {
							log.log(Level.FINE, "Failed to read; will retry");
						} catch (IOException | ClassNotFoundException se) {
							log.log(Level.WARNING, "Failed to read: bad serialization", se);
							stopped = true;
						}
					}
					log.log(Level.INFO, "Client exiting gracefully");
					try {
						socket.close();
					} catch (IOException e) {
						log.log(Level.WARNING, e.getMessage());
					}
				}
			}, hostname + "Listener").start();
			newConnection();
		} catch (IOException e) {
			log.log(Level.WARNING, "Error while handling client connection", e);
		}
	}

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		return move;
	}

	/**
	 * Sets the last move chosen by the player.
	 * <p>
	 * Establece el ultimo movimiento seleccionado por el jugador.
	 * 
	 * @param move
	 *            The last move chosen.
	 *            <p>
	 *            El ultimo movimiento seleccionado.
	 */
	public void setMove(GameMove move) {
		this.move = move;
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		log.log(Level.INFO, "The game is going to start (OnGameStart)");
		sendData(new StartMessage(board, gameDesc, pieces, turn));
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		sendData(new GameOverMessage(board, state, winner));
		if (state.equals(State.Stopped)) {
			sendData(new FatalErrorMessage("An error occurred in the server and the program must be closed.\n"
					+ "Sorry for the incoveniences."));
		} else
			endGame = true;
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		sendData(new MoveStartMessage(board, turn));

	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		sendData(new MoveEndMessage(board, turn, success));

	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		sendData(new ChangeTurnMessage(board, turn));

	}

	@Override
	public void onError(String msg) {
		sendData(new ErrorMessage(msg));
	}

	/**
	 * Send to the client the initialization message.
	 * <p>
	 * Envia al cliente el mensaje de inicializacion.
	 */
	public void newConnection() {
		InitializationMessage initializationMessage = new InitializationMessage(gameFactory, pieces, localPiece);
		sendData(initializationMessage);

	}

	@Override
	public void sendData(Object message) {
		try {
			oos.writeObject(message);
			oos.flush();
			oos.reset();
		} catch (SocketTimeoutException ste) {
			log.log(Level.INFO, "Failed to write; target must be full!", ste);
		} catch (IOException ioe) {
			log.log(Level.WARNING, "Failed to write: bad serialization", ioe);
		}
	}

	@Override
	public void dataReceived(Object message) {
		((ClientMessage) message).notifyMessage(this);
	}

	public synchronized void ctrlMakeMove(Player p) {
		try {
			controller.makeMove(p);
		} catch (GameError e) {
			log.log(Level.WARNING, "Error while making move", e);
		}

	}

	/**
	 * Asks the controller to stop the game.
	 * <p>
	 * Solicita al controlador parar el juego.
	 */
	public synchronized void ctrlStop() {
		if (!endGame && !localPiece.equals(GameServer.observerPiece)) {
			try {
				controller.stop();
			} catch (GameError e) {
				log.log(Level.WARNING, "Error while stopping controller", e);
			}
		} else
			stopped = true;

	}

	/**
	 * Asks the controller to restart the game.
	 * <p>
	 * Solicita al controlador reiniciar el juego.
	 */
	public synchronized void ctrlRestart() {
		try {
			controller.restart();
		} catch (GameError e) {
			log.log(Level.WARNING, "Error while restarting game", e);
		}

	}

	@Override
	public void stopConnection() {
		// TODO Auto-generated method stub

	}

}
