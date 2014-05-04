package pt.utl.ist.cmov.bomberman.controllers;

import pt.utl.ist.cmov.bomberman.game.Game;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DirectionButtonListener implements View.OnTouchListener {

	private static final String TAG = DirectionButtonListener.class
			.getSimpleName();

	private Direction direction;
	private Game game;
	private boolean pressed;

	public DirectionButtonListener(Direction direction, Game game) {
		this.direction = direction;
		this.game = game;
		this.pressed = false;
	}

	private class MoveBombermanTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			long time = -1;

			while (pressed) {
				if (time == -1
						|| SystemClock.currentThreadTimeMillis() - time > 500) {
					Log.d(TAG, "Being pressed");
					game.moveBomberman(direction);
				}
				time = SystemClock.currentThreadTimeMillis();
			}
			return null;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!pressed) {
				Log.d(TAG, "Pressed " + this.direction);
				this.pressed = true;
				new MoveBombermanTask().execute();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d(TAG, "UnPressed Down");
			this.pressed = false;
		}

		return false;
	}

}
