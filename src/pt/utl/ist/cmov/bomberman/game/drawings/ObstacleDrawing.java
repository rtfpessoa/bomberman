package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;

public class ObstacleDrawing extends Drawing {

	public ObstacleDrawing(Context context, Integer id, Position pos) {
		super(id, pos, "images/obstacle.png");
	}

}
