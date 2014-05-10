package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;

public class BombermanDrawing extends Drawing {

	public BombermanDrawing(Context context, Integer id, Position pos,
			Integer bombermanId) {
		super(id, pos, "images/bomberman" + bombermanId + ".png");
	}

}
