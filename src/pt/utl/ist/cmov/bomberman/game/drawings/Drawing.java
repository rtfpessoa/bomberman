package pt.utl.ist.cmov.bomberman.game.drawings;

import java.io.Serializable;

import pt.utl.ist.cmov.bomberman.util.BitmapFactory;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Drawing implements Serializable {

	protected Integer id;
	protected Bitmap bitmap;
	private Position pos;
	private Boolean isConverted = false;
	private String bitmapName;

	public Drawing(Integer id, Position pos, String bitmapName) {
		this.id = id;
		this.pos = pos;
		this.bitmapName = bitmapName;
	}

	public Integer getId() {
		return this.id;
	}

	private void init(Context context) {
		this.pos = new Position(MapMeasurements.SIDE_PADDING
				+ MapMeasurements.POSITION_WIDTH * pos.x,
				MapMeasurements.UP_PADDING + MapMeasurements.POSITION_HEIGHT
						* pos.y);

		Bitmap b = BitmapFactory.getBitmapFromAsset(context, bitmapName);
		this.bitmap = Bitmap.createScaledBitmap(b,
				MapMeasurements.POSITION_WIDTH,
				MapMeasurements.POSITION_HEIGHT, true);
	}

	public void draw(Context context, Canvas canvas) {
		if (!isConverted) {
			init(context);
			this.isConverted = true;
		}

		canvas.drawBitmap(bitmap, pos.x - (bitmap.getWidth() / 2), pos.y
				- (bitmap.getHeight() / 2), null);
	}

}
