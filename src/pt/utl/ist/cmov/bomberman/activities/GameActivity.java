package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.controllers.SimpleGestureController;
import pt.utl.ist.cmov.bomberman.controllers.interfaces.SimpleGestureListener;
import pt.utl.ist.cmov.bomberman.game.GameMap;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import pt.utl.ist.cmov.bomberman.game.models.BombermanModel;
import pt.utl.ist.cmov.bomberman.game.models.EmptyModel;
import pt.utl.ist.cmov.bomberman.game.models.Model;
import pt.utl.ist.cmov.bomberman.game.models.ObstacleModel;
import pt.utl.ist.cmov.bomberman.game.models.RobotModel;
import pt.utl.ist.cmov.bomberman.game.models.WallModel;
import pt.utl.ist.cmov.bomberman.util.Constants;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class GameActivity extends FullScreenActivity implements
		SimpleGestureListener {

	private static final String TAG = GameActivity.class.getSimpleName();

	private static Context context;

	private SimpleGestureController detector;
	private MainGamePanel gamePanel;

	private List<List<Model>> parseMap(GameMap initialMap) {

		List<List<Model>> parsedMap = new ArrayList<List<Model>>();

		for (Integer y = 0; y < initialMap.getHeight(); y++) {
			parsedMap.add(new ArrayList<Model>());

			for (Integer x = 0; x < initialMap.getWidth(); x++) {
				List<Model> line = parsedMap.get(y);
				Character content = initialMap.getContent(x, y);

				if (content == GameMap.WALL) {
					line.add(new WallModel(context, Constants.POSITION_HEIGHT,
							Constants.POSITION_WIDTH, Constants.SIDE_PADDING
									+ Constants.POSITION_WIDTH * x,
							Constants.UP_PADDING + Constants.POSITION_HEIGHT
									* y));
				} else if (content == GameMap.OBSTACLE) {
					line.add(new ObstacleModel(context,
							Constants.POSITION_HEIGHT,
							Constants.POSITION_WIDTH, Constants.SIDE_PADDING
									+ Constants.POSITION_WIDTH * x,
							Constants.UP_PADDING + Constants.POSITION_HEIGHT
									* y));
				} else if (content == GameMap.ROBOT) {
					line.add(new RobotModel(context, Constants.POSITION_HEIGHT,
							Constants.POSITION_WIDTH, Constants.SIDE_PADDING
									+ Constants.POSITION_WIDTH * x,
							Constants.UP_PADDING + Constants.POSITION_HEIGHT
									* y));
				} else if (content == GameMap.EMPTY) {
					line.add(new EmptyModel());
				} else {
					line.add(new BombermanModel(context,
							Constants.POSITION_HEIGHT,
							Constants.POSITION_WIDTH, Constants.SIDE_PADDING
									+ Constants.POSITION_WIDTH * x,
							Constants.UP_PADDING + Constants.POSITION_HEIGHT
									* y, Character.getNumericValue(content)));
				}
			}
		}

		return parsedMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.activity_game);

		String levelName = getIntent().getExtras().getString(
				LevelChoiceActivity.LEVEL_MESSAGE);

		Level level = LevelManager.loadLevel(context.getAssets(), levelName);

		this.gamePanel = new MainGamePanel(context, this.parseMap(level
				.getMap()));

		this.addContentView(this.gamePanel, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

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
