package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.Game;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import pt.utl.ist.cmov.bomberman.listeners.DirectionButtonListener;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends FullScreenActivity {

	private static final String TAG = GameActivity.class.getSimpleName();

	private static Context context;

	private MainGamePanel gamePanel;
	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.activity_game);

		String levelName = getIntent().getExtras().getString(
				LevelChoiceActivity.LEVEL_MESSAGE);

		Level level = LevelManager.loadLevel(context, context.getAssets(),
				levelName);

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);
		this.gamePanel.setMap(level.getMap());

		this.game = new GameServer(this, level, gamePanel);

		this.findViewById(R.id.button_up).setOnTouchListener(
				new DirectionButtonListener(Direction.UP, game));

		this.findViewById(R.id.button_down).setOnTouchListener(
				new DirectionButtonListener(Direction.DOWN, game));

		this.findViewById(R.id.button_left).setOnTouchListener(
				new DirectionButtonListener(Direction.LEFT, game));

		this.findViewById(R.id.button_right).setOnTouchListener(
				new DirectionButtonListener(Direction.RIGHT, game));
	}

	public void bombClick(View view) {
		game.putBomb();
	}

	public void updateTime(Integer remainingTime) {
		if (remainingTime <= 0) {
			endGame();
		}

		TextView timerTextView = (TextView) this.findViewById(R.id.time_left);
		timerTextView.setText(remainingTime.toString() + " s");
	}

	public void endGame() {
		Intent intent = new Intent(this, EndGameActivity.class);

		// TODO: get winner and points
		intent.putExtra(EndGameActivity.INTENT_WINNER, "rtfpessoa");
		intent.putExtra(EndGameActivity.INTENT_WINNER_POINTS, "69");
		gamePanel.destroy();
		finish();
		startActivity(intent);
	}

	@Override
	public void onPause() {
		gamePanel.destroy();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		gamePanel.destroy();
		super.onDestroy();
	}
}
