package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;

public class ExplosionDrawing extends Drawing {

	private static final long serialVersionUID = 1259180693567859559L;

	public ExplosionDrawing() {
		super();
	}

	public ExplosionDrawing(Context context, Integer id, Position pos) {
		super(id, pos, "images/explosion.png");
	}

}
