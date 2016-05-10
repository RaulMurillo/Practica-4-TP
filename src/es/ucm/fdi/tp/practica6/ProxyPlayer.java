package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.ProxyController.ClientMessage;

public class ProxyPlayer extends Player implements GameObserver {
	
	private static final Logger log = Logger.getLogger(Controller.class.getSimpleName());
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private volatile boolean stopped;
	
	private String hostname;
	
	private Controller controller;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	GameMove move;
		
	public static abstract class ControlMessage implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public void notifyMessage(GameObserver gameObserver) {}
		public void updateProxy(ProxyController proxyController) {}
		public void initializeConnection(ProxyController proxyController){}
	}
	
	public ProxyPlayer(Controller controller){
		this("localserver", controller);
	};
	
	public ProxyPlayer(String hostname, Controller controller){
		this.hostname = hostname;
		this.controller = controller;
	}
	
	
	 public void start(final Socket socket) throws IOException {
	        try {
	            oos = new ObjectOutputStream(socket.getOutputStream());
	            
	            new Thread(new Runnable() {
	                public void run() {
	                    try {
	                        ois = new ObjectInputStream(socket.getInputStream());
	                        
	                    } catch (IOException e) {
	                        log.log(Level.WARNING, "Failed to read: could not create object input stream");
	                    }
	                    while ( ! stopped) {
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
	                    try {
							socket.close();
						} catch (IOException e) {
							log.log(Level.WARNING, e.getMessage());
						}
	                }
	            }, hostname+"Listener").start();
	        } catch (IOException e) {
	            log.log(Level.WARNING, "Error while handling client connection", e);
	        }
	    }

	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		return  move;
	}

	public void setMove(GameMove move){
		this.move = move;
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		sendData(new ControlMessage(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			private Board b = board;
			@Override
			public void notifyMessage(GameObserver gameObserver) {
				gameObserver.onGameStart(board, gameDesc, pieces, turn);
			}
			@Override
			public void updateProxy(ProxyController proxyController) {
				proxyController.setBoard(b);
			}
		});
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		sendData(new ControlMessage(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			private Board b = board;

			@Override
			public void notifyMessage(GameObserver gameObserver) {
				gameObserver.onGameOver(board, state, winner);
			}
			@Override
			public void updateProxy(ProxyController proxyController) {
				proxyController.setBoard(b);
			}
		});
		
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		sendData(new ControlMessage(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			private Board b = board;
			
			@Override
			public void notifyMessage(GameObserver gameObserver) {
				gameObserver.onMoveStart(board, turn);
			}
			@Override
			public void updateProxy(ProxyController proxyController) {
				proxyController.setBoard(b);
			}
		});
		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		sendData(new ControlMessage(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private Board b = board;
			
			@Override
			public void notifyMessage(GameObserver gameObserver) {
				gameObserver.onMoveEnd(board, turn, success);
			}
			@Override
			public void updateProxy(ProxyController proxyController) {
				proxyController.setBoard(b);
			}
		});
		
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		sendData(new ControlMessage(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private Board b = board;
			
			@Override
			public void notifyMessage(GameObserver gameObserver) {
				gameObserver.onChangeTurn(board, turn);
			}
			@Override
			public void updateProxy(ProxyController proxyController) {
				proxyController.setBoard(b);
			}
		});
		
	}

	@Override
	public void onError(String msg) {
		sendData(new ControlMessage(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void notifyMessage(GameObserver gameObserver) {
				gameObserver.onError(msg);
			}
		});
		
	}
	
	public void newConnection(Game g, List<Piece> p, GameFactory gf, Piece lp) {
		sendData(new ControlMessage(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			Game game = g;
			List<Piece> pieces =p ;
			GameFactory gameFactory = gf;
			Piece localPiece = lp;
			@Override
			public void initializeConnection(ProxyController proxyController){
				proxyController.connectionEstablished(game, pieces, gameFactory, localPiece);
			}
			
		});
		
	}
	
	public void sendData(ControlMessage message){
		 try {
	            oos.writeObject(message);
	        } catch (SocketTimeoutException ste) {
	            log.log(Level.INFO, "Failed to write; target must be full!");
	        } catch (IOException ioe) {
	            log.log(Level.WARNING, "Failed to write: bad serialization");
	        }
	}
	
	public void dataReceived(Object message){
		((ClientMessage) message).notifyMessage(controller, this);
	}


}
