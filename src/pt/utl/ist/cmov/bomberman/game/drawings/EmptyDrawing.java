package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.graphics.Canvas;

public class EmptyDrawing extends Drawing {

	public EmptyDrawing(Integer id, Position pos) {
		super(id, pos);
	}

	@Override
	public void draw(Canvas canvas) {
		// empty
	}

}
