package pt.utl.ist.cmov.bomberman.controllers;

import pt.utl.ist.cmov.bomberman.game.Game;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.os.Handler;
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

	Handler mHandler = new Handler();
	Runnable moveBomberman = new Runnable() {
		@Override
		public void run() {
			game.moveBomberman(direction);
			if (pressed) {
				mHandler.postDelayed(moveBomberman, 500);
			}
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!pressed) {
				Log.d(TAG, "Pressed " + this.direction);
				this.pressed = true;
				moveBomberman.run();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d(TAG, "UnPressed Down");
			this.pressed = false;
		}

		return false;
	}

}
