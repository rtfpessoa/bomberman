package pt.utl.ist.cmov.bomberman.listener;

import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DirectionButtonListener implements View.OnTouchListener {

	private static final String TAG = DirectionButtonListener.class
			.getSimpleName();

	private Direction direction;
	private GameClient gameClient;
	private boolean pressed;

	public DirectionButtonListener(Direction direction, GameClient gameClient) {
		this.direction = direction;
		this.gameClient = gameClient;
		this.pressed = false;
	}

	Handler mHandler = new Handler();
	Runnable moveBomberman = new Runnable() {
		@Override
		public void run() {
			if (pressed) {
				gameClient.move(direction);
				mHandler.postDelayed(moveBomberman, 300);
			}
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!pressed) {
				Log.v(TAG, "Pressed " + this.direction);
				this.pressed = true;
				moveBomberman.run();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.v(TAG, "UnPressed " + this.direction);
			this.pressed = false;
		}

		return false;
	}

	public void stopAll() {
		this.mHandler.removeCallbacks(this.moveBomberman);
	}

}
