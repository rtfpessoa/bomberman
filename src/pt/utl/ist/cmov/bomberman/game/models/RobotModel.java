package pt.utl.ist.cmov.bomberman.game.models;

import pt.utl.ist.cmov.bomberman.game.GameMap;
import pt.utl.ist.cmov.bomberman.util.BitmapFactory;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class RobotModel extends Model {

	private Direction direction = Direction.LEFT;

	public RobotModel(Context context, int x, int y) {
		super(GameMap.ROBOT, x, y);
		Bitmap b = BitmapFactory
				.getBitmapFromAsset(context, "images/robot.png");
		this.bitmap = Bitmap.createScaledBitmap(b,
				MapMeasurements.POSITION_WIDTH,
				MapMeasurements.POSITION_HEIGHT, true);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, pos.x - (bitmap.getWidth() / 2), pos.y
				- (bitmap.getHeight() / 2), null);
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
