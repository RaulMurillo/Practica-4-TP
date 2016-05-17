package es.ucm.fdi.tp.practica6;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.ucm.fdi.tp.basecode.bgame.control.ConsoleCtrl;
import es.ucm.fdi.tp.basecode.bgame.control.ConsoleCtrlMVC;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica.connectn.ConnectNFactoryExt;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactoryExt;
import es.ucm.fdi.tp.practica6.attt.AdvancedTTTFactoryExt;
import es.ucm.fdi.tp.practica6.server.ProxyController;
import es.ucm.fdi.tp.practica6.server.Server;
import es.ucm.fdi.tp.practica6.ttt.TicTacToeFactoryExt;

/**
 * This is the class with the main method for the board games application.
 * 
 * It uses the Commons-CLI library for parsing command-line arguments: the game
 * to play, the players list, etc.. More information is available at
 * {@link https://commons.apache.org/proper/commons-cli/}.
 * 
 * <p>
 * Esta es la clase con el metodo main de inicio del programa. Se utiliza la
 * libreria Commons-CLI para leer argumentos de la linea de ordenes: el juego al
 * que se quiere jugar y la lista de jugadores. Puedes encontrar mas información
 * sobre esta libreria en {@link https://commons.apache.org/proper/commons-cli/}
 * .
 */
public class Main {

	final private static Logger log = Logger.getLogger("Main");

	/**
	 * The possible views.
	 * <p>
	 * Vistas disponibles.
	 */
	enum ViewInfo {
		WINDOW("window", "Swing"), CONSOLE("console", "Console");

		private String id;
		private String desc;

		ViewInfo(String id, String desc) {
			this.id = id;
			this.desc = desc;
		}

		public String getId() {
			return id;
		}

		public String getDesc() {
			return desc;
		}

		@Override
		public String toString() {
			return id;
		}
	}

	/**
	 * The available games.
	 * <p>
	 * Juegos disponibles.
	 */
	enum GameInfo {
		CONNECTN("cn", "ConnectN"), TicTacToe("ttt", "Tic-Tac-Toe"), AdvancedTicTacToe("attt",
				"Advanced Tic-Tac-Toe"), ATAXX("ataxx", "Ataxx");

		private String id;
		private String desc;

		GameInfo(String id, String desc) {
			this.id = id;
			this.desc = desc;
		}

		public String getId() {
			return id;
		}

		public String getDesc() {
			return desc;
		}

		@Override
		public String toString() {
			return id;
		}

	}

	/**
	 * Player modes (manual, random, etc.)
	 * <p>
	 * Modos de juego.
	 */
	enum PlayerMode {
		MANUAL("m", "Manual"), RANDOM("r", "Random"), AI("a", "Automatics");

		private String id;
		private String desc;

		PlayerMode(String id, String desc) {
			this.id = id;
			this.desc = desc;
		}

		public String getId() {
			return id;
		}

		public String getDesc() {
			return desc;
		}

		@Override
		public String toString() {
			return id;
		}
	}

	/**
	 * Aplication modes (normal, client, server).
	 * <p>
	 * Modos de la aplicacion.
	 */
	enum AppMode {
		NORMAL("normal", "Normal"), CLIENT("client", "Client"), SERVER("server", "Server");

		private String id;
		private String desc;

		AppMode(String id, String desc) {
			this.id = id;
			this.desc = desc;
		}

		public String getId() {
			return id;
		}

		public String getDesc() {
			return desc;
		}

		@Override
		public String toString() {
			return id;
		}
	}

	/**
	 * Default game to play.
	 * <p>
	 * Juego por defecto.
	 */
	final private static GameInfo DEFAULT_GAME = GameInfo.ATAXX;

	/**
	 * default view to use.
	 * <p>
	 * Vista por defecto.
	 */
	final private static ViewInfo DEFAULT_VIEW = ViewInfo.WINDOW;

	/**
	 * Default player mode to use.
	 * <p>
	 * Modo de juego por defecto.
	 */
	final private static PlayerMode DEFAULT_PLAYERMODE = PlayerMode.MANUAL;

	/**
	 * Default app mode to use.
	 * <p>
	 * Modo de aplicacion por defecto.
	 */
	final private static AppMode DEFAULT_APPMODE = AppMode.NORMAL;

	/**
	 * Default port to use when playing on-line.
	 * <p>
	 * Numero de puerto por defecto cuando se juega en red.
	 */
	final private static int DEFAULT_PORT = 2000;

	/**
	 * Default host name to use when playing on-line.
	 * <p>
	 * Nombre de la maquina por defecto sobre la que se está ejecutando el
	 * servidor cuando se juega en red.
	 */
	final private static String DEFAULT_HOST = "localhost";

	/**
	 * Default timeout to use when playing on-line.
	 * <p>
	 * Limite de tiempo por defecto cuando se juega en red.
	 */
	final private static int DEFAULT_TIMEOUT = 2000;

	/**
	 * Default search tree depth when using a MinMax algorithm.
	 * <p>
	 * Profundidad por defecto del arbol de busqueda cuando se emplea un
	 * algoritmo MinMax.
	 */
	final private static int DEFAULT_DEPTH = 3;

	/**
	 * This field includes a game factory that is constructed after parsing the
	 * command-line arguments. Depending on the game selected with the -g option
	 * (by default {@link #DEFAULT_GAME}).
	 * 
	 * <p>
	 * Este atributo incluye una factoria de juego que se crea despues de
	 * extraer los argumentos de la linea de ordenes. Depende del juego
	 * seleccionado con la opcion -g (por defecto, {@link #DEFAULT_GAME}).
	 */
	private static GameFactory gameFactory;

	/**
	 * List of pieces provided with the -p option, or taken from
	 * {@link GameFactory#createDefaultPieces()} if this option was not
	 * provided.
	 * 
	 * <p>
	 * Lista de fichas proporcionadas con la opcion -p, u obtenidas de
	 * {@link GameFactory#createDefaultPieces()} si no hay opcion -p.
	 */
	private static List<Piece> pieces;

	/**
	 * A list of players. The i-th player corresponds to the i-th piece in the
	 * list {@link #pieces}. They correspond to what is provided in the -p
	 * option (or using the default value {@link #DEFAULT_PLAYERMODE}).
	 * 
	 * <p>
	 * Lista de jugadores. El jugador i-esimo corresponde con la ficha i-esima
	 * de la lista {@link #pieces}. Esta lista contiene lo que se proporciona en
	 * la opcion -p (o el valor por defecto {@link #DEFAULT_PLAYERMODE}).
	 */
	private static List<PlayerMode> playerModes;

	/**
	 * The view to use. Depending on the selected view using the -v option or
	 * the default value {@link #DEFAULT_VIEW} if this option was not provided.
	 * 
	 * <p>
	 * Vista a utilizar. Dependiendo de la vista seleccionada con la opcion -v o
	 * el valor por defecto {@link #DEFAULT_VIEW} si el argumento -v no se
	 * proporciona.
	 */
	private static ViewInfo view;

	/**
	 * {@code true} if the option -m was provided, to use a separate view for
	 * each piece, and {@code false} otherwise.
	 * 
	 * <p>
	 * {@code true} si se incluye la opcion -m, para utilizar una vista separada
	 * por cada ficha, o {@code false} en caso contrario.
	 */
	private static boolean multiviews;

	/**
	 * Number of rows provided with the option -d ({@code null} if not
	 * provided).
	 * 
	 * <p>
	 * Numero de filas proporcionadas con la opcion -d, o {@code null} si no se
	 * incluye la opcion -d.
	 */
	private static Integer dimRows;
	/**
	 * Number of columns provided with the option -d ({@code null} if not
	 * provided).
	 * 
	 * <p>
	 * Numero de columnas proporcionadas con la opcion -d, o {@code null} si no
	 * se incluye la opcion -d.
	 * 
	 */
	private static Integer dimCols;

	/**
	 * The algorithm to be used by the automatic player. It can be minmax,
	 * minmaxab or none (by default).
	 * 
	 * <p>
	 * Algoritmo a utilizar por el jugador automatico. Puede ser minmax,
	 * minmaxab o none (por defecto).
	 */
	private static AIAlgorithm aiPlayerAlg;

	/**
	 * Number of obstacles provided with the option -o, or ({@code null} if not
	 * provided).
	 * 
	 * <p>
	 * Numero de obstacles proporcionadas con la opcion -o, o {@code null} si no
	 * se incluye la opcion -o.
	 * 
	 */
	private static Integer obstacles;

	/**
	 * The way you want to start the game application . Take one of the
	 * following values ​​: normal, client or server
	 * <p>
	 * El modo en el que se quiere iniciar la aplicación de juego. Toma uno de
	 * los siguientes valores: normal, client o server.
	 */
	private static AppMode appMode;

	/**
	 * Number of port provided with the option -sp, or ({@code DEFAULT_PORT} if
	 * not provided).
	 * <p>
	 * Numero de puerto proporcionado con la opcion -sp, o {@code DEFAULT_PORT}
	 * si no se incluye la opcion -sp.
	 * 
	 */
	private static Integer serverPort;

	/**
	 * Name of machine provided with the option -sh, or ({@code DEFAULT_HOST} if
	 * not provided).
	 * <p>
	 * Nombre de la maquina proporcionado con la opcion -sh, o
	 * {@code DEFAULT_HOST} si no se incluye la opcion -sh.
	 * 
	 */
	private static String serverHost;

	/**
	 * Processes the command-line arguments and modify the fields of this class
	 * with corresponding values. E.g., the factory, the pieces, etc.
	 *
	 * <p>
	 * Procesa la linea de ordenes del programa y crea los objetos necesarios
	 * para los atributos de esta clase. Por ejemplo, la factoria, las fichas,
	 * etc.
	 * 
	 * 
	 * @param args
	 *            Command line arguments.
	 * 
	 *            <p>
	 *            Lista de argumentos de la linea de ordenes.
	 * 
	 * 
	 */
	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = new Options();
		cmdLineOptions.addOption(constructHelpOption()); // -h or --help
		cmdLineOptions.addOption(constructGameOption()); // -g or --game
		cmdLineOptions.addOption(constructViewOption()); // -v or --view
		cmdLineOptions.addOption(constructMlutiViewOption()); // -m or
																// --multiviews
		cmdLineOptions.addOption(constructPlayersOption()); // -p or --players
		cmdLineOptions.addOption(constructDimensionOption()); // -d or --dim
		cmdLineOptions.addOption(constructObstaclesOption()); // -o or
																// --obstacles
		cmdLineOptions.addOption(constructAppModeOption()); // -am or --app-mode
		cmdLineOptions.addOption(constructServerPortOption()); // -sp or
																// --server-port
		cmdLineOptions.addOption(constructServerHostOption()); // -sh or
																// --server-host
		cmdLineOptions.addOption(constructAIAlgorithmOption()); // -aialg or
																// --ai-algorithm
		cmdLineOptions.addOption(constructMinMaxDepthOption()); // -md or
																// --minmax-depth

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseDimOptionn(line);
			parseObsOption(line);
			parseGameOption(line);
			parseViewOption(line);
			parseMultiViewOption(line);
			parsePlayersOptions(line);
			parseAppModeOption(line);
			parseAIAlgOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException | GameError e) {
			// new Piece(...) might throw GameError exception
			log.log(Level.SEVERE, "Error leyendo args", e);
			System.exit(1);
		}

	}

	/**
	 * Builds the multiview (-m or --multiviews) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -m.
	 * 
	 * @return CLI {@link {@link Option} for the multiview option.
	 */

	private static Option constructMlutiViewOption() {
		return new Option("m", "multiviews", false,
				"Create a separate view for each player (valid only when using the " + ViewInfo.WINDOW + " view)");
	}

	/**
	 * Parses the multiview option (-m or --multiview). It sets the value of
	 * {@link #multiviews} accordingly.
	 * 
	 * <p>
	 * Extrae la opcion multiview (-m) y asigna el valor de {@link #multiviews}.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 */
	private static void parseMultiViewOption(CommandLine line) {
		multiviews = line.hasOption("m");
	}

	/**
	 * Builds the view (-v or --view) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -v.
	 * 
	 * @return CLI {@link Option} for the view option.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructViewOption() {
		String optionInfo = "The view to use ( ";
		for (ViewInfo i : ViewInfo.values()) {
			optionInfo += i.getId() + " [for " + i.getDesc() + "] ";
		}
		optionInfo += "). By defualt, " + DEFAULT_VIEW.getId() + ".";
		Option opt = new Option("v", "view", true, optionInfo);
		opt.setArgName("view identifier");
		return opt;
	}

	/**
	 * Parses the view option (-v or --view). It sets the value of {@link #view}
	 * accordingly.
	 * 
	 * <p>
	 * Extrae la opcion view (-v) y asigna el valor de {@link #view}.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided (the valid values are those
	 *             of {@link ViewInfo}.
	 */
	private static void parseViewOption(CommandLine line) throws ParseException {
		String viewVal = line.getOptionValue("v", DEFAULT_VIEW.getId());
		// view type
		for (ViewInfo v : ViewInfo.values()) {
			if (viewVal.equals(v.getId())) {
				view = v;
			}
		}
		if (view == null) {
			throw new ParseException("Uknown view '" + viewVal + "'");
		}
	}

	/**
	 * Builds the players (-p or --player) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -p.
	 * 
	 * @return CLI {@link Option} for the list of pieces/players.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructPlayersOption() {
		String optionInfo = "A player has the form A:B (or A), where A is sequence of characters (without any whitespace) to be used for the piece identifier, and B is the player mode (";
		for (PlayerMode i : PlayerMode.values()) {
			optionInfo += i.getId() + " [for " + i.getDesc() + "] ";
		}
		optionInfo += "). If B is not given, the default mode '" + DEFAULT_PLAYERMODE.getId()
				+ "' is used. If this option is not given a default list of pieces from the corresponding game is used, each assigmed the mode '"
				+ DEFAULT_PLAYERMODE.getId() + "'.";

		Option opt = new Option("p", "players", true, optionInfo);
		opt.setArgName("list of players");
		return opt;
	}

	/**
	 * Parses the players/pieces option (-p or --players). It sets the value of
	 * {@link #pieces} and {@link #playerModes} accordingly.
	 *
	 * <p>
	 * Extrae la opcion players (-p) y asigna el valor de {@link #pieces} y
	 * {@link #playerModes}.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided (@see
	 *             {@link #constructPlayersOption()}).
	 *             <p>
	 *             Si se proporciona un valor invalido (@see
	 *             {@link #constructPlayersOption()}).
	 */
	private static void parsePlayersOptions(CommandLine line) throws ParseException {

		String playersVal = line.getOptionValue("p");

		if (playersVal == null) {
			// if no -p option, we take the default pieces from the
			// corresponding
			// factory, and for each one we use the default player mode.
			pieces = gameFactory.createDefaultPieces();
			playerModes = new ArrayList<PlayerMode>();
			for (int i = 0; i < pieces.size(); i++) {
				playerModes.add(DEFAULT_PLAYERMODE);
			}
		} else {
			pieces = new ArrayList<Piece>();
			playerModes = new ArrayList<PlayerMode>();
			String[] players = playersVal.split(",");
			for (String player : players) {
				String[] playerInfo = player.split(":");
				if (playerInfo.length == 1) { // only the piece name is provided
					pieces.add(new Piece(playerInfo[0]));
					playerModes.add(DEFAULT_PLAYERMODE);
				} else if (playerInfo.length == 2) { // piece name and mode are
														// provided
					pieces.add(new Piece(playerInfo[0]));
					PlayerMode selectedMode = null;
					for (PlayerMode mode : PlayerMode.values()) {
						if (mode.getId().equals(playerInfo[1])) {
							selectedMode = mode;
						}
					}
					if (selectedMode != null) {
						playerModes.add(selectedMode);
					} else {
						throw new ParseException("Invalid player mode in '" + player + "'");
					}
				} else {
					throw new ParseException("Invalid player information '" + player + "'");
				}
			}
		}
	}

	/**
	 * Builds the game (-g or --game) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -g.
	 * 
	 * @return CLI {@link {@link Option} for the game option.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */

	private static Option constructGameOption() {
		String optionInfo = "The game to play ( ";
		for (GameInfo i : GameInfo.values()) {
			optionInfo += i.getId() + " [for " + i.getDesc() + "] ";
		}
		optionInfo += "). By defualt, " + DEFAULT_GAME.getId() + ".";
		Option opt = new Option("g", "game", true, optionInfo);
		opt.setArgName("game identifier");
		return opt;
	}

	/**
	 * Parses the game option (-g or --game). It sets the value of
	 * {@link #gameFactory} accordingly. Usually it requires that
	 * {@link #parseDimOptionn(CommandLine)} has been called already to parse
	 * the dimension option.
	 * 
	 * <p>
	 * Extrae la opcion de juego (-g). Asigna el valor del atributo
	 * {@link #gameFactory}. Normalmente necesita que se haya llamado antes a
	 * {@link #parseDimOptionn(CommandLine)} para extraer la dimension del
	 * tablero.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided (the valid values are those
	 *             of {@link GameInfo}).
	 *             <p>
	 *             Si se proporciona un valor invalido (Los valores validos son
	 *             los de {@link GameInfo}).
	 */
	private static void parseGameOption(CommandLine line) throws ParseException {
		String gameVal = line.getOptionValue("g", DEFAULT_GAME.getId());
		GameInfo selectedGame = null;

		for (GameInfo g : GameInfo.values()) {
			if (g.getId().equals(gameVal)) {
				selectedGame = g;
				break;
			}
		}

		if (selectedGame == null) {
			throw new ParseException("Uknown game '" + gameVal + "'");
		}

		switch (selectedGame) {
		case AdvancedTicTacToe:
			gameFactory = new AdvancedTTTFactoryExt();
			break;
		case ATAXX:
			if (dimRows != null && dimCols != null && dimRows == dimCols) {
				if (obstacles != null) {
					gameFactory = new AtaxxFactoryExt(dimRows, obstacles);
				} else {
					gameFactory = new AtaxxFactoryExt(dimRows, 0);
				}
			} else {
				if (obstacles != null) {
					gameFactory = new AtaxxFactoryExt(obstacles);
				} else {
					gameFactory = new AtaxxFactoryExt();
				}
			}
			break;
		case CONNECTN:
			if (dimRows != null && dimCols != null && dimRows == dimCols) {
				gameFactory = new ConnectNFactoryExt(dimRows);
			} else {
				gameFactory = new ConnectNFactoryExt();
			}
			break;
		case TicTacToe:
			gameFactory = new TicTacToeFactoryExt();
			break;
		default:
			throw new UnsupportedOperationException("Something went wrong! This program point should be unreachable!");
		}

	}

	/**
	 * Builds the dimension (-d or --dim) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -d.
	 * 
	 * @return CLI {@link {@link Option} for the dimension.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructDimensionOption() {
		return new Option("d", "dim", true,
				"The board size (if allowed by the selected game). It must has the form ROWSxCOLS.");
	}

	/**
	 * Parses the dimension option (-d or --dim). It sets the value of
	 * {@link #dimRows} and {@link #dimCols} accordingly. The dimension is
	 * ROWSxCOLS.
	 * 
	 * <p>
	 * Extrae la opcion dimension (-d). Asigna el valor de los atributos
	 * {@link #dimRows} and {@link #dimCols}. La dimension es de la forma
	 * ROWSxCOLS.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided.
	 *             <p>
	 *             Si se proporciona un valor invalido.
	 */
	private static void parseDimOptionn(CommandLine line) throws ParseException {
		String dimVal = line.getOptionValue("d");
		if (dimVal != null) {
			try {
				String[] dim = dimVal.split("x");
				if (dim.length == 2) {
					dimRows = Integer.parseInt(dim[0]);
					dimCols = Integer.parseInt(dim[1]);
				} else {
					throw new ParseException("Invalid dimension: " + dimVal);
				}
			} catch (NumberFormatException e) {
				throw new ParseException("Invalid dimension: " + dimVal);
			}
		}

	}

	/**
	 * Builds the obstacles (-o or --obstacles) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -o.
	 * 
	 * @return CLI {@link {@link Option} for the obstacles.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructObstaclesOption() {
		return new Option("o", "obstacles", true, "The obstacles in board " + "(if allowed by the selected game).");
	}

	/**
	 * Parses the obstacles option (-o or --obstacles). It sets the value of
	 * {@link #obstacles} accordingly.
	 * 
	 * <p>
	 * Extrae la opcion obstaculos (-o). Asigna el valor de {@link #obstacles}.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided.
	 *             <p>
	 *             Si se proporciona un valor invalido.
	 */
	private static void parseObsOption(CommandLine line) throws ParseException {
		String obstaclesVal = line.getOptionValue("o");
		if (obstaclesVal != null) {
			try {
				obstacles = Integer.parseInt(obstaclesVal);
				obstacles = (obstacles / 4) * 4;
				if ((obstacles > 24) || (obstacles > 12 && (dimRows == null || dimRows == 5))) {
					throw new GameError("Invalid number of obstacles.");
				}
			} catch (NumberFormatException e) {
				throw new ParseException("Invalid obstacles: " + obstaclesVal);
			}
		}
	}

	/**
	 * Builds the app mode (-am or --app-mode) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -am.
	 * 
	 * @return CLI {@link {@link Option} for the app mode.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructAppModeOption() {
		return new Option("am", "app-mode", true, "The mode you want to start the game application. "
				+ "Take one of the following values ​​: normal, client or server (normal by default).");
	}

	/**
	 * Parses the app mode option (-am or --app-mode). It sets the value of
	 * {@link #appMode} accordingly.
	 * 
	 * <p>
	 * Extrae la opcion de modo de la aplicacion (-am). Asigna el valor de
	 * {@link #appMode}.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided.
	 *             <p>
	 *             Si se proporciona un valor invalido.
	 */
	private static void parseAppModeOption(CommandLine line) throws ParseException {
		String appModeVal = line.getOptionValue("am", DEFAULT_APPMODE.getId());
		// app mode type
		for (AppMode m : AppMode.values()) {
			if (appModeVal.equals(m.getId())) {
				appMode = m;
				break;
			}
		}
		if (appMode == null) {
			throw new ParseException("Uknown mode '" + appModeVal + "'");
		} else if (appMode != AppMode.NORMAL) {
			switch (appMode) {
			case CLIENT:
				parseServerHostOption(line);
			case SERVER:
				parseServerPortOption(line);
				break;
			default:
				throw new UnsupportedOperationException(
						"Something went wrong! This program point should be unreachable!");
			}
		}
	}

	/**
	 * Builds the server port (-sp or --server-port) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -sp.
	 * 
	 * @return CLI {@link {@link Option} for the server port.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructServerPortOption() {
		return new Option("sp", "server-port", true, "The port on which the server "
				+ "must be started or on which the server is listening and should connect the client.");
	}

	/**
	 * Parses the server port option (-sp or --server-port). It sets the value
	 * of {@link #serverPort} accordingly.
	 * 
	 * <p>
	 * Extrae la opcion puerto de servidor (-sp). Asigna el valor de
	 * {@link #serverPort}.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided.
	 *             <p>
	 *             Si se proporciona un valor invalido.
	 */
	private static void parseServerPortOption(CommandLine line) throws ParseException {
		String serverPortVal = line.getOptionValue("sp");
		if (serverPortVal != null) {
			try {
				serverPort = Integer.parseInt(serverPortVal);
			} catch (NumberFormatException e) {
				throw new ParseException("Invalid port: " + serverPortVal);
			}
		} else {
			serverPort = DEFAULT_PORT;
		}
	}

	/**
	 * Builds the server host (-sh or --server-host) CLI option.
	 * <p>
	 * Construye la opcion CLI -sh.
	 * 
	 * @return CLI {@link {@link Option} for the server host.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructServerHostOption() {
		return new Option("sh", "server-host", true,
				"Name (or IP address) of " + "the machine that is running the server.");
	}

	/**
	 * Parses the server host option (-sh or --server-host). It sets the value
	 * of {@link #serverHost} accordingly.
	 * 
	 * <p>
	 * Extrae la opcion server host (-sh). Asigna el valor de
	 * {@link #serverHost}.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided.
	 *             <p>
	 *             Si se proporciona un valor invalido.
	 */
	private static void parseServerHostOption(CommandLine line) throws ParseException {
		String serverHostVal = line.getOptionValue("sh");
		if (serverHostVal != null) {
			serverHost = serverHostVal;
		} else {
			serverHost = DEFAULT_HOST;
		}
	}

	/**
	 * Builds the AIAlgorithm (-aialg or --ai-algorithm) CLI option.
	 * <p>
	 * Construye la opcion CLI -aialg.
	 * 
	 * @return CLI {@link {@link Option} for the AIAlgorithm.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructAIAlgorithmOption() {
		return new Option("aialg", "ai-algorithm", true,
				"The algorithm to use for automatic players. " + "Can be minmax, minmaxab or none.");
	}

	/**
	 * Parses the AIAlgorithm option (-aialg or --ai-algorithm). It sets the
	 * value of {@link #aiPlayerAlg} accordingly. {@code none} by default.
	 * 
	 * <p>
	 * Extrae la opcion AIAlgorithm (-aialg). Asigna el valor de
	 * {@link #aiPlayerAlg}. {@code none} por defecto.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @throws ParseException
	 *             If an invalid value is provided.
	 *             <p>
	 *             Si se proporciona un valor invalido.
	 */
	private static void parseAIAlgOption(CommandLine line) throws ParseException {
		String algVal = line.getOptionValue("aialg");
		if (algVal == null || algVal == "none") {
			aiPlayerAlg = null;
		} else {
			switch (algVal) {
			case "minmax":
				aiPlayerAlg = new MinMax(parseMinMaxDepthOption(line), false);
				break;
			case "minmaxab":
				aiPlayerAlg = new MinMax(parseMinMaxDepthOption(line), true);
				break;
			default:
				throw new ParseException("Invalid AI algorithm.");
			}
		}
	}

	/**
	 * Builds the MinMaxDepth (-md or --minmax-depth) CLI option.
	 * <p>
	 * Construye la opcion CLI -md.
	 * 
	 * @return CLI {@link {@link Option} for the MinMaxDepth.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */
	private static Option constructMinMaxDepthOption() {
		return new Option("md", "minmax-depth", true,
				"Indicates the maximum depth of the search tree built by MinMax algorithms.");
	}

	/**
	 * Parses the MinMaxDepth option (-md or --minmax-depth). It sets the value
	 * of {@link #aiPlayerAlg} accordingly.
	 * <p>
	 * Extrae la opcion MinMaxDepth (-md). Asigna el valor de
	 * {@link #aiPlayerAlg}.
	 * 
	 * @param line
	 *            CLI {@link CommandLine} object.
	 * @return The maximum depth of the search tree built.
	 *         <p>
	 *         La profundidad maxima del arbol de busqueda.
	 * @throws ParseException
	 *             If an invalid value is provided.
	 *             <p>
	 *             Si se proporciona un valor invalido.
	 */
	private static int parseMinMaxDepthOption(CommandLine line) throws ParseException {
		String depthVal = line.getOptionValue("md");
		if (depthVal != null) {
			try {
				int depth = Integer.parseInt(depthVal);
				if (depth < 1) {
					throw new GameError("Invalid depth value.");
				}
				return depth;
			} catch (NumberFormatException e) {
				throw new ParseException("Invalid depth: " + depthVal);
			}
		} else
			return DEFAULT_DEPTH;
	}

	/**
	 * Builds the help (-h or --help) CLI option.
	 * 
	 * <p>
	 * Construye la opcion CLI -h.
	 * 
	 * @return CLI {@link {@link Option} for the help option.
	 *         <p>
	 *         Objeto {@link Option} de esta opcion.
	 */

	private static Option constructHelpOption() {
		return new Option("h", "help", false, "Print this message");
	}

	/**
	 * Parses the help option (-h or --help). It print the usage information on
	 * the standard output.
	 * 
	 * <p>
	 * Extrae la opcion help (-h) que imprime informacion de uso del programa en
	 * la salida estandar.
	 * 
	 * @param line
	 *            * CLI {@link CommandLine} object.
	 * @param cmdLineOptions
	 *            CLI {@link Options} object to print the usage information.
	 * 
	 */
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	/**
	 * Starts a game using a {@link ConsoleCtrl} which is not based on MVC. Is
	 * used only for teaching the difference from the MVC one.
	 * 
	 * <p>
	 * Método para iniciar un juego con el controlador {@link ConsoleCtrl}, no
	 * basado en MVC. Solo se utiliza para mostrar las diferencias con el
	 * controlador MVC.
	 * 
	 */
	public static void startGameNoMVC() {
		Game g = new Game(gameFactory.gameRules());
		Controller c = null;

		switch (view) {
		case CONSOLE:
			ArrayList<Player> players = new ArrayList<Player>();
			for (int i = 0; i < pieces.size(); i++) {
				switch (playerModes.get(i)) {
				case AI:
					players.add(gameFactory.createAIPlayer(aiPlayerAlg));
					break;
				case MANUAL:
					players.add(gameFactory.createConsolePlayer());
					break;
				case RANDOM:
					players.add(gameFactory.createRandomPlayer());
					break;
				default:
					throw new UnsupportedOperationException(
							"Something went wrong! This program point should be unreachable!");
				}
			}
			log.log(Level.INFO, "Jugando a {0} con {1} y {2} como jugadores",
					new Object[] { gameFactory.getClass(), Arrays.toString(players.toArray()) });

			c = new ConsoleCtrl(g, pieces, players, new Scanner(System.in));
			break;
		case WINDOW:
			throw new UnsupportedOperationException(
					"Swing Views are not supported in startGameNoMVC!! Please use startGameMVC instead.");
		default:
			throw new UnsupportedOperationException("Something went wrong! This program point should be unreachable!");
		}

		c.start();
	}

	/**
	 * Starts a game. Should be called after {@link #parseArgs(String[])} so
	 * some fields are set to their appropriate values.
	 * 
	 * <p>
	 * Inicia un juego. Debe llamarse despues de {@link #parseArgs(String[])}
	 * para que los atributos tengan los valores correctos.
	 * 
	 */
	public static void startGame() {
		switch (appMode) {
		case NORMAL:
			Game g = new Game(gameFactory.gameRules());
			Controller c = null;
			ArrayList<Player> players = new ArrayList<Player>();

			for (int i = 0; i < pieces.size(); i++) {
				switch (playerModes.get(i)) {
				case AI:
					players.add(gameFactory.createAIPlayer(aiPlayerAlg));
					break;
				case MANUAL:
					players.add(gameFactory.createConsolePlayer());
					break;
				case RANDOM:
					players.add(gameFactory.createRandomPlayer());
					break;
				default:
					throw new UnsupportedOperationException(
							"Something went wrong! This program point should be unreachable!");
				}
			}
			switch (view) {
			case CONSOLE:
				c = new ConsoleCtrlMVC(g, pieces, players, new Scanner(System.in));
				gameFactory.createConsoleView(g, c);
				break;
			case WINDOW:
				c = new Controller(g, pieces);
				final Controller c2 = c;
				if (multiviews) {
					for (Piece p : pieces) {
						gameFactory.createSwingView(g, c2, p, gameFactory.createRandomPlayer(),
								gameFactory.createAIPlayer(aiPlayerAlg));
					}
				} else {
					gameFactory.createSwingView(g, c2, null, gameFactory.createRandomPlayer(),
							gameFactory.createAIPlayer(aiPlayerAlg));
				}
				break;

			default:
				throw new UnsupportedOperationException(
						"Something went wrong! This program point should be unreachable!");
			}
			c.start();
			break;
		case SERVER:
			Server server = new Server(serverPort, DEFAULT_TIMEOUT, gameFactory, pieces);
			server.initializeServer();
			server.start();
			break;
		case CLIENT:
			ProxyController proxyCtrl = new ProxyController(serverHost, serverPort, DEFAULT_TIMEOUT, aiPlayerAlg);
			proxyCtrl.startCtrl();
			break;
		default:
			throw new UnsupportedOperationException("Something went wrong! This program point should be unreachable!");
		}

	}

	///
	public static void logInfo(String text, Object... args) {
		log.log(Level.INFO, text, args);
	}

	/**
	 * The main method. It calls {@link #parseArgs(String[])} and then
	 * {@link #startGame()}.
	 * 
	 * <p>
	 * Metodo main. Llama a {@link #parseArgs(String[])} y a continuacion inicia
	 * un juego con {@link #startGame()}.
	 * 
	 * @param args
	 *            Command-line arguments.
	 * 
	 */
	public static void main(String[] args) {
		setupLogging(Level.INFO);
		parseArgs(args);
		startGame();
	}

	public static void setupLogging(Level level) {

		// configuracion de logs
		Logger log = Logger.getLogger("");
		for (Handler h : log.getHandlers())
			log.removeHandler(h);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(new SimpleFormatter() {
			DecimalFormat fmt = new DecimalFormat("000");
			long previousMillis = System.currentTimeMillis();
			int stanza = 0;

			@Override
			public synchronized String format(LogRecord record) {
				String s = (record.getMillis() - previousMillis > 500) ? "\n *** " + (++stanza) + " *** \n\n" : "";
				previousMillis = record.getMillis();
				return s + fmt.format(previousMillis % 1000) + " " + record.getLevel() + " -- " + record.getMessage()
						+ "\n";
			}
		});
		log.addHandler(ch);
		log.setLevel(level);
		ch.setLevel(level);
	}

}
