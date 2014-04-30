package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import android.content.Context;
import android.os.Bundle;
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
		this.gamePanel.setMap(level.getMap());
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
