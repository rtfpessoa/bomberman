package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;

public class ObstacleDrawing extends Drawing {

	private static final long serialVersionUID = -7041274209824710047L;

	public ObstacleDrawing() {
		super();
	}

	public ObstacleDrawing(Context context, Integer id, Position pos) {
		super(id, pos, "images/obstacle.png");
	}

}
