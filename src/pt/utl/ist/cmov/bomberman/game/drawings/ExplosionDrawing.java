package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.BitmapFactory;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;
import android.graphics.Bitmap;

public class ExplosionDrawing extends Drawing {

	public ExplosionDrawing(Context context, Integer id, Position pos) {
		super(id, pos);
		Bitmap b = BitmapFactory.getBitmapFromAsset(context,
				"images/explosion.png");
		this.bitmap = Bitmap.createScaledBitmap(b,
				MapMeasurements.POSITION_WIDTH,
				MapMeasurements.POSITION_HEIGHT, true);
	}

}
