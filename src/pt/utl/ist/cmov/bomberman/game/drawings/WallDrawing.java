package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;

public class WallDrawing extends Drawing {

	private static final long serialVersionUID = -3021060390444833908L;

	public WallDrawing() {
		super();
	}

	public WallDrawing(Context context, Integer id, Position pos) {
		super(id, pos, "images/wall.png");
	}

}
