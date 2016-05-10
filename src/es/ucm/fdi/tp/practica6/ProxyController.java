package es.ucm.fdi.tp.practica6;

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

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.ataxx.AtaxxFactoryExt;
import es.ucm.fdi.tp.practica6.ProxyPlayer.ControlMessage;

public class ProxyController extends Controller {

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
	
	//
	private boolean initialized = false;

	public ProxyController() {
		this("localhost", 2020, 2000);
	}

	public ProxyController(String hostname, int port, int timeout) {
		super(null, null);
		this.hostname = hostname;
		this.port = port;
		this.timeout = timeout;
		this.stopped = false;
	}

	public interface ClientMessage extends Serializable {
		public void notifyMessage(Controller controller, ProxyPlayer p);
	}

	// MIRAR NOMBRE
	public void startF() throws IOException {
		Socket socket = new Socket(hostname, port);
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
							log.log(Level.WARNING, "Failed to read: bad serialization");
							stopped = true;
						}
					}
					log.log(Level.INFO, "Client exiting gracefully");
				}
			}, hostname + "Listener").start();
		} catch (IOException e) {
			log.log(Level.WARNING, "Error while handling client connection", e);
		}
		socket.close();
	}

	@Override
	public void makeMove(Player player) {
		sendData(new ClientMessage() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			GameMove move = player.requestMove(game.getTurn(), gameBoard, pieces, rules);

			@Override
			public void notifyMessage(Controller controller, ProxyPlayer p) {
				p.setMove(move);
				controller.makeMove(p);
			}
		});
	}

	@Override
	public void stop() {
		sendData(new ClientMessage() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void notifyMessage(Controller controller, ProxyPlayer p) {
				controller.stop();
			}

		});
	}

	@Override
	public void restart() {
		sendData(new ClientMessage() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void notifyMessage(Controller controller, ProxyPlayer p) {
				controller.restart();
			}

		});
	}

	public void sendData(ClientMessage message) {
		try {
			oos.writeObject(message);
		} catch (SocketTimeoutException ste) {
			log.log(Level.INFO, "Failed to write; target must be full!");
		} catch (IOException ioe) {
			log.log(Level.WARNING, "Failed to write: bad serialization");
		}
	}

	public void dataReceived(Object message) {
		ControlMessage controlMessage = (ControlMessage) message;
		if (!initialized) {
			log.log(Level.INFO, "Initializing connection");
			controlMessage.initializeConnection(this);
			
			initialized = true;
		} else {
			controlMessage.updateProxy(this);
			for (GameObserver g : observers) {
				controlMessage.notifyMessage(g);
			}
		}
	}

	// MIRAR NOMBRE
	public void stopF() {
		this.stopped = true;
	}

	public void connectionEstablished(Game game, List<Piece> pieces, GameFactory gameFactory, Piece localPiece) {
		this.gameFactory = gameFactory;
		this.localPiece = localPiece;
		this.game = game;
		this.pieces = pieces;
		initialize();
	}

	public void initialize() {
		this.rules = gameFactory.gameRules();
		gameFactory.createSwingView(game, this, localPiece, gameFactory.createRandomPlayer(),
				gameFactory.createAIPlayer(null));
	}

	public void setBoard(Board gameBoard) {
		this.gameBoard = gameBoard;
	}

	public static void main(String... args) {
		ProxyController ctrl = new ProxyController();
		try {
			ctrl.startF();
		} catch (IOException e) {
			log.log(Level.WARNING, e.getMessage());
		}
	}
}
