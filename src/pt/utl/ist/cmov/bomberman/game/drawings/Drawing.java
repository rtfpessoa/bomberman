package pt.utl.ist.cmov.bomberman.game.drawings;

import java.io.Serializable;

import pt.utl.ist.cmov.bomberman.util.BitmapFactory;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Drawing implements Serializable {

	private static final long serialVersionUID = 2182656568468191715L;

	protected Integer id;
	private Position pos;
	private String bitmapName;
	private Boolean isConverted = false;

	public Drawing() {
	}

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
	}

	public void draw(Context context, Canvas canvas) {
		if (bitmapName == null) {
			return;
		}

		if (!isConverted) {
			init(context);
			this.isConverted = true;
		}

		Bitmap bitmap = BitmapFactory.getInstance(context).getBitmapFromAsset(
				bitmapName);

		canvas.drawBitmap(bitmap, pos.x - (bitmap.getWidth() / 2), pos.y
				- (bitmap.getHeight() / 2), null);
	}

}
