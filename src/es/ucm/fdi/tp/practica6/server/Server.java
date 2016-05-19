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

public class Server implements WindowEventListener {

	private static final Logger log = Logger.getLogger(Controller.class.getSimpleName());

	private int port;
	private int timeout;
	private volatile boolean stopped;
	private int numConnections;
	private ServerSocket server;
	private boolean started;

	private Game game;
	private Controller controller;
	private GameFactory gameFactory;
	private List<Piece> pieces;
	public final static Piece observerPiece = new Piece("Observer");
	private ServerWindow swInfo;

	public Server(int port, int timeout, GameFactory gameFactory, List<Piece> pieces) {
		this.port = port;
		this.timeout = timeout;
		this.gameFactory = gameFactory;
		this.pieces = pieces;
		this.numConnections = 0;
		this.started = false;
		this.swInfo = new ServerWindow(this);
		initializeServer();
	}

	public Server(int port, GameFactory gameFactory, List<Piece> pieces) {
		this(port, 2000, gameFactory, pieces);
	}

	public Server(GameFactory gameFactory, List<Piece> pieces) {
		this(2020, 2000, gameFactory, pieces);
	}

	public void initializeServer() {
		this.game = new Game(gameFactory.gameRules());
		controller = new Controller(game, pieces);
	}

	/**
	 * Starts accepting clients
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
					while (!stopped) {
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
				} catch (IOException e) {
					log.log(Level.WARNING, "Error while receiving connections", e);
				}
			}

		}, "Server").start();
	}

	private ProxyPlayer createProxyPlayer(String string) {
		return new ProxyPlayer(string, controller, gameFactory, pieces, getLocalPiece());
	}

	public Piece getLocalPiece() {
		if(!started)return pieces.get(numConnections - 1);
		else  return observerPiece;
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
