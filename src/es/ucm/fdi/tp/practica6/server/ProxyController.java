package es.ucm.fdi.tp.practica6.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.lobby.demo.ClientLauncher;
import es.ucm.fdi.tp.practica6.server.ProxyPlayer.InitializationMessage;
import es.ucm.fdi.tp.practica6.server.ProxyPlayer.ObservableMessage;

/**
 * A class to represent a {@link Controller} from the client side. It works as
 * an endpoint to send and receive data with the server.
 * <p>
 * Clase que representa un {@link Controller} desde el lado del cliente.
 * Funciona como un endpoint para enviar y recibir datos con el servidor.
 * 
 * @author Raul Murillo and Antonio Valdivia.
 *
 */
public class ProxyController extends Controller implements Observable<GameObserver>, SocketEndpoint {

	private static final Logger log = Logger.getLogger(Controller.class.getSimpleName());

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
	 * Indicates if the server has been stopped.
	 * <p>
	 * Indica si el servidor ha sido parado.
	 */
	private volatile boolean stopped;

	/**
	 * Name of the object/endpoint.
	 * <p>
	 * Nombre del objeto/endpoint.
	 */
	private String hostname;

	/**
	 * Port on which the client will be listening.
	 * <p>
	 * Puerto en el que el cliente estara escuchando.
	 */
	private int port;

	/**
	 * Time limit for awaiting connection, in milliseconds.
	 * <p>
	 * Plazo para la espera de conexion, en milisegundos.
	 */
	private int timeout;

	/**
	 * List of observers.
	 * <p>
	 * Lista de observadores.
	 */
	private List<GameObserver> observers;

	/**
	 * The piece that handles this client.
	 * <p>
	 * La pieza que maneja este cliente.
	 */
	private Piece localPiece;

	/**
	 * Factory of the game.
	 * <p>
	 * Factoria del juego.
	 */
	private GameFactory gameFactory;

	/**
	 * Rules of the game.
	 * <p>
	 * Reglas del juego.
	 */
	private GameRules rules;

	/**
	 * Board of the game.
	 * <p>
	 * Tablero del juego.
	 */
	private Board gameBoard;

	/**
	 * Indicates if the server accepted this client.
	 * <p>
	 * Indica si el servidor acepto a este cleinte.
	 */
	private boolean initialized;

	/**
	 * Algorithm for the AI Player.
	 * <p>
	 * Algoritmo para le jugador inteligente.
	 */
	private AIAlgorithm aiPlayerAlg;

	/**
	 * A generic, abstract message to send to the server.
	 * <p>
	 * Un mensaje generico y abstracto para enviar al servidor.
	 */
	public static abstract class ClientMessage implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6602000723654830197L;

		/**
		 * Executes the content of the message.
		 * <p>
		 * Ejecuta el contenido del mensaje.
		 * 
		 * @param player
		 *            The player from the server side to which the message is
		 *            sent.
		 *            <p>
		 *            El jugador del lado del servidor al que se envia el
		 *            mensaje.
		 */
		public void notifyMessage(ProxyPlayer player) {
		}
	}

	/**
	 * A message to notify a game move.
	 * <p>
	 * Un mensaje para notificar un movimiento del juego.
	 */
	public static class MakeMoveMessage extends ClientMessage {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3336919685539615536L;
		
		/**
		 * Move to send.
		 * <p>
		 * Movimiento a enviar. 
		 */
		private GameMove move;

		public MakeMoveMessage(GameMove move) {
			this.move = move;
		}

		@Override
		public void notifyMessage(ProxyPlayer p) {
			p.setMove(move);
			p.ctrlMakeMove(p);
		}
	}

	/**
	 * A message to stop the game.
	 * <p>
	 * Un mensaje para parar el juego.
	 */
	public static class StopMessage extends ClientMessage {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5504413514289998746L;

		@Override
		public void notifyMessage(ProxyPlayer p) {
			p.ctrlStop();
		}
	}

	/**
	 * A message to restart the game.
	 * <p>
	 * Un mensaje para reiniciar el juego.
	 */
	public static class RestartMessage extends ClientMessage {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7717151285424567564L;

		@Override
		public void notifyMessage(ProxyPlayer p) {
			p.ctrlRestart();
		}
	}

	/**
	 * Creates a Client/Controller with name {@code localhost}, in port 2020,
	 * timelimit 2000 and no algorithm for teh AI Player.
	 * <p>
	 * Crea un Cliente/Controlador con nombre {@code localhost}, en el puerto
	 * 2020, tiempo limite de 2000 y sin agoritmo de AI.
	 */
	public ProxyController() {
		this("localhost", 2020, 2000, null);
	}

	/**
	 * Creates a Client/Controller with the specified data.
	 * <p>
	 * Crea un Cliente/Controlador con los datos especificados.
	 * 
	 * @param hostname
	 *            Name of the client.
	 *            <p>
	 *            Nombre del cliente.
	 * @param port
	 *            Port on which connect.
	 *            <p>
	 *            Puerto al que conectarse.
	 * @param timeout
	 *            Time limit for connection awaiting.
	 *            <p>
	 *            Tiempo limite de espera de conexion.
	 * @param aiPlayerAlg
	 *            Algorithm for the AI Player.
	 *            <p>
	 *            Algoritmo para el jugador inteligente.
	 */
	public ProxyController(String hostname, int port, int timeout, AIAlgorithm aiPlayerAlg) {
		super(null, null);
		this.hostname = hostname;
		this.port = port;
		this.timeout = timeout;
		this.stopped = false;
		this.initialized = false;
		this.observers = new ArrayList<GameObserver>();
		this.aiPlayerAlg = aiPlayerAlg;
	}

	@Override
	public void startConnection(final Socket socket, int timeout) throws IOException {
		try {
			socket.setSoTimeout(timeout);
			new ClientLauncher().launchInMessageWindow();
			oos = new ObjectOutputStream(socket.getOutputStream());

			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						ois = new ObjectInputStream(socket.getInputStream());
					} catch (IOException e) {
						log.log(Level.WARNING, "Failed to read: could not create object input stream");
					}
					while (!stopped) {
						try {
							if (!initialized) {
								connectionEstablished(ois.readObject());
							} else {
								dataReceived(ois.readObject());
							}
						} catch (SocketTimeoutException ste) {
							log.log(Level.FINE, "Failed to read; will retry");
						} catch (IOException | ClassNotFoundException se) {
							log.log(Level.WARNING, "Failed to read: bad serialization", se);
							stopped = true;
						}
					}
					log.log(Level.INFO, "Client exiting gracefully");
				}
			}, hostname + "Listener");
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				log.log(Level.WARNING, "Error while waiting the thread", e);
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "Error while handling client connection", e);
		}
		socket.close();
	}

	@Override
	public void makeMove(Player player) {
		GameMove move = player.requestMove(localPiece, gameBoard, pieces, rules);
		sendData(new MakeMoveMessage(move));
	}

	@Override
	public void stop() {
		sendData(new StopMessage());
	}

	@Override
	public void restart() {
		sendData(new RestartMessage());
	}

	@Override
	public void sendData(Object message) {
		try {
			oos.writeObject(message);
			oos.flush();
			oos.reset();
		} catch (SocketTimeoutException ste) {
			log.log(Level.INFO, "Failed to write; target must be full!");
		} catch (IOException ioe) {
			log.log(Level.WARNING, "Failed to write: bad serialization");
		}
	}

	@Override
	public void dataReceived(Object message) {
		log.log(Level.INFO, "Message received");
		ObservableMessage controlMessage = (ObservableMessage) message;
		if (gameBoard == null) {
			this.start();
		}
		controlMessage.updateProxy(this);
		log.log(Level.INFO, "Notifying observers");
		for (GameObserver g : observers) {
			controlMessage.notifyMessage(g);
		}
	}

	@Override
	public void stopConnection() {
		this.stopped = true;
	}

	/**
	 * Sets the class's attributes when the connection with the server is
	 * established.
	 * <p>
	 * Establece los atributos de la clase cuando la conexion con el servidor se
	 * ha establecido.
	 * 
	 * @param message
	 *            Initialization message.
	 *            <p>
	 *            Mensaje de inicializacion.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void connectionEstablished(Object message) throws ClassNotFoundException, IOException {
		log.log(Level.INFO, "Connection established");
		InitializationMessage iniMessage = (InitializationMessage) message;
		this.gameFactory = iniMessage.getGameFactory();
		this.localPiece = iniMessage.getLocalPiece();
		this.pieces = iniMessage.getPieces();
		initialize();
	}

	/**
	 * Initializes the game.
	 * <p>
	 * Inicializa el juego.
	 */
	private void initialize() {
		this.game = new Game(gameFactory.gameRules());
		this.rules = gameFactory.gameRules();
		gameFactory.createSwingView(this, this, localPiece, gameFactory.createRandomPlayer(),
				gameFactory.createAIPlayer(aiPlayerAlg));
		initialized = true;
		log.log(Level.INFO, "The ProxyController has been initialized");
	}

	/**
	 * Updates the board of the game.
	 * <p>
	 * Actualiza el tablero de juego.
	 * 
	 * @param board
	 *            The new game board.
	 *            <p>
	 *            EL nuevo tablero de juego.
	 */
	public void updateBoard(Board board) {
		this.gameBoard = board;
	}

	@Override
	public void addObserver(GameObserver o) {
		observers.add(o);

	}

	@Override
	public void removeObserver(GameObserver o) {
		observers.remove(o);
	}

	/**
	 * Shows a dialog with a fatal error that causes the game end.
	 * <p>
	 * Muestra un mensaje de error fatal que causa el cierre del juego.
	 * 
	 * @param msg
	 *            The error message.
	 *            <p>
	 *            El mensaje de error.
	 */
	public void showFatalError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Game error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	/**
	 * Starts the controller from the client side.
	 * <p>
	 * Inicia el controlador del lado del cliente.
	 */
	public void startCtrl() {
		try {
			startConnection(new Socket(hostname, port), timeout);
		} catch (IOException e) {
			log.log(Level.WARNING, e.getMessage());
		}
	}

}
