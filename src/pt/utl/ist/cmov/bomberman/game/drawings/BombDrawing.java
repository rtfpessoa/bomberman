package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;

public class BombDrawing extends Drawing {

	private static final long serialVersionUID = 3660148771402000957L;

	public BombDrawing() {
		super();
	}

	public BombDrawing(Context context, Integer id, Position pos) {
		super(id, pos, "images/bomb.png");
	}

}
