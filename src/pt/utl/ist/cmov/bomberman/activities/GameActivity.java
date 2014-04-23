package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.controllers.SimpleGestureController;
import pt.utl.ist.cmov.bomberman.controllers.interfaces.SimpleGestureListener;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

public class GameActivity extends FullScreenActivity implements
		SimpleGestureListener {

	private static final String TAG = GameActivity.class.getSimpleName();

	private static Context context;

	private SimpleGestureController detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.activity_game);

		String levelName = getIntent().getExtras().getString(
				LevelChoiceActivity.LEVEL_MESSAGE);

		Level level = LevelManager.loadLevel(context.getAssets(), levelName);

		detector = new SimpleGestureController(this, this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		String str = "";

		switch (direction) {

		case SimpleGestureController.SWIPE_RIGHT:
			str = "Swipe Right";
			break;
		case SimpleGestureController.SWIPE_LEFT:
			str = "Swipe Left";
			break;
		case SimpleGestureController.SWIPE_DOWN:
			str = "Swipe Down";
			break;
		case SimpleGestureController.SWIPE_UP:
			str = "Swipe Up";
			break;

		}
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDoubleTap() {
		Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
	}
}
