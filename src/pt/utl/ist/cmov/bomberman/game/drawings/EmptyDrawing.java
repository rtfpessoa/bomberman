package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;
import android.graphics.Canvas;

public class EmptyDrawing extends Drawing {

	public EmptyDrawing(Integer id, Position pos) {
		super(id, pos, null);
	}

	@Override
	public void draw(Context context, Canvas canvas) {
		// empty
	}

}
