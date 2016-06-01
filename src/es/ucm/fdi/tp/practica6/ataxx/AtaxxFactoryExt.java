package es.ucm.fdi.tp.practica6.ataxx;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactory;

/**
 * A class that extends AtaxxFactory and that adds the new functionality needed
 * to create a swingview.
 * <p>
 * Una clase que extiende AtaxxFactory y que añade la nueva funcionalidad
 * necesaria para crear una vista de ventana.
 * 
 * @author Antonio Valdivia y Raul Murillo
 */
public class AtaxxFactoryExt extends AtaxxFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7883943985725547424L;

	/**
	 * The empty constructor of the class. It uses default dimension and
	 * obstacles.
	 * <p>
	 * El constructor vacio de la clase. Usa una dimension y unos obstaculos por
	 * defecto.
	 */
	public AtaxxFactoryExt() {
		super();
	}

	/**
	 * The constructor with parameters of the class.
	 * <p>
	 * El constructor con parametros de la clase.
	 * 
	 * @param dim
	 * @param obstacles
	 */
	public AtaxxFactoryExt(int dim, int obstacles) {
		super(dim, obstacles);
	}

	/**
	 * The constructor with only one parameter. It uses a default dimension.
	 * <p>
	 * El constructor con solo el parametro obstaculos. Usa una dimension por
	 * defecto.
	 * <p>
	 * 
	 * @param obstacles
	 */
	public AtaxxFactoryExt(Integer obstacles) {
		super(obstacles);
	}

	@Override
	public void createSwingView(final Observable<GameObserver> game, final Controller ctrl, final Piece viewPiece,
			final Player randPlayer, final Player aiPlayer) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new AtaxxSwingView(game, ctrl, viewPiece, randPlayer, aiPlayer);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new GameError(e.getMessage());
		}
	}

}
