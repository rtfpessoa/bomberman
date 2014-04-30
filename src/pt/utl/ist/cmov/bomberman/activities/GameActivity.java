package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.GameMap;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import pt.utl.ist.cmov.bomberman.game.models.BombermanModel;
import pt.utl.ist.cmov.bomberman.game.models.EmptyModel;
import pt.utl.ist.cmov.bomberman.game.models.Model;
import pt.utl.ist.cmov.bomberman.game.models.ObstacleModel;
import pt.utl.ist.cmov.bomberman.game.models.RobotModel;
import pt.utl.ist.cmov.bomberman.game.models.WallModel;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

public class GameActivity extends FullScreenActivity {

	private static final String TAG = GameActivity.class.getSimpleName();

	private static Context context;

	private MainGamePanel gamePanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.activity_game);

		String levelName = getIntent().getExtras().getString(
				LevelChoiceActivity.LEVEL_MESSAGE);

		Level level = LevelManager.loadLevel(context.getAssets(), levelName);

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);
		this.gamePanel.setModelsMap(this.parseMap(level.getMap()));
	}

	private List<List<Model>> parseMap(GameMap initialMap) {

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;
		int mapWidth = initialMap.getWidth();
		int mapHeight = initialMap.getHeight();

		MapMeasurements.updateMapMeasurements(screenWidth, screenHeight,
				mapWidth, mapHeight);

		List<List<Model>> parsedMap = new ArrayList<List<Model>>();

		for (Integer y = 0; y < mapHeight; y++) {
			parsedMap.add(new ArrayList<Model>());

			for (Integer x = 0; x < mapWidth; x++) {
				List<Model> line = parsedMap.get(y);
				Character content = initialMap.getContent(x, y);

				if (content == GameMap.WALL) {
					line.add(new WallModel(context,
							MapMeasurements.POSITION_HEIGHT,
							MapMeasurements.POSITION_WIDTH,
							MapMeasurements.SIDE_PADDING
									+ MapMeasurements.POSITION_WIDTH * x,
							MapMeasurements.UP_PADDING
									+ MapMeasurements.POSITION_HEIGHT * y));
				} else if (content == GameMap.OBSTACLE) {
					line.add(new ObstacleModel(context,
							MapMeasurements.POSITION_HEIGHT,
							MapMeasurements.POSITION_WIDTH,
							MapMeasurements.SIDE_PADDING
									+ MapMeasurements.POSITION_WIDTH * x,
							MapMeasurements.UP_PADDING
									+ MapMeasurements.POSITION_HEIGHT * y));
				} else if (content == GameMap.ROBOT) {
					line.add(new RobotModel(context,
							MapMeasurements.POSITION_HEIGHT,
							MapMeasurements.POSITION_WIDTH,
							MapMeasurements.SIDE_PADDING
									+ MapMeasurements.POSITION_WIDTH * x,
							MapMeasurements.UP_PADDING
									+ MapMeasurements.POSITION_HEIGHT * y));
				} else if (content == GameMap.EMPTY) {
					line.add(new EmptyModel());
				} else {
					line.add(new BombermanModel(context,
							MapMeasurements.POSITION_HEIGHT,
							MapMeasurements.POSITION_WIDTH,
							MapMeasurements.SIDE_PADDING
									+ MapMeasurements.POSITION_WIDTH * x,
							MapMeasurements.UP_PADDING
									+ MapMeasurements.POSITION_HEIGHT * y,
							Character.getNumericValue(content)));
				}
			}
		}

		return parsedMap;
	}

	public void upClick(View view) {
		Toast.makeText(this, "CLICK UP", Toast.LENGTH_SHORT).show();
	}

	public void downClick(View view) {
		Toast.makeText(this, "CLICK DOWN", Toast.LENGTH_SHORT).show();
	}

	public void rightClick(View view) {
		Toast.makeText(this, "CLICK RIGHT", Toast.LENGTH_SHORT).show();
	}

	public void leftClick(View view) {
		Toast.makeText(this, "CLICK LEFT", Toast.LENGTH_SHORT).show();
	}

	public void bombClick(View view) {
		Toast.makeText(this, "CLICK BOMB", Toast.LENGTH_SHORT).show();
	}
}
