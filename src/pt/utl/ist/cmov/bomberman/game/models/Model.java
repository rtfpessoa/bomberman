package pt.utl.ist.cmov.bomberman.game.models;

import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Model {

	protected Bitmap bitmap; // the actual bitmap
	protected Position pos;
	private final Character type;

	public Model(Character type, int x, int y) {
		super();

		this.type = type;
		this.pos = new Position(MapMeasurements.SIDE_PADDING
				+ MapMeasurements.POSITION_WIDTH * x,
				MapMeasurements.UP_PADDING + MapMeasurements.POSITION_HEIGHT
						* y);
	}

	public Character getType() {
		return type;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = new Position(MapMeasurements.SIDE_PADDING
				+ MapMeasurements.POSITION_WIDTH * pos.x,
				MapMeasurements.UP_PADDING + MapMeasurements.POSITION_HEIGHT
						* pos.y);
	}

	public abstract void draw(Canvas canvas);

}
