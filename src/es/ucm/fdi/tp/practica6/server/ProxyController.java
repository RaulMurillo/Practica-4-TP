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
import es.ucm.fdi.tp.practica6.server.ProxyPlayer.InitializationMessage;
import es.ucm.fdi.tp.practica6.server.ProxyPlayer.ObservableMessage;

public class ProxyController extends Controller implements Observable<GameObserver>, SocketEndpoint {

	private static final Logger log = Logger.getLogger(Controller.class.getSimpleName());

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private volatile boolean stopped;

	private String hostname;
	private int port;
	private int timeout;

	private List<GameObserver> observers;
	private Piece localPiece;
	private GameFactory gameFactory;
	private GameRules rules;
	private Board gameBoard;
	private boolean initialized;
	private AIAlgorithm aiPlayerAlg;

	public ProxyController() {
		this("localhost", 2020, 2000, null);
	}

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

	public static abstract class ClientMessage implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6602000723654830197L;

		public void notifyMessage(ProxyPlayer p) {
		}
	}

	public static class MakeMoveMessage extends ClientMessage {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3336919685539615536L;
		GameMove move;

		public MakeMoveMessage(GameMove move) {
			this.move = move;
		}

		@Override
		public void notifyMessage(ProxyPlayer p) {
			p.setMove(move);
			p.ctrlMakeMove(p);
		}
	}

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

	public void startConnection(Socket socket, int timeout) throws IOException {
		// Socket socket = new Socket(hostname, port);
		try {
			socket.setSoTimeout(timeout);
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
							if (!initialized)
								connectionEstablished(ois.readObject());
							else
								dataReceived(ois.readObject());
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
			log.log(Level.WARNING, "Error while handling client connection", e);
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

	public void stopConnection() {
		this.stopped = true;
	}

	public void connectionEstablished(Object message) throws ClassNotFoundException, IOException {
		log.log(Level.INFO, "Connection established");
		InitializationMessage iniMessage = (InitializationMessage) message;
		this.gameFactory = iniMessage.getGameFactory();
		this.localPiece = iniMessage.getLocalPiece();
		this.pieces = iniMessage.getPieces();
		initialize();
	}

	public void initialize() {
		this.game = new Game(gameFactory.gameRules());
		this.rules = gameFactory.gameRules();
		gameFactory.createSwingView(this, this, localPiece, gameFactory.createRandomPlayer(),
				gameFactory.createAIPlayer(aiPlayerAlg));
		initialized = true;
		log.log(Level.INFO, "The ProxyController has been initialized");
	}

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

	public void showFatalError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Game error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	public void startCtrl() {
		try {
			startConnection(new Socket(hostname, port), timeout);
		} catch (IOException e) {
			log.log(Level.WARNING, e.getMessage());
		}
	}

}
