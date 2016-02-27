package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayerFromListOfMoves;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectN.ConnectNFactory;
import es.ucm.fdi.tp.basecode.connectN.ConnectNRandomPlayer;
import es.ucm.fdi.tp.basecode.ttt.TicTacToeRules;

public class AtaxxFactory extends ConnectNFactory{

	private int dim;
	
	@Override
	public GameRules gameRules() {
		return new AtaxxRules(dim);
	}
	
	@Override
	public Player createConsolePlayer() {
		return new ConsolePlayerFromListOfMoves(new Scanner(System.in));
	}
	
	@Override
	public Player createRandomPlayer() {
		return new AtaxxRandomPlayer();
	}
	
	//createDefaultPieces??
	
}
