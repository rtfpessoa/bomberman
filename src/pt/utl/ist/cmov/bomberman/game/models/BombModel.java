package pt.utl.ist.cmov.bomberman.game.models;

import pt.utl.ist.cmov.bomberman.util.BitmapFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BombModel extends Model {

	public BombModel(Context context, int height, int width, int x, int y) {
		super();
		Bitmap b = BitmapFactory.getBitmapFromAsset(context, "images/bomb.png");
		this.bitmap = Bitmap.createScaledBitmap(b, width, height, true);
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2),
				y - (bitmap.getHeight() / 2), null);
	}

}
