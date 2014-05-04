package pt.utl.ist.cmov.bomberman.game.models;

import pt.utl.ist.cmov.bomberman.util.BitmapFactory;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ObstacleModel extends Model {

	public ObstacleModel(Context context, int height, int width, int x, int y) {
		super();
		Bitmap b = BitmapFactory.getBitmapFromAsset(context,
				"images/obstacle.png");
		this.bitmap = Bitmap.createScaledBitmap(b, width, height, true);
		this.pos = new Position(x, y);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, pos.x - (bitmap.getWidth() / 2), pos.y
				- (bitmap.getHeight() / 2), null);
	}

}
