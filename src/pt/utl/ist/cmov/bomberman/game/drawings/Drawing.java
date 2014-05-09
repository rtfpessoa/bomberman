package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Drawing {

	protected Integer id;
	protected Bitmap bitmap;
	private Position pos;

	public Drawing(Integer id, Position pos) {
		this.id = id;
		this.pos = new Position(MapMeasurements.SIDE_PADDING
				+ MapMeasurements.POSITION_WIDTH * pos.x,
				MapMeasurements.UP_PADDING + MapMeasurements.POSITION_HEIGHT
						* pos.y);
		;
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, pos.x - (bitmap.getWidth() / 2), pos.y
				- (bitmap.getHeight() / 2), null);
	}

}
