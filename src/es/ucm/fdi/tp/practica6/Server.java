package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class Server{

	

    private static final Logger log = Logger.getLogger(Controller.class.getSimpleName());

    private int port;
    private int timeout;
    private volatile boolean stopped;
    private int numConnections;

    private Game game;
    private Controller controller;
    private GameFactory gameFactory;
    private List<Piece> pieces;
    
    
    public Server(int port, int timeout, GameFactory gameFactory, List<Piece> pieces) {
		this.port = port;
        this.timeout = timeout;
        this.gameFactory = gameFactory;
        this.pieces = pieces;
        this.numConnections = 0;
        initializeServer();
	}
    public Server(GameFactory gameFactory, List<Piece> pieces) {
        this(2020, 2000, gameFactory, pieces);
    }
    
    public void initializeServer(){
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
                    ServerSocket server = new ServerSocket(port);
                    server.setSoTimeout(timeout);
                    log.info("AbstractServer waiting for connections");
                    while (!stopped && game.getState() == game.getState().Starting) {
                        Socket s = null;
                        try {
                            s = server.accept();
                        } catch (SocketTimeoutException ste) {
                            log.log(Level.FINE, "Timeout; checking stop flag and re-accepting");
                            continue;
                        }
                        numConnections++;
                        log.info("Connection " + numConnections + " accepted by server");
                        ProxyPlayer proxyPlayer = createProxyPlayer("ServerCon-"+numConnections);
                        proxyPlayer.start(s);
                        proxyPlayer.newConnection(game, pieces, gameFactory, getLocalPiece());
                    }
                   server.close();
                } catch (IOException e) {
                    log.log(Level.WARNING, "Error while receiving connections", e);
                }
            }

        }, "Server").start();
    }

	



private ProxyPlayer createProxyPlayer(String string) {
	return new ProxyPlayer(string, controller);
}

public Piece getLocalPiece(){
	return pieces.get(numConnections-1);
}

}
