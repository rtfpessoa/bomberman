package pt.utl.ist.cmov.bomberman.game.models;

import pt.utl.ist.cmov.bomberman.game.GameMap;
import android.graphics.Canvas;

public class EmptyModel extends Model {

	public EmptyModel() {
		super(GameMap.EMPTY, 0, 0);
	}

	@Override
	public void draw(Canvas canvas) {
		// empty
	}

}
