package es.ucm.fdi.tp.practica6.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.lobby.demo.ServerLauncherExt;
import es.ucm.fdi.tp.practica6.server.ServerWindow.WindowEventListener;

/**
 * A simple multithreaded server. Makes no assumptions regarding data that gets
 * exchanged between servers and clients.
 * <p>
 * Un sencillo servidor multihilo. No hace suposiciones respecto a datos que se
 * intercambian entre servidores y clientes.
 * 
 * @author Raul Murillo & Antonio Valdivia
 *
 */
public class GameServer implements WindowEventListener {

	private static final Logger log = Logger.getLogger(Controller.class.getSimpleName());

	/**
	 * Port on which the server must be started.
	 * <p>
	 * Puerto sobre el que debe iniciarse el servidor.
	 */
	private int port;

	/**
	 * Timeout, in milliseconds, of the server.
	 * <p>
	 * Tiempo límite, en milisegundos, del servidor.
	 */
	private int timeout;

	/**
	 * Indicates if the server has been stopped.
	 * <p>
	 * Indica si el servidor ha sido parado.
	 */
	private volatile boolean stopped;

	/**
	 * Indicates if the game has been started.
	 * <p>
	 * Indica si el juego ha sido iniciado.
	 */
	private boolean started;

	/**
	 * Number of accepted conections.
	 * <p>
	 * Numero de conexiones aceptadas.
	 */
	private int numConnections;

	/**
	 * Server socket of the class.
	 * <p>
	 * Server socket de la clase.
	 */
	private ServerSocket server;

	/**
	 * The game on which the server operates.
	 * <p>
	 * Juego sobre el que opera el servidor.
	 */
	private Game game;

	/**
	 * Controller of the game. Realizes the internal processes of the game.
	 * <p>
	 * Controlador del juego que realiza los procesos internos.
	 */
	private Controller controller;

	/**
	 * Factory of the game.
	 * <p>
	 * Factoria del juego.
	 */
	private GameFactory gameFactory;

	/**
	 * Pieces of the current game.
	 * <p>
	 * Piezas del juego actual.
	 */
	private List<Piece> pieces;

	/**
	 * Piece for adding observers to an started game.
	 * <p>
	 * Pieza apra añadir observadores a un juego empezado.
	 */
	public final static Piece observerPiece = new Piece("Observer");

	/**
	 * Graphical window of the class.
	 * <p>
	 * Ventan grafica de la clase.
	 */
	private ServerWindow swInfo;

	/**
	 * Full constructor of the class.
	 * <p>
	 * Constructor completo de la clase.
	 * 
	 * @param port
	 *            Port on which the server must be started.
	 *            <p>
	 *            Puerto sobre el que debe iniciarse el servidor.
	 * @param timeout
	 *            Timeout, in milliseconds, of the server.
	 *            <p>
	 *            Tiempo límite, en milisegundos, del servidor.
	 * @param gameFactory
	 *            Factory of the game.
	 *            <p>
	 *            Factoria del juego.
	 * @param pieces
	 *            Pieces of the current game.
	 *            <p>
	 *            Piezas del juego actual.
	 */
	public GameServer(int port, int timeout, GameFactory gameFactory, List<Piece> pieces) {
		this.port = port;
		this.timeout = timeout;
		this.gameFactory = gameFactory;
		this.pieces = pieces;
		this.numConnections = 0;
		this.started = false;
		this.swInfo = new ServerWindow(this);
		initializeServer();
	}

	/**
	 * Constructor of the class without timeout.
	 * <p>
	 * Constructor de la clase sin timeout.
	 * 
	 * @param port
	 *            Port on which the server must be started.
	 *            <p>
	 *            Puerto sobre el que debe iniciarse el servidor.
	 * @param gameFactory
	 *            Factory of the game.
	 *            <p>
	 *            Factoria del juego.
	 * @param pieces
	 *            Pieces of the current game.
	 *            <p>
	 *            Piezas del juego actual.
	 */
	public GameServer(int port, GameFactory gameFactory, List<Piece> pieces) {
		this(port, 2000, gameFactory, pieces);
	}

	/**
	 * Constructor of the class without port or timeout. Uses 2020 as server
	 * port.
	 * <p>
	 * Constructor de la clase sin puerto ni timeout. Utiliza 2020 como puerto
	 * de servidor.
	 * 
	 * @param gameFactory
	 *            Factory of the game.
	 *            <p>
	 *            Factoria del juego.
	 * @param pieces
	 *            Pieces of the current game.
	 *            <p>
	 *            Piezas del juego actual.
	 */
	public GameServer(GameFactory gameFactory, List<Piece> pieces) {
		this(2020, 2000, gameFactory, pieces);
	}

	/**
	 * Initializes the game and the controller of the server.
	 * <p>
	 * Inicializa el juego y el controlador del servidor.
	 */
	public void initializeServer() {
		this.game = new Game(gameFactory.gameRules());
		controller = new Controller(game, pieces);
	}

	/**
	 * Starts accepting clients.
	 * <p>
	 * Comineza a aceptar clientes.
	 */
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					server = new ServerSocket(port);
					server.setSoTimeout(timeout);
					new ServerLauncherExt().startChat();
					swInfo.showMessage("Server waiting for connections...");
					log.info("Server waiting for connections");

					// Add players-observers to the game.
					while (!stopped && numConnections < pieces.size()) {
						Socket s = null;
						try {
							s = server.accept();
						} catch (SocketTimeoutException ste) {
							log.log(Level.FINE, "Timeout; checking stop flag and re-accepting");
							continue;
						}
						numConnections++;
						swInfo.showMessage("Connection " + numConnections + " accepted by server.");
						log.info("Connection " + numConnections + " accepted by server");
						ProxyPlayer proxyPlayer = createProxyPlayer("ServerCon-" + numConnections);
						proxyPlayer.startConnection(s, timeout);
						game.addObserver(proxyPlayer);
					}
					swInfo.showMessage("All connections has just been accepted.\n" + "The game is going to start.");
					log.info("All connections has just been accepted.\n The game is going to start.");
					controller.start();
					started = true;

					// Add observers to the game - these ones only can watch and
					// send messages, not play.
					while (!stopped) {
						Socket s = null;
						try {
							s = server.accept();
						} catch (SocketTimeoutException ste) {
							log.log(Level.FINE, "Timeout; checking stop flag and re-accepting");
							continue;
						}
						numConnections++;
						swInfo.showMessage("Connection " + numConnections
								+ " accepted by server.\n(This user can only observe the game)");
						log.info("Connection " + numConnections
								+ " accepted by server\n(This user can only observe the game)");
						ProxyPlayer proxyPlayer = createProxyPlayer("ServerCon-" + numConnections);
						proxyPlayer.startConnection(s, timeout);
						game.addObserver(proxyPlayer);
					}
				} catch (IOException e) {
					log.log(Level.WARNING, "Error while receiving connections", e);
				}
			}

		}, "Server").start();
	}

	/**
	 * Creates a ProxyPlayer as a server endpoint of the connection.
	 * <p>
	 * Crea un {@link ProxyPlayer} endpoint de la conexion.
	 * 
	 * @param string
	 *            The name of the endpoint.
	 *            <p>
	 *            El nombre del endpoint.
	 * @return An endponit for the server.
	 *         <p>
	 *         Un endpoint para el servidor.
	 */
	private ProxyPlayer createProxyPlayer(String string) {
		return new ProxyPlayer(string, controller, gameFactory, pieces, getLocalPiece());
	}

	/**
	 * Gives a piece for the next player, according to its connection order.
	 * <p>
	 * Proporciona una pieza de juego para el siguiente jugador, segun su orden
	 * de conexion.
	 * 
	 * @return The piece of the next player.
	 *         <p>
	 *         La pieza para le siguiente jugador.
	 */
	public Piece getLocalPiece() {
		if (!started)
			return pieces.get(numConnections - 1);
		else
			return observerPiece;
	}

	@Override
	public void stopPressed() {
		swInfo.enableButton(false);
		game.stop();
		swInfo.showMessage("The game has been stopped.");
		swInfo.showMessage("Server will be closed in 3 seconds.");
		new SwingWorker<Object, Object>() {

			@Override
			protected Object doInBackground() throws Exception {
				Utils.sleep(3000);
				server.close();
				System.exit(0);
				return null;
			}
		}.execute();
	}

}
