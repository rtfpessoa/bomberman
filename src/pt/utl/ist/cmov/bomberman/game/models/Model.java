package pt.utl.ist.cmov.bomberman.game.models;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class Model {

	protected Bitmap bitmap; // the actual bitmap
	protected Position pos;
	protected boolean touched;

	public Model() {
		super();
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
		this.pos = pos;
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	public abstract void draw(Canvas canvas);

	/**
	 * Handles the {@link MotionEvent.ACTION_DOWN} event. If the event happens
	 * on the bitmap surface then the touched state is set to <code>true</code>
	 * otherwise to <code>false</code>
	 * 
	 * @param eventX
	 *            - the event's X coordinate
	 * @param eventY
	 *            - the event's Y coordinate
	 */
	public void handleActionDown(int eventX, int eventY) {
		if (eventX >= (pos.x - bitmap.getWidth() / 2)
				&& (eventX <= (pos.x + bitmap.getWidth() / 2))) {
			if (eventY >= (pos.y - bitmap.getHeight() / 2)
					&& (pos.y <= (pos.y + bitmap.getHeight() / 2))) {
				// droid touched
				setTouched(true);
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}

	}

}
