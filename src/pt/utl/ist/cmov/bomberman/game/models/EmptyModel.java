package pt.utl.ist.cmov.bomberman.game.models;

import android.graphics.Canvas;

public class EmptyModel extends Model {

	public EmptyModel() {
		super(0, 0);
	}

	@Override
	public void draw(Canvas canvas) {
		// empty
	}

}
